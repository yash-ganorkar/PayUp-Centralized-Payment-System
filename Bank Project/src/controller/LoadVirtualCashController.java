package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import datamodels.Transaction;
import datamodels.VirtualCash;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class LoadVirtualCashController implements Initializable, IActionMethods{
	@FXML
	private Label labelWelcomeMessage;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private TextField txtVirtualCardNumber,txtVirtualCardCVV,txtRechargeAmount,txtPayUpAccountNumber;
	@FXML
	private Label labelAlias,labelCreditCardCVV;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;

	@FXML
	private Button btnExit;

	@FXML
	public void ExitForm(ActionEvent event) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AccountHolderConsole.fxml"));
		Parent root1 = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Account Console");
		stage.setScene(new Scene(root1));

		AccountHolderController controller = fxmlLoader.<AccountHolderController>getController();
		controller.setDataToDataModels();
		stage.show();
	}

	private LoginDatabaseOperations loginDatabaseOperations;
	private ResultSet resultSet;
	private VirtualCash virtualCash;
	private Transaction transaction;
	private double accountB = 0;
	private String transactionType;

	public void resetFields(){
		txtVirtualCardNumber.setText("");
		txtVirtualCardCVV.setText("");
		txtRechargeAmount.setText("");
		txtPayUpAccountNumber.setText("");
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();
		virtualCash = new VirtualCash();
		transaction = new Transaction();

		getVirtualCard();

		txtPayUpAccountNumber.setText(AccountHolder.getAccountNumber().toString());

		txtRechargeAmount.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtRechargeAmount.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

	}

	private void getVirtualCard() {
		try {
			resultSet = loginDatabaseOperations.getVirtualCardNumber(AccountHolder.getAccountNumber());
			while(resultSet.next()){
				virtualCash.setvCardNumber1(resultSet.getLong(2));
				virtualCash.setvCardNumber2(resultSet.getLong(3));
				virtualCash.setvCardNumber3(resultSet.getLong(4));
				virtualCash.setvCardNumber4(resultSet.getLong(5));
				virtualCash.setCVV(resultSet.getInt(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txtVirtualCardNumber.setText(virtualCash.getvCardNumber1().toString()+""+virtualCash.getvCardNumber2().toString()+""+virtualCash.getvCardNumber3().toString()+""+virtualCash.getvCardNumber4().toString());
		txtVirtualCardCVV.setText(virtualCash.getCVV().toString());

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

		/*		else if(event.getSource()==unlinkCreditCards){
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
	public void saveData(){
		try{
			if(checkForBlank()){
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-DD-YYYY HH:MM:SS");

				String[] accountHolderName = labelWelcomeMessage.getText().split(", ");

				transaction.setTransactionFrom(accountHolderName[1]);
				transaction.setTransactionTo("SELF VIRTUAL CARD");
				transaction.setFromAccountNo(String.valueOf(AccountHolder.getAccountNumber()));
				transaction.setToAccountNo(String.valueOf(txtVirtualCardNumber.getText()));
				transaction.setTransactionAmount(Double.parseDouble(txtRechargeAmount.getText()));
				transaction.setTransactionComment("Virtual Card Recharge");
				transaction.setTransactionStatus("PENDING");
				transaction.setTransactionType("RECHARGE");
				transaction.setAccountNumber(AccountHolder.getAccountNumber());

				/*
				 * 			
			transaction.setTransactionFrom(accountHolderName[1]);
			transaction.setTransactionTo("SELF ACCOUNT");
			transaction.setFromAccountNo(String.valueOf(listCreditCard.get(0).getCreditcardnumber()));
			transaction.setToAccountNo(String.valueOf(listCreditCard.get(0).getAccountnumber()));
			transaction.setTransactionAmount(Double.parseDouble(txtRechargeAmount.getText()));
			transaction.setTransactionComment("Account Recharge Done using Credit Card");
			transaction.setTransactionStatus("PENDING");
			transaction.setTransactionType("RECHARGE");
				 */
				if(AccountHolder.getAccountBalance().doubleValue() > transaction.getTransactionAmount().doubleValue()){
					if(loginDatabaseOperations.insertVirtualCardTransaction(transaction) == 1)
						accountB = AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue();
					System.out.println(AccountHolder.getAccountBalance().doubleValue());
					System.out.println(transaction.getTransactionAmount().doubleValue());
					System.out.println(AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue());
					double ans = AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue();
					System.out.println(ans);
					if(loginDatabaseOperations.updateAccountBalance(ans, AccountHolder.getAccountNumber()) == 1){
						if(loginDatabaseOperations.updateVirtualCardBalance(transaction.getTransactionAmount().doubleValue(), AccountHolder.getAccountNumber()) == 1){
							new Alert(Alert.AlertType.CONFIRMATION, "Virtual Card successfully recharged").showAndWait();
						}
					}
				}
				else
				{
					new Alert(Alert.AlertType.ERROR, "Insufficient Balance... Cannot Proceed with Transaction").showAndWait();
				}
			}
			else
			{
				new Alert(Alert.AlertType.ERROR, "Fields are blank. Canoot Proceed.").showAndWait();
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void initData(String string) {
		// TODO Auto-generated method stub
		labelWelcomeMessage.setText(string);
	}	

	public Boolean checkForBlank(){
		if(txtVirtualCardNumber.getText().toString().equals(""))
			return false;
		else if(txtVirtualCardCVV.getText().toString().equals(""))
			return false;
		else if(txtRechargeAmount.getText().equals(""))
			return false;
		else if(txtPayUpAccountNumber.getText().equals(""))
			return false;
		else
			return true;
	}

}