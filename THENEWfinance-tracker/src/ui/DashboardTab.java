package ui;

import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

// first tab - summary cards and recent transactions
public class DashboardTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private JPanel panel;

	public DashboardTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.panel = new JPanel();
		buildContent();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void refresh() {
		panel.removeAll();
		buildContent();
		panel.revalidate();
		panel.repaint();
	}

	private void buildContent() {
		panel.setLayout(new BorderLayout(0, 16));
		panel.setBackground(new Color(26, 29, 46));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// heading
		JLabel heading = new JLabel("Financial Overview");
		heading.setFont(new Font("Georgia", Font.BOLD, 20));
		heading.setForeground(new Color(232, 213, 163));
		heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

		// summary cards
		JPanel cards = buildSummaryCards();

		// recent transactions area
		JLabel recentLabel = new JLabel("Recent Transactions");
		recentLabel.setFont(new Font("Georgia", Font.BOLD, 15));
		recentLabel.setForeground(new Color(200, 204, 219));

		JTextArea recentArea = buildRecentList();
		JScrollPane recentScroll = new JScrollPane(recentArea);
		recentScroll.setBorder(BorderFactory.createLineBorder(new Color(42, 45, 62)));
		recentScroll.setPreferredSize(new Dimension(0, 160));

		// view all button
		JButton viewAllBtn = makeButton("View All Transactions", new Color(42, 100, 150));
		viewAllBtn.addActionListener(e -> mainWindow.goToTransactions());

		// center panel stacks everything
		JPanel center = new JPanel();
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		center.setBackground(new Color(26, 29, 46));
		center.add(recentLabel);
		center.add(Box.createVerticalStrut(8));
		center.add(recentScroll);
		center.add(Box.createVerticalStrut(12));
		center.add(viewAllBtn);

		panel.add(heading, BorderLayout.NORTH);
		panel.add(cards, BorderLayout.CENTER);
		panel.add(center, BorderLayout.SOUTH);
	}

	private JPanel buildSummaryCards() {
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

		JPanel cards = new JPanel(new GridLayout(1, 5, 12, 0));
		cards.setBackground(new Color(26, 29, 46));
		cards.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

		cards.add(makeCard("Net Balance",  String.format("$%.2f", netBalance),  netBalance >= 0 ? new Color(39, 174, 96) : new Color(231, 76, 60)));
		cards.add(makeCard("Income",       String.format("$%.2f", totalIncome),  new Color(41, 128, 185)));
		cards.add(makeCard("Expenses",     String.format("$%.2f", totalExpenses),new Color(230, 126, 34)));
		cards.add(makeCard("Invested",     String.format("$%.2f", totalInvested),new Color(142, 68, 173)));
		cards.add(makeCard("Debt",         String.format("$%.2f", totalDebt),    new Color(192, 57, 43)));

		return cards;
	}

	private JPanel makeCard(String title, String value, Color color) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(new Color(34, 37, 58));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 4, 0, 0, color),
				BorderFactory.createEmptyBorder(12, 12, 12, 12)
		));

		JLabel titleLbl = new JLabel(title);
		titleLbl.setFont(new Font("Georgia", Font.PLAIN, 11));
		titleLbl.setForeground(new Color(138, 143, 168));

		JLabel valueLbl = new JLabel(value);
		valueLbl.setFont(new Font("Georgia", Font.BOLD, 15));
		valueLbl.setForeground(color);

		card.add(titleLbl);
		card.add(Box.createVerticalStrut(4));
		card.add(valueLbl);
		return card;
	}

	private JTextArea buildRecentList() {
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
		if (sb.length() == 0) sb.append("  No transactions yet.");

		JTextArea area = new JTextArea(sb.toString());
		area.setEditable(false);
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		area.setBackground(new Color(34, 37, 58));
		area.setForeground(new Color(200, 204, 219));
		area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		return area;
	}

	private JButton makeButton(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Georgia", Font.BOLD, 13));
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		return btn;
	}
}