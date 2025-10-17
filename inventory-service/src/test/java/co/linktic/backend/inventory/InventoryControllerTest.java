package co.linktic.backend.inventory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import co.linktic.backend.inventory.client.ProductsClient;
import co.linktic.backend.inventory.model.InventoryItem;
import co.linktic.backend.inventory.repository.InventoryRepository;

/**
 * 
 * <p>
 * The purchaseReducesInventory 
 * </p>
 * 
 * @author <a href="mailto:joslopez.bernal@gmail.com">José Luis López Bernal</a>
 * 
 * @version 1.0
 *
 */

@SpringBootTest(properties = { "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.username=sa",
		"spring.datasource.password=", "spring.jpa.hibernate.ddl-auto=create-drop" })
@AutoConfigureMockMvc
class InventoryControllerTest {
	@Autowired
	MockMvc mvc;
	@MockBean
	ProductsClient productsClient;
	@Autowired
	InventoryRepository repo;

	@Test
	void purchaseReducesInventory() throws Exception {
		when(productsClient.getProduct(1L))
				.thenReturn(Map.of("data", Map.of("id", 1L, "type", "product", "attributes", Map.of("precio", 10.0))));
		repo.save(new InventoryItem(1L, 5));
		mvc.perform(post("/purchase").contentType(MediaType.APPLICATION_JSON)
				.header("X-API-Key", "inventory-secret-key").content("{\"productId\":1,\"cantidad\":2}"))
				.andExpect(status().isOk());
		var item = repo.findById(1L).get();
		assertEquals(3, item.getCantidad());
	}
}
