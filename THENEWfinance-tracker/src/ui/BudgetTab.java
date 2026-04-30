package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

public class BudgetTab {

    private FinanceManager manager;
    private MainWindow mainWindow;
    private VBox content;
    private VBox categoryList;
    private Label feedbackLabel;

    public BudgetTab(FinanceManager manager, MainWindow mainWindow) {
        this.manager = manager;
        this.mainWindow = mainWindow;
        this.content = new VBox(12);
        buildContent();
    }

    public VBox getContent() {
        return content;
    }

    public void refresh() {
        rebuildCategoryList();
    }

    private void buildContent() {
        content.setStyle("-fx-background-color: #1a1d2e; -fx-padding: 20;");

        Label heading = new Label("Budget Categories");
        heading.setStyle("-fx-font-family: Georgia; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #e8d5a3;");

        feedbackLabel = new Label(" ");
        feedbackLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        categoryList = new VBox(10);
        categoryList.setStyle("-fx-background-color: #1a1d2e;");
        rebuildCategoryList();

        ScrollPane scroll = new ScrollPane(categoryList);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #1a1d2e; -fx-border-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        HBox addForm = buildAddForm();

        content.getChildren().addAll(heading, feedbackLabel, scroll, addForm);
    }

    private void rebuildCategoryList() {
        categoryList.getChildren().clear();

        if (manager.getCategories().isEmpty()) {
            Label none = new Label("  No categories added yet.");
            none.setStyle("-fx-font-family: Georgia; -fx-font-size: 14; -fx-text-fill: #555878;");
            categoryList.getChildren().add(none);
            return;
        }

        for (BudgetCategory cat : manager.getCategories()) {
            categoryList.getChildren().add(buildCategoryCard(cat));
        }
    }

    private VBox buildCategoryCard(BudgetCategory cat) {
        VBox card = new VBox(6);
        card.setStyle("-fx-background-color: #22253a; -fx-padding: 12 14 12 14; -fx-border-color: #2a2d3e; -fx-border-width: 1;");

        // top row
        BorderPane topRow = new BorderPane();
        topRow.setStyle("-fx-background-color: #22253a;");

        Label nameLbl = new Label(cat.getName());
        nameLbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #e8d5a3;");

        String remainText = String.format("$%.2f left of $%.2f", cat.getRemainingBudget(), cat.getBudgetLimit());
        Label remainLbl = new Label(remainText);
        remainLbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 12; -fx-text-fill: " +
                (cat.isOverBudget() ? "#e74c3c" : "#8a8fa8") + ";");

        topRow.setLeft(nameLbl);
        topRow.setRight(remainLbl);

        // progress bar
        double pct = Math.min(cat.getUsagePercent() / 100.0, 1.0);
        ProgressBar bar = new ProgressBar(pct);
        bar.setMaxWidth(Double.MAX_VALUE);
        String barColor = cat.isOverBudget() ? "#e74c3c" : pct > 0.75 ? "#e67e22" : "#27ae60";
        bar.setStyle("-fx-accent: " + barColor + ";");

        Label pctLbl = new Label(String.format("%.1f%% used — Spent: $%.2f",
                cat.getUsagePercent(), cat.getCurrentSpending()));
        pctLbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 11; -fx-text-fill: #555878;");

        card.getChildren().addAll(topRow, bar, pctLbl);

        if (cat.isOverBudget()) {
            Label warn = new Label("⚠️  Over budget by $" + String.format("%.2f", -cat.getRemainingBudget()));
            warn.setStyle("-fx-font-family: Georgia; -fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
            card.getChildren().add(warn);
        }

        return card;
    }

    private HBox buildAddForm() {
        HBox form = new HBox(10);
        form.setStyle("-fx-background-color: #1a1d2e; -fx-padding: 10 0 0 0; -fx-border-color: #2a2d3e; -fx-border-width: 1 0 0 0;");

        Label addHeading = new Label("Add New Category:");
        addHeading.setStyle("-fx-font-family: Georgia; -fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: #c8ccdb;");

        TextField nameField  = tf("Category name");
        TextField limitField = tf("Budget limit ($)");
        nameField.setPrefWidth(160);
        limitField.setPrefWidth(130);

        Button addBtn = new Button("Add Category");
        addBtn.setStyle("-fx-font-family: Georgia; -fx-font-weight: bold; -fx-background-color: #2a6496; -fx-text-fill: white; -fx-cursor: hand;");
        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String limitStr = limitField.getText().trim();
            if (name.isEmpty() || limitStr.isEmpty()) { showError("Please fill in both fields."); return; }
            try {
                double limit = Double.parseDouble(limitStr);
                if (limit <= 0) throw new NumberFormatException();
                manager.addCategory(new BudgetCategory(name, limit));
                nameField.clear();
                limitField.clear();
                mainWindow.refreshAll();
                showSuccess("Category '" + name + "' added!");
            } catch (NumberFormatException ex) { showError("Please enter a valid limit."); }
        });

        form.getChildren().addAll(addHeading,
                lbl("Name:"), nameField, lbl("Limit ($):"), limitField, addBtn);
        return form;
    }

    private TextField tf(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #2a2d3e; -fx-text-fill: #c8ccdb; -fx-prompt-text-fill: #555878;");
        return tf;
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #8a8fa8; -fx-font-family: Georgia; -fx-font-size: 13;");
        return l;
    }

    private void showSuccess(String msg) {
        feedbackLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #27ae60;");
        feedbackLabel.setText("✅ " + msg);
    }

    private void showError(String msg) {
        feedbackLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
        feedbackLabel.setText("❌ " + msg);
    }
}