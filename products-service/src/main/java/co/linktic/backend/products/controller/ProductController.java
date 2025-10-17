package co.linktic.backend.products.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.linktic.backend.products.model.Product;
import co.linktic.backend.products.repository.ProductRepository;

/**
 * 
 * <p>
 * The Class Controller
 * </p>
 * 
 * @author <a href="mailto:joslopez.bernal@gmail.com">José Luis López Bernal</a>
 * 
 * @version 1.0
 *
 */

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository repo;
    public ProductController(ProductRepository repo) { this.repo = repo; }

    @PostMapping
    public ResponseEntity<Map<String,Object>> create(@RequestBody Map<String,Object> body) {
        String nombre = (String) body.get("nombre");
        Double precio = body.get("precio") == null ? null : Double.valueOf(String.valueOf(body.get("precio")));
        String descripcion = (String) body.get("descripcion");
        Product p = new Product(nombre, precio, descripcion);
        p = repo.save(p);
        return ResponseEntity.status(201).body(Map.of(
            "data", Map.of("type","product","id", String.valueOf(p.getId()), "attributes",
                Map.of("nombre", p.getNombre(), "precio", p.getPrecio(), "descripcion", p.getDescripcion()))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Map<String, Object>>> get(@PathVariable Long id) {
        return repo.findById(id)
            .map(p -> ResponseEntity.ok(Map.of("data", Map.of("type","product","id", String.valueOf(p.getId()), "attributes",
                Map.of("nombre", p.getNombre(), "precio", p.getPrecio(), "descripcion", p.getDescripcion()))
            )))
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of("errors", Map.of("detail","Product not found"))));
    }
    
    @GetMapping
    public List<Product> list() { 
        return repo.findAll(); 
    }	
}