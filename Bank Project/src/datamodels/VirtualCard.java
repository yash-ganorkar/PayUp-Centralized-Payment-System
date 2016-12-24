package datamodels;

public class VirtualCard {

	private Long number1;
	private Long number2;
	private Long number3;
	private Long number4;
	private String month;
	private String year;
	private String firstName;
	private Long cvv;
	private Long accountNumber;
	
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Long getNumber1() {
		return number1;
	}
	public Long getNumber2() {
		return number2;
	}
	public Long getNumber3() {
		return number3;
	}
	public Long getNumber4() {
		return number4;
	}
	public String getMonth() {
		return month;
	}
	public String getYear() {
		return year;
	}
	public String getFirstName() {
		return firstName;
	}
	public Long getCvv() {
		return cvv;
	}
	public void setNumber1(Long number1) {
		this.number1 = number1;
	}
	public void setNumber2(Long number2) {
		this.number2 = number2;
	}
	public void setNumber3(Long number3) {
		this.number3 = number3;
	}
	public void setNumber4(Long number4) {
		this.number4 = number4;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setCvv(Long cvv) {
		this.cvv = cvv;
	}
}