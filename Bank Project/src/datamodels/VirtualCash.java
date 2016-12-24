package datamodels;

public class VirtualCash {
	private Long vCardNumber1;
	private Long vCardNumber2;
	private Long vCardNumber3;
	private Long vCardNumber4;
	private Integer CVV;
	private Double rechargeAmount;
	public Long getvCardNumber1() {
		return vCardNumber1;
	}
	public Long getvCardNumber2() {
		return vCardNumber2;
	}
	public Long getvCardNumber3() {
		return vCardNumber3;
	}
	public Long getvCardNumber4() {
		return vCardNumber4;
	}
	public Integer getCVV() {
		return CVV;
	}
	public Double getRechargeAmount() {
		return rechargeAmount;
	}
	public void setvCardNumber1(Long vCardNumber1) {
		this.vCardNumber1 = vCardNumber1;
	}
	public void setvCardNumber2(Long vCardNumber2) {
		this.vCardNumber2 = vCardNumber2;
	}
	public void setvCardNumber3(Long vCardNumber3) {
		this.vCardNumber3 = vCardNumber3;
	}
	public void setvCardNumber4(Long vCardNumber4) {
		this.vCardNumber4 = vCardNumber4;
	}
	public void setCVV(int cVV) {
		CVV = cVV;
	}
	public void setRechargeAmount(Double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}
}
