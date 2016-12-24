package datamodels;

public class ForexTransaction {
	
	private String ccy1,ccy2,direction;
	private Double ccy1amt,ccy2amt,exchangerate;
	private Long accountNo;
	
	public Long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(Long accountNo) {
		this.accountNo = accountNo;
	}
	public String getCcy1() {
		return ccy1;
	}
	public String getCcy2() {
		return ccy2;
	}
	public String getDirection() {
		return direction;
	}
	public Double getCcy1amt() {
		return ccy1amt;
	}
	public Double getCcy2amt() {
		return ccy2amt;
	}
	public Double getExchangerate() {
		return exchangerate;
	}
	public void setCcy1(String ccy1) {
		this.ccy1 = ccy1;
	}
	public void setCcy2(String ccy2) {
		this.ccy2 = ccy2;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public void setCcy1amt(Double ccy1amt) {
		this.ccy1amt = ccy1amt;
	}
	public void setCcy2amt(Double ccy2amt) {
		this.ccy2amt = ccy2amt;
	}
	public void setExchangerate(Double exchangerate) {
		this.exchangerate = exchangerate;
	}

}
