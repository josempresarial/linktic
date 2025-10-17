package co.linktic.backend.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.linktic.backend.products.model.Product;

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

public interface ProductRepository extends JpaRepository<Product, Long> {}