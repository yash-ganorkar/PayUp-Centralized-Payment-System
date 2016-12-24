package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import datamodels.Payee;
import datamodels.Transaction;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.EmailSending;
import models.LoginDatabaseOperations;

public class TransferFundsController extends EmailSending implements Initializable{
	@FXML
	private Label labelWelcomeMessage;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private TextField txtPayUpAccountNumber,txtPayUpAccountHolder,txtPayeeEmailAddress,txtTransferAmount;
	@FXML 
	private ComboBox<String> comboPayee = new ComboBox();
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private Alert alert;

	private LoginDatabaseOperations loginDatabaseOperations;
	private List<String> listPayee;
	private Payee payee;
	private List<Payee> listPayeeDetails;
	private ResultSet resultSet = null;
	private Transaction transaction;
	private String selectedPayee;
	private double payeeAccountBalance;
	
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


	public void resetFields(){
		txtPayUpAccountNumber.setText("");
		txtPayUpAccountHolder.setText("");
		txtPayeeEmailAddress.setText("");
		txtTransferAmount.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		listPayee = new ArrayList<>();
		listPayeeDetails = new ArrayList<>();
		txtPayUpAccountNumber.setText(AccountHolder.getAccountNumber().toString());
		txtPayUpAccountHolder.setText(AccountHolder.getAccountHolderName().toString());
		loginDatabaseOperations = new LoginDatabaseOperations();
		getPayee();
		transaction = new Transaction();

		comboPayee.valueProperty().addListener(new ChangeListener<String>() {
			@Override public void changed(ObservableValue ov, String t, String t1) {
				System.out.println(t1);
				selectedPayee = t1;

				for(Payee p : listPayeeDetails){
					if(p.getPayeeName().equals(selectedPayee)){
						txtPayeeEmailAddress.setText(p.getPayeeContactNo());
					}
				}
			}    
		});	

		txtTransferAmount.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtTransferAmount.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});
	}

	public void initData(String string) {
		// TODO Auto-generated method stub
		labelWelcomeMessage.setText(string);
	}

	private void getPayee() {
		// TODO Auto-generated method stub
		try{
			resultSet = loginDatabaseOperations.selectAllPayees(AccountHolder.getAccountNumber());
			while(resultSet.next()){
				payee = new Payee(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4),"","");
				listPayeeDetails.add(payee);
			}

			for(Payee p : listPayeeDetails){
				listPayee.add(p.getPayeeName() + " - " +p.getPayeeAccountNo());
			}
			ObservableList<String> obList = FXCollections.observableList(listPayee);
			comboPayee.getItems().clear();
			comboPayee.setItems(obList);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException, SQLException{
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
			if(emailValid(txtPayeeEmailAddress.getText().toString()))
			{
				String[] accountHolderName = labelWelcomeMessage.getText().split(", ");
				String[] payeeselected = selectedPayee.split(" - ");
				transaction.setTransactionFrom(accountHolderName[1]);
				transaction.setTransactionTo(payeeselected[0]);
				transaction.setFromAccountNo(String.valueOf(AccountHolder.getAccountNumber()));
				transaction.setToAccountNo(payeeselected[1]);		
				transaction.setTransactionAmount(Double.parseDouble(txtTransferAmount.getText()));
				transaction.setTransactionComment("FUNDS TRANSFER");
				transaction.setTransactionType("TRANSFER");
				transaction.setTransactionStatus("PENDING");
				transaction.setAccountNumber(AccountHolder.getAccountNumber());
				resultSet = loginDatabaseOperations.selectBalance(Long.parseLong(payeeselected[1]));

				while(resultSet.next()){
					payeeAccountBalance = resultSet.getDouble(1);
				}

				if(AccountHolder.getAccountBalance().doubleValue() > transaction.getTransactionAmount().doubleValue()){
					if(loginDatabaseOperations.insertVirtualCardTransaction(transaction) == 1)
						//		accountB = AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue();
						System.out.println(AccountHolder.getAccountBalance().doubleValue());
					System.out.println(transaction.getTransactionAmount().doubleValue());
					System.out.println(AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue());
					double ans = AccountHolder.getAccountBalance().doubleValue() - transaction.getTransactionAmount().doubleValue();
					System.out.println(ans);

					payeeAccountBalance+= transaction.getTransactionAmount().doubleValue();
					if(loginDatabaseOperations.updateAccountBalance(ans, AccountHolder.getAccountNumber()) == 1){
						if(loginDatabaseOperations.updateAccountBalance(payeeAccountBalance, Long.parseLong(payeeselected[1])) == 1){
							alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Transfer Funds");
							alert.setContentText("$ "+transaction.getTransactionAmount().doubleValue()+ " amount transfer to account number " +payeeselected[1]);
							Optional<ButtonType> result = alert.showAndWait();
						}
					}
				}
			}
			else
			{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Transfer Funds");
				alert.setContentText("Please Enter Valid Email Address.");
				Optional<ButtonType> result = alert.showAndWait();				
			}
			}else{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Transfer Funds");
				alert.setContentText("Fields are blank.");
				Optional<ButtonType> result = alert.showAndWait();				

			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public Boolean checkForBlank(){
		if(txtPayUpAccountNumber.getText().toString().equals(""))
			return false;
		else if(txtPayUpAccountHolder.getText().toString().equals(""))
			return false;
		else if(txtPayeeEmailAddress.getText().toString().equals(""))
			return false;
		else if(txtTransferAmount.getText().toString().equals(""))
			return false;
		else if(comboPayee.getValue().equals(""))
			return false;
		else
			return true;
	}

}