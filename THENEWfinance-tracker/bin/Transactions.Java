package model;

import java.time.LocalDate;

/**
 * Abstract superclass representing a general financial transaction.
 * All transaction types (Income, Expense, Investment, Debt) extend this class.
 */
public abstract class Transaction {

    // Common attributes shared by all transaction types
    protected String description;
    protected double amount;
    protected LocalDate date;
    protected String category;

    /**
     * Constructor for Transaction.
     */
    public Transaction(String description, double amount, LocalDate date, String category) {
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    // --- Abstract Methods ---

    /**
     * Returns the type of transaction (e.g., "Income", "Expense").
     * Each subclass must implement this.
     */
    public abstract String getType();

    /**
     * Displays a summary of the transaction.
     * Each subclass must implement this differently (polymorphism).
     */
    public abstract void displaySummary();

    // --- Getters and Setters ---

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return "[" + getType() + "] " + description + " | $" + amount + " | " + date;
    }
}