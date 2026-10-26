package com.medina.fragrances.fragrance_app.repository;

import com.medina.fragrances.fragrance_app.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByMarca(String marca);
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
}
