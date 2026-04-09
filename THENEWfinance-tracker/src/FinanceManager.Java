package model;

import java.util.ArrayList;

/**
 * Manager / Controller class for the Personal Finance Tracker.
 * Responsible for storing and managing all transactions and budget categories.
 */
public class FinanceManager {

    private String ownerName;
    private double accountBalance;
    private ArrayList<Transaction> transactions;   // polymorphic storage
    private ArrayList<BudgetCategory> categories;

    public FinanceManager(String ownerName, double initialBalance) {
        this.ownerName = ownerName;
        this.accountBalance = initialBalance;
        this.transactions = new ArrayList<>();
        this.categories = new ArrayList<>();
    }

    // --- Transaction Management ---

    /**
     * Adds any transaction (Income, Expense, Investment, Debt) to the list.
     */
    public void addTransaction(Transaction t) {
        transactions.add(t);
        System.out.println("Added: " + t);
    }

    /**
     * Displays all stored transactions using polymorphism (displaySummary).
     */
    public void displayAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        System.out.println("=== All Transactions for " + ownerName + " ===");
        for (Transaction t : transactions) {
            t.displaySummary();
            System.out.println("---");
        }
    }

    /**
     * Searches for transactions by description keyword (case-insensitive).
     */
    public void searchByDescription(String keyword) {
        System.out.println("=== Search Results for \"" + keyword + "\" ===");
        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                t.displaySummary();
                found = true;
            }
        }
        if (!found) System.out.println("No transactions found.");
    }

    /**
     * Displays only transactions of a specific type (e.g., "Income", "Expense").
     */
    public void displayByType(String type) {
        System.out.println("=== " + type + " Transactions ===");
        for (Transaction t : transactions) {
            if (t.getType().equalsIgnoreCase(type)) {
                t.displaySummary();
                System.out.println("---");
            }
        }
    }

    // --- Budget Category Management ---

    public void addCategory(BudgetCategory category) {
        categories.add(category);
        System.out.println("Category added: " + category.getName());
    }

    public void displayAllCategories() {
        if (categories.isEmpty()) {
            System.out.println("No budget categories set.");
            return;
        }
        System.out.println("=== Budget Categories ===");
        for (BudgetCategory bc : categories) {
            bc.displayStatus();
            System.out.println("---");
        }
    }

    // --- Summary / Balance ---

    /**
     * Calculates and prints a simple financial summary.
     */
    public void displaySummary() {
        // TODO: calculate net balance from income vs. expenses
        System.out.println("=== Financial Summary for " + ownerName + " ===");
        System.out.println("  Starting Balance : $" + accountBalance);
        System.out.println("  Total Transactions: " + transactions.size());
        System.out.println("  (Full summary calculation coming soon)");
    }

    // --- Getters and Setters ---

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public double getAccountBalance() { return accountBalance; }
    public void setAccountBalance(double accountBalance) { this.accountBalance = accountBalance; }

    public ArrayList<Transaction> getTransactions() { return transactions; }
    public ArrayList<BudgetCategory> getCategories() { return categories; }
}