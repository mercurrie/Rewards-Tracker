package com.javaproject.demo;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class HelloController {

    private List<TransactionCsv> transactions = new ArrayList<>();

    @Value("${transactions.file.path:src/main/resources/transactions.csv}")
    private String transactionsFilePath;

    // Load transactions from the CSV file when the application starts
    @PostConstruct
    public void loadTransactions() {
        try (FileReader reader = new FileReader(transactionsFilePath)) {
            transactions = new CsvToBeanBuilder<TransactionCsv>(reader)
                    .withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create a new transaction
    @PostMapping
    public String createTransaction(@RequestBody TransactionCsv transaction) {
        // Generate a new transaction ID by incrementing the last ID
        String lastTransactionId = transactions.stream()
                .map(TransactionCsv::getTransactionId)
                .max(String::compareTo)
                .orElse("txn_100000"); // Default starting ID if the list is empty

        int newId = Integer.parseInt(lastTransactionId.split("_")[1]) + 1;
        String newTransactionId = "txn_" + String.format("%06d", newId);

        // Set the generated ID and current date to the new transaction
        transaction.setTransactionId(newTransactionId);
        transaction.setTransactionDate(java.time.LocalDate.now());

        // Add the transaction to the list
        transactions.add(transaction);

        return "Transaction created: " + transaction.toString();
    }

    // Get a transaction by ID
    @GetMapping("/{transactionId}")
    public TransactionCsv getTransactionById(@PathVariable String transactionId) {
        Optional<TransactionCsv> transaction = transactions.stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst();
        return transaction.orElse(null);
    }

    // Get all transactions
    @GetMapping
    public List<TransactionCsv> getAllTransactions() {
        return transactions;
    }

    // Update a transaction by ID
    @PutMapping("/{transactionId}")
    public String updateTransaction(@PathVariable String transactionId, @RequestBody TransactionCsv updatedTransaction) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getTransactionId().equals(transactionId)) {
                TransactionCsv existingTransaction = transactions.get(i);
                
                // Preserve original ID, user ID, and date
                updatedTransaction.setTransactionId(existingTransaction.getTransactionId());
                updatedTransaction.setUserId(existingTransaction.getUserId());
                updatedTransaction.setTransactionDate(existingTransaction.getTransactionDate());
                
                // Update only amount and category
                transactions.set(i, updatedTransaction);
                return "Transaction updated: " + updatedTransaction.toString();
            }
        }
        return "Transaction not found with ID: " + transactionId;
    }

    // Delete a transaction by ID
    @DeleteMapping("/{transactionId}")
    public String deleteTransaction(@PathVariable String transactionId) {
        transactions.removeIf(t -> t.getTransactionId().equals(transactionId));
        return "Transaction deleted with ID: " + transactionId;
    }
}
