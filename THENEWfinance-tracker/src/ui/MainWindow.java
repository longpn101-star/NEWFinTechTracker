package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.FinanceManager;

public class MainWindow {

    private FinanceManager manager;
    private Stage stage;
    private TabPane tabPane;

    private DashboardTab dashboardTab;
    private TransactionsTab transactionsTab;
    private AddTransactionTab addTransactionTab;
    private BudgetTab budgetTab;

    public MainWindow(FinanceManager manager, Stage stage) {
        this.manager = manager;
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1d2e;");

        root.setTop(buildHeader());

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: #1a1d2e;");

        dashboardTab      = new DashboardTab(manager, this);
        transactionsTab   = new TransactionsTab(manager, this);
        addTransactionTab = new AddTransactionTab(manager, this);
        budgetTab         = new BudgetTab(manager, this);

        Tab tab1 = new Tab("📊 Dashboard",       dashboardTab.getContent());
        Tab tab2 = new Tab("📋 Transactions",     transactionsTab.getContent());
        Tab tab3 = new Tab("➕ Add Transaction",  addTransactionTab.getContent());
        Tab tab4 = new Tab("📂 Budget",           budgetTab.getContent());

        tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 900, 650);
        stage.setTitle("Personal Finance Tracker — " + manager.getOwnerName());
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #12141f; -fx-padding: 12 20 12 20;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("💰 Personal Finance Tracker");
        title.setStyle("-fx-font-family: Georgia; -fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #e8d5a3;");

        Label owner = new Label("👤 " + manager.getOwnerName());
        owner.setStyle("-fx-font-family: Georgia; -fx-font-size: 13; -fx-text-fill: #8a8fa8;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(title, spacer, owner);
        return header;
    }

    public void refreshAll() {
        dashboardTab.refresh();
        transactionsTab.refresh();
        budgetTab.refresh();
    }

    public void goToTransactions() {
        tabPane.getSelectionModel().select(1);
    }

    public Stage getStage() {
        return stage;
    }
}