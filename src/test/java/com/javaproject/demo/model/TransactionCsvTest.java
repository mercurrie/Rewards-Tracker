package com.javaproject.demo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionCsvTest {

    private TransactionCsv transactionCsv;

    @BeforeEach
    void setUp() {
        transactionCsv = new TransactionCsv();
        transactionCsv.setTransactionId("txn_000123");
        transactionCsv.setUserId("testUser");
        transactionCsv.setAmount(new BigDecimal("99.99"));
        transactionCsv.setCategory("Food");
        transactionCsv.setTransactionDate(LocalDate.of(2024, 1, 15));
    }

    @Test
    void defaultConstructorTest() {
        // When
        TransactionCsv newTransactionCsv = new TransactionCsv();

        // Then
        assertThat(newTransactionCsv).isNotNull();
        assertThat(newTransactionCsv.getTransactionId()).isNull();
        assertThat(newTransactionCsv.getUserId()).isNull();
        assertThat(newTransactionCsv.getAmount()).isNull();
        assertThat(newTransactionCsv.getCategory()).isNull();
        assertThat(newTransactionCsv.getTransactionDate()).isNull();
    }

    @Test
    void allArgsConstructorTest() {
        // When
        TransactionCsv newTransactionCsv = new TransactionCsv("txn_000456", "user123", 
                new BigDecimal("150.50"), "Transport", LocalDate.of(2024, 2, 20));

        // Then
        assertThat(newTransactionCsv.getTransactionId()).isEqualTo("txn_000456");
        assertThat(newTransactionCsv.getUserId()).isEqualTo("user123");
        assertThat(newTransactionCsv.getAmount()).isEqualTo(new BigDecimal("150.50"));
        assertThat(newTransactionCsv.getCategory()).isEqualTo("Transport");
        assertThat(newTransactionCsv.getTransactionDate()).isEqualTo(LocalDate.of(2024, 2, 20));
    }

    @Test
    void gettersAndSettersTest() {
        // Given
        TransactionCsv testTransactionCsv = new TransactionCsv();

        // When & Then
        testTransactionCsv.setTransactionId("txn_000789");
        assertThat(testTransactionCsv.getTransactionId()).isEqualTo("txn_000789");

        testTransactionCsv.setUserId("newUser");
        assertThat(testTransactionCsv.getUserId()).isEqualTo("newUser");

        BigDecimal testAmount = new BigDecimal("200.75");
        testTransactionCsv.setAmount(testAmount);
        assertThat(testTransactionCsv.getAmount()).isEqualTo(testAmount);

        testTransactionCsv.setCategory("Entertainment");
        assertThat(testTransactionCsv.getCategory()).isEqualTo("Entertainment");

        LocalDate testDate = LocalDate.of(2024, 3, 25);
        testTransactionCsv.setTransactionDate(testDate);
        assertThat(testTransactionCsv.getTransactionDate()).isEqualTo(testDate);
    }

    @Test
    void toStringTest() {
        // When
        String result = transactionCsv.toString();

        // Then
        assertThat(result).contains("TransactionCsv{");
        assertThat(result).contains("transactionId='txn_000123'");
        assertThat(result).contains("userId='testUser'");
        assertThat(result).contains("amount=99.99");
        assertThat(result).contains("category='Food'");
        assertThat(result).contains("transactionDate=2024-01-15");
    }

    @Test
    void fieldValidationTest() {
        // Given
        TransactionCsv validTransaction = new TransactionCsv();
        validTransaction.setTransactionId("txn_000001");
        validTransaction.setUserId("user1");
        validTransaction.setAmount(new BigDecimal("50.00"));
        validTransaction.setCategory("Shopping");
        validTransaction.setTransactionDate(LocalDate.now());

        // Then
        assertThat(validTransaction.getTransactionId()).isNotEmpty();
        assertThat(validTransaction.getTransactionId()).startsWith("txn_");
        assertThat(validTransaction.getUserId()).isNotEmpty();
        assertThat(validTransaction.getAmount()).isPositive();
        assertThat(validTransaction.getCategory()).isNotEmpty();
        assertThat(validTransaction.getTransactionDate()).isNotNull();
    }

    @Test
    void amountPrecisionTest() {
        // Given
        BigDecimal preciseAmount = new BigDecimal("123.456789");
        
        // When
        transactionCsv.setAmount(preciseAmount);
        
        // Then
        assertThat(transactionCsv.getAmount()).isEqualTo(preciseAmount);
        assertThat(transactionCsv.getAmount().scale()).isEqualTo(6);
    }

    @Test
    void dateHandlingTest() {
        // Given
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        LocalDate currentDate = LocalDate.now();

        // When & Then
        transactionCsv.setTransactionDate(pastDate);
        assertThat(transactionCsv.getTransactionDate()).isEqualTo(pastDate);

        transactionCsv.setTransactionDate(futureDate);
        assertThat(transactionCsv.getTransactionDate()).isEqualTo(futureDate);

        transactionCsv.setTransactionDate(currentDate);
        assertThat(transactionCsv.getTransactionDate()).isEqualTo(currentDate);
    }

    @Test
    void negativeAmountTest() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-50.00");

        // When
        transactionCsv.setAmount(negativeAmount);

        // Then
        assertThat(transactionCsv.getAmount()).isEqualTo(negativeAmount);
        assertThat(transactionCsv.getAmount()).isNegative();
    }

    @Test
    void zeroAmountTest() {
        // Given
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // When
        transactionCsv.setAmount(zeroAmount);

        // Then
        assertThat(transactionCsv.getAmount()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void longUserIdTest() {
        // Given
        String longUserId = "very_long_user_id_that_exceeds_normal_length_but_should_still_work";

        // When
        transactionCsv.setUserId(longUserId);

        // Then
        assertThat(transactionCsv.getUserId()).isEqualTo(longUserId);
    }

    @Test
    void specialCharactersInCategoryTest() {
        // Given
        String specialCategory = "Food & Dining (Restaurant)";

        // When
        transactionCsv.setCategory(specialCategory);

        // Then
        assertThat(transactionCsv.getCategory()).isEqualTo(specialCategory);
    }

    @Test
    void transactionIdFormatTest() {
        // Given
        String[] validIds = {"txn_000001", "txn_999999", "txn_100000"};

        // When & Then
        for (String id : validIds) {
            transactionCsv.setTransactionId(id);
            assertThat(transactionCsv.getTransactionId()).isEqualTo(id);
            assertThat(transactionCsv.getTransactionId()).startsWith("txn_");
            assertThat(transactionCsv.getTransactionId()).hasSize(10); // "txn_" + 6 digits
        }
    }
}
