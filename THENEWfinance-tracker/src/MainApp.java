import javafx.application.Application;
import javafx.stage.Stage;
import model.*;
import ui.MainWindow;
import java.time.LocalDate;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		FinanceManager manager = new FinanceManager("Max", 2500.00);

		// your same sample data setup here
		manager.addCategory(new BudgetCategory("Food", 400.00));
		manager.addCategory(new BudgetCategory("Housing", 1200.00));
		manager.addTransaction(new Income(
				"Monthly Salary", 3200.00, LocalDate.now(), "Employment", "Employer", true));
		// ... rest of your transactions

		MainWindow window = new MainWindow(manager, primaryStage);
		window.show();
	}

	public static void main(String[] args) {
		launch(args); // JavaFX launch instead of SwingUtilities.invokeLater
	}
}