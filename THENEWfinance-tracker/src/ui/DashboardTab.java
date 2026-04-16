package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import model.*;
import java.util.ArrayList;

// first tab - shows a summary of the finances and recent transactions
public class DashboardTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private Tab tab;

	public DashboardTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.tab = new Tab("📊  Dashboard");
		buildContent();
	}

	public Tab getTab() {
		return tab;
	}

	// called when data changes so the dashboard updates
	public void refresh() {
		buildContent();
	}

	private void buildContent() {
		VBox root = new VBox(20);
		root.setPadding(new Insets(24));
		root.setStyle("-fx-background-color: #1a1d2e;");

		Label heading = new Label("Financial Overview");
		heading.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
		heading.setTextFill(Color.web("#e8d5a3"));

		HBox cards = buildSummaryCards();

		Label recentLabel = new Label("Recent Transactions");
		recentLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
		recentLabel.setTextFill(Color.web("#c8ccdb"));

		TextArea recentArea = buildRecentList();

		Button viewAllBtn = makeButton("View All Transactions", "#2a6496");
		viewAllBtn.setOnAction(e -> mainWindow.goToTransactions());

		root.getChildren().addAll(heading, cards, recentLabel, recentArea, viewAllBtn);

		ScrollPane scroll = new ScrollPane(root);
		scroll.setFitToWidth(true);
		scroll.setStyle("-fx-background-color: #1a1d2e; -fx-background: #1a1d2e;");
		tab.setContent(scroll);
	}

	private HBox buildSummaryCards() {
		// calculate totals from all transactions
		double totalIncome = 0, totalExpenses = 0, totalInvested = 0, totalDebt = 0;
		for (Transaction t : manager.getTransactions()) {
			switch (t.getType()) {
				case "Income"     -> totalIncome    += t.getAmount();
				case "Expense"    -> totalExpenses  += t.getAmount();
				case "Investment" -> totalInvested  += t.getAmount();
				case "Debt"       -> totalDebt      += t.getAmount();
			}
		}
		double netBalance = manager.getStartingBalance() + totalIncome - totalExpenses - totalInvested;

		HBox cards = new HBox(16);
		cards.setAlignment(Pos.CENTER_LEFT);
		cards.getChildren().addAll(
				makeCard("💵 Net Balance",  String.format("$%.2f", netBalance),  netBalance >= 0 ? "#27ae60" : "#e74c3c"),
				makeCard("📈 Income",       String.format("$%.2f", totalIncome),  "#2980b9"),
				makeCard("📉 Expenses",     String.format("$%.2f", totalExpenses),"#e67e22"),
				makeCard("🏦 Invested",     String.format("$%.2f", totalInvested),"#8e44ad"),
				makeCard("⚠️ Debt",         String.format("$%.2f", totalDebt),    "#c0392b")
		);
		return cards;
	}

	private VBox makeCard(String title, String value, String color) {
		VBox card = new VBox(6);
		card.setPadding(new Insets(16, 20, 16, 20));
		card.setAlignment(Pos.CENTER_LEFT);
		card.setPrefWidth(155);
		card.setStyle(
				"-fx-background-color: #22253a;" +
				"-fx-background-radius: 10;" +
				"-fx-border-color: " + color + ";" +
				"-fx-border-width: 0 0 0 4;" +
				"-fx-border-radius: 10;"
		);

		Label titleLbl = new Label(title);
		titleLbl.setFont(Font.font("Georgia", 11));
		titleLbl.setTextFill(Color.web("#8a8fa8"));

		Label valueLbl = new Label(value);
		valueLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
		valueLbl.setTextFill(Color.web(color));

		card.getChildren().addAll(titleLbl, valueLbl);
		return card;
	}

	private TextArea buildRecentList() {
		ArrayList<Transaction> all = manager.getTransactions();
		StringBuilder sb = new StringBuilder();

		// show last 5 transactions
		int start = Math.max(0, all.size() - 5);
		for (int i = all.size() - 1; i >= start; i--) {
			Transaction t = all.get(i);
			sb.append(String.format("  %-12s %-28s  $%10.2f   %s%n",
					t.getType(), t.getDescription(), t.getAmount(),
					t.getDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"))));
		}
		if (sb.length() == 0) {
			sb.append("  No transactions yet.");
		}

		TextArea area = new TextArea(sb.toString());
		area.setEditable(false);
		area.setPrefHeight(150);
		area.setFont(Font.font("Monospaced", 13));
		area.setStyle(
				"-fx-control-inner-background: #22253a;" +
				"-fx-text-fill: #c8ccdb;" +
				"-fx-background-color: #22253a;" +
				"-fx-border-color: #2a2d3e;" +
				"-fx-border-radius: 6;" +
				"-fx-background-radius: 6;"
		);
		return area;
	}

	private Button makeButton(String text, String color) {
		Button btn = new Button(text);
		btn.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
		btn.setTextFill(Color.WHITE);
		btn.setPadding(new Insets(10, 22, 10, 22));
		btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 8; -fx-cursor: hand;");
		btn.setOnMouseEntered(e -> btn.setOpacity(0.85));
		btn.setOnMouseExited(e -> btn.setOpacity(1.0));
		return btn;
	}
}