package com.medina.fragrances.fragrance_app.controller;

import com.medina.fragrances.fragrance_app.model.Contacto;
import com.medina.fragrances.fragrance_app.service.ContactoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactoController {

    private final ContactoService contactoService;

    public ContactoController(ContactoService contactoService) {
        this.contactoService = contactoService;
    }

    @GetMapping({"/contacto", "/contacto.html"})
    public String mostrarContacto(Model model, HttpSession session) {
        model.addAttribute("contacto", new Contacto());
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        return "contacto";
    }

    @PostMapping("/contacto/enviar")
    public String enviarMensaje(@Valid @ModelAttribute("contacto") Contacto contacto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "contacto";
        }
        contactoService.guardar(contacto);
        return "redirect:/contacto?enviado=true";
    }
}
