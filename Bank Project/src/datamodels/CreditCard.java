package datamodels;

public class CreditCard {
	private Long creditcardnumber;
	private String cvvnumber;
	private Long accountnumber;
	
	public Long getCreditcardnumber() {
		return creditcardnumber;
	}
	public String getCvvnumber() {
		return cvvnumber;
	}
	public Long getAccountnumber() {
		return accountnumber;
	}
	public void setCreditcardnumber(Long creditcardnumber) {
		this.creditcardnumber = creditcardnumber;
	}
	public void setCvvnumber(String cvvnumber) {
		this.cvvnumber = cvvnumber;
	}
	public void setAccountnumber(Long accountnumber) {
		this.accountnumber = accountnumber;
	}
}
