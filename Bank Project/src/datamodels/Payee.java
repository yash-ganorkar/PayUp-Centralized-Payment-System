package datamodels;

import javafx.beans.property.SimpleStringProperty;

public class Payee {

	private final SimpleStringProperty payeeID;
	private final SimpleStringProperty payeeName;
	private final SimpleStringProperty payeeAccountNo;
	private final SimpleStringProperty payeeContactNo;
	private final SimpleStringProperty payeeAlias;
	private final SimpleStringProperty payerAccountNo;
	
	public String getPayeeAlias() {
		return payeeAlias.get();
	}

	public Payee(String pId, String pName, String pAccountNo, String pContactNo, String pAlias, String pAccountNumber){
		this.payeeID = new SimpleStringProperty(pId);
		this.payeeName = new SimpleStringProperty(pName);
		this.payeeAccountNo = new SimpleStringProperty(pAccountNo);
		this.payeeContactNo = new SimpleStringProperty(pContactNo);
		this.payeeAlias = new SimpleStringProperty(pAlias);
		this.payerAccountNo = new SimpleStringProperty(pAccountNumber);
	}
	
	public String getPayeeID() {
		return payeeID.get();
	}

	public String getPayeeName() {
		return payeeName.get();
	}

	public String getPayeeAccountNo() {
		return payeeAccountNo.get();
	}

	public String getPayeeContactNo() {
		return payeeContactNo.get();
	}
	
	public void setPayeeID(String pId){
		payeeID.set(pId);
	}
	public void setPayeeName(String pName){
		payeeName.set(pName);
	}

	public void setPayeeAccountNo(String pAccountNo){
		payeeAccountNo.set(pAccountNo);
	}

	public void setPayeeContactNo(String pContactNo){
		payeeContactNo.set(pContactNo);
	}
	public void setPayeeAlias(String pAlias){
		payeeAlias.set(pAlias);
	}

	public String getPayerAccountNo() {
		return payerAccountNo.get();
	}
	
	public void setPayerAccountNumber(String pAccountNumber){
		payerAccountNo.set(pAccountNumber);
	}

}
