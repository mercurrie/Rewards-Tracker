package com.javaproject.demo.repository;

import com.javaproject.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    // Find transaction by transaction ID (already provided by JpaRepository as findById)
    
    // Find all transactions by user ID
    List<Transaction> findByUserId(String userId);
    
    // Find all transactions by category
    List<Transaction> findByCategory(String category);
    
    // Check if transaction exists by transaction ID
    boolean existsByTransactionId(String transactionId);
}
