package datamodels;

import javafx.beans.property.SimpleStringProperty;

public class Transaction {

	private String transactionFrom;
	private String transactionTo;
	private String fromAccountNo;
	private String toAccountNo;
	private Double transactionAmount;
	private String transactionComment;
	private String transactionType;
	private String transactionStatus;
	private Long transactionId;
	private Long accountNumber;
	
	private SimpleStringProperty transactId;
	private SimpleStringProperty transactFromAccountNo;
	private SimpleStringProperty transactToAccountNo;
	private SimpleStringProperty transactAmount;
	private SimpleStringProperty transactType;
	private SimpleStringProperty transactStatus;
	
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public String getTransactionFrom() {
		return transactionFrom;
	}
	public String getTransactionTo() {
		return transactionTo;
	}
	public String getFromAccountNo() {
		return fromAccountNo;
	}
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public String getTransactionComment() {
		return transactionComment;
	}
	public void setTransactionFrom(String transactionFrom) {
		this.transactionFrom = transactionFrom;
	}
	public void setTransactionTo(String transactionTo) {
		this.transactionTo = transactionTo;
	}
	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public void setTransactionComment(String transactionComment) {
		this.transactionComment = transactionComment;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getToAccountNo() {
		return toAccountNo;
	}
	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}
	public String getTransactId() {
		return transactId.get();
	}
	public String getTransactFromAccountNo() {
		return transactFromAccountNo.get();
	}
	public String getTransactToAccountNo() {
		return transactToAccountNo.get();
	}
	public String getTransactAmount() {
		return transactAmount.get();
	}
	public String getTransactType() {
		return transactType.get();
	}
	public String getTransactStatus() {
		return transactStatus.get();
	}
	public void setTransactId(SimpleStringProperty transactId) {
		this.transactId = transactId;
	}
	public void setTransactFromAccountNo(SimpleStringProperty transactFromAccountNo) {
		this.transactFromAccountNo = transactFromAccountNo;
	}
	public void setTransactToAccountNo(SimpleStringProperty transactToAccountNo) {
		this.transactToAccountNo = transactToAccountNo;
	}
	public void setTransactAmount(SimpleStringProperty transactAmount) {
		this.transactAmount = transactAmount;
	}
	public void setTransactType(SimpleStringProperty transactType) {
		this.transactType = transactType;
	}
	public void setTransactStatus(SimpleStringProperty transactStatus) {
		this.transactStatus = transactStatus;
	}
	
	public Transaction(String tId, String tFromAccNo, String tToAccNo, String tAmount, String tType, String tStatus){
		this.transactId = new SimpleStringProperty(tId);
		this.transactFromAccountNo = new SimpleStringProperty(tFromAccNo);
		this.transactToAccountNo = new SimpleStringProperty(tToAccNo);
		this.transactAmount = new SimpleStringProperty(tAmount);
		this.transactType = new SimpleStringProperty(tType);
		this.transactStatus = new SimpleStringProperty(tStatus);
	}
	
	public Transaction(){}
	public Long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(Long accountNumber) {
		this.accountNumber = accountNumber;
	}
}