package datamodels;

import javafx.beans.property.SimpleStringProperty;

public class Currency {

	private final SimpleStringProperty currencyId;
	private final SimpleStringProperty currencyPair;
	private final SimpleStringProperty currencyBuyRate;
	private final SimpleStringProperty currencySellRate;
	
	
	public Currency(String cId, String cPair, String cBuyRate, String cSellRate){
		this.currencyId = new SimpleStringProperty(cId);
		this.currencyPair = new SimpleStringProperty(cPair);
		this.currencyBuyRate = new SimpleStringProperty(cBuyRate);
		this.currencySellRate = new SimpleStringProperty(cSellRate);
	}
	
	public String getCurrencyId() {
		return currencyId.get();
	}

	public String getCurrencyPair() {
		return currencyPair.get();
	}

	public String getCurrencyBuyRate() {
		return currencyBuyRate.get();
	}

	public String getCurrencySellRate() {
		return currencySellRate.get();
	}
	
	public void setCurrencyId(String cId){
		currencyId.set(cId);
	}
	public void setCurrencyPair(String cPair){
		currencyPair.set(cPair);
	}

	public void setCurrencyBuyRate(String cBuyRate){
		currencyBuyRate.set(cBuyRate);
	}

	public void setCurrencySellRate(String cSellRate){
		currencySellRate.set(cSellRate);
	}
}
