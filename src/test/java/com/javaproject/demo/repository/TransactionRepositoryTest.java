package com.javaproject.demo.repository;

import com.javaproject.demo.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction testTransaction1;
    private Transaction testTransaction2;
    private Transaction testTransaction3;

    @BeforeEach
    void setUp() {
        testTransaction1 = new Transaction();
        testTransaction1.setTransactionId("txn_000001");
        testTransaction1.setUserId("user123");
        testTransaction1.setAmount(new BigDecimal("100.50"));
        testTransaction1.setCategory("Food");
        testTransaction1.setTransactionDate(LocalDate.of(2024, 1, 15));

        testTransaction2 = new Transaction();
        testTransaction2.setTransactionId("txn_000002");
        testTransaction2.setUserId("user123");
        testTransaction2.setAmount(new BigDecimal("75.25"));
        testTransaction2.setCategory("Transport");
        testTransaction2.setTransactionDate(LocalDate.of(2024, 1, 16));

        testTransaction3 = new Transaction();
        testTransaction3.setTransactionId("txn_000003");
        testTransaction3.setUserId("user456");
        testTransaction3.setAmount(new BigDecimal("200.00"));
        testTransaction3.setCategory("Food");
        testTransaction3.setTransactionDate(LocalDate.of(2024, 1, 17));
    }

    @Test
    void findAllTransactionsTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);
        entityManager.persistAndFlush(testTransaction2);
        entityManager.persistAndFlush(testTransaction3);

        // When
        List<Transaction> transactions = transactionRepository.findAll();

        // Then
        assertThat(transactions).hasSize(3);
        assertThat(transactions).extracting(Transaction::getTransactionId)
                .containsExactlyInAnyOrder("txn_000001", "txn_000002", "txn_000003");
    }

    @Test
    void findByIdTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);

        // When
        Optional<Transaction> found = transactionRepository.findById("txn_000001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo("user123");
        assertThat(found.get().getAmount()).isEqualTo(new BigDecimal("100.50"));
    }

    @Test
    void findByIdNotFoundTest() {
        // When
        Optional<Transaction> found = transactionRepository.findById("txn_999999");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void findByUserIdTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);
        entityManager.persistAndFlush(testTransaction2);
        entityManager.persistAndFlush(testTransaction3);

        // When
        List<Transaction> transactions = transactionRepository.findByUserId("user123");

        // Then
        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting(Transaction::getTransactionId)
                .containsExactlyInAnyOrder("txn_000001", "txn_000002");
    }

    @Test
    void findByUserIdNoResultsTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);

        // When
        List<Transaction> transactions = transactionRepository.findByUserId("nonexistent");

        // Then
        assertThat(transactions).isEmpty();
    }

    @Test
    void findByCategoryTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);
        entityManager.persistAndFlush(testTransaction2);
        entityManager.persistAndFlush(testTransaction3);

        // When
        List<Transaction> transactions = transactionRepository.findByCategory("Food");

        // Then
        assertThat(transactions).hasSize(2);
        assertThat(transactions).extracting(Transaction::getTransactionId)
                .containsExactlyInAnyOrder("txn_000001", "txn_000003");
    }

    @Test
    void existsByTransactionIdTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);

        // When
        boolean exists = transactionRepository.existsByTransactionId("txn_000001");
        boolean notExists = transactionRepository.existsByTransactionId("txn_999999");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void saveTransactionTest() {
        // Given
        Transaction newTransaction = new Transaction();
        newTransaction.setTransactionId("txn_000100");
        newTransaction.setUserId("newUser");
        newTransaction.setAmount(new BigDecimal("50.00"));
        newTransaction.setCategory("Shopping");
        newTransaction.setTransactionDate(LocalDate.now());

        // When
        Transaction saved = transactionRepository.save(newTransaction);

        // Then
        assertThat(saved.getTransactionId()).isEqualTo("txn_000100");
        assertThat(transactionRepository.findById("txn_000100")).isPresent();
    }

    @Test
    void deleteTransactionTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);
        assertThat(transactionRepository.findById("txn_000001")).isPresent();

        // When
        transactionRepository.deleteById("txn_000001");

        // Then
        assertThat(transactionRepository.findById("txn_000001")).isEmpty();
    }

    @Test
    void countTransactionsTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);
        entityManager.persistAndFlush(testTransaction2);

        // When
        long count = transactionRepository.count();

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void findByIdLongConvenienceMethodTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);

        // When
        Optional<Transaction> found = transactionRepository.findById("txn_000001");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTransactionId()).isEqualTo("txn_000001");
    }

    @Test
    void existsByIdTest() {
        // Given
        entityManager.persistAndFlush(testTransaction1);

        // When
        boolean exists = transactionRepository.existsById("txn_000001");

        // Then
        assertThat(exists).isTrue();
    }
}
