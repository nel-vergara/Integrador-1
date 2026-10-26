package com.medina.fragrances.fragrance_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegistroUsuarioDto {

    @NotEmpty(message = "El nombre completo es obligatorio")
    private String nombre;

    @NotEmpty(message = "El correo es obligatorio")
    @Email(message = "Ingrese un correo valido")
    private String email;

    @NotEmpty(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;

    @NotEmpty(message = "El telefono es obligatorio")
    @Size(min = 9, max = 9, message = "El telefono debe tener 9 digitos")
    @Pattern(regexp = "^[0-9]+$", message = "El telefono solo debe contener numeros")
    private String telefono;

    @NotEmpty(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener exactamente 8 digitos")
    @Pattern(regexp = "^[0-9]+$", message = "El DNI solo debe contener numeros")
    private String dni;

    @NotEmpty(message = "La direccion es obligatoria")
    private String direccion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
