package model;

import java.time.LocalDate;

// represents money going out like rent, groceries, subscriptions
public class Expense extends Transaction implements Trackable {

	private String paymentMethod;
	private boolean isNecessity; // true = need, false = want

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
		System.out.println("  [-] EXPENSE: " + description);
		System.out.println("      Amount    : $" + String.format("%.2f", amount));
		System.out.println("      Payment   : " + paymentMethod);
		System.out.println("      Necessity : " + (isNecessity ? "Yes" : "No"));
		System.out.println("      Date      : " + date.format(DATE_FORMAT));
		System.out.println("      Category  : " + category);
	}

	@Override
	public String track() {
		return "Expense '" + description + "' paid with " + paymentMethod + ": $" + String.format("%.2f", amount);
	}

	// flag big discretionary spending
	@Override
	public boolean isFlagged(double threshold) {
		return !isNecessity && amount > threshold;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public boolean isNecessity() {
		return isNecessity;
	}

	public void setNecessity(boolean necessity) {
		isNecessity = necessity;
	}
}