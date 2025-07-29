package com.javaproject.demo.model;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    private static final Logger logger = LogManager.getLogger(Transaction.class);

    @Id
    @Column(name = "transaction_id", length = 20)
    private String transactionId;

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    // Default constructor (required by JPA)
    public Transaction() {}

    // Constructor with all fields
    public Transaction(String transactionId, String userId, BigDecimal amount, String category, LocalDate transactionDate) {
        logger.debug("Creating new Transaction with ID: {}, User: {}, Amount: {}, Category: {}", 
                    transactionId, userId, amount, category);
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.transactionDate = transactionDate;
    }

    // Constructor from TransactionCsv (for easy conversion)
    public Transaction(TransactionCsv csvTransaction) {
        logger.debug("Converting TransactionCsv to Transaction: {}", csvTransaction.getTransactionId());
        this.transactionId = csvTransaction.getTransactionId();
        this.userId = csvTransaction.getUserId();
        this.amount = csvTransaction.getAmount();
        this.category = csvTransaction.getCategory();
        this.transactionDate = csvTransaction.getTransactionDate();
    }

    // Convert to TransactionCsv (for compatibility)
    public TransactionCsv toTransactionCsv() {
        return new TransactionCsv(this.transactionId, this.userId, this.amount, this.category, this.transactionDate);
    }

    // Getters and setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return transactionId != null ? transactionId.equals(that.transactionId) : that.transactionId == null;
    }

    @Override
    public int hashCode() {
        return transactionId != null ? transactionId.hashCode() : 0;
    }
}
