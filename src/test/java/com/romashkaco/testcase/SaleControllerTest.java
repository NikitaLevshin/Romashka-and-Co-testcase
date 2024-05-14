package com.romashkaco.testcase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romashkaco.testcase.delivery.controller.DeliveryController;
import com.romashkaco.testcase.delivery.dto.DeliveryDto;
import com.romashkaco.testcase.delivery.service.DeliveryService;
import com.romashkaco.testcase.sale.controller.SaleController;
import com.romashkaco.testcase.sale.dto.SaleFullDto;
import com.romashkaco.testcase.sale.dto.SaleNewDto;
import com.romashkaco.testcase.sale.service.SaleService;
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
public class SaleControllerTest {

    @Mock
    SaleService saleService;

    @InjectMocks
    private SaleController saleController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;

    private final SaleFullDto saleFullDto = new SaleFullDto(1, "Молоток", 3, 100);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(saleController)
                .build();
    }

    @Test
    void createSaleTest() throws Exception {
        when(saleService.create(any()))
                .thenReturn(saleFullDto);

        mvc.perform(post("/product/sale")
                        .content(objectMapper.writeValueAsString(saleFullDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(saleFullDto.getId())))
                .andExpect(jsonPath("$.name", is(saleFullDto.getName())))
                .andExpect(jsonPath("$.amount", is(saleFullDto.getAmount())))
                .andExpect(jsonPath("$.sum", is(saleFullDto.getSum())));

    }

    @Test
    void getAllSalesTest() throws Exception {
        when(saleService.getAll())
                .thenReturn(List.of(saleFullDto));

        mvc.perform(get("/product/sale"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(saleFullDto.getId())))
                .andExpect(jsonPath("$[0].name", is(saleFullDto.getName())))
                .andExpect(jsonPath("$[0].amount", is(saleFullDto.getAmount())))
                .andExpect(jsonPath("$[0].sum", is(saleFullDto.getSum())));
    }

    @Test
    void getSaleByIdTest() throws Exception {
        when(saleService.getById(anyInt()))
                .thenReturn(saleFullDto);

        mvc.perform(get("/product/sale/" + saleFullDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saleFullDto.getId())))
                .andExpect(jsonPath("$.name", is(saleFullDto.getName())))
                .andExpect(jsonPath("$.amount", is(saleFullDto.getAmount())))
                .andExpect(jsonPath("$.sum", is(saleFullDto.getSum())));
    }

    @Test
    void deleteSaleTest() throws Exception {
        mvc.perform(delete("/product/sale/" + saleFullDto.getId()))
                .andExpect(status().isNoContent());
    }
}
