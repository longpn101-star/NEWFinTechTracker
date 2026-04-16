package ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import model.*;

// second tab - shows all transactions in a table with search and filter
public class TransactionsTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private Tab tab;
	private TableView<Transaction> table;
	private TextField searchField;
	private ComboBox<String> typeFilter;
	private Label statusLabel;

	public TransactionsTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.tab = new Tab("📋  Transactions");
		buildContent();
	}

	public Tab getTab() {
		return tab;
	}

	public void refresh() {
		applyFilter();
	}

	private void buildContent() {
		VBox root = new VBox(14);
		root.setPadding(new Insets(24));
		root.setStyle("-fx-background-color: #1a1d2e;");

		Label heading = new Label("All Transactions");
		heading.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
		heading.setTextFill(Color.web("#e8d5a3"));

		// search and filter bar
		HBox filterBar = new HBox(12);
		filterBar.setAlignment(Pos.CENTER_LEFT);

		searchField = new TextField();
		searchField.setPromptText("Search by description...");
		searchField.setPrefWidth(260);
		styleTextField(searchField);
		searchField.textProperty().addListener((obs, old, newVal) -> applyFilter());

		typeFilter = new ComboBox<>();
		typeFilter.getItems().addAll("All", "Income", "Expense", "Investment", "Debt");
		typeFilter.setValue("All");
		typeFilter.setStyle("-fx-background-color: #22253a; -fx-text-fill: #c8ccdb; -fx-border-color: #3a3d5e; -fx-border-radius: 6;");
		typeFilter.setOnAction(e -> applyFilter());

		Button clearBtn = makeButton("Clear", "#444466");
		clearBtn.setOnAction(e -> {
			searchField.clear();
			typeFilter.setValue("All");
		});

		Label filterLbl = new Label("Filter:");
		filterLbl.setFont(Font.font("Georgia", 13));
		filterLbl.setTextFill(Color.web("#8a8fa8"));

		filterBar.getChildren().addAll(searchField, filterLbl, typeFilter, clearBtn);

		table = buildTable();

		// buttons for actions on selected row
		HBox actions = new HBox(12);
		actions.setAlignment(Pos.CENTER_LEFT);

		Button removeBtn = makeButton("🗑  Remove Selected", "#c0392b");
		removeBtn.setOnAction(e -> removeSelected());

		Button markPaidBtn = makeButton("✅  Mark Debt as Paid", "#27ae60");
		markPaidBtn.setOnAction(e -> markDebtPaid());

		statusLabel = new Label("");
		statusLabel.setFont(Font.font("Georgia", 12));
		statusLabel.setTextFill(Color.web("#8a8fa8"));

		actions.getChildren().addAll(removeBtn, markPaidBtn, statusLabel);

		root.getChildren().addAll(heading, filterBar, table, actions);

		ScrollPane scroll = new ScrollPane(root);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background-color: #1a1d2e; -fx-background: #1a1d2e;");
		tab.setContent(scroll);

		applyFilter();
	}

	@SuppressWarnings("unchecked")
	private TableView<Transaction> buildTable() {
		TableView<Transaction> tv = new TableView<>();
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tv.setPrefHeight(400);
		tv.setStyle("-fx-background-color: #22253a; -fx-border-color: #2a2d3e; -fx-border-radius: 8;");
		tv.setPlaceholder(new Label("No transactions found."));

		// type column with color coding
		TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
		typeCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getType()));
		typeCol.setPrefWidth(100);
		typeCol.setCellFactory(col -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) { setText(null); return; }
				setText(item);
				String color = switch (item) {
					case "Income"     -> "#27ae60";
					case "Expense"    -> "#e67e22";
					case "Investment" -> "#8e44ad";
					case "Debt"       -> "#e74c3c";
					default           -> "#c8ccdb";
				};
				setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
			}
		});

		TableColumn<Transaction, String> descCol = new TableColumn<>("Description");
		descCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescription()));
		descCol.setPrefWidth(220);

		TableColumn<Transaction, String> amtCol = new TableColumn<>("Amount");
		amtCol.setCellValueFactory(d -> new SimpleStringProperty(String.format("$%.2f", d.getValue().getAmount())));
		amtCol.setPrefWidth(110);

		TableColumn<Transaction, String> catCol = new TableColumn<>("Category");
		catCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCategory()));
		catCol.setPrefWidth(130);

		TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
		dateCol.setCellValueFactory(d -> new SimpleStringProperty(
				d.getValue().getDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))));
		dateCol.setPrefWidth(120);

		TableColumn<Transaction, String> extraCol = new TableColumn<>("Details");
		extraCol.setCellValueFactory(d -> {
			Transaction t = d.getValue();
			String detail = switch (t.getType()) {
				case "Income"     -> "Source: " + ((Income) t).getSource();
				case "Expense"    -> "Via: " + ((Expense) t).getPaymentMethod();
				case "Investment" -> ((Investment) t).getInvestmentType() + " | " + ((Investment) t).getExpectedReturn() + "% return";
				case "Debt"       -> ((Debt) t).getInterestRate() + "% interest | " + (((Debt) t).isPaid() ? "PAID" : "Outstanding");
				default           -> "";
			};
			return new SimpleStringProperty(detail);
		});
		extraCol.setPrefWidth(200);

		tv.getColumns().addAll(typeCol, descCol, amtCol, catCol, dateCol, extraCol);
		return tv;
	}

	private void applyFilter() {
		String keyword = searchField.getText().toLowerCase().trim();
		String type = typeFilter.getValue();

		ObservableList<Transaction> filtered = FXCollections.observableArrayList();
		for (Transaction t : manager.getTransactions()) {
			boolean matchType = type.equals("All") || t.getType().equalsIgnoreCase(type);
			boolean matchKw   = keyword.isEmpty() || t.getDescription().toLowerCase().contains(keyword);
			if (matchType && matchKw) {
				filtered.add(t);
			}
		}
		table.setItems(filtered);
		statusLabel.setText("Showing " + filtered.size() + " of " + manager.getTransactions().size());
	}

	private void removeSelected() {
		Transaction selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			statusLabel.setText("Select a transaction first.");
			return;
		}
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
				"Remove \"" + selected.getDescription() + "\"?", ButtonType.YES, ButtonType.NO);
		confirm.setHeaderText("Confirm");
		confirm.showAndWait().ifPresent(btn -> {
			if (btn == ButtonType.YES) {
				manager.getTransactions().remove(selected);
				mainWindow.refreshAll();
				statusLabel.setText("Removed: " + selected.getDescription());
			}
		});
	}

	private void markDebtPaid() {
		Transaction selected = table.getSelectionModel().getSelectedItem();
		if (selected == null) {
			statusLabel.setText("Select a transaction first.");
			return;
		}
		if (!(selected instanceof Debt debt)) {
			statusLabel.setText("That transaction is not a Debt.");
			return;
		}
		if (debt.isPaid()) {
			statusLabel.setText("Already marked as paid.");
			return;
		}
		debt.markAsPaid();
		mainWindow.refreshAll();
		statusLabel.setText("Marked as paid: " + debt.getDescription());
	}

	private void styleTextField(TextField tf) {
		tf.setStyle(
				"-fx-background-color: #22253a;" +
				"-fx-text-fill: #c8ccdb;" +
				"-fx-prompt-text-fill: #555878;" +
				"-fx-border-color: #3a3d5e;" +
				"-fx-border-radius: 6;" +
				"-fx-background-radius: 6;" +
				"-fx-padding: 6 10 6 10;"
		);
	}

	private Button makeButton(String text, String color) {
		Button btn = new Button(text);
		btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
		btn.setTextFill(Color.WHITE);
		btn.setPadding(new Insets(9, 18, 9, 18));
		btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8; -fx-cursor: hand;");
		btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
		btn.setOnMouseExited(e -> btn.setOpacity(1.0));
		return btn;
	}
}