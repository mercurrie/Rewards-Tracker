package com.javaproject.demo.repository;

import com.javaproject.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
    // Find transaction by transaction ID (already provided by JpaRepository as findById)
    
    // Find all transactions by user ID
    List<Transaction> findByUserId(String userId);
    
    // Find all transactions by category
    List<Transaction> findByCategory(String category);
    
    // Check if transaction exists by transaction ID
    boolean existsByTransactionId(String transactionId);
    
    // Check if exists by Long ID (for convenience)
    default boolean existsById(Long id) {
        return existsByTransactionId(String.valueOf(id));
    }
    
    // Find by Long ID (for convenience)
    default Optional<Transaction> findById(Long id) {
        return findById(String.valueOf(id));
    }
    
    // Delete by Long ID (for convenience)
    default void deleteById(Long id) {
        deleteById(String.valueOf(id));
    }
}
