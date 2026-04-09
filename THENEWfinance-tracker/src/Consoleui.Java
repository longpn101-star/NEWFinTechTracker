package ui;

import model.*;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Console-based user interface for the Personal Finance Tracker.
 * Handles all user input/output in Phase 1 & 2 before JavaFX GUI is added.
 */
public class ConsoleUI {

    private FinanceManager manager;
    private Scanner scanner;

    public ConsoleUI(FinanceManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main console menu loop.
     */
    public void start() {
        System.out.println("====================================");
        System.out.println("  Personal Finance Tracker");
        System.out.println("  Welcome, " + manager.getOwnerName());
        System.out.println("====================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addIncomeMenu();
                case 2 -> addExpenseMenu();
                case 3 -> addInvestmentMenu();
                case 4 -> addDebtMenu();
                case 5 -> manager.displayAllTransactions();
                case 6 -> manager.displaySummary();
                case 7 -> manager.displayAllCategories();
                case 0 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add Income");
        System.out.println("2. Add Expense");
        System.out.println("3. Add Investment");
        System.out.println("4. Add Debt");
        System.out.println("5. View All Transactions");
        System.out.println("6. View Summary");
        System.out.println("7. View Budget Categories");
        System.out.println("0. Exit");
    }

    private void addIncomeMenu() {
        System.out.println("\n-- Add Income --");
        String desc = readString("Description: ");
        double amount = readDouble("Amount: $");
        String source = readString("Source (e.g., Employer, Freelance): ");
        String category = readString("Category: ");

        Income income = new Income(desc, amount, LocalDate.now(), category, source, false);
        manager.addTransaction(income);
    }

    private void addExpenseMenu() {
        System.out.println("\n-- Add Expense --");
        String desc = readString("Description: ");
        double amount = readDouble("Amount: $");
        String method = readString("Payment method: ");
        String category = readString("Category: ");

        Expense expense = new Expense(desc, amount, LocalDate.now(), category, method, true);
        manager.addTransaction(expense);
    }

    private void addInvestmentMenu() {
        System.out.println("\n-- Add Investment --");
        String desc = readString("Description: ");
        double amount = readDouble("Amount: $");
        String type = readString("Investment type (e.g., Stocks, ETF): ");
        double returns = readDouble("Expected return (%): ");

        Investment investment = new Investment(desc, amount, LocalDate.now(), "Investment", type, returns);
        manager.addTransaction(investment);
    }

    private void addDebtMenu() {
        System.out.println("\n-- Add Debt --");
        String desc = readString("Description: ");
        double amount = readDouble("Amount: $");
        double rate = readDouble("Interest rate (%): ");
        String category = readString("Category: ");

        Debt debt = new Debt(desc, amount, LocalDate.now(), category, rate, LocalDate.now().plusMonths(12));
        manager.addTransaction(debt);
    }

    // --- Input Helpers ---

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, defaulting to 0.0");
            return 0.0;
        }
    }
}