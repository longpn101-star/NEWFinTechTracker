package ui;

import model.*;
import java.time.LocalDate;
import java.util.Scanner;

// handles all the console input and output
// this was the Phase 1 & 2 UI before we added JavaFX
public class ConsoleUI {

	private FinanceManager manager;
	private Scanner scanner;

	public ConsoleUI(FinanceManager manager) {
		this.manager = manager;
		this.scanner = new Scanner(System.in);
	}

	public void start() {
		printHeader();

		boolean running = true;
		while (running) {
			printMainMenu();
			int choice = readInt("Enter choice: ");
			System.out.println();

			switch (choice) {
				case 1 -> addMenu();
				case 2 -> viewMenu();
				case 3 -> manageMenu();
				case 4 -> manager.displaySummary();
				case 5 -> manager.displayAllCategories();
				case 0 -> {
					System.out.println("  Goodbye!");
					running = false;
				}
				default -> System.out.println("  Invalid option, try again.");
			}
		}
	}

	private void printHeader() {
		System.out.println();
		System.out.println("  ====================================");
		System.out.println("    Personal Finance Tracker");
		System.out.println("    Welcome, " + manager.getOwnerName());
		System.out.println("  ====================================");
	}

	private void printMainMenu() {
		System.out.println("\n  --- Main Menu ---");
		System.out.println("  1. Add Transaction");
		System.out.println("  2. View Transactions");
		System.out.println("  3. Manage");
		System.out.println("  4. Financial Summary");
		System.out.println("  5. Budget Categories");
		System.out.println("  0. Exit");
	}

	private void addMenu() {
		System.out.println("  --- Add Transaction ---");
		System.out.println("  1. Income");
		System.out.println("  2. Expense");
		System.out.println("  3. Investment");
		System.out.println("  4. Debt");
		System.out.println("  0. Back");
		int choice = readInt("  Choice: ");
		System.out.println();

		switch (choice) {
			case 1 -> addIncomeMenu();
			case 2 -> addExpenseMenu();
			case 3 -> addInvestmentMenu();
			case 4 -> addDebtMenu();
			case 0 -> {}
			default -> System.out.println("  Invalid option.");
		}
	}

	private void viewMenu() {
		System.out.println("  --- View Transactions ---");
		System.out.println("  1. View All (detailed)");
		System.out.println("  2. View All (list)");
		System.out.println("  3. Filter by Type");
		System.out.println("  4. Search by Description");
		System.out.println("  0. Back");
		int choice = readInt("  Choice: ");
		System.out.println();

		switch (choice) {
			case 1 -> manager.displayAllTransactions();
			case 2 -> manager.listTransactions();
			case 3 -> {
				String type = readString("  Type (Income/Expense/Investment/Debt): ");
				manager.displayByType(type);
			}
			case 4 -> {
				String keyword = readString("  Search keyword: ");
				manager.searchByDescription(keyword);
			}
			case 0 -> {}
			default -> System.out.println("  Invalid option.");
		}
	}

	private void manageMenu() {
		System.out.println("  --- Manage ---");
		System.out.println("  1. Remove Transaction");
		System.out.println("  2. Mark Debt as Paid");
		System.out.println("  3. Add Budget Category");
		System.out.println("  4. Tracking Report");
		System.out.println("  5. Flag Large Transactions");
		System.out.println("  0. Back");
		int choice = readInt("  Choice: ");
		System.out.println();

		switch (choice) {
			case 1 -> {
				manager.listTransactions();
				int idx = readInt("  Enter # to remove: ");
				manager.removeTransaction(idx);
			}
			case 2 -> {
				manager.displayByType("Debt");
				int idx = readInt("  Enter debt # to mark as paid: ");
				manager.markDebtAsPaid(idx);
			}
			case 3 -> {
				String name = readString("  Category name: ");
				double limit = readDouble("  Budget limit ($): ");
				manager.addCategory(new BudgetCategory(name, limit));
			}
			case 4 -> manager.displayTrackingReport();
			case 5 -> {
				double threshold = readDouble("  Flag transactions above ($): ");
				manager.flagLargeTransactions(threshold);
			}
			case 0 -> {}
			default -> System.out.println("  Invalid option.");
		}
	}

	private void addIncomeMenu() {
		System.out.println("  --- Add Income ---");
		String desc   = readString("  Description: ");
		double amount = readDouble("  Amount ($): ");
		String source = readString("  Source: ");
		String cat    = readString("  Category: ");
		boolean recur = readYesNo("  Recurring? (y/n): ");

		manager.addTransaction(new Income(desc, amount, LocalDate.now(), cat, source, recur));
	}

	private void addExpenseMenu() {
		System.out.println("  --- Add Expense ---");
		String desc   = readString("  Description: ");
		double amount = readDouble("  Amount ($): ");
		String method = readString("  Payment method: ");
		String cat    = readString("  Category: ");
		boolean nec   = readYesNo("  Necessity? (y/n): ");

		manager.addTransaction(new Expense(desc, amount, LocalDate.now(), cat, method, nec));
	}

	private void addInvestmentMenu() {
		System.out.println("  --- Add Investment ---");
		String desc   = readString("  Description: ");
		double amount = readDouble("  Amount ($): ");
		String type   = readString("  Investment type: ");
		double ret    = readDouble("  Expected return (%): ");

		manager.addTransaction(new Investment(desc, amount, LocalDate.now(), "Investment", type, ret));
	}

	private void addDebtMenu() {
		System.out.println("  --- Add Debt ---");
		String desc   = readString("  Description: ");
		double amount = readDouble("  Principal ($): ");
		double rate   = readDouble("  Interest rate (%): ");
		String cat    = readString("  Category: ");
		int months    = readInt("  Repayment period (months): ");

		manager.addTransaction(new Debt(desc, amount, LocalDate.now(), cat, rate,
				LocalDate.now().plusMonths(months)));
	}

	// input helpers
	private String readString(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}

	private int readInt(String prompt) {
		System.out.print(prompt);
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private double readDouble(String prompt) {
		System.out.print(prompt);
		try {
			return Double.parseDouble(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("  Invalid number, using 0.");
			return 0.0;
		}
	}

	private boolean readYesNo(String prompt) {
		System.out.print(prompt);
		String input = scanner.nextLine().trim().toLowerCase();
		return input.equals("y") || input.equals("yes");
	}
}