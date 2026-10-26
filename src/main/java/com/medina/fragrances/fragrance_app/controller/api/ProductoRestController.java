package com.medina.fragrances.fragrance_app.controller.api;

import com.medina.fragrances.fragrance_app.dto.ProductoDto;
import com.medina.fragrances.fragrance_app.model.Producto;
import com.medina.fragrances.fragrance_app.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoRestController {

    private final ProductoService productoService;

    public ProductoRestController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listar(@RequestParam(required = false) String buscar,
                                                 @RequestParam(required = false) String categoria) {
        if (buscar != null && !buscar.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorNombre(buscar));
        }

        if (categoria != null && !categoria.isBlank()) {
            return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
        }

        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoDto productoDto) {
        Producto producto = new Producto();
        copiarDatos(productoDto, producto);
        Producto creado = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody ProductoDto productoDto) {
        return productoService.buscarPorId(id)
                .map(producto -> {
                    copiarDatos(productoDto, producto);
                    return ResponseEntity.ok(productoService.guardar(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private void copiarDatos(ProductoDto origen, Producto destino) {
        destino.setNombre(origen.getNombre());
        destino.setMarca(origen.getMarca());
        destino.setPrecio(origen.getPrecio());
        destino.setDescripcion(origen.getDescripcion());
        destino.setCategoria(origen.getCategoria());
        destino.setImageUrl(origen.getImageUrl());
        destino.setDescuentoLabel(origen.getDescuentoLabel());
        destino.setStock(origen.getStock());
    }
}
