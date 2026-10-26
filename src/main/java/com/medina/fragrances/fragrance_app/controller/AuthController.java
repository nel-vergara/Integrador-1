package com.medina.fragrances.fragrance_app.controller;

import com.medina.fragrances.fragrance_app.dto.RegistroUsuarioDto;
import com.medina.fragrances.fragrance_app.model.Usuario;
import com.medina.fragrances.fragrance_app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new RegistroUsuarioDto());
        return "autch/registro";
    }

    @PostMapping("/auth/registrar")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") RegistroUsuarioDto registroDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "autch/registro";
        }

        if (usuarioService.existeEmail(registroDto.getEmail())) {
            model.addAttribute("errorEmail", "Este correo ya esta registrado. Intente con otro.");
            return "autch/registro";
        }

        if (usuarioService.existeDni(registroDto.getDni())) {
            model.addAttribute("errorDni", "Este DNI ya esta registrado.");
            return "autch/registro";
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroDto.getNombre());
        usuario.setEmail(registroDto.getEmail());
        usuario.setPassword(registroDto.getPassword());
        usuario.setTelefono(registroDto.getTelefono());
        usuario.setDni(registroDto.getDni());
        usuario.setDireccion(registroDto.getDireccion());
        usuario.setRol("CLIENTE");
        usuarioService.registrar(usuario);
        return "redirect:/login?registrado=true";
    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "registrado", required = false) String registrado,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               @RequestParam(value = "redirect", required = false, defaultValue = "/") String redirect,
                               Model model) {
        usuarioService.asegurarAdminPrincipal();
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("redirectUrl", redirect);
        if (registrado != null) {
            model.addAttribute("mensajeExito", "Cuenta creada exitosamente. Ya puedes iniciar sesion.");
        }
        if (error != null) {
            model.addAttribute("errorLogin", "Correo o contrasena incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("mensajeExito", "Sesion cerrada correctamente.");
        }
        return "autch/login";
    }
}
