package com.medina.fragrances.fragrance_app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping({ "/", "/index.html" })
    public String home(HttpSession session, Model model) {
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        return "home/index";
    }

    @GetMapping({ "/marcas", "/marcas.html" })
    public String marcas(HttpSession session, Model model) {
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        return "marcas";
    }

    @GetMapping({ "/decants", "/decants.html" })
    public String decants(HttpSession session, Model model) {
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        return "decants";
    }

    @GetMapping({ "/carrito", "/carrito.html" })
    public String carrito(HttpSession session, Model model) {
        model.addAttribute("nombreUsuario", session.getAttribute("nombreUsuario"));
        return "carrito";
    }

    @GetMapping({ "/privacidad", "/privacidad.html" })
    public String privacidad() { return "privacidad"; }

    @GetMapping({ "/terminos", "/terminos.html" })
    public String terminos() { return "terminos"; }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/{page}.html")
    public String htmlPage(@PathVariable String page) {
        if ("index".equals(page)) return "home/index";
        if ("login".equals(page) || "registro".equals(page)) return "autch/" + page;
        return page;
    }
}
