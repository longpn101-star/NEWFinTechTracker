package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.*;
import java.util.ArrayList;

public class TransactionsTab {

    private FinanceManager manager;
    private MainWindow mainWindow;
    private VBox content;
    private TableView<TransactionRow> table;
    private ObservableList<TransactionRow> tableData;
    private TextField searchField;
    private ComboBox<String> typeFilter;
    private Label statusLabel;

    public TransactionsTab(FinanceManager manager, MainWindow mainWindow) {
        this.manager = manager;
        this.mainWindow = mainWindow;
        this.content = new VBox(12);
        buildContent();
    }

    public VBox getContent() {
        return content;
    }

    public void refresh() {
        applyFilter();
    }

    private void buildContent() {
        content.setStyle("-fx-background-color: #1a1d2e; -fx-padding: 20;");

        Label heading = new Label("All Transactions");
        heading.setStyle("-fx-font-family: Georgia; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #e8d5a3;");

        // filter bar
        HBox filterBar = new HBox(10);
        filterBar.setStyle("-fx-background-color: #1a1d2e;");

        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setStyle("-fx-background-color: #22253a; -fx-text-fill: #c8ccdb; -fx-prompt-text-fill: #555878;");
        searchField.setPrefWidth(200);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        typeFilter = new ComboBox<>(FXCollections.observableArrayList("All", "Income", "Expense", "Investment", "Debt"));
        typeFilter.setValue("All");
        typeFilter.setStyle("-fx-background-color: #22253a; -fx-text-fill: #c8ccdb;");
        typeFilter.setOnAction(e -> applyFilter());

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #44446a; -fx-text-fill: #c8ccdb; -fx-cursor: hand;");
        clearBtn.setOnAction(e -> { searchField.clear(); typeFilter.setValue("All"); });

        filterBar.getChildren().addAll(new Label("Search:") {{ setStyle("-fx-text-fill: #8a8fa8;"); }},
                searchField, new Label("Filter:") {{ setStyle("-fx-text-fill: #8a8fa8;"); }}, typeFilter, clearBtn);

        // table
        tableData = FXCollections.observableArrayList();
        table = new TableView<>(tableData);
        table.setStyle("-fx-background-color: #22253a; -fx-text-fill: #c8ccdb;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<TransactionRow, String> typeCol  = col("Type",        "type",        120);
        TableColumn<TransactionRow, String> descCol  = col("Description", "description", 200);
        TableColumn<TransactionRow, String> amtCol   = col("Amount",      "amount",      100);
        TableColumn<TransactionRow, String> catCol   = col("Category",    "category",    120);
        TableColumn<TransactionRow, String> dateCol  = col("Date",        "date",        120);
        TableColumn<TransactionRow, String> detCol   = col("Details",     "details",     180);

        table.getColumns().addAll(typeCol, descCol, amtCol, catCol, dateCol, detCol);

        // action buttons
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 12; -fx-text-fill: #8a8fa8;");

        Button removeBtn = new Button("🗑  Remove Selected");
        removeBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        removeBtn.setOnAction(e -> removeSelected());

        Button markPaidBtn = new Button("✅  Mark Debt as Paid");
        markPaidBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        markPaidBtn.setOnAction(e -> markDebtPaid());

        HBox actions = new HBox(10, removeBtn, markPaidBtn, statusLabel);
        actions.setStyle("-fx-background-color: #1a1d2e;");

        content.getChildren().addAll(heading, filterBar, table, actions);
        applyFilter();
    }

    private TableColumn<TransactionRow, String> col(String title, String field, int width) {
        TableColumn<TransactionRow, String> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(field));
        col.setPrefWidth(width);
        col.setStyle("-fx-text-fill: #c8ccdb;");
        return col;
    }

    private void applyFilter() {
        String keyword = searchField == null ? "" : searchField.getText().toLowerCase().trim();
        String type = typeFilter == null ? "All" : typeFilter.getValue();

        tableData.clear();
        int count = 0;
        for (Transaction t : manager.getTransactions()) {
            boolean matchType = type.equals("All") || t.getType().equalsIgnoreCase(type);
            boolean matchKw   = keyword.isEmpty() || t.getDescription().toLowerCase().contains(keyword);
            if (matchType && matchKw) {
                String detail = switch (t.getType()) {
                    case "Income"     -> "Source: " + ((Income) t).getSource();
                    case "Expense"    -> "Via: " + ((Expense) t).getPaymentMethod();
                    case "Investment" -> ((Investment) t).getInvestmentType() + " | " + ((Investment) t).getExpectedReturn() + "%";
                    case "Debt"       -> ((Debt) t).getInterestRate() + "% | " + (((Debt) t).isPaid() ? "PAID" : "Outstanding");
                    default           -> "";
                };
                tableData.add(new TransactionRow(
                        t.getType(),
                        t.getDescription(),
                        String.format("$%.2f", t.getAmount()),
                        t.getCategory(),
                        t.getDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        detail
                ));
                count++;
            }
        }
        if (statusLabel != null)
            statusLabel.setText("Showing " + count + " of " + manager.getTransactions().size());
    }

    private void removeSelected() {
        TransactionRow row = table.getSelectionModel().getSelectedItem();
        if (row == null) { statusLabel.setText("Select a transaction first."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Remove \"" + row.getDescription() + "\"?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                ArrayList<Transaction> list = manager.getTransactions();
                list.removeIf(t -> t.getDescription().equals(row.getDescription()));
                mainWindow.refreshAll();
                statusLabel.setText("Removed: " + row.getDescription());
            }
        });
    }

    private void markDebtPaid() {
        TransactionRow row = table.getSelectionModel().getSelectedItem();
        if (row == null) { statusLabel.setText("Select a transaction first."); return; }
        if (!row.getType().equals("Debt")) { statusLabel.setText("That is not a Debt."); return; }

        for (Transaction t : manager.getTransactions()) {
            if (t instanceof Debt debt && t.getDescription().equals(row.getDescription())) {
                if (debt.isPaid()) { statusLabel.setText("Already paid."); return; }
                debt.markAsPaid();
                mainWindow.refreshAll();
                statusLabel.setText("Marked as paid: " + row.getDescription());
                return;
            }
        }
    }

    // simple data class for the TableView rows
    public static class TransactionRow {
        private final String type, description, amount, category, date, details;

        public TransactionRow(String type, String description, String amount,
                              String category, String date, String details) {
            this.type = type; this.description = description; this.amount = amount;
            this.category = category; this.date = date; this.details = details;
        }

        public String getType()        { return type; }
        public String getDescription() { return description; }
        public String getAmount()      { return amount; }
        public String getCategory()    { return category; }
        public String getDate()        { return date; }
        public String getDetails()     { return details; }
    }
}