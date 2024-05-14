package com.romashkaco.testcase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romashkaco.testcase.delivery.controller.DeliveryController;
import com.romashkaco.testcase.delivery.dto.DeliveryDto;
import com.romashkaco.testcase.delivery.service.DeliveryService;
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
public class DeliveryControllerTest {

    @Mock
    DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;

    private final DeliveryDto deliveryDto = new DeliveryDto(1, "Молоток", 3);

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(deliveryController)
                .build();
    }

    @Test
    void createDeliveryTest() throws Exception {
        when(deliveryService.create(any()))
                .thenReturn(deliveryDto);

        mvc.perform(post("/product/delivery")
                        .content(objectMapper.writeValueAsString(deliveryDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(deliveryDto.getId())))
                .andExpect(jsonPath("$.name", is(deliveryDto.getName())))
                .andExpect(jsonPath("$.amount", is(deliveryDto.getAmount())));

    }

    @Test
    void getAllDeliveriesTest() throws Exception {
        when(deliveryService.getAll())
                .thenReturn(List.of(deliveryDto));

        mvc.perform(get("/product/delivery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(deliveryDto.getId())))
                .andExpect(jsonPath("$[0].name", is(deliveryDto.getName())))
                .andExpect(jsonPath("$[0].amount", is(deliveryDto.getAmount())));
    }

    @Test
    void getDeliveryByIdTest() throws Exception {
        when(deliveryService.getById(anyInt()))
                .thenReturn(deliveryDto);

        mvc.perform(get("/product/delivery/" + deliveryDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(deliveryDto.getId())))
                .andExpect(jsonPath("$.name", is(deliveryDto.getName())))
                .andExpect(jsonPath("$.amount", is(deliveryDto.getAmount())));
    }

    @Test
    void deleteDeliveryTest() throws Exception {
        mvc.perform(delete("/product/delivery/" + deliveryDto.getId()))
                .andExpect(status().isNoContent());
    }

}
