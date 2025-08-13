package com.javaproject.demo.model;

import com.javaproject.demo.config.LocalDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionCsv {
    private static final Logger logger = LogManager.getLogger(TransactionCsv.class);

    @CsvBindByName(column = "transaction_id")
    private String transactionId;

    @CsvBindByName(column = "user_id")
    private String userId;

    @CsvBindByName
    private BigDecimal amount;

    @CsvBindByName
    private String category;

    @CsvCustomBindByName(column = "transaction_date", converter = LocalDateConverter.class)
    private LocalDate transactionDate;

    // Default constructor
    public TransactionCsv() {}

    // All-args constructor
    public TransactionCsv(String transactionId, String userId, BigDecimal amount, String category, LocalDate transactionDate) {
        logger.debug("Creating new TransactionCsv with ID: {}, User: {}, Amount: {}, Category: {}", 
                    transactionId, userId, amount, category);
        this.transactionId = transactionId;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.transactionDate = transactionDate;
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
        return "TransactionCsv{" +
                "transactionId='" + transactionId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
