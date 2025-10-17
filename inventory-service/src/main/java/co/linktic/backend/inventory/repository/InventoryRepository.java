package co.linktic.backend.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.linktic.backend.inventory.model.InventoryItem;

/**
 * 
 * <p>
 * The Interface Repository
 * </p>
 * 
 * @author <a href="mailto:joslopez.bernal@gmail.com">José Luis López Bernal</a>
 * 
 * @version 1.0
 *
 */

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {}