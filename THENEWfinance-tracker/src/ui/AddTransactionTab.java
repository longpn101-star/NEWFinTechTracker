package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import java.time.LocalDate;

public class AddTransactionTab {

    private FinanceManager manager;
    private MainWindow mainWindow;
    private VBox content;
    private Label feedbackLabel;

    public AddTransactionTab(FinanceManager manager, MainWindow mainWindow) {
        this.manager = manager;
        this.mainWindow = mainWindow;
        this.content = new VBox(12);
        buildContent();
    }

    public VBox getContent() {
        return content;
    }

    private void buildContent() {
        content.setStyle("-fx-background-color: #1a1d2e; -fx-padding: 20;");

        Label heading = new Label("Add New Transaction");
        heading.setStyle("-fx-font-family: Georgia; -fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #e8d5a3;");

        feedbackLabel = new Label(" ");
        feedbackLabel.setStyle("-fx-font-family: Georgia; -fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        TabPane formTabs = new TabPane();
        formTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        formTabs.setStyle("-fx-background-color: #1a1d2e;");

        formTabs.getTabs().addAll(
                new Tab("💵 Income",     buildIncomeForm()),
                new Tab("🛒 Expense",    buildExpenseForm()),
                new Tab("📈 Investment", buildInvestmentForm()),
                new Tab("⚠️ Debt",       buildDebtForm())
        );

        VBox.setVgrow(formTabs, Priority.ALWAYS);
        content.getChildren().addAll(heading, feedbackLabel, formTabs);
    }

    private GridPane buildIncomeForm() {
        TextField descField   = tf("e.g. Monthly Salary");
        TextField amountField = tf("e.g. 3200.00");
        TextField sourceField = tf("e.g. Employer");
        TextField catField    = tf("e.g. Employment");
        CheckBox  recurChk    = chk("Recurring monthly income");

        Button addBtn   = btn("Add Income", "#27ae60");
        Button clearBtn = clearBtn();

        addBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText().trim());
                if (amt <= 0) throw new NumberFormatException();
                manager.addTransaction(new Income(
                        descField.getText().trim(), amt, LocalDate.now(),
                        catField.getText().trim(), sourceField.getText().trim(), recurChk.isSelected()));
                mainWindow.refreshAll();
                showSuccess("Income added successfully!");
                clear(descField, amountField, sourceField, catField);
                recurChk.setSelected(false);
            } catch (NumberFormatException ex) { showError("Please enter a valid positive amount."); }
        });
        clearBtn.setOnAction(e -> clear(descField, amountField, sourceField, catField));

        return buildForm(
                new String[]{"Description:", "Amount ($):", "Source:", "Category:"},
                new TextField[]{descField, amountField, sourceField, catField},
                recurChk, addBtn, clearBtn);
    }

    private GridPane buildExpenseForm() {
        TextField descField   = tf("e.g. Grocery Run");
        TextField amountField = tf("e.g. 87.50");
        TextField methodField = tf("e.g. Debit Card");
        TextField catField    = tf("e.g. Food");
        CheckBox  necChk      = chk("This is a necessity");

        Button addBtn   = btn("Add Expense", "#e67e22");
        Button clearBtn = clearBtn();

        addBtn.setOnAction(e -> {
            try {
                double amt = Double.parseDouble(amountField.getText().trim());
                if (amt <= 0) throw new NumberFormatException();
                manager.addTransaction(new Expense(
                        descField.getText().trim(), amt, LocalDate.now(),
                        catField.getText().trim(), methodField.getText().trim(), necChk.isSelected()));
                mainWindow.refreshAll();
                showSuccess("Expense added successfully!");
                clear(descField, amountField, methodField, catField);
                necChk.setSelected(false);
            } catch (NumberFormatException ex) { showError("Please enter a valid positive amount."); }
        });
        clearBtn.setOnAction(e -> clear(descField, amountField, methodField, catField));

        return buildForm(
                new String[]{"Description:", "Amount ($):", "Payment Method:", "Category:"},
                new TextField[]{descField, amountField, methodField, catField},
                necChk, addBtn, clearBtn);
    }

    private GridPane buildInvestmentForm() {
        TextField descField   = tf("e.g. S&P 500 ETF");
        TextField amountField = tf("e.g. 500.00");
        TextField typeField   = tf("e.g. ETF, Stocks");
        TextField retField    = tf("e.g. 7.5");

        Button addBtn   = btn("Add Investment", "#8e44ad");
        Button clearBtn = clearBtn();

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
                clear(descField, amountField, typeField, retField);
            } catch (NumberFormatException ex) { showError("Please enter valid numbers."); }
        });
        clearBtn.setOnAction(e -> clear(descField, amountField, typeField, retField));

        return buildForm(
                new String[]{"Description:", "Amount ($):", "Investment Type:", "Expected Return (%):"},
                new TextField[]{descField, amountField, typeField, retField},
                null, addBtn, clearBtn);
    }

    private GridPane buildDebtForm() {
        TextField descField   = tf("e.g. Student Loan");
        TextField amountField = tf("e.g. 15000.00");
        TextField rateField   = tf("e.g. 4.5");
        TextField catField    = tf("e.g. Education");
        TextField monthsField = tf("e.g. 60");

        Button addBtn   = btn("Add Debt", "#c0392b");
        Button clearBtn = clearBtn();

        addBtn.setOnAction(e -> {
            try {
                double amt   = Double.parseDouble(amountField.getText().trim());
                double rate  = Double.parseDouble(rateField.getText().trim());
                int    months = Integer.parseInt(monthsField.getText().trim());
                if (amt <= 0 || months <= 0) throw new NumberFormatException();
                manager.addTransaction(new Debt(
                        descField.getText().trim(), amt, LocalDate.now(),
                        catField.getText().trim(), rate, LocalDate.now().plusMonths(months)));
                mainWindow.refreshAll();
                showSuccess("Debt added successfully!");
                clear(descField, amountField, rateField, catField, monthsField);
            } catch (NumberFormatException ex) { showError("Please enter valid numbers."); }
        });
        clearBtn.setOnAction(e -> clear(descField, amountField, rateField, catField, monthsField));

        return buildForm(
                new String[]{"Description:", "Principal ($):", "Interest Rate (%):", "Category:", "Repayment (months):"},
                new TextField[]{descField, amountField, rateField, catField, monthsField},
                null, addBtn, clearBtn);
    }

    private GridPane buildForm(String[] labels, TextField[] fields, CheckBox checkbox, Button addBtn, Button clearBtn) {
        GridPane form = new GridPane();
        form.setStyle("-fx-background-color: #1a1d2e; -fx-padding: 20;");
        form.setHgap(10);
        form.setVgap(10);

        for (int i = 0; i < labels.length; i++) {
            Label lbl = new Label(labels[i]);
            lbl.setStyle("-fx-font-family: Georgia; -fx-font-size: 13; -fx-text-fill: #8a8fa8;");
            form.add(lbl, 0, i);
            form.add(fields[i], 1, i);
            GridPane.setHgrow(fields[i], Priority.ALWAYS);
        }

        int next = labels.length;
        if (checkbox != null) { form.add(checkbox, 1, next++); }

        HBox btnRow = new HBox(8, addBtn, clearBtn);
        btnRow.setStyle("-fx-background-color: #1a1d2e;");
        form.add(btnRow, 1, next);

        return form;
    }

    private TextField tf(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #2a2d3e; -fx-text-fill: #c8ccdb; -fx-prompt-text-fill: #555878;");
        return tf;
    }

    private CheckBox chk(String text) {
        CheckBox chk = new CheckBox(text);
        chk.setStyle("-fx-text-fill: #c8ccdb; -fx-font-family: Georgia; -fx-font-size: 13;");
        return chk;
    }

    private Button btn(String text, String color) {
        Button b = new Button(text);
        b.setStyle("-fx-font-family: Georgia; -fx-font-weight: bold; -fx-background-color: " + color + "; -fx-text-fill: white; -fx-cursor: hand;");
        return b;
    }

    private Button clearBtn() {
        Button b = new Button("Clear");
        b.setStyle("-fx-font-family: Georgia; -fx-background-color: #2a2d3e; -fx-text-fill: #c8ccdb; -fx-cursor: hand;");
        return b;
    }

    private void clear(TextField... fields) {
        for (TextField f : fields) f.clear();
        feedbackLabel.setText(" ");
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