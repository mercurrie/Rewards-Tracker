package com.javaproject.demo.service;

import com.javaproject.demo.model.Transaction;
import com.javaproject.demo.model.TransactionCsv;
import com.javaproject.demo.repository.TransactionRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component; - Commented out since @Component is disabled

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

// @Component - Commented out to disable automatic CSV loading since data is already in database
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${transactions.file.path:src/main/resources/transactions.csv}")
    private String transactionsFilePath;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists in the database
        if (transactionRepository.count() == 0) {
            loadTransactionsFromCsv();
        } else {
            System.out.println("Database already contains " + transactionRepository.count() + " transactions. Skipping CSV import.");
        }
    }

    private void loadTransactionsFromCsv() {
        try (FileReader reader = new FileReader(transactionsFilePath)) {
            List<TransactionCsv> csvTransactions = new CsvToBeanBuilder<TransactionCsv>(reader)
                    .withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            // Convert CSV transactions to JPA entities and save to database
            for (TransactionCsv csvTransaction : csvTransactions) {
                Transaction transaction = new Transaction(csvTransaction);
                transactionRepository.save(transaction);
            }

            System.out.println("Successfully loaded " + csvTransactions.size() + " transactions from CSV into database.");

        } catch (IOException e) {
            System.err.println("Error loading transactions from CSV: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error during CSV import: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
