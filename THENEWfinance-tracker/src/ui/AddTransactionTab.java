package ui;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

// third tab - forms to add new transactions
// uses a JTabbedPane inside the tab for each transaction type
public class AddTransactionTab {

	private FinanceManager manager;
	private MainWindow mainWindow;
	private JPanel panel;
	private JLabel feedbackLabel;

	public AddTransactionTab(FinanceManager manager, MainWindow mainWindow) {
		this.manager = manager;
		this.mainWindow = mainWindow;
		this.panel = new JPanel(new BorderLayout(0, 12));
		buildContent();
	}

	public JPanel getPanel() {
		return panel;
	}

	private void buildContent() {
		panel.setBackground(new Color(26, 29, 46));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel heading = new JLabel("Add New Transaction");
		heading.setFont(new Font("Georgia", Font.BOLD, 20));
		heading.setForeground(new Color(232, 213, 163));

		feedbackLabel = new JLabel(" ");
		feedbackLabel.setFont(new Font("Georgia", Font.BOLD, 13));
		feedbackLabel.setForeground(new Color(39, 174, 96));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(new Color(26, 29, 46));
		topPanel.add(heading);
		topPanel.add(Box.createVerticalStrut(6));
		topPanel.add(feedbackLabel);

		// sub-tabs for each transaction type
		JTabbedPane formTabs = new JTabbedPane();
		formTabs.setFont(new Font("Georgia", Font.PLAIN, 13));
		formTabs.setBackground(new Color(26, 29, 46));

		formTabs.addTab("💵 Income",      buildIncomeForm());
		formTabs.addTab("🛒 Expense",     buildExpenseForm());
		formTabs.addTab("📈 Investment",  buildInvestmentForm());
		formTabs.addTab("⚠️ Debt",        buildDebtForm());

		panel.add(topPanel, BorderLayout.NORTH);
		panel.add(formTabs, BorderLayout.CENTER);
	}

	private JPanel buildIncomeForm() {
		JTextField descField   = tf("e.g. Monthly Salary");
		JTextField amountField = tf("e.g. 3200.00");
		JTextField sourceField = tf("e.g. Employer, Freelance");
		JTextField catField    = tf("e.g. Employment");
		JCheckBox  recurChk    = new JCheckBox("Recurring monthly income");
		recurChk.setBackground(new Color(26, 29, 46));
		recurChk.setForeground(new Color(200, 204, 219));
		recurChk.setFont(new Font("Georgia", Font.PLAIN, 13));

		JButton addBtn   = makeButton("Add Income", new Color(39, 174, 96));
		JButton clearBtn = makeClearButton();

		addBtn.addActionListener(e -> {
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
		clearBtn.addActionListener(e -> clearFields(descField, amountField, sourceField, catField));

		return buildForm(
			new String[]{"Description:", "Amount ($):", "Source:", "Category:"},
			new JTextField[]{descField, amountField, sourceField, catField},
			recurChk, addBtn, clearBtn
		);
	}

	private JPanel buildExpenseForm() {
		JTextField descField   = tf("e.g. Grocery Run");
		JTextField amountField = tf("e.g. 87.50");
		JTextField methodField = tf("e.g. Debit Card, Cash");
		JTextField catField    = tf("e.g. Food");
		JCheckBox  necChk      = new JCheckBox("This is a necessity");
		necChk.setBackground(new Color(26, 29, 46));
		necChk.setForeground(new Color(200, 204, 219));
		necChk.setFont(new Font("Georgia", Font.PLAIN, 13));

		JButton addBtn   = makeButton("Add Expense", new Color(230, 126, 34));
		JButton clearBtn = makeClearButton();

		addBtn.addActionListener(e -> {
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
		clearBtn.addActionListener(e -> clearFields(descField, amountField, methodField, catField));

		return buildForm(
			new String[]{"Description:", "Amount ($):", "Payment Method:", "Category:"},
			new JTextField[]{descField, amountField, methodField, catField},
			necChk, addBtn, clearBtn
		);
	}

	private JPanel buildInvestmentForm() {
		JTextField descField   = tf("e.g. S&P 500 ETF");
		JTextField amountField = tf("e.g. 500.00");
		JTextField typeField   = tf("e.g. ETF, Stocks, Crypto");
		JTextField retField    = tf("e.g. 7.5");

		JButton addBtn   = makeButton("Add Investment", new Color(142, 68, 173));
		JButton clearBtn = makeClearButton();

		addBtn.addActionListener(e -> {
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
		clearBtn.addActionListener(e -> clearFields(descField, amountField, typeField, retField));

		return buildForm(
			new String[]{"Description:", "Amount ($):", "Investment Type:", "Expected Return (%):"},
			new JTextField[]{descField, amountField, typeField, retField},
			null, addBtn, clearBtn
		);
	}

	private JPanel buildDebtForm() {
		JTextField descField   = tf("e.g. Student Loan");
		JTextField amountField = tf("e.g. 15000.00");
		JTextField rateField   = tf("e.g. 4.5");
		JTextField catField    = tf("e.g. Education");
		JTextField monthsField = tf("e.g. 60");

		JButton addBtn   = makeButton("Add Debt", new Color(192, 57, 43));
		JButton clearBtn = makeClearButton();

		addBtn.addActionListener(e -> {
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
		clearBtn.addActionListener(e -> clearFields(descField, amountField, rateField, catField, monthsField));

		return buildForm(
			new String[]{"Description:", "Principal ($):", "Interest Rate (%):", "Category:", "Repayment (months):"},
			new JTextField[]{descField, amountField, rateField, catField, monthsField},
			null, addBtn, clearBtn
		);
	}

	// builds a standard form panel from labels, fields, optional checkbox, and buttons
	private JPanel buildForm(String[] labels, JTextField[] fields, JCheckBox checkbox, JButton addBtn, JButton clearBtn) {
		JPanel form = new JPanel(new GridBagLayout());
		form.setBackground(new Color(26, 29, 46));
		form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(6, 6, 6, 6);
		gc.fill = GridBagConstraints.HORIZONTAL;

		for (int i = 0; i < labels.length; i++) {
			gc.gridx = 0; gc.gridy = i; gc.weightx = 0;
			JLabel lbl = new JLabel(labels[i]);
			lbl.setFont(new Font("Georgia", Font.PLAIN, 13));
			lbl.setForeground(new Color(138, 143, 168));
			form.add(lbl, gc);

			gc.gridx = 1; gc.weightx = 1;
			form.add(fields[i], gc);
		}

		if (checkbox != null) {
			gc.gridx = 1; gc.gridy = labels.length; gc.weightx = 1;
			form.add(checkbox, gc);
		}

		JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		btnRow.setBackground(new Color(26, 29, 46));
		btnRow.add(addBtn);
		btnRow.add(clearBtn);

		gc.gridx = 1; gc.gridy = labels.length + (checkbox != null ? 1 : 0); gc.weightx = 1;
		form.add(btnRow, gc);

		// filler to push everything up
		gc.gridx = 0; gc.gridy = labels.length + 2; gc.weighty = 1; gc.gridwidth = 2;
		form.add(Box.createVerticalGlue(), gc);

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

	private JButton makeClearButton() {
		JButton btn = new JButton("Clear");
		btn.setFont(new Font("Georgia", Font.PLAIN, 13));
		btn.setBackground(new Color(42, 45, 62));
		btn.setForeground(new Color(200, 204, 219));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return btn;
	}

	private void clearFields(JTextField... fields) {
		for (JTextField f : fields) f.setText("");
		feedbackLabel.setText(" ");
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