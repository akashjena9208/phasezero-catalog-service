package com.phasezero.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.dto.ProductResponse;
import com.phasezero.catalog.service.ProductService;
import com.phasezero.catalog.util.ResponseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;
    private ProductService productService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        productService = Mockito.mock(ProductService.class);
        ProductController controller = new ProductController(productService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addProduct_returns201() throws Exception {
        ProductRequest request = new ProductRequest(
                "P-1001", "Hydraulic Filter", "filters", 1200.50, 10
        );

        ProductResponse response = new ProductResponse(
                1L, "P-1001", "hydraulic filter", "filters", 1200.50, 10
        );

        Mockito.when(productService.addProduct(any())).thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.partNumber").value("P-1001"));
    }

//    @Test
//    void getAllProducts_returnsList() throws Exception {
//        Mockito.when(productService.getAllProducts())
//                .thenReturn(java.util.List.of(
//                        new ProductResponse(1L, "P-1001", "hydraulic filter", "filters", 1200.5, 10)
//                ));
//
//        mockMvc.perform(get("/products"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].partNumber").value("P-1001"));
//    }

    @Test
    void getAllProducts_returnsList() throws Exception {

        Mockito.when(productService.getAllProducts(0, 20))
                .thenReturn(List.of(
                        new ProductResponse(1L, "P-1001", "hydraulic filter", "filters", 1200.5, 10)
                ));

        mockMvc.perform(get("/products?page=0&size=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].partNumber").value("P-1001"));
    }

}
