package com.javaproject.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.demo.model.TransactionCsv;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullTransactionLifecycleTest() throws Exception {
        // Create a new transaction
        TransactionCsv newTransaction = new TransactionCsv();
        newTransaction.setUserId("integrationUser");
        newTransaction.setAmount(new BigDecimal("125.75"));
        newTransaction.setCategory("Integration Test");

        String transactionJson = objectMapper.writeValueAsString(newTransaction);

        // POST - Create transaction
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("integrationUser"))
                .andExpect(jsonPath("$.amount").value(125.75))
                .andExpect(jsonPath("$.category").value("Integration Test"));

        // GET - Retrieve all transactions (should include our new one)
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        // GET - Retrieve by user ID
        mockMvc.perform(get("/api/transactions/user/integrationUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].userId").value("integrationUser"));

        // GET - Retrieve by category
        mockMvc.perform(get("/api/transactions/category/Integration Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].category").value("Integration Test"));
    }

    @Test
    void errorHandlingTest() throws Exception {
        // GET - Non-existent transaction (returns null, so empty response with 200)
        mockMvc.perform(get("/api/transactions/txn_999999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // PUT - Non-existent transaction (returns null, so empty response with 200)
        TransactionCsv updateRequest = new TransactionCsv();
        updateRequest.setAmount(new BigDecimal("200.00"));
        updateRequest.setCategory("Updated");

        mockMvc.perform(put("/api/transactions/txn_999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // DELETE - Non-existent transaction (returns specific message with 200)
        mockMvc.perform(delete("/api/transactions/txn_999999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction with ID txn_999999 not found."));
    }
}
