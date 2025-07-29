package com.javaproject.demo.controller;

import com.javaproject.demo.model.Transaction;
import com.javaproject.demo.model.TransactionCsv;
import com.javaproject.demo.repository.TransactionRepository;
import com.opencsv.bean.CsvToBeanBuilder;

import org.springframework.beans.factory.annotation.Autowired;
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
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    private List<TransactionCsv> transactions = new ArrayList<>();

    @Value("${transactions.file.path:src/main/resources/transactions.csv}")
    private String transactionsFilePath;

    @PostConstruct
    public void init() {
        loadTransactionsFromCsv();
    }

    private void loadTransactionsFromCsv() {
        try (FileReader reader = new FileReader(transactionsFilePath)) {
            transactions = new CsvToBeanBuilder<TransactionCsv>(reader)
                    .withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            System.out.println("Successfully loaded " + transactions.size() + " transactions from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading transactions from CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // GET all transactions from database
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // GET transaction by ID from database
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionRepository.findById(id);
        return transaction.orElse(null);
    }

    // GET transactions by userId from database
    @GetMapping("/user/{userId}")
    public List<Transaction> getTransactionsByUserId(@PathVariable String userId) {
        return transactionRepository.findByUserId(userId);
    }

    // GET transactions by category from database
    @GetMapping("/category/{category}")
    public List<Transaction> getTransactionsByCategory(@PathVariable String category) {
        return transactionRepository.findByCategory(category);
    }

    // POST - Create new transaction (auto-generate ID and date, save to database)
    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionCsv newTransaction) {
        // Generate new transaction ID by finding the max ID and adding 1
        Long maxId = transactionRepository.findAll().stream()
                .mapToLong(Transaction::getTransactionIdAsLong)
                .max()
                .orElse(0L);
        
        // Set auto-generated values
        newTransaction.setTransactionId(maxId + 1);
        newTransaction.setTransactionDate(java.time.LocalDate.now());
        
        // Convert to JPA entity and save to database
        Transaction transaction = new Transaction(newTransaction);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Also add to in-memory list for consistency
        transactions.add(newTransaction);
        
        return savedTransaction;
    }

    // PUT - Update existing transaction (preserve ID, userId, date; allow amount/category updates)
    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody TransactionCsv updatedTransaction) {
        Optional<Transaction> existingTransactionOpt = transactionRepository.findById(id);
        
        if (existingTransactionOpt.isPresent()) {
            Transaction existingTransaction = existingTransactionOpt.get();
            
            // Preserve original ID, userId, and date
            updatedTransaction.setTransactionId(existingTransaction.getTransactionIdAsLong());
            updatedTransaction.setUserId(existingTransaction.getUserId());
            updatedTransaction.setTransactionDate(existingTransaction.getTransactionDate());
            
            // Update the database entity
            existingTransaction.setAmount(updatedTransaction.getAmount());
            existingTransaction.setCategory(updatedTransaction.getCategory());
            
            Transaction savedTransaction = transactionRepository.save(existingTransaction);
            
            // Update in-memory list for consistency
            for (int i = 0; i < transactions.size(); i++) {
                if (transactions.get(i).getTransactionId().equals(id)) {
                    transactions.set(i, updatedTransaction);
                    break;
                }
            }
            
            return savedTransaction;
        }
        return null;
    }

    // DELETE transaction by ID from database
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            
            // Remove from in-memory list for consistency
            transactions.removeIf(t -> t.getTransactionId().equals(id));
            
            return "Transaction with ID " + id + " deleted successfully.";
        } else {
            return "Transaction with ID " + id + " not found.";
        }
    }
}
