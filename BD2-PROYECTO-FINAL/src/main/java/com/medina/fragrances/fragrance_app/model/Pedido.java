package com.medina.fragrances.fragrance_app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relacion con Usuario (muchos pedidos -> un usuario)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacion con Producto (muchos pedidos -> un producto)
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "estado", length = 50)
    private String estado = "PENDIENTE";

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Column(name = "metodo_entrega", length = 50)
    private String metodoEntrega;

    @Column(name = "direccion_entrega", length = 200)
    private String direccionEntrega;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Column(name = "notas", length = 500)
    private String notas;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
        if (this.producto != null && this.cantidad != null) {
            this.total = this.producto.getPrecio() * this.cantidad;
        }
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getMetodoEntrega() { return metodoEntrega; }
    public void setMetodoEntrega(String metodoEntrega) { this.metodoEntrega = metodoEntrega; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
