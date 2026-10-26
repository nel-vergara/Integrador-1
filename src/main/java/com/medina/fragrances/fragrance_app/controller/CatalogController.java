package com.medina.fragrances.fragrance_app.controller;

import com.medina.fragrances.fragrance_app.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import com.medina.fragrances.fragrance_app.model.Fragrance;

@Controller
public class CatalogController {

    private final ProductoService productoService;

    public CatalogController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // --- PRIMER MÉTODO (DINÁMICO DESDE BASE DE DATOS) ---
    @GetMapping({"/catalogo", "/catalogo.html", "/catalogo-dinamico"})
    public String catalogo(Model model,
                           HttpSession session,
                           @RequestParam(required = false) String categoria,
                           @RequestParam(required = false) String buscar) {

        // Carga productos iniciales si la tabla esta vacia
        productoService.cargarProductosIniciales();

        // Filtrar por busqueda o categoria, si no mostrar todos
        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("productos", productoService.buscarPorNombre(buscar));
            model.addAttribute("buscar", buscar);
        } else if (categoria != null && !categoria.isBlank()) {
            model.addAttribute("productos", productoService.buscarPorCategoria(categoria));
            model.addAttribute("categoriaActiva", categoria);
        } else {
            model.addAttribute("productos", productoService.listarTodos());
        }

        // Pasar nombre de usuario logueado al navbar
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        
        return "catalogo/catalogo-view"; 
    } // <-- Aquí faltaba cerrar este método original

    // --- SEGUNDO MÉTODO (LISTA FIJA EN MEMORIA) ---
    @GetMapping("/catalogo-estatico") // Le cambié la ruta para evitar errores de duplicado
    public String catalogoEstatico(Model model) {
        List<Fragrance> fragancias = List.of(
            new Fragrance(1, "AFNAN 9 PM EAU DE PARFUM", "AFNAN", 224.00, "/img/9pm.webp", "Fragancia oriental amaderada para hombre", "Para Él", "ON SALE 20%"),
            new Fragrance(2, "AFNAN 9 PM REBEL EAU DE PARFUM UNISEX", "AFNAN", 278.40, "/img/9pm Rebel.webp", "Nafais Sharq Eau de Parfum", "Unisex", "ON SALE 13%"),
            new Fragrance(3, "Amber Oud Aqua Dubai", "Amber", 464.00, "/img/Amber Oud Aqua Dubai.webp", "Club Couture Parfum", "Unisex", "ON SALE 20%"),
            new Fragrance(4, "Amethyst", "Amethyst", 650.00, "/img/Amethyst.webp", "Coco Mademoiselle", "Unisex", ""),
            new Fragrance(5, "Asad Bourbon", "Asad Bourbon", 648.00, "/img/Asad Bourbon.webp", "Sauvage Elixir", "Unisex", "ON SALE 10%"),
            new Fragrance(6, "Asad by Lattafa", "Asad by Lattafa", 590.00, "/img/Asad by Lattafa.webp", "Black Orchid", "Unisex", ""),
            new Fragrance(7, "Fakhar Black", "Fakhar Black", 422.40, "/img/Fakhar Black.webp", "Fakhar Black", "Unisex", "ON SALE 12%"),
            new Fragrance(8, "Fakhar Extrait (Gold)", "Fakhar Extrait", 520.00, "/img/Fakhar Extrait (Gold).webp", "Libre Eau de Parfum", "Unisex", ""),
            new Fragrance(9, "Honor And Glory", "Honor And Glory", 311.60, "/img/Honor And Glory.webp", "Honor And Glory", "Unisex", "ON SALE 18%"),
            new Fragrance(10, "Ishq Al Shuyukh", "Ishq Al Shuyukh", 490.00, "/img/Ishq Al Shuyukh.webp", "Ishq Al Shuyukh", "Unisex", ""),
            new Fragrance(11, "khamrah dukhan", "khamrah dukhan", 391.00, "/img/khamrah dukhan.webp", "khamrah dukhan", "Unisex", "ON SALE 15%"),
            new Fragrance(12, "khamrah Qahwa", "khamrah Qahwa", 580.00, "/img/khamrah Qahwa.webp", "khamrah Qahwa", "Unisex", ""),
            new Fragrance(13, "khamrah", "khamrah", 378.00, "/img/khamrah.webp", "khamrah", "Unisex", "ON SALE 10%"),
            new Fragrance(14, "Mandarin Sky Elixir", "Mandarin Sky Elixir", 340.00, "/img/Mandarin Sky Elixir.webp", "Bombshell", "Unisex", ""),
            new Fragrance(15, "Mandarin Sky", "Mandarin Sky", 312.00, "/img/Mandarin Sky.webp", "1 Million", "Unisex", "ON SALE 20%"),
            new Fragrance(16, "Odyssey Artisto", "Odyssey Artisto", 360.00, "/img/Odyssey Artisto.webp", "Odyssey Artisto", "Unisex", ""),
            new Fragrance(17, "Oud For Glory", "Oud For Glory", 238.00, "/img/Oud For Glory.webp", "Oud For Glory", "Unisex", "ON SALE 15%"),
            new Fragrance(18, "Shaheen Gold", "Shaheen Gold", 510.00, "/img/Shaheen Gold.webp", "Shaheen Gold", "Unisex", ""),
            new Fragrance(19, "Sublime", "Lataffa", 378.40, "/img/Sublime.webp", "Sublime", "Unisex", "ON SALE 12%"),
            new Fragrance(20, "Vintage Radio", "Vintage Radio", 410.00, "/img/Vintage Radio.webp", "Vintage Radio", "Unisex", ""),
            new Fragrance(21, "Xerjoff Erba Pura", "Xerjoff", 410.00, "/img/Xerjoff Erba Pura.webp", "Xerjoff Erba Pura", "Unisex", ""),
            new Fragrance(22, "Valentino Uomo", "Valentino", 410.00, "/img/Valentino Uomo.webp", "Valentino Uomo", "Para Él", ""),
            new Fragrance(23, "CAROLINA HERRERA GOOD GIRL", "CAROLINA HERRERA", 410.00, "/img/CAROLINA HERRERA GOOD GIRL.webp", "CAROLINA HERRERA GOOD GIRL", "Para Ella", "")
        );
        model.addAttribute("fragancias", fragancias);
        return "catalogo/catalogo-view";
    }
}