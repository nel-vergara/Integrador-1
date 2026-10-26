package com.medina.fragrances.fragrance_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @NotBlank(message = "La marca es obligatoria")
    @Column(name = "marca", length = 100, nullable = false)
    private String marca;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "descripcion", length = 300)
    private String descripcion;

    @Column(name = "categoria", length = 50)
    private String categoria;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "descuento_label", length = 50)
    private String descuentoLabel;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    // Metodo utilitario para mostrar precio formateado
    public String getPrecioFormateado() {
        return String.format("S/. %.2f", precio);
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescuentoLabel() { return descuentoLabel; }
    public void setDescuentoLabel(String descuentoLabel) { this.descuentoLabel = descuentoLabel; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
