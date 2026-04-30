package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import java.util.ArrayList;

public class DashboardTab {

    private FinanceManager manager;
    private MainWindow mainWindow;
    private VBox content;

    public DashboardTab(FinanceManager manager, MainWindow mainWindow) {
        this.manager = manager;
        this.mainWindow = mainWindow;
        this.content = new VBox(16);
        buildContent();
    }

    public VBox getContent() {
        return content;
    }

    public void refresh() {
        content.getChildren().clear();
        buildContent();
    }

    private void buildContent() {
        content.setStyle("-fx-background-color: #1a1d2e; -fx-padding: 20;");

        Label heading = new Label("Financial Overview");
        heading.setStyle("-fx-font-family: Georgia; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #e8d5a3;");

        HBox cards = buildSummaryCards();

        Label recentLabel = new Label("Recent Transactions");
        recentLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #c8ccdb;");

        TextArea recentArea = buildRecentList();
        ScrollPane recentScroll = new ScrollPane(recentArea);
        recentScroll.setFitToWidth(true);
        recentScroll.setPrefHeight(160);
        recentScroll.setStyle("-fx-background-color: #22253a; -fx-border-color: #2a2d3e;");

        Button viewAllBtn = new Button("View All Transactions");
        viewAllBtn.setStyle("-fx-font-family: Georgia; -fx-font-weight: bold; -fx-background-color: #2a6496; -fx-text-fill: white; -fx-cursor: hand;");
        viewAllBtn.setOnAction(e -> mainWindow.goToTransactions());

        VBox bottom = new VBox(8, recentLabel, recentScroll, viewAllBtn);
        bottom.setStyle("-fx-background-color: #1a1d2e;");

        content.getChildren().addAll(heading, cards, bottom);
    }

    private HBox buildSummaryCards() {
        double totalIncome = 0, totalExpenses = 0, totalInvested = 0, totalDebt = 0;
        for (Transaction t : manager.getTransactions()) {
            switch (t.getType()) {
                case "Income"     -> totalIncome   += t.getAmount();
                case "Expense"    -> totalExpenses += t.getAmount();
                case "Investment" -> totalInvested += t.getAmount();
                case "Debt"       -> totalDebt     += t.getAmount();
            }
        }
        double netBalance = manager.getStartingBalance() + totalIncome - totalExpenses - totalInvested;

        HBox cards = new HBox(12);
        cards.setStyle("-fx-background-color: #1a1d2e;");

        VBox c1 = makeCard("Net Balance", String.format("$%.2f", netBalance), netBalance >= 0 ? "#27ae60" : "#e74c3c");
        VBox c2 = makeCard("Income",      String.format("$%.2f", totalIncome),   "#2980b9");
        VBox c3 = makeCard("Expenses",    String.format("$%.2f", totalExpenses),  "#e67e22");
        VBox c4 = makeCard("Invested",    String.format("$%.2f", totalInvested),  "#8e44ad");
        VBox c5 = makeCard("Debt",        String.format("$%.2f", totalDebt),      "#c0392b");

        for (VBox card : new VBox[]{c1, c2, c3, c4, c5}) {
            HBox.setHgrow(card, Priority.ALWAYS);
        }
        cards.getChildren().addAll(c1, c2, c3, c4, c5);
        return cards;
    }

    private VBox makeCard(String title, String value, String color) {
        VBox card = new VBox(4);
        card.setStyle("-fx-background-color: #22253a; -fx-padding: 12; " +
                "-fx-border-color: " + color + "; -fx-border-width: 0 0 0 4;");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 11; -fx-text-fill: #8a8fa8;");

        Label valueLbl = new Label(value);
        valueLbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        card.getChildren().addAll(titleLbl, valueLbl);
        return card;
    }

    private TextArea buildRecentList() {
        ArrayList<Transaction> all = manager.getTransactions();
        StringBuilder sb = new StringBuilder();

        int start = Math.max(0, all.size() - 5);
        for (int i = all.size() - 1; i >= start; i--) {
            Transaction t = all.get(i);
            sb.append(String.format("  %-12s %-28s  $%10.2f   %s%n",
                    t.getType(), t.getDescription(), t.getAmount(),
                    t.getDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))));
        }
        if (sb.length() == 0) sb.append("  No transactions yet.");

        TextArea area = new TextArea(sb.toString());
        area.setEditable(false);
        area.setStyle("-fx-font-family: Monospaced; -fx-font-size: 12; " +
                "-fx-control-inner-background: #22253a; -fx-text-fill: #c8ccdb;");
        return area;
    }
}