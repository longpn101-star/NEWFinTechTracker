package model;

import java.util.ArrayList;

// this is the main manager class that holds everything together
// it stores all the transactions and budget categories
// and has methods to add, remove, search and display them
public class FinanceManager {

	private String ownerName;
	private double startingBalance;

	// using ArrayList so we can store any Transaction subtype (polymorphism)
	private ArrayList<Transaction> transactions;
	private ArrayList<BudgetCategory> categories;

	public FinanceManager(String ownerName, double startingBalance) {
		this.ownerName = ownerName;
		this.startingBalance = startingBalance;
		this.transactions = new ArrayList<>();
		this.categories = new ArrayList<>();
	}

	// adds any transaction type since they all extend Transaction
	public void addTransaction(Transaction t) {
		transactions.add(t);

		// if it's an expense, update the matching budget category automatically
		if (t instanceof Expense) {
			BudgetCategory cat = findCategory(t.getCategory());
			if (cat != null) {
				cat.addSpending(t.getAmount());
			}
		}

		System.out.println("  Added: " + t);
	}

	public boolean removeTransaction(int index) {
		int i = index - 1; // convert from 1-based to 0-based
		if (i < 0 || i >= transactions.size()) {
			System.out.println("  Invalid index.");
			return false;
		}
		Transaction removed = transactions.remove(i);
		System.out.println("  Removed: " + removed);
		return true;
	}

	// displays all transactions - each one calls its own displaySummary()
	// this is where polymorphism really shows, each type prints differently
	public void displayAllTransactions() {
		if (transactions.isEmpty()) {
			System.out.println("  No transactions recorded yet.");
			return;
		}
		System.out.println("\n=== All Transactions for " + ownerName + " ===");
		for (int i = 0; i < transactions.size(); i++) {
			System.out.println("\n  #" + (i + 1));
			transactions.get(i).displaySummary();
		}
		System.out.println("==========================================");
	}

	// compact numbered list
	public void listTransactions() {
		if (transactions.isEmpty()) {
			System.out.println("  No transactions recorded yet.");
			return;
		}
		System.out.println("\n  #    Type         Description                        Amount         Date");
		System.out.println("  " + "-".repeat(80));
		for (int i = 0; i < transactions.size(); i++) {
			System.out.println("  " + (i + 1) + ".  " + transactions.get(i).toShortString());
		}
	}

	public void searchByDescription(String keyword) {
		System.out.println("\n=== Results for \"" + keyword + "\" ===");
		boolean found = false;
		for (int i = 0; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
			if (t.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
				System.out.println("\n  #" + (i + 1));
				t.displaySummary();
				found = true;
			}
		}
		if (!found) {
			System.out.println("  Nothing found.");
		}
	}

	public void displayByType(String type) {
		System.out.println("\n=== " + type + " Transactions ===");
		boolean found = false;
		for (int i = 0; i < transactions.size(); i++) {
			Transaction t = transactions.get(i);
			if (t.getType().equalsIgnoreCase(type)) {
				System.out.println("\n  #" + (i + 1));
				t.displaySummary();
				found = true;
			}
		}
		if (!found) {
			System.out.println("  No " + type + " transactions found.");
		}
	}

	public void markDebtAsPaid(int index) {
		int i = index - 1;
		if (i < 0 || i >= transactions.size()) {
			System.out.println("  Invalid index.");
			return;
		}
		Transaction t = transactions.get(i);
		if (t instanceof Debt) {
			((Debt) t).markAsPaid();
		} else {
			System.out.println("  That transaction is not a Debt.");
		}
	}

	// uses the Trackable interface to print tracking info for every transaction
	public void displayTrackingReport() {
		System.out.println("\n=== Tracking Report ===");
		for (Transaction t : transactions) {
			if (t instanceof Trackable) {
				System.out.println("  " + ((Trackable) t).track());
			}
		}
		System.out.println("=======================");
	}

	public void flagLargeTransactions(double threshold) {
		System.out.println("\n=== Flagged Transactions (over $" + String.format("%.2f", threshold) + ") ===");
		boolean any = false;
		for (Transaction t : transactions) {
			if (t instanceof Trackable trackable) {
				if (trackable.isFlagged(threshold)) {
					System.out.println("  FLAG: " + t);
					any = true;
				}
			}
		}
		if (!any) {
			System.out.println("  None flagged.");
		}
	}

	public void addCategory(BudgetCategory category) {
		categories.add(category);
		System.out.println("  Category added: " + category.getName()
				+ " (limit: $" + String.format("%.2f", category.getBudgetLimit()) + ")");
	}

	public void displayAllCategories() {
		if (categories.isEmpty()) {
			System.out.println("  No categories set yet.");
			return;
		}
		System.out.println("\n=== Budget Categories ===");
		for (BudgetCategory bc : categories) {
			bc.displayStatus();
			System.out.println("  " + "-".repeat(40));
		}
	}

	// finds a category by name, returns null if not found
	public BudgetCategory findCategory(String name) {
		for (BudgetCategory bc : categories) {
			if (bc.getName().equalsIgnoreCase(name)) {
				return bc;
			}
		}
		return null;
	}

	// calculates and prints the full financial summary
	public void displaySummary() {
		double totalIncome = 0;
		double totalExpenses = 0;
		double totalInvested = 0;
		double totalDebt = 0;
		int unpaidDebts = 0;

		for (Transaction t : transactions) {
			if (t.getType().equals("Income")) {
				totalIncome += t.getAmount();
			} else if (t.getType().equals("Expense")) {
				totalExpenses += t.getAmount();
			} else if (t.getType().equals("Investment")) {
				totalInvested += t.getAmount();
			} else if (t.getType().equals("Debt")) {
				totalDebt += t.getAmount();
				if (!((Debt) t).isPaid()) {
					unpaidDebts++;
				}
			}
		}

		double netBalance = startingBalance + totalIncome - totalExpenses - totalInvested;

		System.out.println("\n=========================================");
		System.out.println("  Financial Summary — " + ownerName);
		System.out.println("=========================================");
		System.out.printf("  Starting Balance : $%.2f%n", startingBalance);
		System.out.printf("  Total Income     : $%.2f%n", totalIncome);
		System.out.printf("  Total Expenses   : $%.2f%n", totalExpenses);
		System.out.printf("  Total Invested   : $%.2f%n", totalInvested);
		System.out.println("-----------------------------------------");
		System.out.printf("  Net Balance      : $%.2f%n", netBalance);
		System.out.printf("  Outstanding Debt : $%.2f (%d unpaid)%n", totalDebt, unpaidDebts);
		System.out.println("=========================================");

		if (netBalance < 0) {
			System.out.println("  WARNING: Net balance is negative!");
		}
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public double getStartingBalance() {
		return startingBalance;
	}

	public void setStartingBalance(double startingBalance) {
		this.startingBalance = startingBalance;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public ArrayList<BudgetCategory> getCategories() {
		return categories;
	}
}