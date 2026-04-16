package model;

import java.time.LocalDate;

// represents money coming in like salary, freelance work, etc.
public class Income extends Transaction implements Trackable {

	private String source;
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
		System.out.println("  [+] INCOME: " + description);
		System.out.println("      Amount    : $" + String.format("%.2f", amount));
		System.out.println("      Source    : " + source);
		System.out.println("      Recurring : " + (isRecurring ? "Yes" : "No"));
		System.out.println("      Date      : " + date.format(DATE_FORMAT));
		System.out.println("      Category  : " + category);
	}

	@Override
	public String track() {
		return "Income '" + description + "' from " + source + ": $" + String.format("%.2f", amount);
	}

	@Override
	public boolean isFlagged(double threshold) {
		return amount > threshold;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isRecurring() {
		return isRecurring;
	}

	public void setRecurring(boolean recurring) {
		isRecurring = recurring;
	}
}