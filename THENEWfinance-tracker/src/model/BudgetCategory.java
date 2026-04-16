package model;

// tracks spending for a specific category like Food or Housing
// not a transaction itself, just a way to organize expenses
public class BudgetCategory {

	private String name;
	private double budgetLimit;
	private double currentSpending;

	public BudgetCategory(String name, double budgetLimit) {
		this.name = name;
		this.budgetLimit = budgetLimit;
		this.currentSpending = 0.0;
	}

	// called when an expense gets added to this category
	public void addSpending(double amount) {
		currentSpending += amount;
	}

	public double getRemainingBudget() {
		return budgetLimit - currentSpending;
	}

	public boolean isOverBudget() {
		return currentSpending > budgetLimit;
	}

	// how much of the budget has been used as a percentage
	public double getUsagePercent() {
		if (budgetLimit == 0) return 0;
		return (currentSpending / budgetLimit) * 100;
	}

	public void displayStatus() {
		// build a simple text progress bar
		int filled = (int) Math.min(getUsagePercent() / 5, 20);
		String bar = "[" + "#".repeat(filled) + "-".repeat(20 - filled) + "]";

		System.out.println("  Category  : " + name);
		System.out.println("  Limit     : $" + String.format("%.2f", budgetLimit));
		System.out.println("  Spent     : $" + String.format("%.2f", currentSpending)
				+ " (" + String.format("%.1f", getUsagePercent()) + "%)");
		System.out.println("  Progress  : " + bar);
		System.out.println("  Remaining : $" + String.format("%.2f", getRemainingBudget()));

		if (isOverBudget()) {
			System.out.println("  WARNING: Over budget by $" + String.format("%.2f", -getRemainingBudget()) + "!");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getBudgetLimit() {
		return budgetLimit;
	}

	public void setBudgetLimit(double budgetLimit) {
		this.budgetLimit = budgetLimit;
	}

	public double getCurrentSpending() {
		return currentSpending;
	}

	public void setCurrentSpending(double currentSpending) {
		this.currentSpending = currentSpending;
	}
}