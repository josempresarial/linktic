package com.example.products;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void createAndGetProduct() throws Exception {
        mvc.perform(post("/products")
            .contentType("application/json")
            .content("{"nombre":"Test","precio":10.5}"))
            .andExpect(status().isCreated());

        mvc.perform(get("/products/1"))
            .andExpect(status().isOk());
    }
}