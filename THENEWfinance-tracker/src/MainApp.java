import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import ui.MainWindow;
import java.time.LocalDate;

// Phase 3 - JavaFX entry point
// run with: java --module-path /usr/share/java --add-modules javafx.controls,javafx.graphics,javafx.base -cp out MainApp
public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {

		// same setup as Main.java but launches the GUI instead
		FinanceManager manager = new FinanceManager("Max", 2500.00);

		manager.addCategory(new BudgetCategory("Food", 400.00));
		manager.addCategory(new BudgetCategory("Housing", 1200.00));
		manager.addCategory(new BudgetCategory("Entertainment", 150.00));
		manager.addCategory(new BudgetCategory("Transport", 200.00));

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

		MainWindow window = new MainWindow(manager);
		window.show(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}