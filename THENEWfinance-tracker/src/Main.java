import model.*;
import ui.ConsoleUI;
import java.time.LocalDate;

// entry point for the console version of the app (Phase 1 & 2)
public class Main {

	public static void main(String[] args) {

		// create the manager with a starting balance
		FinanceManager manager = new FinanceManager("Max", 2500.00);

		// add some budget categories
		manager.addCategory(new BudgetCategory("Food", 400.00));
		manager.addCategory(new BudgetCategory("Housing", 1200.00));
		manager.addCategory(new BudgetCategory("Entertainment", 150.00));
		manager.addCategory(new BudgetCategory("Transport", 200.00));

		// load some sample transactions to test with
		manager.addTransaction(new Income(
				"Monthly Salary", 3200.00, LocalDate.now(), "Employment", "Employer", true));
		manager.addTransaction(new Income(
				"Freelance Project", 750.00, LocalDate.now().minusDays(5), "Freelance", "Client A", false));
		manager.addTransaction(new Expense(
				"Grocery Run", 87.50, LocalDate.now(), "Food", "Debit Card", true));
		manager.addTransaction(new Expense(
				"Netflix", 15.99, LocalDate.now(), "Entertainment", "Credit Card", false));
		manager.addTransaction(new Expense(
				"Monthly Rent", 950.00, LocalDate.now(), "Housing", "Bank Transfer", true));
		manager.addTransaction(new Investment(
				"S&P 500 ETF", 500.00, LocalDate.now(), "Investment", "ETF", 7.5));
		manager.addTransaction(new Investment(
				"Bitcoin", 200.00, LocalDate.now().minusDays(10), "Investment", "Crypto", 15.0));
		manager.addTransaction(new Debt(
				"Student Loan", 15000.00, LocalDate.now().minusYears(2), "Education", 4.5,
				LocalDate.now().plusYears(5)));
		manager.addTransaction(new Debt(
				"Credit Card Balance", 850.00, LocalDate.now().minusMonths(1), "Credit", 19.99,
				LocalDate.now().plusMonths(3)));

		// start the console UI
		ConsoleUI ui = new ConsoleUI(manager);
		ui.start();
	}
}