package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import model.BudgetCategory;
import model.FinanceManager;

// fourth tab - shows budget categories and lets you add new ones
public class BudgetTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private Tab tab;
	private VBox categoryList;
	private Label feedbackLabel;

	public BudgetTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.tab = new Tab("📂  Budget");
		buildContent();
	}

	public Tab getTab() {
		return tab;
	}

	public void refresh() {
		rebuildCategoryList();
	}

	private void buildContent() {
		VBox root = new VBox(20);
		root.setPadding(new Insets(24));
		root.setStyle("-fx-background-color: #1a1d2e;");

		Label heading = new Label("Budget Categories");
		heading.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
		heading.setTextFill(Color.web("#e8d5a3"));

		feedbackLabel = new Label("");
		feedbackLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));

		categoryList = new VBox(12);
		rebuildCategoryList();

		Separator sep = new Separator();
		sep.setStyle("-fx-background-color: #2a2d3e;");

		Label addHeading = new Label("Add New Category");
		addHeading.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
		addHeading.setTextFill(Color.web("#c8ccdb"));

		HBox addForm = buildAddForm();

		root.getChildren().addAll(heading, feedbackLabel, categoryList, sep, addHeading, addForm);

		ScrollPane scroll = new ScrollPane(root);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background-color: #1a1d2e; -fx-background: #1a1d2e;");
		tab.setContent(scroll);
	}

	private void rebuildCategoryList() {
		categoryList.getChildren().clear();

		if (manager.getCategories().isEmpty()) {
			Label none = new Label("  No categories added yet.");
			none.setFont(Font.font("Georgia", 14));
			none.setTextFill(Color.web("#555878"));
			categoryList.getChildren().add(none);
			return;
		}

		for (BudgetCategory cat : manager.getCategories()) {
			categoryList.getChildren().add(buildCategoryCard(cat));
		}
	}

	private VBox buildCategoryCard(BudgetCategory cat) {
		VBox card = new VBox(8);
		card.setPadding(new Insets(16));
		card.setStyle(
				"-fx-background-color: #22253a;" +
				"-fx-background-radius: 10;" +
				"-fx-border-color: #2a2d3e;" +
				"-fx-border-radius: 10;"
		);

		// name and remaining amount on same line
		HBox topRow = new HBox();
		topRow.setAlignment(Pos.CENTER_LEFT);

		Label nameLbl = new Label(cat.getName());
		nameLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
		nameLbl.setTextFill(Color.web("#e8d5a3"));

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		String remainText = String.format("$%.2f left of $%.2f", cat.getRemainingBudget(), cat.getBudgetLimit());
		Label remainLbl = new Label(remainText);
		remainLbl.setFont(Font.font("Georgia", 12));
		remainLbl.setTextFill(cat.isOverBudget() ? Color.web("#e74c3c") : Color.web("#8a8fa8"));

		topRow.getChildren().addAll(nameLbl, spacer, remainLbl);

		// progress bar - color changes as it fills up
		double pct = Math.min(cat.getUsagePercent() / 100.0, 1.0);
		ProgressBar bar = new ProgressBar(pct);
		bar.setMaxWidth(Double.MAX_VALUE);
		bar.setPrefHeight(14);

		String barColor;
		if (cat.isOverBudget()) {
			barColor = "#e74c3c";
		} else if (pct > 0.75) {
			barColor = "#e67e22";
		} else {
			barColor = "#27ae60";
		}
		bar.setStyle("-fx-accent: " + barColor + ";");

		Label pctLbl = new Label(String.format("%.1f%% used — Spent: $%.2f", cat.getUsagePercent(), cat.getCurrentSpending()));
		pctLbl.setFont(Font.font("Georgia", 11));
		pctLbl.setTextFill(Color.web("#555878"));

		card.getChildren().addAll(topRow, bar, pctLbl);

		if (cat.isOverBudget()) {
			Label warn = new Label("⚠️  Over budget by $" + String.format("%.2f", -cat.getRemainingBudget()));
			warn.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
			warn.setTextFill(Color.web("#e74c3c"));
			card.getChildren().add(warn);
		}

		return card;
	}

	private HBox buildAddForm() {
		HBox form = new HBox(12);
		form.setAlignment(Pos.CENTER_LEFT);
		form.setPadding(new Insets(12, 0, 0, 0));

		TextField nameField  = tf("Category name");
		TextField limitField = tf("Budget limit ($)");

		Button addBtn = makeButton("Add Category", "#2a6496");
		addBtn.setOnAction(e -> {
			String name = nameField.getText().trim();
			String limitStr = limitField.getText().trim();

			if (name.isEmpty() || limitStr.isEmpty()) {
				showError("Please fill in both fields.");
				return;
			}

			try {
				double limit = Double.parseDouble(limitStr);
				if (limit <= 0) throw new NumberFormatException();
				manager.addCategory(new BudgetCategory(name, limit));
				nameField.clear();
				limitField.clear();
				mainWindow.refreshAll();
				showSuccess("Category '" + name + "' added!");
			} catch (NumberFormatException ex) {
				showError("Please enter a valid limit amount.");
			}
		});

		form.getChildren().addAll(fieldLabel("Name:"), nameField, fieldLabel("Limit ($):"), limitField, addBtn);
		return form;
	}

	private TextField tf(String prompt) {
		TextField tf = new TextField();
		tf.setPromptText(prompt);
		tf.setPrefWidth(180);
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

	private Label fieldLabel(String text) {
		Label l = new Label(text);
		l.setFont(Font.font("Georgia", 13));
		l.setTextFill(Color.web("#8a8fa8"));
		return l;
	}

	private Button makeButton(String text, String color) {
		Button btn = new Button(text);
		btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
		btn.setTextFill(Color.WHITE);
		btn.setPadding(new Insets(10, 20, 10, 20));
		btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8; -fx-cursor: hand;");
		btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
		btn.setOnMouseExited(e -> btn.setOpacity(1.0));
		return btn;
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