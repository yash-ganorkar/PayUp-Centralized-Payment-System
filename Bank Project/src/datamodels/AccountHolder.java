package datamodels;

public class AccountHolder {
	
	private static Long accountNumber;
	private static String accountHolderName;
	private static Double accountBalance;
	public static Long getAccountNumber() {
		return accountNumber;
	}
	public static String getAccountHolderName() {
		return accountHolderName;
	}
	public static Double getAccountBalance() {
		return accountBalance;
	}
	public static void setAccountNumber(Long accNumber) {
		accountNumber = accNumber;
	}
	public static void setAccountHolderName(String accHolderName) {
		accountHolderName = accHolderName;
	}
	public static void setAccountBalance(Double accBalance) {
		accountBalance = accBalance;
	}

}
