package com.javaproject.demo.controller;

import com.javaproject.demo.model.Transaction;
import com.javaproject.demo.model.TransactionCsv;
import com.javaproject.demo.repository.TransactionRepository;
import com.opencsv.bean.CsvToBeanBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(TransactionController.class);
    
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
        logger.info("Loading transactions from CSV file: {}", transactionsFilePath);
        try (FileReader reader = new FileReader(transactionsFilePath)) {
            transactions = new CsvToBeanBuilder<TransactionCsv>(reader)
                    .withType(TransactionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            logger.info("Successfully loaded {} transactions from CSV.", transactions.size());
            System.out.println("Successfully loaded " + transactions.size() + " transactions from CSV.");
        } catch (IOException e) {
            logger.error("IO Error loading transactions from CSV: {}", e.getMessage(), e);
            System.err.println("Error loading transactions from CSV: " + e.getMessage());
        } catch (RuntimeException e) {
            logger.error("CSV parsing error: {}", e.getMessage(), e);
            System.err.println("CSV parsing error: " + e.getMessage());
            // Initialize with empty list to prevent application startup failure
            transactions = new ArrayList<>();
        } catch (Exception e) {
            logger.error("Unexpected error during CSV loading: {}", e.getMessage(), e);
            System.err.println("Unexpected error during CSV loading: " + e.getMessage());
            // Initialize with empty list to prevent application startup failure
            transactions = new ArrayList<>();
        }
    }

    // GET all transactions from database
    @GetMapping
    public List<Transaction> getAllTransactions() {
        logger.debug("Fetching all transactions from database");
        List<Transaction> allTransactions = transactionRepository.findAll();
        logger.info("Retrieved {} transactions from database", allTransactions.size());
        return allTransactions;
    }

    // GET transaction by ID from database
    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable String id) {
        logger.debug("Fetching transaction with ID: {}", id);
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            logger.info("Found transaction with ID: {}", id);
            return transaction.get();
        } else {
            logger.warn("Transaction not found with ID: {}", id);
            return null;
        }
    }

    // GET transactions by userId from database
    @GetMapping("/user/{userId}")
    public List<Transaction> getTransactionsByUserId(@PathVariable String userId) {
        logger.debug("Fetching transactions for user ID: {}", userId);
        List<Transaction> userTransactions = transactionRepository.findByUserId(userId);
        logger.info("Found {} transactions for user ID: {}", userTransactions.size(), userId);
        return userTransactions;
    }

    // GET transactions by category from database
    @GetMapping("/category/{category}")
    public List<Transaction> getTransactionsByCategory(@PathVariable String category) {
        logger.debug("Fetching transactions for category: {}", category);
        List<Transaction> categoryTransactions = transactionRepository.findByCategory(category);
        logger.info("Found {} transactions for category: {}", categoryTransactions.size(), category);
        return categoryTransactions;
    }

    // POST - Create new transaction (auto-generate ID and date, save to database)
    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionCsv newTransaction) {
        logger.info("Creating new transaction for user: {}, category: {}, amount: {}", 
                    newTransaction.getUserId(), newTransaction.getCategory(), newTransaction.getAmount());
        
        // Generate new transaction ID by finding the max numeric ID and adding 1
        Long maxNumericId = transactionRepository.findAll().stream()
                .map(Transaction::getTransactionId)
                .filter(id -> id != null && id.startsWith("txn_"))
                .map(id -> id.substring(4)) // Remove "txn_" prefix
                .filter(id -> id.matches("\\d+")) // Only numeric suffixes
                .mapToLong(Long::parseLong)
                .max()
                .orElse(99999L); // Start from 100000 if no existing transactions
        
        // Set auto-generated values with "txn_" prefix
        String newId = "txn_" + String.format("%06d", maxNumericId + 1);
        newTransaction.setTransactionId(newId);
        newTransaction.setTransactionDate(java.time.LocalDate.now());
        
        logger.debug("Generated new transaction ID: {}", newId);
        
        // Convert to JPA entity and save to database
        Transaction transaction = new Transaction(newTransaction);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // Also add to in-memory list for consistency
        transactions.add(newTransaction);
        
        logger.info("Successfully created transaction with ID: {}", newId);
        return savedTransaction;
    }

    // PUT - Update existing transaction (preserve ID, userId, date; allow amount/category updates)
    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable String id, @RequestBody TransactionCsv updatedTransaction) {
        logger.info("Updating transaction with ID: {}", id);
        Optional<Transaction> existingTransactionOpt = transactionRepository.findById(id);
        
        if (existingTransactionOpt.isPresent()) {
            Transaction existingTransaction = existingTransactionOpt.get();
            logger.debug("Found existing transaction with ID: {}, updating amount: {} -> {}, category: {} -> {}", 
                        id, existingTransaction.getAmount(), updatedTransaction.getAmount(),
                        existingTransaction.getCategory(), updatedTransaction.getCategory());
            
            // Preserve original ID, userId, and date
            updatedTransaction.setTransactionId(existingTransaction.getTransactionId());
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
            
            logger.info("Successfully updated transaction with ID: {}", id);
            return savedTransaction;
        } else {
            logger.warn("Transaction not found for update with ID: {}", id);
            return null;
        }
    }

    // DELETE transaction by ID from database
    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable String id) {
        logger.info("Attempting to delete transaction with ID: {}", id);
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            
            // Remove from in-memory list for consistency
            transactions.removeIf(t -> t.getTransactionId().equals(id));
            
            logger.info("Successfully deleted transaction with ID: {}", id);
            return "Transaction with ID " + id + " deleted successfully.";
        } else {
            logger.warn("Transaction not found for deletion with ID: {}", id);
            return "Transaction with ID " + id + " not found.";
        }
    }
}
