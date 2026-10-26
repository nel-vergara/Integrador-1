package com.medina.fragrances.fragrance_app.repository;

import com.medina.fragrances.fragrance_app.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
}
