package com.medina.fragrances.fragrance_app.controller;

import com.medina.fragrances.fragrance_app.model.Pedido;
import com.medina.fragrances.fragrance_app.model.Producto;
import com.medina.fragrances.fragrance_app.model.Usuario;
import com.medina.fragrances.fragrance_app.service.PedidoService;
import com.medina.fragrances.fragrance_app.service.ProductoService;
import com.medina.fragrances.fragrance_app.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CheckoutController {

    private final PedidoService pedidoService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public CheckoutController(PedidoService pedidoService,
                              ProductoService productoService,
                              UsuarioService usuarioService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/checkout", "/checkout.html"})
    public String checkout(HttpSession session, Model model) {
        Optional<Usuario> usuario = obtenerUsuarioSesion(session);
        if (usuario.isEmpty()) {
            return "redirect:/login?redirect=/checkout";
        }

        model.addAttribute("nombreUsuario", usuario.get().getNombre());
        model.addAttribute("usuario", usuario.get());
        return "checkout";
    }

    @PostMapping("/checkout/procesar")
    public String procesarCompra(@RequestParam(name = "itemId", required = false) List<String> itemIds,
                                 @RequestParam(name = "nombre", required = false) List<String> nombres,
                                 @RequestParam(name = "precio", required = false) List<Double> precios,
                                 @RequestParam(name = "imagen", required = false) List<String> imagenes,
                                 @RequestParam(name = "descripcion", required = false) List<String> descripciones,
                                 @RequestParam(name = "cantidad", required = false) List<Integer> cantidades,
                                 @RequestParam String metodoPago,
                                 @RequestParam String metodoEntrega,
                                 @RequestParam String direccionEntrega,
                                 @RequestParam String telefonoContacto,
                                 @RequestParam(required = false) String referencia,
                                 @RequestParam(required = false) String notas,
                                 HttpSession session,
                                 Model model) {
        Optional<Usuario> usuarioOpt = obtenerUsuarioSesion(session);
        if (usuarioOpt.isEmpty()) {
            return "redirect:/login?redirect=/checkout";
        }

        if (nombres == null || precios == null || cantidades == null || nombres.isEmpty()) {
            model.addAttribute("nombreUsuario", usuarioOpt.get().getNombre());
            model.addAttribute("usuario", usuarioOpt.get());
            model.addAttribute("errorCheckout", "Tu carrito esta vacio o no se pudo procesar.");
            return "checkout";
        }

        Usuario usuario = usuarioOpt.get();
        List<Pedido> pedidosCreados = new ArrayList<>();
        int limite = Math.min(nombres.size(), Math.min(precios.size(), cantidades.size()));

        for (int i = 0; i < limite; i++) {
            Integer cantidad = cantidades.get(i);
            Double precio = precios.get(i);
            String nombre = nombres.get(i);
            if (cantidad == null || cantidad < 1 || precio == null || precio <= 0 || nombre == null || nombre.isBlank()) {
                continue;
            }

            Producto producto = resolverProducto(
                    itemIds != null && i < itemIds.size() ? itemIds.get(i) : null,
                    nombre,
                    precio,
                    imagenes != null && i < imagenes.size() ? imagenes.get(i) : null,
                    descripciones != null && i < descripciones.size() ? descripciones.get(i) : null
            );

            Pedido pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setProducto(producto);
            pedido.setCantidad(cantidad);
            pedido.setEstado("PENDIENTE");
            pedido.setMetodoPago(metodoPago);
            pedido.setMetodoEntrega(metodoEntrega);
            pedido.setDireccionEntrega(direccionEntrega);
            pedido.setTelefonoContacto(telefonoContacto);
            pedido.setNotas(construirNotas(referencia, notas));
            pedido.setTotal(producto.getPrecio() * cantidad);
            pedidosCreados.add(pedidoService.guardar(pedido));

            if (producto.getStock() != null && producto.getStock() > 0) {
                producto.setStock(Math.max(0, producto.getStock() - cantidad));
                productoService.guardar(producto);
            }
        }

        if (pedidosCreados.isEmpty()) {
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("usuario", usuario);
            model.addAttribute("errorCheckout", "No se pudo crear el pedido. Revisa tu carrito e intenta nuevamente.");
            return "checkout";
        }

        double total = pedidosCreados.stream()
                .map(Pedido::getTotal)
                .filter(valor -> valor != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        model.addAttribute("nombreUsuario", usuario.getNombre());
        model.addAttribute("pedidos", pedidosCreados);
        model.addAttribute("totalCompra", total);
        model.addAttribute("metodoPago", metodoPago);
        model.addAttribute("metodoEntrega", metodoEntrega);
        return "checkout-exito";
    }

    private Optional<Usuario> obtenerUsuarioSesion(HttpSession session) {
        Object usuarioId = session.getAttribute("usuarioId");
        if (usuarioId instanceof Integer id) {
            return usuarioService.buscarPorId(id);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return usuarioService.buscarPorEmail(authentication.getName());
        }

        return Optional.empty();
    }

    private Producto resolverProducto(String itemId,
                                      String nombre,
                                      Double precio,
                                      String imagen,
                                      String descripcion) {
        Integer productoId = parseEntero(itemId);
        if (productoId != null) {
            Optional<Producto> productoExistente = productoService.buscarPorId(productoId);
            if (productoExistente.isPresent()) {
                return productoExistente.get();
            }
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setMarca(detectarMarca(nombre));
        producto.setPrecio(precio);
        producto.setImageUrl(imagen == null || imagen.isBlank() ? "/img/placeholder-product.webp" : imagen);
        producto.setDescripcion(descripcion == null ? "Producto agregado desde checkout." : descripcion);
        producto.setCategoria(nombre.toLowerCase().contains("decant") || (itemId != null && !itemId.matches("\\d+")) ? "Decant" : "Unisex");
        producto.setDescuentoLabel("");
        producto.setStock(0);
        return productoService.guardar(producto);
    }

    private Integer parseEntero(String valor) {
        try {
            return valor == null ? null : Integer.parseInt(valor);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String detectarMarca(String nombre) {
        String limpio = nombre == null ? "Medina" : nombre.trim();
        int espacio = limpio.indexOf(' ');
        if (espacio > 1) {
            return limpio.substring(0, espacio);
        }
        return "Medina";
    }

    private String construirNotas(String referencia, String notas) {
        StringBuilder builder = new StringBuilder();
        if (referencia != null && !referencia.isBlank()) {
            builder.append("Referencia: ").append(referencia.trim());
        }
        if (notas != null && !notas.isBlank()) {
            if (!builder.isEmpty()) {
                builder.append(" | ");
            }
            builder.append(notas.trim());
        }
        return builder.toString();
    }
}
