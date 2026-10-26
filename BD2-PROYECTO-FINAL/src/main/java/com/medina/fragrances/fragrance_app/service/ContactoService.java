package com.medina.fragrances.fragrance_app.service;

import com.medina.fragrances.fragrance_app.model.Contacto;
import com.medina.fragrances.fragrance_app.repository.ContactoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ContactoService {

    private final ContactoRepository contactoRepository;

    public ContactoService(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    @Transactional
    public Contacto guardar(Contacto contacto) {
        return contactoRepository.save(contacto);
    }

    @Transactional(readOnly = true)
    public List<Contacto> listarTodos() {
        return contactoRepository.findAll();
    }
}
