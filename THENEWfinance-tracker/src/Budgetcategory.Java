package model;

/**
 * Represents a budget category (e.g., Food, Housing, Entertainment).
 * Used to organize and limit spending within the finance tracker.
 */
public class BudgetCategory {

    private String name;
    private double budgetLimit;
    private double currentSpending;

    public BudgetCategory(String name, double budgetLimit) {
        this.name = name;
        this.budgetLimit = budgetLimit;
        this.currentSpending = 0.0;//max sucks
    }

    /**
     * Adds an expense amount to this category's current spending.
     */
    public void addSpending(double amount) {
        this.currentSpending += amount;
    }

    /**
     * Returns remaining budget for this category.
     */
    public double getRemainingBudget() {
        return budgetLimit - currentSpending;
    }

    /**
     * Returns whether the category is over budget.
     */
    public boolean isOverBudget() {
        return currentSpending > budgetLimit;
    }

    public void displayStatus() {
        System.out.println("Category : " + name);
        System.out.println("  Limit    : $" + budgetLimit);
        System.out.println("  Spent    : $" + currentSpending);
        System.out.println("  Remaining: $" + getRemainingBudget());
        if (isOverBudget()) System.out.println("  WARNING: Over budget!");
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(double budgetLimit) { this.budgetLimit = budgetLimit; }

    public double getCurrentSpending() { return currentSpending; }
    public void setCurrentSpending(double currentSpending) { this.currentSpending = currentSpending; }
}