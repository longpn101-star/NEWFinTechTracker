package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// this is the base class for all our transactions
// Income, Expense, Investment and Debt all extend this
public abstract class Transaction {

	protected String description;
	protected double amount;
	protected LocalDate date;
	protected String category;

	// I used this formatter in a few places so I just put it here
	protected static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy");

	public Transaction(String description, double amount, LocalDate date, String category) {
		this.description = description;
		this.amount = amount;
		this.date = date;
		this.category = category;
	}

	// every subclass needs to say what type it is
	public abstract String getType();

	// every subclass prints its info differently (polymorphism)
	public abstract void displaySummary();

	// short one-line version used in lists
	public String toShortString() {
		return String.format("%-12s | %-30s | $%10.2f | %s",
				getType(), description, amount, date.format(DATE_FORMAT));
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "[" + getType() + "] " + description + " | $" + String.format("%.2f", amount) + " | " + date.format(DATE_FORMAT);
	}
}