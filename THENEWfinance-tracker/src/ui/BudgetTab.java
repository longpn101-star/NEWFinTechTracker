package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

// fourth tab - budget categories with progress bars and add form
public class BudgetTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private JPanel panel;
	private JPanel categoryList;
	private JLabel feedbackLabel;

	public BudgetTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.panel = new JPanel(new BorderLayout(0, 12));
		buildContent();
	}

	public JPanel getPanel() {
		return panel;
	}

	public void refresh() {
		rebuildCategoryList();
		panel.revalidate();
		panel.repaint();
	}

	private void buildContent() {
		panel.setBackground(new Color(26, 29, 46));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel heading = new JLabel("Budget Categories");
		heading.setFont(new Font("Georgia", Font.BOLD, 20));
		heading.setForeground(new Color(232, 213, 163));

		feedbackLabel = new JLabel(" ");
		feedbackLabel.setFont(new Font("Georgia", Font.BOLD, 13));
		feedbackLabel.setForeground(new Color(39, 174, 96));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(new Color(26, 29, 46));
		topPanel.add(heading);
		topPanel.add(Box.createVerticalStrut(4));
		topPanel.add(feedbackLabel);

		// scrollable category list
		categoryList = new JPanel();
		categoryList.setLayout(new BoxLayout(categoryList, BoxLayout.Y_AXIS));
		categoryList.setBackground(new Color(26, 29, 46));
		rebuildCategoryList();

		JScrollPane scroll = new JScrollPane(categoryList);
		scroll.getViewport().setBackground(new Color(26, 29, 46));
		scroll.setBorder(BorderFactory.createEmptyBorder());

		// add category form at the bottom
		JPanel addForm = buildAddForm();

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);
		panel.add(addForm, BorderLayout.SOUTH);
	}

	private void rebuildCategoryList() {
		categoryList.removeAll();

		if (manager.getCategories().isEmpty()) {
			JLabel none = new JLabel("  No categories added yet.");
			none.setFont(new Font("Georgia", Font.PLAIN, 14));
			none.setForeground(new Color(85, 88, 120));
			categoryList.add(none);
			return;
		}

		for (BudgetCategory cat : manager.getCategories()) {
			categoryList.add(buildCategoryCard(cat));
			categoryList.add(Box.createVerticalStrut(10));
		}
	}

	private JPanel buildCategoryCard(BudgetCategory cat) {
		JPanel card = new JPanel();
		card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
		card.setBackground(new Color(34, 37, 58));
		card.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(42, 45, 62)),
				BorderFactory.createEmptyBorder(12, 14, 12, 14)
		));
		card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

		// top row: name + remaining
		JPanel topRow = new JPanel(new BorderLayout());
		topRow.setBackground(new Color(34, 37, 58));

		JLabel nameLbl = new JLabel(cat.getName());
		nameLbl.setFont(new Font("Georgia", Font.BOLD, 14));
		nameLbl.setForeground(new Color(232, 213, 163));

		String remainText = String.format("$%.2f left of $%.2f", cat.getRemainingBudget(), cat.getBudgetLimit());
		JLabel remainLbl = new JLabel(remainText);
		remainLbl.setFont(new Font("Georgia", Font.PLAIN, 12));
		remainLbl.setForeground(cat.isOverBudget() ? new Color(231, 76, 60) : new Color(138, 143, 168));

		topRow.add(nameLbl, BorderLayout.WEST);
		topRow.add(remainLbl, BorderLayout.EAST);

		// progress bar
		int pct = (int) Math.min(cat.getUsagePercent(), 100);
		JProgressBar bar = new JProgressBar(0, 100);
		bar.setValue(pct);
		bar.setStringPainted(false);
		bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));

		Color barColor = cat.isOverBudget() ? new Color(231, 76, 60)
				       : pct > 75           ? new Color(230, 126, 34)
				                            : new Color(39, 174, 96);
		bar.setForeground(barColor);
		bar.setBackground(new Color(42, 45, 62));
		bar.setBorder(BorderFactory.createEmptyBorder());

		JLabel pctLbl = new JLabel(String.format("%.1f%% used — Spent: $%.2f",
				cat.getUsagePercent(), cat.getCurrentSpending()));
		pctLbl.setFont(new Font("Georgia", Font.PLAIN, 11));
		pctLbl.setForeground(new Color(85, 88, 120));

		card.add(topRow);
		card.add(Box.createVerticalStrut(6));
		card.add(bar);
		card.add(Box.createVerticalStrut(4));
		card.add(pctLbl);

		if (cat.isOverBudget()) {
			JLabel warn = new JLabel("⚠️  Over budget by $" + String.format("%.2f", -cat.getRemainingBudget()));
			warn.setFont(new Font("Georgia", Font.BOLD, 12));
			warn.setForeground(new Color(231, 76, 60));
			card.add(Box.createVerticalStrut(4));
			card.add(warn);
		}

		return card;
	}

	private JPanel buildAddForm() {
		JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		form.setBackground(new Color(26, 29, 46));
		form.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(42, 45, 62)));

		JLabel addHeading = new JLabel("Add New Category:");
		addHeading.setFont(new Font("Georgia", Font.BOLD, 13));
		addHeading.setForeground(new Color(200, 204, 219));

		JTextField nameField  = tf("Category name");
		JTextField limitField = tf("Budget limit ($)");
		nameField.setPreferredSize(new Dimension(160, 28));
		limitField.setPreferredSize(new Dimension(130, 28));

		JButton addBtn = makeButton("Add Category", new Color(42, 100, 150));
		addBtn.addActionListener(e -> {
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
				nameField.setText("");
				limitField.setText("");
				mainWindow.refreshAll();
				showSuccess("Category '" + name + "' added!");
			} catch (NumberFormatException ex) {
				showError("Please enter a valid limit.");
			}
		});

		form.add(addHeading);
		form.add(new JLabel("Name:") {{ setForeground(new Color(138, 143, 168)); setFont(new Font("Georgia", Font.PLAIN, 13)); }});
		form.add(nameField);
		form.add(new JLabel("Limit ($):") {{ setForeground(new Color(138, 143, 168)); setFont(new Font("Georgia", Font.PLAIN, 13)); }});
		form.add(limitField);
		form.add(addBtn);

		return form;
	}

	private JTextField tf(String prompt) {
		JTextField tf = new JTextField();
		tf.setFont(new Font("Georgia", Font.PLAIN, 13));
		tf.setBackground(new Color(42, 45, 62));
		tf.setForeground(new Color(200, 204, 219));
		tf.setCaretColor(Color.WHITE);
		tf.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(58, 61, 94)),
				BorderFactory.createEmptyBorder(4, 8, 4, 8)));
		tf.setToolTipText(prompt);
		return tf;
	}

	private JButton makeButton(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Georgia", Font.BOLD, 13));
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private void showSuccess(String msg) {
		feedbackLabel.setForeground(new Color(39, 174, 96));
		feedbackLabel.setText("✅ " + msg);
	}

	private void showError(String msg) {
		feedbackLabel.setForeground(new Color(231, 76, 60));
		feedbackLabel.setText("❌ " + msg);
	}
}