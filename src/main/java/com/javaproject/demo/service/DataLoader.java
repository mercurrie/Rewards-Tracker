package com.javaproject.demo.service;

import com.javaproject.demo.model.Transaction;
import com.javaproject.demo.model.TransactionCsv;
import com.javaproject.demo.repository.TransactionRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component; - Commented out since @Component is disabled

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

// @Component - Commented out to disable automatic CSV loading since data is already in database
public class DataLoader implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(DataLoader.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${transactions.file.path:src/main/resources/transactions.csv}")
    private String transactionsFilePath;

    @Override
    public void run(String... args) throws Exception {
        logger.info("DataLoader started - checking if data needs to be loaded");
        // Check if data already exists in the database
        if (transactionRepository.count() == 0) {
            logger.info("Database is empty, loading transactions from CSV");
            loadTransactionsFromCsv();
        } else {
            logger.info("Database already contains {} transactions. Skipping CSV import.", 
                       transactionRepository.count());
        }
    }

    private void loadTransactionsFromCsv() {
        logger.debug("Starting CSV file reading from: {}", transactionsFilePath);
        try (FileReader reader = new FileReader(transactionsFilePath)) {
            List<TransactionCsv> csvTransactions = new CsvToBeanBuilder<TransactionCsv>(reader)
                    .withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            logger.info("Parsed {} transactions from CSV file", csvTransactions.size());

            // Convert CSV transactions to JPA entities and save to database
            for (TransactionCsv csvTransaction : csvTransactions) {
                Transaction transaction = new Transaction(csvTransaction);
                transactionRepository.save(transaction);
                logger.debug("Saved transaction with ID: {}", transaction.getTransactionId());
            }

            logger.info("Successfully loaded {} transactions from CSV into database", csvTransactions.size());

        } catch (IOException e) {
            logger.error("IO Error loading transactions from CSV: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during CSV import: {}", e.getMessage(), e);
        }
    }
}
