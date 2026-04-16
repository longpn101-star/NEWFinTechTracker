package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// represents money owed like loans, credit card debt, etc.
public class Debt extends Transaction implements Trackable {

	private double interestRate;
	private LocalDate dueDate;
	private boolean isPaid;

	public Debt(String description, double amount, LocalDate date, String category, double interestRate, LocalDate dueDate) {
		super(description, amount, date, category);
		this.interestRate = interestRate;
		this.dueDate = dueDate;
		this.isPaid = false; // starts unpaid obviously
	}

	@Override
	public String getType() {
		return "Debt";
	}

	@Override
	public void displaySummary() {
		long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

		System.out.println("  [!] DEBT: " + description);
		System.out.println("      Principal     : $" + String.format("%.2f", amount));
		System.out.println("      Interest Rate : " + interestRate + "%");
		System.out.println("      Total Cost    : $" + String.format("%.2f", calculateTotalCost()));

		if (daysLeft < 0) {
			System.out.println("      Due Date      : " + dueDate.format(DATE_FORMAT) + " (OVERDUE!)");
		} else {
			System.out.println("      Due Date      : " + dueDate.format(DATE_FORMAT) + " (" + daysLeft + " days left)");
		}

		System.out.println("      Status        : " + (isPaid ? "PAID" : "Outstanding"));
		System.out.println("      Category      : " + category);
	}

	@Override
	public String track() {
		return "Debt '" + description + "': $" + String.format("%.2f", amount)
				+ " at " + interestRate + "% | Due: " + dueDate.format(DATE_FORMAT)
				+ " | " + (isPaid ? "PAID" : "Outstanding");
	}

	// simple interest calculation over the loan period
	public double calculateTotalCost() {
		long months = ChronoUnit.MONTHS.between(date, dueDate);
		double years = months / 12.0;
		return amount + (amount * (interestRate / 100) * years);
	}

	public void markAsPaid() {
		isPaid = true;
		System.out.println("  Debt '" + description + "' marked as paid!");
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean paid) {
		isPaid = paid;
	}
}