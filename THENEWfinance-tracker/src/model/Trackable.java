package model;

// interface that makes sure all transaction types can be tracked
// implemented by Income, Expense, Investment, Debt
public interface Trackable {

	// returns a summary string for tracking purposes
	String track();

	// checks if the amount is over a certain threshold
	// I made this a default method so not every class has to override it
	default boolean isFlagged(double threshold) {
		return false;
	}
}