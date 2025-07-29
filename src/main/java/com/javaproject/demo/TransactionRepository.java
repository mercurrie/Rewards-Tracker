package com.javaproject.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    // Find transaction by transaction ID (already provided by JpaRepository as findById)
    
    // Find all transactions by user ID
    Optional<Transaction> findByUserId(String userId);
    
    // Find all transactions by category
    Optional<Transaction> findByCategory(String category);
    
    // Check if transaction exists by transaction ID
    boolean existsByTransactionId(String transactionId);
    
    // Find the transaction with the highest transaction ID (for auto-generation)
    Optional<Transaction> findTopByOrderByTransactionIdDesc();

    Optional<Transaction> findByTransactionId(String transactionId);
}
