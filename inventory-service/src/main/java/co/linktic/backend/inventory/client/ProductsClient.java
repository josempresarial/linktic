package co.linktic.backend.inventory.client;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

/**
 * 
 * <p>
 * The Class Client
 * </p>
 * 
 * @author <a href="mailto:joslopez.bernal@gmail.com">José Luis López Bernal</a>
 * 
 * @version 1.0
 *
 */

@Component
public class ProductsClient {
    private final WebClient client;
    private final String apiKey;

    public ProductsClient(@Value("${app.products.url:http://products:8081}") String baseUrl,
                          @Value("${app.products.api-key:products-secret-key}") String apiKey,
                          WebClient.Builder builder) {
        this.client = builder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public Map<String,Object> getProduct(Long id) {
        try {
            Mono<Map> mono = client.get()
                .uri("/products/{id}", id)
                .header("X-API-Key", apiKey)
                .retrieve()
                .bodyToMono(Map.class);
            return (Map<String,Object>) mono.block(Duration.ofSeconds(5));
        } catch (WebClientResponseException.NotFound ex) {
            return null;
        } catch (Exception ex) {
            throw new RuntimeException("Products service unreachable", ex);
        }
    }
}