package model;

import java.time.LocalDate;

/**
 * Represents an income transaction (e.g., salary, freelance, gift).
 * Extends the abstract Transaction class.
 */
public class Income extends Transaction implements Trackable {

    private String source; // e.g., "Employer", "Freelance", "Investment"
    private boolean isRecurring;

    public Income(String description, double amount, LocalDate date, String category, String source, boolean isRecurring) {
        super(description, amount, date, category);
        this.source = source;
        this.isRecurring = isRecurring;
    }

    @Override
    public String getType() {
        return "Income";
    }

    @Override
    public void displaySummary() {
        System.out.println("+ INCOME: " + description);
        System.out.println("  Amount   : $" + amount);
        System.out.println("  Source   : " + source);
        System.out.println("  Recurring: " + (isRecurring ? "Yes" : "No"));
        System.out.println("  Date     : " + date);
        System.out.println("  Category : " + category);
    }

    @Override
    public String track() {
        // TODO: implement tracking logic (e.g., monthly summary)
        return "Tracking income: " + description;
    }

    // Getters and Setters
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public boolean isRecurring() { return isRecurring; }
    public void setRecurring(boolean recurring) { isRecurring = recurring; }
}