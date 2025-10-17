package co.linktic.backend.inventory.model;

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
@Table(name = "inventory")
public class InventoryItem {
    @Id
    private Long productId;
    private Integer cantidad;

    public InventoryItem() {}
    public InventoryItem(Long productId, Integer cantidad) { this.productId = productId; this.cantidad = cantidad; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryItem)) return false;
        InventoryItem that = (InventoryItem) o;
        return Objects.equals(productId, that.productId);
    }
    @Override
    public int hashCode() { return Objects.hash(productId); }
}