package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import javafx.beans.value.ChangeListener;
import datamodels.AccountHolder;
import datamodels.Currency;
import datamodels.ForexTransaction;
import datamodels.Transaction;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class ForexAccountController implements Initializable, IActionMethods {

	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private Label labelWelcomeMessage,labelCcyDetails;
	@FXML 
	private TableView<Currency> tableView;
	@FXML
	private ObservableList<Currency> currency;
	@FXML
	private TableColumn<Currency, String> ccyId;
	@FXML
	private TableColumn<Currency, String> ccyPair;
	@FXML
	private TableColumn<Currency, String> buyRate;
	@FXML
	private TableColumn<Currency, String> sellRate;
	@FXML
	private TextField txtCcy1,txtCcy2,txtAmount,txtEqAmt;
	@FXML
	private ComboBox<String> comboBuySell= new ComboBox();
	@FXML
	private Button btnConfirm,btnReset;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;

	private String buysell;
	private ResultSet resultSet;
	private LoginDatabaseOperations loginDatabaseOperations;
	private List<Currency> currencies;
	private Currency rowData;
	private Transaction transaction;
	private ForexTransaction forexTransaction;
	private Double exchangeRate;

	public void resetFields(){
		txtCcy1.setText("");
		txtCcy2.setText("");
		txtAmount.setText("");
		txtEqAmt.setText("");
	}

	@FXML
	private Button btnExit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();

		forexTransaction = new ForexTransaction();
		transaction = new Transaction();

		List<String> list = new ArrayList<String>();
		list.add("BUY");
		list.add("SELL");
		ObservableList<String> obList = FXCollections.observableList(list);
		comboBuySell.getItems().clear();
		comboBuySell.setItems(obList);

		tableView.setRowFactory(new Callback<TableView<Currency>, TableRow<Currency>>() {

			@Override
			public TableRow<Currency> call(TableView<Currency> tv) {
				TableRow<Currency> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
						rowData = row.getItem();
						//System.out.println(rowData.getCurrencyPair());
						String ccyPair = rowData.getCurrencyPair();
						String currency[] = ccyPair.split(" - ");

						txtCcy1.setText(currency[0]);
						txtCcy2.setText(currency[1]);

						/*labelCcyDetails.setText("1 " +currency[0]+" = ");*/
					}
				});
				return row;
			}
		});

		comboBuySell.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String t, String t1) {
				System.out.println(t1);
				//	buysell = t1;
				forexTransaction.setDirection(t1);
			}    
		});	

		txtAmount.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub

				String pattern = "###,###.##";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);

				if(forexTransaction.getDirection().equalsIgnoreCase("Buy")){
					Double multiply = Double.parseDouble(txtAmount.getText()) * Double.parseDouble(rowData.getCurrencyBuyRate());
					txtEqAmt.setText(String.valueOf(decimalFormat.format(multiply)));
					exchangeRate = Double.parseDouble(rowData.getCurrencyBuyRate());
					//forexTransaction.setDirection("Buy");
					labelCcyDetails.setText("1 " +txtCcy1.getText()+" = " +rowData.getCurrencyBuyRate()+ " " +txtCcy2.getText());
					forexTransaction.setCcy1(txtCcy1.getText());
					forexTransaction.setCcy2(txtCcy2.getText());
					forexTransaction.setExchangerate(exchangeRate);
					forexTransaction.setCcy1amt(Double.parseDouble(txtAmount.getText()));
					forexTransaction.setCcy2amt(Double.parseDouble(txtEqAmt.getText()));
				}
				else if(forexTransaction.getDirection().equalsIgnoreCase("Sell")){
					Double multiply = Double.parseDouble(txtAmount.getText()) * Double.parseDouble(rowData.getCurrencySellRate());
					txtEqAmt.setText(String.valueOf(decimalFormat.format(multiply)));
					exchangeRate = Double.parseDouble(rowData.getCurrencySellRate());
					//forexTransaction.setDirection("Sell");
					labelCcyDetails.setText("1 " +txtCcy1.getText()+" = " +rowData.getCurrencySellRate()+ " " +txtCcy2.getText());
					forexTransaction.setCcy1(txtCcy1.getText());
					forexTransaction.setCcy2(txtCcy2.getText());
					forexTransaction.setCcy1amt(Double.parseDouble(txtAmount.getText()));
					forexTransaction.setExchangerate(exchangeRate);
					forexTransaction.setCcy2amt(Double.parseDouble(txtEqAmt.getText()));
				}
			}
		});

		txtAmount.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtAmount.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});
	}

	public void parseUserList() {
		// TODO Auto-generated method stub		
		try {
			resultSet = loginDatabaseOperations.selectAllCurrencies();
			currencies = new ArrayList<>();
			currency = FXCollections.observableArrayList();
			Currency ccy;
			while(resultSet.next()){
				ccy =  new Currency(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));				
				currency.add(ccy);
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ccyId.setCellValueFactory(new PropertyValueFactory<Currency,String>("currencyId"));
		ccyPair.setCellValueFactory(new PropertyValueFactory<Currency,String>("currencyPair"));
		buyRate.setCellValueFactory(new PropertyValueFactory<Currency,String>("currencyBuyRate"));
		sellRate.setCellValueFactory(new PropertyValueFactory<Currency,String>("currencySellRate"));
		tableView.setItems(currency);
	}

	public void initData(String string) {
		// TODO Auto-generated method stub
		labelWelcomeMessage.setText(string);
		parseUserList();
	}
	@FXML
	public void handleButtonAction(ActionEvent event) throws IOException, SQLException{
		if(event.getSource()==menuForexAccount){
			//get reference to the button's stage         
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			//load up OTHER FXML document
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ForexAccount.fxml"));
			root = fxmlLoader.load();

			ForexAccountController controller = fxmlLoader.<ForexAccountController>getController();
			controller.initData(labelWelcomeMessage.getText().toString());
		}
		else if(event.getSource()==menuProfile){
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/fxml/ViewProfile.fxml"));
		}
		else if(event.getSource()==viewPayUpTransactions){
			//get reference to the button's stage         
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			//load up OTHER FXML document
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ViewTransactions.fxml"));

			root = fxmlLoader.load();

			ViewTransactionsController controller = fxmlLoader.<ViewTransactionsController>getController();
			controller.initData();
		}
		else if(event.getSource()==payFriend){
			//get reference to the button's stage         
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			//load up OTHER FXML document
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/TransferFunds.fxml"));
			root = fxmlLoader.load();

			TransferFundsController controller = fxmlLoader.<TransferFundsController>getController();
			controller.initData(labelWelcomeMessage.getText().toString());
		}
		else if(event.getSource()==linkCreditCards){
			//			root = FXMLLoader.load(getClass().getResource("/fxml/LinkDebitCreditCards.fxml"));
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			//load up OTHER FXML document
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LinkDebitCreditCards.fxml"));
			root = fxmlLoader.load();

			LinkCreditCardController controller = fxmlLoader.<LinkCreditCardController>getController();
			controller.initData(labelWelcomeMessage.getText().toString());
		}

		/*	else if(event.getSource()==unlinkCreditCards){
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("/fxml/UnlinkCreditDebitCards.fxml"));
		}
		 */		else if(event.getSource()==addPayee){
			 stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			 //load up OTHER FXML document
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddPayee.fxml"));
			 root = fxmlLoader.load();

			 AddPayee controller = fxmlLoader.<AddPayee>getController();
			 controller.initData(labelWelcomeMessage.getText().toString());
		 }

		 else if(event.getSource()==removePayee){
			 stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RemovePayee.fxml"));
			 root = fxmlLoader.load();

			 RemovePayeeController controller = fxmlLoader.<RemovePayeeController>getController();
			 controller.initData(labelWelcomeMessage.getText().toString());
		 }
		 else if(event.getSource()==addVirtualCard){
			 stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddVirtualCard.fxml"));
			 root = fxmlLoader.load();

			 AddVirtualCardController controller = fxmlLoader.<AddVirtualCardController>getController();
			 controller.initData(labelWelcomeMessage.getText().toString());
		 }
		 else if(event.getSource()==addVirtualCash){
			 stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/LoadVirtualCash.fxml"));
			 root = fxmlLoader.load();

			 LoadVirtualCashController controller = fxmlLoader.<LoadVirtualCashController>getController();
			 controller.initData(labelWelcomeMessage.getText().toString());
		 }
		 else if(event.getSource()==rechargeAccountBalance){
			 stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/RechargePayUpBalance.fxml"));
			 root = fxmlLoader.load();

			 RechargePayUpBalanceController controller = fxmlLoader.<RechargePayUpBalanceController>getController();
			 controller.initData(labelWelcomeMessage.getText().toString());
		 }
		//
		//create a new scene with root and set the stage
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void saveForexData(){
		if(checkForBlank()){
			transaction.setTransactionFrom(AccountHolder.getAccountHolderName());
			transaction.setTransactionTo("SELF FOREX ACCOUNT");
			transaction.setFromAccountNo(String.valueOf(AccountHolder.getAccountNumber()));
			transaction.setToAccountNo(String.valueOf(AccountHolder.getAccountNumber()));		
			transaction.setTransactionAmount(forexTransaction.getCcy1amt());
			transaction.setTransactionComment(forexTransaction.getDirection() + " " +forexTransaction.getCcy1() + " of amount " +forexTransaction.getCcy1amt() + " against " +forexTransaction.getCcy2());
			transaction.setTransactionType("FOREX");
			transaction.setTransactionStatus("APPROVED");
			transaction.setAccountNumber(AccountHolder.getAccountNumber());		

			if(AccountHolder.getAccountBalance().doubleValue() > transaction.getTransactionAmount().doubleValue()){
				if(loginDatabaseOperations.insertVirtualCardTransaction(transaction) == 1)
					//		accountB = AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue();
					System.out.println(AccountHolder.getAccountBalance().doubleValue());
				System.out.println(transaction.getTransactionAmount().doubleValue());
				System.out.println(AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue());
				double ans = AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue();
				System.out.println(ans);
				forexTransaction.setAccountNo(AccountHolder.getAccountNumber());

				if(loginDatabaseOperations.updateAccountBalance(ans, AccountHolder.getAccountNumber()) == 1){
					if(loginDatabaseOperations.insertForexTransaction(forexTransaction) == 1)
						new Alert(Alert.AlertType.CONFIRMATION, "Transaction Completed Successfully.").showAndWait();
				}
			}
			else
			{
				new Alert(Alert.AlertType.ERROR, "Insufficient Balance... Cannot Proceed with Transaction").showAndWait();
			}
		}
		else
		{
			new Alert(Alert.AlertType.ERROR, "Field is blank. Cannot proceed.").showAndWait();
		}

	}

	public Boolean checkForBlank(){
		if(txtCcy1.getText().toString().equals(""))
			return false;
		else if(txtCcy2.getText().toString().equals(""))
			return false;
		else if(txtEqAmt.getText().equals(""))
			return false;
		else if(txtAmount.getText().equals(""))
			return false;
		else
			return true;
	}
	@Override
	public void ExitForm(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		stage=(Stage) labelWelcomeMessage.getScene().getWindow();
		//load up OTHER FXML document
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AccountHolderConsole.fxml"));
		root = fxmlLoader.load();

		AccountHolderController controller = fxmlLoader.<AccountHolderController>getController();
		controller.initData(labelWelcomeMessage.getText().toString());
		
	}

}