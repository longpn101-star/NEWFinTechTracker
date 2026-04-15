package model;

import java.time.LocalDate;

/**
 * Represents an investment transaction (e.g., stocks, crypto, real estate).
 * Extends the abstract Transaction class.
 */
public class Investment extends Transaction implements Trackable {

    private String investmentType; // e.g., "Stocks", "Crypto", "ETF"
    private double expectedReturn;  // as a percentage

    public Investment(String description, double amount, LocalDate date, String category, String investmentType, double expectedReturn) {
        super(description, amount, date, category);
        this.investmentType = investmentType;
        this.expectedReturn = expectedReturn;
    }
//hijdfhjdfhgdfhjdgfhkfdkdfkgdfh
    @Override
    public String getType() {
        return "Investment";
    }

    @Override
    public void displaySummary() {
        System.out.println("~ INVESTMENT: " + description);
        System.out.println("  Amount         : $" + amount);
        System.out.println("  Type           : " + investmentType);
        System.out.println("  Expected Return: " + expectedReturn + "%");
        System.out.println("  Date           : " + date);
        System.out.println("  Category       : " + category);
    }

    @Override
    public String track() {
        // TODO: calculate projected growth over time
        return "Tracking investment: " + description;
    }

    // Getters and Setters
    public String getInvestmentType() { return investmentType; }
    public void setInvestmentType(String investmentType) { this.investmentType = investmentType; }

    public double getExpectedReturn() { return expectedReturn; }
    public void setExpectedReturn(double expectedReturn) { this.expectedReturn = expectedReturn; }
}