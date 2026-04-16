package ui;

import model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

// second tab - shows all transactions in a JTable with search, filter, remove, mark paid
public class TransactionsTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private JPanel panel;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField searchField;
	private JComboBox<String> typeFilter;
	private JLabel statusLabel;

	public TransactionsTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.panel = new JPanel(new BorderLayout(0, 12));
		buildContent();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void refresh() {
		applyFilter();
	}

	private void buildContent() {
		panel.setBackground(new Color(26, 29, 46));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// heading
		JLabel heading = new JLabel("All Transactions");
		heading.setFont(new Font("Georgia", Font.BOLD, 20));
		heading.setForeground(new Color(232, 213, 163));

		// filter bar
		JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		filterBar.setBackground(new Color(26, 29, 46));

		searchField = new JTextField(20);
		searchField.setFont(new Font("Georgia", Font.PLAIN, 13));
		searchField.setBackground(new Color(34, 37, 58));
		searchField.setForeground(new Color(200, 204, 219));
		searchField.setCaretColor(Color.WHITE);
		searchField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(58, 61, 94)),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
			public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
			public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
		});

		typeFilter = new JComboBox<>(new String[]{"All", "Income", "Expense", "Investment", "Debt"});
		typeFilter.setFont(new Font("Georgia", Font.PLAIN, 13));
		typeFilter.addActionListener(e -> applyFilter());

		JButton clearBtn = makeButton("Clear", new Color(68, 68, 102));
		clearBtn.addActionListener(e -> {
			searchField.setText("");
			typeFilter.setSelectedIndex(0);
		});

		JLabel filterLbl = makeLabel("Filter:");
		JLabel searchLbl = makeLabel("Search:");

		filterBar.add(searchLbl);
		filterBar.add(searchField);
		filterBar.add(filterLbl);
		filterBar.add(typeFilter);
		filterBar.add(clearBtn);

		// table
		String[] columns = {"Type", "Description", "Amount", "Category", "Date", "Details"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};
		table = new JTable(tableModel);
		table.setFont(new Font("Georgia", Font.PLAIN, 12));
		table.setRowHeight(24);
		table.setBackground(new Color(34, 37, 58));
		table.setForeground(new Color(200, 204, 219));
		table.setGridColor(new Color(42, 45, 62));
		table.setSelectionBackground(new Color(58, 61, 94));
		table.setSelectionForeground(Color.WHITE);
		table.getTableHeader().setFont(new Font("Georgia", Font.BOLD, 12));
		table.getTableHeader().setBackground(new Color(26, 29, 46));
		table.getTableHeader().setForeground(new Color(232, 213, 163));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// color code the Type column
		table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
				super.getTableCellRendererComponent(t, val, sel, foc, r, c);
				setBackground(sel ? new Color(58, 61, 94) : new Color(34, 37, 58));
				Color color = switch (val.toString()) {
					case "Income"     -> new Color(39, 174, 96);
					case "Expense"    -> new Color(230, 126, 34);
					case "Investment" -> new Color(142, 68, 173);
					case "Debt"       -> new Color(231, 76, 60);
					default           -> new Color(200, 204, 219);
				};
				setForeground(color);
				setFont(new Font("Georgia", Font.BOLD, 12));
				return this;
			}
		});

		JScrollPane tableScroll = new JScrollPane(table);
		tableScroll.getViewport().setBackground(new Color(34, 37, 58));
		tableScroll.setBorder(BorderFactory.createLineBorder(new Color(42, 45, 62)));

		// action buttons
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		actions.setBackground(new Color(26, 29, 46));

		JButton removeBtn = makeButton("🗑  Remove Selected", new Color(192, 57, 43));
		removeBtn.addActionListener(e -> removeSelected());

		JButton markPaidBtn = makeButton("✅  Mark Debt as Paid", new Color(39, 174, 96));
		markPaidBtn.addActionListener(e -> markDebtPaid());

		statusLabel = makeLabel("");

		actions.add(removeBtn);
		actions.add(markPaidBtn);
		actions.add(statusLabel);

		panel.add(heading, BorderLayout.NORTH);
		panel.add(filterBar, BorderLayout.BEFORE_FIRST_LINE);

		JPanel center = new JPanel(new BorderLayout(0, 8));
		center.setBackground(new Color(26, 29, 46));
		center.add(filterBar, BorderLayout.NORTH);
		center.add(tableScroll, BorderLayout.CENTER);
		center.add(actions, BorderLayout.SOUTH);

		panel.add(heading, BorderLayout.NORTH);
		panel.add(center, BorderLayout.CENTER);

		applyFilter();
	}

	private void applyFilter() {
		String keyword = searchField.getText().toLowerCase().trim();
		String type = (String) typeFilter.getSelectedItem();

		tableModel.setRowCount(0);
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
				tableModel.addRow(new Object[]{
					t.getType(),
					t.getDescription(),
					String.format("$%.2f", t.getAmount()),
					t.getCategory(),
					t.getDate().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")),
					detail
				});
				count++;
			}
		}
		if (statusLabel != null) {
			statusLabel.setText("Showing " + count + " of " + manager.getTransactions().size());
		}
	}

	private void removeSelected() {
		int row = table.getSelectedRow();
		if (row < 0) { statusLabel.setText("Select a transaction first."); return; }

		String desc = (String) tableModel.getValueAt(row, 1);
		int confirm = JOptionPane.showConfirmDialog(mainWindow.getFrame(),
				"Remove \"" + desc + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			// find and remove from manager
			ArrayList<Transaction> list = manager.getTransactions();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getDescription().equals(desc)) {
					list.remove(i);
					break;
				}
			}
			mainWindow.refreshAll();
			statusLabel.setText("Removed: " + desc);
		}
	}

	private void markDebtPaid() {
		int row = table.getSelectedRow();
		if (row < 0) { statusLabel.setText("Select a transaction first."); return; }

		String type = (String) tableModel.getValueAt(row, 0);
		String desc = (String) tableModel.getValueAt(row, 1);

		if (!type.equals("Debt")) { statusLabel.setText("That is not a Debt."); return; }

		for (Transaction t : manager.getTransactions()) {
			if (t instanceof Debt debt && t.getDescription().equals(desc)) {
				if (debt.isPaid()) { statusLabel.setText("Already paid."); return; }
				debt.markAsPaid();
				mainWindow.refreshAll();
				statusLabel.setText("Marked as paid: " + desc);
				return;
			}
		}
	}

	private JButton makeButton(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Georgia", Font.BOLD, 12));
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private JLabel makeLabel(String text) {
		JLabel l = new JLabel(text);
		l.setFont(new Font("Georgia", Font.PLAIN, 12));
		l.setForeground(new Color(138, 143, 168));
		return l;
	}
}