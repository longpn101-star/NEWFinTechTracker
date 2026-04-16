package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import model.FinanceManager;

// the main window of the GUI, holds all the tabs
public class MainWindow {

	private FinanceManager manager;
	private TabPane tabPane;

	// keep references to each tab so we can refresh them
	private DashboardTab dashboardTab;
	private TransactionsTab transactionsTab;
	private AddTransactionTab addTransactionTab;
	private BudgetTab budgetTab;

	public MainWindow(FinanceManager manager) {
		this.manager = manager;
	}

	public void show(Stage stage) {
		stage.setTitle("Personal Finance Tracker — " + manager.getOwnerName());
		stage.setMinWidth(860);
		stage.setMinHeight(620);

		HBox header = buildHeader();

		// create all four tabs
		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		dashboardTab      = new DashboardTab(manager, this);
		transactionsTab   = new TransactionsTab(manager, this);
		addTransactionTab = new AddTransactionTab(manager, this);
		budgetTab         = new BudgetTab(manager, this);

		tabPane.getTabs().addAll(
				dashboardTab.getTab(),
				transactionsTab.getTab(),
				addTransactionTab.getTab(),
				budgetTab.getTab()
		);

		BorderPane root = new BorderPane();
		root.setTop(header);
		root.setCenter(tabPane);
		root.setStyle("-fx-background-color: #1a1d2e;");

		Scene scene = new Scene(root, 900, 650);
		stage.setScene(scene);
		stage.show();
	}

	private HBox buildHeader() {
		HBox header = new HBox();
		header.setPadding(new Insets(14, 24, 14, 24));
		header.setAlignment(Pos.CENTER_LEFT);
		header.setSpacing(12);
		header.setStyle("-fx-background-color: #12141f; -fx-border-color: #2a2d3e; -fx-border-width: 0 0 1 0;");

		Label icon = new Label("💰");
		icon.setFont(Font.font(24));

		Label title = new Label("Personal Finance Tracker");
		title.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
		title.setTextFill(Color.web("#e8d5a3"));

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		Label owner = new Label("👤 " + manager.getOwnerName());
		owner.setFont(Font.font("Georgia", 13));
		owner.setTextFill(Color.web("#8a8fa8"));

		header.getChildren().addAll(icon, title, spacer, owner);
		return header;
	}

	// called after adding/removing transactions so all tabs stay up to date
	public void refreshAll() {
		dashboardTab.refresh();
		transactionsTab.refresh();
		budgetTab.refresh();
	}

	public void goToTransactions() {
		tabPane.getSelectionModel().select(1);
	}
}