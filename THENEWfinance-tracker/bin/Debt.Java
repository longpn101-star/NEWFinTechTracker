package model;

import java.time.LocalDate;

/**
 * Represents a debt entry (e.g., student loan, credit card debt, mortgage).
 * Extends the abstract Transaction class.
 */
public class Debt extends Transaction implements Trackable {

    private double interestRate;   // annual interest rate as a percentage
    private LocalDate dueDate;
    private boolean isPaid;

    public Debt(String description, double amount, LocalDate date, String category, double interestRate, LocalDate dueDate) {
        super(description, amount, date, category);
        this.interestRate = interestRate;
        this.dueDate = dueDate;
        this.isPaid = false;
    }

    @Override
    public String getType() {
        return "Debt";
    }

    @Override
    public void displaySummary() {
        System.out.println("! DEBT: " + description);
        System.out.println("  Amount       : $" + amount);
        System.out.println("  Interest Rate: " + interestRate + "%");
        System.out.println("  Due Date     : " + dueDate);
        System.out.println("  Status       : " + (isPaid ? "Paid" : "Outstanding"));
        System.out.println("  Category     : " + category);
    }

    @Override
    public String track() {
        // TODO: calculate total cost with interest over time
        return "Tracking debt: " + description;
    }

    public void markAsPaid() {
        this.isPaid = true;
        System.out.println("Debt '" + description + "' marked as paid.");
    }

    // Getters and Setters
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }
}