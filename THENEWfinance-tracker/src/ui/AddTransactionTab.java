package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import model.*;
import java.time.LocalDate;

// third tab - forms to add new transactions
// has a sub-tab for each transaction type
public class AddTransactionTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private Tab tab;
	private Label feedbackLabel;

	public AddTransactionTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.tab = new Tab("➕  Add Transaction");
		buildContent();
	}

	public Tab getTab() {
		return tab;
	}

	private void buildContent() {
		VBox root = new VBox(16);
		root.setPadding(new Insets(24));
		root.setStyle("-fx-background-color: #1a1d2e;");

		Label heading = new Label("Add New Transaction");
		heading.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
		heading.setTextFill(Color.web("#e8d5a3"));

		// shows success or error after submitting a form
		feedbackLabel = new Label("");
		feedbackLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));

		TabPane formTabs = new TabPane();
		formTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		formTabs.setStyle("-fx-background-color: #22253a;");
		formTabs.getTabs().addAll(
				buildIncomeForm(),
				buildExpenseForm(),
				buildInvestmentForm(),
				buildDebtForm()
		);

		root.getChildren().addAll(heading, feedbackLabel, formTabs);

		ScrollPane scroll = new ScrollPane(root);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background-color: #1a1d2e; -fx-background: #1a1d2e;");
		tab.setContent(scroll);
	}

	private Tab buildIncomeForm() {
		Tab t = new Tab("💵  Income");
		GridPane grid = makeGrid();

		TextField descField   = tf("e.g. Monthly Salary");
		TextField amountField = tf("e.g. 3200.00");
		TextField sourceField = tf("e.g. Employer, Freelance");
		TextField catField    = tf("e.g. Employment");
		CheckBox recurChk     = new CheckBox("Recurring monthly income");
		recurChk.setTextFill(Color.web("#c8ccdb"));

		addRow(grid, 0, "Description:", descField);
		addRow(grid, 1, "Amount ($):",  amountField);
		addRow(grid, 2, "Source:",      sourceField);
		addRow(grid, 3, "Category:",    catField);
		grid.add(recurChk, 1, 4);

		Button addBtn   = makeButton("Add Income", "#27ae60");
		Button clearBtn = makeClearButton();

		addBtn.setOnAction(e -> {
			try {
				double amt = Double.parseDouble(amountField.getText().trim());
				if (amt <= 0) throw new NumberFormatException();
				manager.addTransaction(new Income(
						descField.getText().trim(), amt, LocalDate.now(),
						catField.getText().trim(), sourceField.getText().trim(),
						recurChk.isSelected()));
				mainWindow.refreshAll();
				showSuccess("Income added successfully!");
				clearFields(descField, amountField, sourceField, catField);
				recurChk.setSelected(false);
			} catch (NumberFormatException ex) {
				showError("Please enter a valid positive amount.");
			}
		});
		clearBtn.setOnAction(e -> clearFields(descField, amountField, sourceField, catField));

		t.setContent(makeFormBox(grid, makeButtonRow(addBtn, clearBtn)));
		return t;
	}

	private Tab buildExpenseForm() {
		Tab t = new Tab("🛒  Expense");
		GridPane grid = makeGrid();

		TextField descField   = tf("e.g. Grocery Run");
		TextField amountField = tf("e.g. 87.50");
		TextField methodField = tf("e.g. Debit Card, Cash");
		TextField catField    = tf("e.g. Food");
		CheckBox necChk       = new CheckBox("This is a necessity");
		necChk.setTextFill(Color.web("#c8ccdb"));

		addRow(grid, 0, "Description:",    descField);
		addRow(grid, 1, "Amount ($):",     amountField);
		addRow(grid, 2, "Payment Method:", methodField);
		addRow(grid, 3, "Category:",       catField);
		grid.add(necChk, 1, 4);

		Button addBtn   = makeButton("Add Expense", "#e67e22");
		Button clearBtn = makeClearButton();

		addBtn.setOnAction(e -> {
			try {
				double amt = Double.parseDouble(amountField.getText().trim());
				if (amt <= 0) throw new NumberFormatException();
				manager.addTransaction(new Expense(
						descField.getText().trim(), amt, LocalDate.now(),
						catField.getText().trim(), methodField.getText().trim(),
						necChk.isSelected()));
				mainWindow.refreshAll();
				showSuccess("Expense added successfully!");
				clearFields(descField, amountField, methodField, catField);
				necChk.setSelected(false);
			} catch (NumberFormatException ex) {
				showError("Please enter a valid positive amount.");
			}
		});
		clearBtn.setOnAction(e -> clearFields(descField, amountField, methodField, catField));

		t.setContent(makeFormBox(grid, makeButtonRow(addBtn, clearBtn)));
		return t;
	}

	private Tab buildInvestmentForm() {
		Tab t = new Tab("📈  Investment");
		GridPane grid = makeGrid();

		TextField descField   = tf("e.g. S&P 500 ETF");
		TextField amountField = tf("e.g. 500.00");
		TextField typeField   = tf("e.g. ETF, Stocks, Crypto");
		TextField retField    = tf("e.g. 7.5");

		addRow(grid, 0, "Description:",         descField);
		addRow(grid, 1, "Amount ($):",          amountField);
		addRow(grid, 2, "Investment Type:",     typeField);
		addRow(grid, 3, "Expected Return (%):", retField);

		Button addBtn   = makeButton("Add Investment", "#8e44ad");
		Button clearBtn = makeClearButton();

		addBtn.setOnAction(e -> {
			try {
				double amt = Double.parseDouble(amountField.getText().trim());
				double ret = Double.parseDouble(retField.getText().trim());
				if (amt <= 0) throw new NumberFormatException();
				manager.addTransaction(new Investment(
						descField.getText().trim(), amt, LocalDate.now(),
						"Investment", typeField.getText().trim(), ret));
				mainWindow.refreshAll();
				showSuccess("Investment added successfully!");
				clearFields(descField, amountField, typeField, retField);
			} catch (NumberFormatException ex) {
				showError("Please enter valid numbers.");
			}
		});
		clearBtn.setOnAction(e -> clearFields(descField, amountField, typeField, retField));

		t.setContent(makeFormBox(grid, makeButtonRow(addBtn, clearBtn)));
		return t;
	}

	private Tab buildDebtForm() {
		Tab t = new Tab("⚠️  Debt");
		GridPane grid = makeGrid();

		TextField descField   = tf("e.g. Student Loan");
		TextField amountField = tf("e.g. 15000.00");
		TextField rateField   = tf("e.g. 4.5");
		TextField catField    = tf("e.g. Education");
		TextField monthsField = tf("e.g. 60");

		addRow(grid, 0, "Description:",        descField);
		addRow(grid, 1, "Principal ($):",      amountField);
		addRow(grid, 2, "Interest Rate (%):",  rateField);
		addRow(grid, 3, "Category:",           catField);
		addRow(grid, 4, "Repayment (months):", monthsField);

		Button addBtn   = makeButton("Add Debt", "#c0392b");
		Button clearBtn = makeClearButton();

		addBtn.setOnAction(e -> {
			try {
				double amt  = Double.parseDouble(amountField.getText().trim());
				double rate = Double.parseDouble(rateField.getText().trim());
				int months  = Integer.parseInt(monthsField.getText().trim());
				if (amt <= 0 || months <= 0) throw new NumberFormatException();
				manager.addTransaction(new Debt(
						descField.getText().trim(), amt, LocalDate.now(),
						catField.getText().trim(), rate,
						LocalDate.now().plusMonths(months)));
				mainWindow.refreshAll();
				showSuccess("Debt added successfully!");
				clearFields(descField, amountField, rateField, catField, monthsField);
			} catch (NumberFormatException ex) {
				showError("Please enter valid numbers.");
			}
		});
		clearBtn.setOnAction(e -> clearFields(descField, amountField, rateField, catField, monthsField));

		t.setContent(makeFormBox(grid, makeButtonRow(addBtn, clearBtn)));
		return t;
	}

	// helper methods to keep the form building code cleaner
	private GridPane makeGrid() {
		GridPane grid = new GridPane();
		grid.setHgap(16);
		grid.setVgap(14);
		grid.setPadding(new Insets(20, 0, 20, 0));
		grid.getColumnConstraints().addAll(
				new ColumnConstraints(180),
				new ColumnConstraints(320)
		);
		return grid;
	}

	private void addRow(GridPane grid, int row, String labelText, Control field) {
		Label lbl = new Label(labelText);
		lbl.setFont(Font.font("Georgia", 13));
		lbl.setTextFill(Color.web("#8a8fa8"));
		lbl.setMaxWidth(Double.MAX_VALUE);
		lbl.setAlignment(Pos.CENTER_RIGHT);
		grid.add(lbl, 0, row);
		grid.add(field, 1, row);
	}

	private TextField tf(String prompt) {
		TextField tf = new TextField();
		tf.setPromptText(prompt);
		tf.setStyle(
				"-fx-background-color: #2a2d3e;" +
				"-fx-text-fill: #c8ccdb;" +
				"-fx-prompt-text-fill: #555878;" +
				"-fx-border-color: #3a3d5e;" +
				"-fx-border-radius: 6;" +
				"-fx-background-radius: 6;" +
				"-fx-padding: 8 10 8 10;"
		);
		return tf;
	}

	private Button makeButton(String text, String color) {
		Button btn = new Button(text);
		btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
		btn.setTextFill(Color.WHITE);
		btn.setPadding(new Insets(10, 24, 10, 24));
		btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8; -fx-cursor: hand;");
		btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
		btn.setOnMouseExited(e -> btn.setOpacity(1.0));
		return btn;
	}

	private Button makeClearButton() {
		Button btn = new Button("Clear");
		btn.setFont(Font.font("Georgia", 13));
		btn.setTextFill(Color.web("#c8ccdb"));
		btn.setPadding(new Insets(9, 18, 9, 18));
		btn.setStyle("-fx-background-color: #2a2d3e; -fx-background-radius: 8; -fx-cursor: hand;");
		btn.setOnMouseEntered(e -> btn.setOpacity(0.75));
		btn.setOnMouseExited(e -> btn.setOpacity(1.0));
		return btn;
	}

	private HBox makeButtonRow(Button... buttons) {
		HBox row = new HBox(12, buttons);
		row.setAlignment(Pos.CENTER_LEFT);
		return row;
	}

	private VBox makeFormBox(GridPane grid, HBox buttons) {
		VBox box = new VBox(12, grid, buttons);
		box.setPadding(new Insets(24));
		box.setStyle("-fx-background-color: #1a1d2e;");
		return box;
	}

	private void clearFields(TextField... fields) {
		for (TextField f : fields) f.clear();
		feedbackLabel.setText("");
	}

	private void showSuccess(String msg) {
		feedbackLabel.setTextFill(Color.web("#27ae60"));
		feedbackLabel.setText("✅ " + msg);
	}

	private void showError(String msg) {
		feedbackLabel.setTextFill(Color.web("#e74c3c"));
		feedbackLabel.setText("❌ " + msg);
	}
}