package com.javaproject.demo.service;

import com.javaproject.demo.model.Transaction;
import com.javaproject.demo.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @TempDir
    Path tempDir;

    private Path csvFile;

    @BeforeEach
    void setUp() throws IOException {
        csvFile = tempDir.resolve("test-transactions.csv");
        
        // Create a test CSV file
        String csvContent = """
                transaction_id,user_id,amount,category,transaction_date
                txn_000001,user123,100.50,Food,2024-01-15
                txn_000002,user456,75.25,Transport,2024-01-16
                txn_000003,user789,200.00,Shopping,2024-01-17
                """;
        
        Files.write(csvFile, csvContent.getBytes());
        
        // Set the file path in the DataLoader
        ReflectionTestUtils.setField(dataLoader, "transactionsFilePath", csvFile.toString());
    }

    @Test
    void runWhenDatabaseIsEmptyTest() throws Exception {
        // Given
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        verify(transactionRepository, times(3)).save(any(Transaction.class));
    }

    @Test
    void runWhenDatabaseHasDataTest() throws Exception {
        // Given
        when(transactionRepository.count()).thenReturn(5L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository, times(2)).count(); // Called twice: once for check, once for logging
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void loadTransactionsFromCsvSuccessTest() throws Exception {
        // Given
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository, times(3)).save(any(Transaction.class));
    }

    @Test
    void loadTransactionsFromCsvFileNotFoundTest() throws Exception {
        // Given
        ReflectionTestUtils.setField(dataLoader, "transactionsFilePath", "nonexistent.csv");
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void loadTransactionsFromEmptyCsvTest() throws Exception {
        // Given
        Path emptyCsvFile = tempDir.resolve("empty.csv");
        String emptyCsvContent = "transaction_id,user_id,amount,category,transaction_date\n";
        Files.write(emptyCsvFile, emptyCsvContent.getBytes());
        
        ReflectionTestUtils.setField(dataLoader, "transactionsFilePath", emptyCsvFile.toString());
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void loadTransactionsFromInvalidCsvFormatTest() throws Exception {
        // Given
        Path invalidCsvFile = tempDir.resolve("invalid.csv");
        String invalidCsvContent = """
                transaction_id,user_id,amount,category,transaction_date
                invalid_id,user123,invalid_amount,Food,2024-01-15
                """;
        Files.write(invalidCsvFile, invalidCsvContent.getBytes());
        
        ReflectionTestUtils.setField(dataLoader, "transactionsFilePath", invalidCsvFile.toString());
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        // The exact behavior depends on how OpenCSV handles invalid data
        // but we should not crash
    }

    @Test
    void loadTransactionsWithDatabaseExceptionTest() throws Exception {
        // Given
        when(transactionRepository.count()).thenReturn(0L);
        when(transactionRepository.save(any(Transaction.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        verify(transactionRepository, atLeastOnce()).save(any(Transaction.class));
        // Should handle the exception gracefully
    }

    @Test
    void loadTransactionsWithSpecialCharactersTest() throws Exception {
        // Given
        Path specialCsvFile = tempDir.resolve("special.csv");
        String specialCsvContent = """
                transaction_id,user_id,amount,category,transaction_date
                1,"user with spaces",100.50,"Food & Dining",2024-01-15
                2,user@email.com,75.25,"Transport/Travel",2024-01-16
                """;
        Files.write(specialCsvFile, specialCsvContent.getBytes());
        
        ReflectionTestUtils.setField(dataLoader, "transactionsFilePath", specialCsvFile.toString());
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void loadTransactionsWithDifferentDateFormatsTest() throws Exception {
        // Given
        Path dateCsvFile = tempDir.resolve("dates.csv");
        String dateCsvContent = """
                transaction_id,user_id,amount,category,transaction_date
                1,user123,100.50,Food,2024-01-15
                2,user456,75.25,Transport,2024-12-31
                """;
        Files.write(dateCsvFile, dateCsvContent.getBytes());
        
        ReflectionTestUtils.setField(dataLoader, "transactionsFilePath", dateCsvFile.toString());
        when(transactionRepository.count()).thenReturn(0L);

        // When
        dataLoader.run();

        // Then
        verify(transactionRepository).count();
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }
}
