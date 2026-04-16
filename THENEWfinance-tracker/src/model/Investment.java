package model;

import java.time.LocalDate;

// represents money put into stocks, ETFs, crypto, etc.
public class Investment extends Transaction implements Trackable {

	private String investmentType;
	private double expectedReturn; // annual % return

	public Investment(String description, double amount, LocalDate date, String category, String investmentType, double expectedReturn) {
		super(description, amount, date, category);
		this.investmentType = investmentType;
		this.expectedReturn = expectedReturn;
	}

	@Override
	public String getType() {
		return "Investment";
	}

	@Override
	public void displaySummary() {
		// calculate what this would be worth after a year
		double projectedValue = amount * (1 + expectedReturn / 100);

		System.out.println("  [~] INVESTMENT: " + description);
		System.out.println("      Amount          : $" + String.format("%.2f", amount));
		System.out.println("      Type            : " + investmentType);
		System.out.println("      Expected Return : " + expectedReturn + "%");
		System.out.println("      Projected (1yr) : $" + String.format("%.2f", projectedValue));
		System.out.println("      Date            : " + date.format(DATE_FORMAT));
		System.out.println("      Category        : " + category);
	}

	@Override
	public String track() {
		double gain = amount * (expectedReturn / 100);
		return "Investment '" + description + "' (" + investmentType + "): $"
				+ String.format("%.2f", amount) + " | projected gain: $" + String.format("%.2f", gain) + "/yr";
	}

	// calculates value after n years using compound interest formula
	public double getProjectedValue(int years) {
		return amount * Math.pow(1 + expectedReturn / 100, years);
	}

	public String getInvestmentType() {
		return investmentType;
	}

	public void setInvestmentType(String investmentType) {
		this.investmentType = investmentType;
	}

	public double getExpectedReturn() {
		return expectedReturn;
	}

	public void setExpectedReturn(double expectedReturn) {
		this.expectedReturn = expectedReturn;
	}
}