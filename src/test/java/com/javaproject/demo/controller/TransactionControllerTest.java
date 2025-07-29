package com.javaproject.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaproject.demo.model.Transaction;
import com.javaproject.demo.model.TransactionCsv;
import com.javaproject.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TransactionController.class)
@SpringJUnitConfig
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Transaction testTransaction;
    private TransactionCsv testTransactionCsv;

    @BeforeEach
    void setUp() {
        testTransaction = new Transaction();
        testTransaction.setTransactionId("txn_000001");
        testTransaction.setUserId("user123");
        testTransaction.setAmount(new BigDecimal("100.50"));
        testTransaction.setCategory("Food");
        testTransaction.setTransactionDate(LocalDate.of(2024, 1, 15));

        testTransactionCsv = new TransactionCsv();
        testTransactionCsv.setTransactionId("txn_000001");
        testTransactionCsv.setUserId("user123");
        testTransactionCsv.setAmount(new BigDecimal("100.50"));
        testTransactionCsv.setCategory("Food");
        testTransactionCsv.setTransactionDate(LocalDate.of(2024, 1, 15));
    }

    @Test
    void getAllTransactionsTest() throws Exception {
        // Given
        List<Transaction> transactions = Arrays.asList(testTransaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // When & Then
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].transactionId").value("txn_000001"))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].amount").value(100.50))
                .andExpect(jsonPath("$[0].category").value("Food"));

        verify(transactionRepository).findAll();
    }

    @Test
    void getTransactionByIdFoundTest() throws Exception {
        // Given
        when(transactionRepository.findById("txn_000001")).thenReturn(Optional.of(testTransaction));

        // When & Then
        mockMvc.perform(get("/api/transactions/txn_000001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value("txn_000001"))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.category").value("Food"));

        verify(transactionRepository).findById("txn_000001");
    }

    @Test
    void getTransactionByIdNotFoundTest() throws Exception {
        // Given
        when(transactionRepository.findById("txn_999999")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/transactions/txn_999999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(transactionRepository).findById("txn_999999");
    }

    @Test
    void getTransactionsByUserIdTest() throws Exception {
        // Given
        List<Transaction> userTransactions = Arrays.asList(testTransaction);
        when(transactionRepository.findByUserId("user123")).thenReturn(userTransactions);

        // When & Then
        mockMvc.perform(get("/api/transactions/user/user123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].transactionId").value("txn_000001"))
                .andExpect(jsonPath("$[0].userId").value("user123"));

        verify(transactionRepository).findByUserId("user123");
    }

    @Test
    void getTransactionsByCategoryTest() throws Exception {
        // Given
        List<Transaction> categoryTransactions = Arrays.asList(testTransaction);
        when(transactionRepository.findByCategory("Food")).thenReturn(categoryTransactions);

        // When & Then
        mockMvc.perform(get("/api/transactions/category/Food"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].transactionId").value("txn_000001"))
                .andExpect(jsonPath("$[0].category").value("Food"));

        verify(transactionRepository).findByCategory("Food");
    }

    @Test
    void createTransactionSuccessTest() throws Exception {
        // Given
        Transaction savedTransaction = new Transaction();
        savedTransaction.setTransactionId("txn_100000");
        savedTransaction.setUserId("user123");
        savedTransaction.setAmount(new BigDecimal("100.50"));
        savedTransaction.setCategory("Food");
        savedTransaction.setTransactionDate(LocalDate.now());

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(testTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionCsv newTransactionCsv = new TransactionCsv();
        newTransactionCsv.setUserId("user123");
        newTransactionCsv.setAmount(new BigDecimal("100.50"));
        newTransactionCsv.setCategory("Food");

        // When & Then
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTransactionCsv)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value("txn_100000"))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.amount").value(100.50))
                .andExpect(jsonPath("$.category").value("Food"));

        verify(transactionRepository).findAll();
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void updateTransactionSuccessTest() throws Exception {
        // Given
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setTransactionId("txn_000001");
        updatedTransaction.setUserId("user123");
        updatedTransaction.setAmount(new BigDecimal("150.75"));
        updatedTransaction.setCategory("Transport");
        updatedTransaction.setTransactionDate(LocalDate.of(2024, 1, 15));

        when(transactionRepository.findById("txn_000001")).thenReturn(Optional.of(testTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(updatedTransaction);

        TransactionCsv updateCsv = new TransactionCsv();
        updateCsv.setAmount(new BigDecimal("150.75"));
        updateCsv.setCategory("Transport");

        // When & Then
        mockMvc.perform(put("/api/transactions/txn_000001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCsv)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value("txn_000001"))
                .andExpect(jsonPath("$.amount").value(150.75))
                .andExpect(jsonPath("$.category").value("Transport"));

        verify(transactionRepository).findById("txn_000001");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void updateTransactionNotFoundTest() throws Exception {
        // Given
        when(transactionRepository.findById("txn_999999")).thenReturn(Optional.empty());

        TransactionCsv updateCsv = new TransactionCsv();
        updateCsv.setAmount(new BigDecimal("150.75"));
        updateCsv.setCategory("Transport");

        // When & Then
        mockMvc.perform(put("/api/transactions/txn_999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCsv)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(transactionRepository).findById("txn_999999");
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void deleteTransactionSuccessTest() throws Exception {
        // Given
        when(transactionRepository.existsById("txn_000001")).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/transactions/txn_000001"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction with ID txn_000001 deleted successfully."));

        verify(transactionRepository).existsById("txn_000001");
        verify(transactionRepository).deleteById("txn_000001");
    }

    @Test
    void deleteTransactionNotFoundTest() throws Exception {
        // Given
        when(transactionRepository.existsById("txn_999999")).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/transactions/txn_999999"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction with ID txn_999999 not found."));

        verify(transactionRepository).existsById("txn_999999");
        verify(transactionRepository, never()).deleteById("txn_999999");
    }
}
