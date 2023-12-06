package Application;

import java.io.Serializable;

//Finance class represents the orders that are taken on finance
public class Finance implements Serializable{
	private int FinanceID;
	private int FinanceCustomerID;
	private int FinancePurchaseID;
	private long FinanceAmount;
	private int FinanceDuration;
	
	//Constructor
	public Finance() {
		
	}
	
	//Parametrised constructor
	public Finance(int financeID, int financeCustomerID, int financePurchaseID, long financeAmount, int financeDuration) {
        this.setFinanceID(financeID);
        this.setFinanceCustomerID(financeCustomerID);
        this.setFinancePurchaseID(financePurchaseID);
        this.setFinanceAmount(financeAmount);
        this.setFinanceDuration(financeDuration);
    }

	//Getters and setters
	public int getFinanceID() {
		return FinanceID;
	}

	public void setFinanceID(int financeID) {
		FinanceID = financeID;
	}

	public int getFinanceCustomerID() {
		return FinanceCustomerID;
	}

	public void setFinanceCustomerID(int financeCustomerID) {
		FinanceCustomerID = financeCustomerID;
	}

	 public int getFinancePurchaseID() {
        return FinancePurchaseID;
    }

    public void setFinancePurchaseID(int financePurchaseID) {
    	FinancePurchaseID = financePurchaseID;
    }

	public long getFinanceAmount() {
		return FinanceAmount;
	}

	public void setFinanceAmount(long financeAmount) {
		FinanceAmount = financeAmount;
	}

	public int getFinanceDuration() {
		return FinanceDuration;
	}

	public void setFinanceDuration(int financeDuration) {
		FinanceDuration = financeDuration;
	}
	
}
