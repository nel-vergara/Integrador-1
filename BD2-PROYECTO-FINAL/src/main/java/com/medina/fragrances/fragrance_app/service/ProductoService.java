package com.medina.fragrances.fragrance_app.service;

import com.medina.fragrances.fragrance_app.model.Producto;
import com.medina.fragrances.fragrance_app.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    // Carga inicial: si la tabla esta vacia, inserta todos los productos del catalogo
    @Transactional
    public void cargarProductosIniciales() {
        if (productoRepository.count() == 0) {
            List<Producto> iniciales = List.of(
                crearProducto("AFNAN 9 PM EAU DE PARFUM", "AFNAN", 224.00, "/img/9pm.webp", "Fragancia oriental amaderada para hombre", "Para Él", "ON SALE 20%", 15),
                crearProducto("AFNAN 9 PM REBEL EAU DE PARFUM UNISEX", "AFNAN", 278.40, "/img/9pm Rebel.webp", "Fragancia fresca y especiada", "Unisex", "ON SALE 13%", 10),
                crearProducto("Amber Oud Aqua Dubai", "Amber", 464.00, "/img/Amber Oud Aqua Dubai.webp", "Club Couture Parfum", "Unisex", "ON SALE 20%", 8),
                crearProducto("Amethyst", "Amethyst", 650.00, "/img/Amethyst.webp", "Fragancia lujosa e intensa", "Unisex", "", 5),
                crearProducto("Asad Bourbon", "Asad Bourbon", 648.00, "/img/Asad Bourbon.webp", "Notas amaderadas y bourbon", "Unisex", "ON SALE 10%", 7),
                crearProducto("Asad by Lattafa", "Lattafa", 590.00, "/img/Asad by Lattafa.webp", "Fragancia oriental poderosa", "Unisex", "", 9),
                crearProducto("Fakhar Black", "Fakhar", 422.40, "/img/Fakhar Black.webp", "Fragancia oscura y seductora", "Unisex", "ON SALE 12%", 12),
                crearProducto("Fakhar Extrait (Gold)", "Fakhar", 520.00, "/img/Fakhar Extrait (Gold).webp", "Extrait de parfum dorado", "Unisex", "", 6),
                crearProducto("Honor And Glory", "Honor And Glory", 311.60, "/img/Honor And Glory.webp", "Fragancia fresca y elegante", "Unisex", "ON SALE 18%", 14),
                crearProducto("Ishq Al Shuyukh", "Ishq", 490.00, "/img/Ishq Al Shuyukh.webp", "Fragancia árabe exclusiva", "Unisex", "", 4),
                crearProducto("Khamrah Dukhan", "Khamrah", 391.00, "/img/khamrah dukhan.webp", "Fragancia ahumada y cálida", "Unisex", "ON SALE 15%", 11),
                crearProducto("Khamrah Qahwa", "Khamrah", 580.00, "/img/khamrah Qahwa.webp", "Notas de café árabe", "Unisex", "", 8),
                crearProducto("Khamrah", "Khamrah", 378.00, "/img/khamrah.webp", "Fragancia dulce y especiada", "Unisex", "ON SALE 10%", 13),
                crearProducto("Mandarin Sky Elixir", "Mandarin Sky", 340.00, "/img/Mandarin Sky Elixir.webp", "Notas cítricas y frescas", "Unisex", "", 9),
                crearProducto("Mandarin Sky", "Mandarin Sky", 312.00, "/img/Mandarin Sky.webp", "Fragancia cítrica luminosa", "Unisex", "ON SALE 20%", 16),
                crearProducto("Odyssey Artisto", "Odyssey", 360.00, "/img/Odyssey Artisto.webp", "Fragancia artística y única", "Unisex", "", 7),
                crearProducto("Oud For Glory", "Oud For Glory", 238.00, "/img/Oud For Glory.webp", "Oud clásico y elegante", "Unisex", "ON SALE 15%", 18),
                crearProducto("Shaheen Gold", "Shaheen", 510.00, "/img/Shaheen Gold.webp", "Fragancia dorada y opulenta", "Unisex", "", 5),
                crearProducto("Sublime", "Lattafa", 378.40, "/img/Sublime.webp", "Fragancia sublime y sofisticada", "Unisex", "ON SALE 12%", 10),
                crearProducto("Vintage Radio", "Vintage Radio", 410.00, "/img/Vintage Radio.webp", "Fragancia retro y elegante", "Unisex", "", 6),
                crearProducto("Xerjoff Erba Pura", "Xerjoff", 410.00, "/img/Xerjoff Erba Pura.webp", "Fragancia premium italiana", "Unisex", "", 4),
                crearProducto("Valentino Uomo", "Valentino", 410.00, "/img/Valentino Uomo.webp", "Fragancia masculina italiana", "Para Él", "", 8),
                crearProducto("Carolina Herrera Good Girl", "Carolina Herrera", 410.00, "/img/CAROLINA HERRERA GOOD GIRL.webp", "Fragancia femenina icónica", "Para Ella", "", 11)
            );
            productoRepository.saveAll(iniciales);
            System.out.println(">>> Productos iniciales cargados en MySQL: " + iniciales.size());
        }
    }

    private Producto crearProducto(String nombre, String marca, Double precio,
                                    String imageUrl, String descripcion,
                                    String categoria, String descuento, Integer stock) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setMarca(marca);
        p.setPrecio(precio);
        p.setImageUrl(imageUrl);
        p.setDescripcion(descripcion);
        p.setCategoria(categoria);
        p.setDescuentoLabel(descuento);
        p.setStock(stock);
        return p;
    }
}
