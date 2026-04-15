package model;

import java.time.LocalDate;
//max sucks
/**
 * Represents an expense transaction (e.g., rent, groceries, utilities).
 * Extends the abstract Transaction class.
 */
public class Expense extends Transaction implements Trackable {

    private String paymentMethod; // e.g., "Cash", "Credit Card", "Debit"
    private boolean isNecessity;

    public Expense(String description, double amount, LocalDate date, String category, String paymentMethod, boolean isNecessity) {
        super(description, amount, date, category);
        this.paymentMethod = paymentMethod;
        this.isNecessity = isNecessity;
    }

    @Override
    public String getType() {
        return "Expense";
    }

    @Override
    public void displaySummary() {
        System.out.println("- EXPENSE: " + description);
        System.out.println("  Amount       : $" + amount);
        System.out.println("  Payment      : " + paymentMethod);
        System.out.println("  Necessity    : " + (isNecessity ? "Yes" : "No"));
        System.out.println("  Date         : " + date);
        System.out.println("  Category     : " + category);
    }

    @Override
    public String track() {
        // TODO: implement tracking logic (e.g., flag overspending)
        return "Tracking expense: " + description;
    }

    // Getters and Setters
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public boolean isNecessity() { return isNecessity; }
    public void setNecessity(boolean necessity) { isNecessity = necessity; }
}