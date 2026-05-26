package com.example.bfhl.controller;

import com.example.bfhl.dto.BfhlRequest;
import com.example.bfhl.dto.BfhlResponse;
import com.example.bfhl.service.BfhlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BfhlService bfhlService;

    @Test
    void testHandlePost_Success() throws Exception {
        BfhlRequest request = new BfhlRequest(Arrays.asList("a", "1"));
        BfhlResponse response = new BfhlResponse(
                true,
                "john_doe_17091999",
                "john.doe@example.com",
                "ABCD123",
                Collections.singletonList("1"),
                Collections.emptyList(),
                Collections.singletonList("A"),
                Collections.emptyList(),
                "1",
                "A"
        );

        Mockito.when(bfhlService.processRequest(Mockito.any(BfhlRequest.class))).thenReturn(response);

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success", is(true)))
                .andExpect(jsonPath("$.user_id", is("john_doe_17091999")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.roll_number", is("ABCD123")))
                .andExpect(jsonPath("$.odd_numbers[0]", is("1")))
                .andExpect(jsonPath("$.alphabets[0]", is("A")))
                .andExpect(jsonPath("$.sum", is("1")))
                .andExpect(jsonPath("$.concat_string", is("A")));
    }

    @Test
    void testHandlePost_ValidationFailure() throws Exception {
        BfhlRequest request = new BfhlRequest(null); // Will trigger @NotNull

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testHandleGet_Success() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code", is(1)));
    }
}
