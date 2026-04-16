# Personal Finance Tracker — Design Document

**Course:** Intro to Programming II  
**Project:** Final Project — Object-Oriented System with GUI  
**Team:** Max & Long

---

## System Overview

Our project is a Personal Finance Tracker that lets a user add and manage different types of financial transactions — income, expenses, investments, and debts. The system also tracks budget categories and shows a financial summary. It was built first as a console application (Phase 1 & 2) and then extended with a JavaFX GUI (Phase 3).

---

## Design Decisions

We chose a personal finance tracker because it naturally fits multiple distinct types that share common data (description, amount, date, category), which made it a good fit for an abstract class hierarchy. The four transaction types — Income, Expense, Investment, and Debt — each have their own specific fields and behaviors, which we handle through inheritance and polymorphism.

We kept the model and UI completely separate by putting all data logic in the `model/` package and all display logic in the `ui/` package. This way, the console UI and the JavaFX GUI both work with the same manager class and model objects without any changes to the model.

---

## Use of Inheritance

`Transaction` is the abstract superclass. It holds the four fields that all transaction types share: `description`, `amount`, `date`, and `category`. It also defines two abstract methods: `getType()` and `displaySummary()`. Every subclass must implement both.

The four concrete subclasses extend `Transaction`:

- **Income** adds `source` and `isRecurring`
- **Expense** adds `paymentMethod` and `isNecessity`
- **Investment** adds `investmentType` and `expectedReturn`, plus a `getProjectedValue()` method
- **Debt** adds `interestRate`, `dueDate`, and `isPaid`, plus `markAsPaid()` and `calculateTotalCost()`

`FinanceManager` stores all transactions as `ArrayList<Transaction>`, which means it can hold any subtype. This is what allows us to call `displaySummary()` on each transaction in a loop without knowing the specific type — each one prints its own format.

---

## Use of Interface

We defined a `Trackable` interface with one abstract method (`track()`) and one default method (`isFlagged(double threshold)`). All four transaction subclasses implement `Trackable`.

- `track()` returns a one-line summary string used in the tracking report feature
- `isFlagged()` returns true when a transaction should be flagged — each class overrides it with its own logic. For example, `Expense` flags large non-necessary spending, and `Debt` flags high-interest unpaid debts. The default in the interface just returns false, so any class that doesn't need custom flagging doesn't have to override it.

`FinanceManager` uses the interface directly in `displayTrackingReport()` and `flagLargeTransactions()` — it casts each transaction to `Trackable` and calls `track()` or `isFlagged()` without caring which specific class it is.

---

## How Polymorphism Is Used

The main place polymorphism shows up is in `FinanceManager.displayAllTransactions()`:

```java
for (Transaction t : transactions) {
    t.displaySummary();
}
```

Each call to `displaySummary()` prints completely different output depending on whether `t` is an Income, Expense, Investment, or Debt — even though the code calling it only knows it's a `Transaction`. The same loop also works for `toShortString()` and `toString()`.

We also use `instanceof` in a few specific places where we need to access subclass-specific behavior, like calling `markAsPaid()` on a `Debt` object or checking `isPaid()` in the summary calculation. We kept these cases minimal so that most of the code works through the abstract interface.

---

## GUI Summary (Phase 3)

The JavaFX application uses a `TabPane` with four tabs:

1. **Dashboard** — summary cards (net balance, income, expenses, investments, debt) and a recent transactions list
2. **Transactions** — a `TableView` with search, type filter, remove, and mark-debt-paid buttons
3. **Add Transaction** — a sub-tabbed form for each transaction type with input validation and feedback messages
4. **Budget** — category cards with `ProgressBar` indicators and a form to add new categories

All four tabs hold a reference to `FinanceManager` and call `mainWindow.refreshAll()` after any data change so every tab stays synchronized.

---

*Word count: ~500 words*