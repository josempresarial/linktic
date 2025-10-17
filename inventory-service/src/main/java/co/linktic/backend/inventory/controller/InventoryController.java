package co.linktic.backend.inventory.controller;

import co.linktic.backend.inventory.model.InventoryItem;
import co.linktic.backend.inventory.model.Purchase;
import co.linktic.backend.inventory.repository.InventoryRepository;
import co.linktic.backend.inventory.repository.PurchaseRepository;
import co.linktic.backend.inventory.client.ProductsClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Objects;

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
public class InventoryController {
    private final InventoryRepository inventoryRepo;
    private final PurchaseRepository purchaseRepo;
    private final ProductsClient productsClient;
    private final String apiKey;

    public InventoryController(InventoryRepository inventoryRepo, PurchaseRepository purchaseRepo,
                               ProductsClient productsClient, @Value("${app.api-key:inventory-secret-key}") String apiKey) {
        this.inventoryRepo = inventoryRepo;
        this.purchaseRepo = purchaseRepo;
        this.productsClient = productsClient;
        this.apiKey = apiKey;
    }

    private void verifyApiKey(String key) {
        if (!Objects.equals(key, apiKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }
    }

    @GetMapping("/inventory/{productId}")
    public ResponseEntity<Map<String, Map<String, Object>>> getInventory(@PathVariable Long productId,
                                                           @RequestHeader(value="X-API-Key", required=false) String key) {
        verifyApiKey(key);
        return inventoryRepo.findById(productId)
            .map(item -> ResponseEntity.ok(Map.of("data", Map.of("type","inventory","id", productId.toString(), "attributes",
                Map.of("cantidad", item.getCantidad())))))
            .orElseGet(() -> ResponseEntity.status(404).body(Map.of("errors", Map.of("detail","Inventory not found"))));
    }

    @PutMapping("/inventory/{productId}")
    public ResponseEntity<Map<String,Object>> updateInventory(@PathVariable Long productId,
                                                              @RequestBody Map<String,Object> body,
                                                              @RequestHeader(value="X-API-Key", required=false) String key) {
        verifyApiKey(key);
        Integer cantidad = Integer.valueOf(String.valueOf(body.get("cantidad")));
        InventoryItem item = inventoryRepo.findById(productId).orElse(new InventoryItem(productId, cantidad));
        item.setCantidad(cantidad);
        inventoryRepo.save(item);
        return ResponseEntity.ok(Map.of("data", Map.of("type","inventory","id", productId.toString(), "attributes", Map.of("cantidad", item.getCantidad()))));
    }

    @PostMapping("/purchase")
    public ResponseEntity<Map<String,Object>> purchase(@RequestBody Map<String,Object> body,
                                                       @RequestHeader(value="X-API-Key", required=false) String key) {
        verifyApiKey(key);
        Long productId = Long.valueOf(String.valueOf(body.get("productId")));
        Integer cantidad = Integer.valueOf(String.valueOf(body.get("cantidad")));

        Map<String,Object> productData = productsClient.getProduct(productId);
        if (productData == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return ResponseEntity.ok(performPurchaseTransactional(productId, cantidad, productData));
    }

    @Transactional
    public Map<String,Object> performPurchaseTransactional(Long productId, Integer cantidad, Map<String,Object> productData) {
        InventoryItem item = inventoryRepo.findById(productId).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Inventory not found"));
        if (item.getCantidad() < cantidad) {
            return Map.of("error","Insufficient inventory");
        }
        item.setCantidad(item.getCantidad() - cantidad);
        inventoryRepo.save(item);

        Map<String, Object> data = (Map<String, Object>) productData.get("data");
        Map<String, Object> attrs = (Map<String, Object>) data.get("attributes");
        Double precio = Double.valueOf(String.valueOf(attrs.get("precio")));
        Purchase purchase = new Purchase();
        purchase.setProductId(productId);
        purchase.setCantidad(cantidad);
        purchase.setPrecioUnitario(precio);
        purchaseRepo.save(purchase);

        return Map.of("data", Map.of("type","purchase","id", String.valueOf(purchase.getId()), "attributes",
            Map.of("product_id", purchase.getProductId(), "cantidad", purchase.getCantidad(), "precio_unitario", purchase.getPrecioUnitario(), "created_at", purchase.getCreatedAt().toString())
        ));
    }
}