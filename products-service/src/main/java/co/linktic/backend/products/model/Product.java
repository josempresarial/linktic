package co.linktic.backend.products.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * 
 * <p>
 * The Class Entity
 * </p>
 * 
 * @author <a href="mailto:joslopez.bernal@gmail.com">José Luis López Bernal</a>
 * 
 * @version 1.0
 *
 */

@Entity
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Double precio;
    private String descripcion;

    public Product() {}

    public Product(String nombre, Double precio, String descripcion) {
        this.nombre = nombre; this.precio = precio; this.descripcion = descripcion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product p = (Product) o;
        return Objects.equals(id, p.id);
    }
    @Override
    public int hashCode() { return Objects.hash(id); }
}