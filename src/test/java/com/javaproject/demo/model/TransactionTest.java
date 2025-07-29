package com.javaproject.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;
    private TransactionCsv transactionCsv;

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        transaction.setTransactionId("txn_000123");
        transaction.setUserId("user456");
        transaction.setAmount(new BigDecimal("99.99"));
        transaction.setCategory("Entertainment");
        transaction.setTransactionDate(LocalDate.of(2024, 2, 15));

        transactionCsv = new TransactionCsv();
        transactionCsv.setTransactionId("txn_000456");
        transactionCsv.setUserId("csvUser");
        transactionCsv.setAmount(new BigDecimal("150.50"));
        transactionCsv.setCategory("Travel");
        transactionCsv.setTransactionDate(LocalDate.of(2024, 3, 10));
    }

    @Test
    void defaultConstructorTest() {
        // When
        Transaction newTransaction = new Transaction();

        // Then
        assertThat(newTransaction).isNotNull();
        assertThat(newTransaction.getTransactionId()).isNull();
        assertThat(newTransaction.getUserId()).isNull();
        assertThat(newTransaction.getAmount()).isNull();
        assertThat(newTransaction.getCategory()).isNull();
        assertThat(newTransaction.getTransactionDate()).isNull();
    }

    @Test
    void allArgsConstructorTest() {
        // When
        Transaction newTransaction = new Transaction("txn_000999", "testUser", 
                new BigDecimal("250.75"), "Health", LocalDate.of(2024, 4, 20));

        // Then
        assertThat(newTransaction.getTransactionId()).isEqualTo("txn_000999");
        assertThat(newTransaction.getUserId()).isEqualTo("testUser");
        assertThat(newTransaction.getAmount()).isEqualTo(new BigDecimal("250.75"));
        assertThat(newTransaction.getCategory()).isEqualTo("Health");
        assertThat(newTransaction.getTransactionDate()).isEqualTo(LocalDate.of(2024, 4, 20));
    }

    @Test
    void constructorFromTransactionCsvTest() {
        // When
        Transaction newTransaction = new Transaction(transactionCsv);

        // Then
        assertThat(newTransaction.getTransactionId()).isEqualTo("txn_000456");
        assertThat(newTransaction.getUserId()).isEqualTo("csvUser");
        assertThat(newTransaction.getAmount()).isEqualTo(new BigDecimal("150.50"));
        assertThat(newTransaction.getCategory()).isEqualTo("Travel");
        assertThat(newTransaction.getTransactionDate()).isEqualTo(LocalDate.of(2024, 3, 10));
    }

    @Test
    void toTransactionCsvTest() {
        // When
        TransactionCsv converted = transaction.toTransactionCsv();

        // Then
        assertThat(converted.getTransactionId()).isEqualTo("txn_000123");
        assertThat(converted.getUserId()).isEqualTo("user456");
        assertThat(converted.getAmount()).isEqualTo(new BigDecimal("99.99"));
        assertThat(converted.getCategory()).isEqualTo("Entertainment");
        assertThat(converted.getTransactionDate()).isEqualTo(LocalDate.of(2024, 2, 15));
    }

    @Test
    void gettersAndSettersTest() {
        // Given
        Transaction testTransaction = new Transaction();

        // When & Then
        testTransaction.setTransactionId("testId");
        assertThat(testTransaction.getTransactionId()).isEqualTo("testId");

        testTransaction.setUserId("testUser");
        assertThat(testTransaction.getUserId()).isEqualTo("testUser");

        BigDecimal testAmount = new BigDecimal("123.45");
        testTransaction.setAmount(testAmount);
        assertThat(testTransaction.getAmount()).isEqualTo(testAmount);

        testTransaction.setCategory("testCategory");
        assertThat(testTransaction.getCategory()).isEqualTo("testCategory");

        LocalDate testDate = LocalDate.of(2024, 5, 15);
        testTransaction.setTransactionDate(testDate);
        assertThat(testTransaction.getTransactionDate()).isEqualTo(testDate);
    }

    @Test
    void toStringTest() {
        // When
        String result = transaction.toString();

        // Then
        assertThat(result).contains("Transaction{");
        assertThat(result).contains("transactionId='txn_000123'");
        assertThat(result).contains("userId='user456'");
        assertThat(result).contains("amount=99.99");
        assertThat(result).contains("category='Entertainment'");
        assertThat(result).contains("transactionDate=2024-02-15");
    }

    @Test
    void equalsTest() {
        // Given
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId("txn_000001");
        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId("txn_000001");
        Transaction transaction3 = new Transaction();
        transaction3.setTransactionId("txn_000002");

        // When & Then
        assertThat(transaction1).isEqualTo(transaction2);
        assertThat(transaction1).isNotEqualTo(transaction3);
        assertThat(transaction1).isEqualTo(transaction1); // reflexive
        assertThat(transaction1).isNotEqualTo(null);
        assertThat(transaction1).isNotEqualTo("not a transaction");
    }

    @Test
    void hashCodeTest() {
        // Given
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId("txn_000001");
        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId("txn_000001");

        // When & Then
        assertThat(transaction1.hashCode()).isEqualTo(transaction2.hashCode());
    }

    @Test
    void equalsWithNullTransactionIdTest() {
        // Given
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();

        // When & Then
        assertThat(transaction1).isEqualTo(transaction2);
    }

    @Test
    void hashCodeWithNullTransactionIdTest() {
        // Given
        Transaction transactionWithNullId = new Transaction();

        // When & Then
        assertThat(transactionWithNullId.hashCode()).isEqualTo(0);
    }
}
