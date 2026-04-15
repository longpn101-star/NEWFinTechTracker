import model.*;
import ui.ConsoleUI;
import java.time.LocalDate;
 //hias
/**
 * Entry point for the Personal Finance Tracker application.
 * Initializes the manager and launches the console UI.
 */
public class Main {
 
    public static void main(String[] args) {
 
        // Create the finance manager for the user
        FinanceManager manager = new FinanceManager("Max", 2500.00);
 
        // Pre-load some sample budget categories
        manager.addCategory(new BudgetCategory("Food", 400.00));
        manager.addCategory(new BudgetCategory("Housing", 1200.00));
        manager.addCategory(new BudgetCategory("Entertainment", 150.00));
 
        // Pre-load some sample transactions for testing
        manager.addTransaction(new Income(
            "Monthly Salary", 3200.00, LocalDate.now(), "Employment", "Employer", true
        ));
        manager.addTransaction(new Expense(
            "Grocery Run", 87.50, LocalDate.now(), "Food", "Debit Card", true
        ));
        manager.addTransaction(new Investment(
            "S&P 500 ETF", 500.00, LocalDate.now(), "Investment", "ETF", 7.5
        ));
        manager.addTransaction(new Debt(
            "Student Loan", 15000.00, LocalDate.now(), "Education", 4.5,
            LocalDate.now().plusYears(5)
        ));
 
        // Launch the console UI
        ConsoleUI ui = new ConsoleUI(manager);
        ui.start();
    }
}
