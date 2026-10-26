package com.medina.fragrances.fragrance_app.controller;

import com.medina.fragrances.fragrance_app.model.Usuario;
import com.medina.fragrances.fragrance_app.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelControllerAdvice {

    private final UsuarioService usuarioService;

    public GlobalModelControllerAdvice(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void agregarDatosDeSesion(Model model, HttpSession session, Authentication authentication) {
        Object nombreSesion = session.getAttribute("nombreUsuario");
        Object rolSesion = session.getAttribute("rolUsuario");

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            Usuario usuario = usuarioService.buscarPorEmail(authentication.getName()).orElse(null);
            if (usuario != null) {
                nombreSesion = usuario.getNombre();
                rolSesion = usuario.getRol();
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("nombreUsuario", usuario.getNombre());
                session.setAttribute("rolUsuario", usuario.getRol());
                session.setAttribute("adminAutenticado", "ADMIN".equals(usuario.getRol()));
            }
        }

        model.addAttribute("nombreUsuario", nombreSesion);
        model.addAttribute("esAdmin", "ADMIN".equals(rolSesion));
    }
}
