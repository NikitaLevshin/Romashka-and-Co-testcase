package com.romashkaco.testcase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romashkaco.testcase.product.controller.ProductController;
import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.model.ProductSort;
import com.romashkaco.testcase.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Mock
    ProductService productService;

    @InjectMocks
    private ProductController productController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;
    private final ProductDto productDto = new ProductDto(
            1, "Молоток", "Хороший молоток", 122.99, true);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(productController)
                .build();
    }

    @Test
    void createProductTest() throws Exception {
        when(productService.create(any()))
                .thenReturn(productDto);

        mvc.perform(post("/product")
                        .content(objectMapper.writeValueAsString(productDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(productDto.getId())))
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice())))
                .andExpect(jsonPath("$.isOnStock", is(productDto.getIsOnStock())));

    }

    @Test
    void getAllProductsTest() throws Exception {
        when(productService.getAll("Молоток", 123.12, null,
                false, ProductSort.PRICEASC, 0))
                .thenReturn(List.of(productDto));

        mvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(productDto.getId())))
                .andExpect(jsonPath("$[0].name", is(productDto.getName())))
                .andExpect(jsonPath("$[0].description", is(productDto.getDescription())))
                .andExpect(jsonPath("$[0].price", is(productDto.getPrice())))
                .andExpect(jsonPath("$[0].isOnStock", is(productDto.getIsOnStock())));
    }

    @Test
    void getProductByIdTest() throws Exception {
        when(productService.getById(anyInt()))
                .thenReturn(productDto);

        mvc.perform(get("/product/" + productDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productDto.getId())))
                .andExpect(jsonPath("$.name", is(productDto.getName())))
                .andExpect(jsonPath("$.description", is(productDto.getDescription())))
                .andExpect(jsonPath("$.price", is(productDto.getPrice())))
                .andExpect(jsonPath("$.isOnStock", is(productDto.getIsOnStock())));
    }

    @Test
    void deleteProductTest() throws Exception {
        mvc.perform(delete("/product/" + productDto.getId()))
                .andExpect(status().isNoContent());
    }
}
