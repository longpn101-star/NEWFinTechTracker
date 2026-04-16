package ui;

import model.FinanceManager;
import javax.swing.*;
import java.awt.*;

// main window of the GUI - uses a JTabbedPane to hold all four tabs
public class MainWindow {

	private FinanceManager manager;
	private JFrame frame;
	private JTabbedPane tabs;

	// keep references so we can refresh them
	private DashboardTab dashboardTab;
	private TransactionsTab transactionsTab;
	private AddTransactionTab addTransactionTab;
	private BudgetTab budgetTab;

	public MainWindow(FinanceManager manager) {
		this.manager = manager;
	}

	public void show() {
		frame = new JFrame("Personal Finance Tracker — " + manager.getOwnerName());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 650);
		frame.setLocationRelativeTo(null); // center on screen
		frame.setLayout(new BorderLayout());

		// header panel at the top
		frame.add(buildHeader(), BorderLayout.NORTH);

		// create the four tabs
		tabs = new JTabbedPane();
		tabs.setFont(new Font("Georgia", Font.PLAIN, 13));

		dashboardTab      = new DashboardTab(manager, this);
		transactionsTab   = new TransactionsTab(manager, this);
		addTransactionTab = new AddTransactionTab(manager, this);
		budgetTab         = new BudgetTab(manager, this);

		tabs.addTab("📊 Dashboard",        dashboardTab.getPanel());
		tabs.addTab("📋 Transactions",     transactionsTab.getPanel());
		tabs.addTab("➕ Add Transaction",  addTransactionTab.getPanel());
		tabs.addTab("📂 Budget",           budgetTab.getPanel());

		frame.add(tabs, BorderLayout.CENTER);
		frame.setVisible(true);
	}

	private JPanel buildHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(new Color(18, 20, 31));
		header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

		JLabel title = new JLabel("💰 Personal Finance Tracker");
		title.setFont(new Font("Georgia", Font.BOLD, 18));
		title.setForeground(new Color(232, 213, 163));

		JLabel owner = new JLabel("👤 " + manager.getOwnerName());
		owner.setFont(new Font("Georgia", Font.PLAIN, 13));
		owner.setForeground(new Color(138, 143, 168));

		header.add(title, BorderLayout.WEST);
		header.add(owner, BorderLayout.EAST);
		return header;
	}

	// called after data changes so all tabs update
	public void refreshAll() {
		dashboardTab.refresh();
		transactionsTab.refresh();
		budgetTab.refresh();
	}

	// switches to the transactions tab
	public void goToTransactions() {
		tabs.setSelectedIndex(1);
	}

	public JFrame getFrame() {
		return frame;
	}
}