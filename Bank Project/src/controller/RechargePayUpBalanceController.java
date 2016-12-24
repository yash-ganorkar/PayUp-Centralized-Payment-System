package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import datamodels.CreditCard;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class RechargePayUpBalanceController implements Initializable, IActionMethods{
	@FXML
	private Label labelWelcomeMessage;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private TextField txtAccountNumber,txtRechargeAmount,txtCVV;
	@FXML
	private ComboBox<String> comboCreditCard = new ComboBox<>();
	@FXML
	private Label labelAlias,labelCreditCardCVV;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private Button btnRecharge;
	@FXML
	private Alert alert;

	private LoginDatabaseOperations loginDatabaseOperations;
	private ResultSet resultSet;
	private CreditCard creditCard;
	private List<CreditCard> listCreditCard;
	private List<String> listCreditCardView;
	private Transaction transaction;
	private double accountB = 0;
	private String transactionType;

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
		txtAccountNumber.setText("");
		txtRechargeAmount.setText("");
		txtCVV.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();
		creditCard = new CreditCard();
		transaction = new Transaction();
		listCreditCard = new ArrayList<>();
		listCreditCardView = new ArrayList<>();

		getCreditCard();
		if(listCreditCard.size() == 0){
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Account");
			alert.setContentText("Credit Card Not Linked. Please link Credit Card first.");
			Optional<ButtonType> result = alert.showAndWait();
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AccountHolderConsole.fxml"));
			Parent root1 = null;
			try {
				root1 = (Parent) fxmlLoader.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Account Console");
			stage.setScene(new Scene(root1));

			AccountHolderController controller = fxmlLoader.<AccountHolderController>getController();
			controller.setDataToDataModels();
			stage.show();

		}
		else
		{
			for(CreditCard c: listCreditCard){
				listCreditCardView.add(c.getCreditcardnumber().toString());
			}

			ObservableList<String> obList = FXCollections.observableList(listCreditCardView);
			comboCreditCard.getItems().clear();
			comboCreditCard.setItems(obList);

			txtAccountNumber.setText(listCreditCard.get(0).getAccountnumber().toString());

			txtRechargeAmount.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					if(!newValue.matches("\\d*")){
						txtRechargeAmount.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}

			});

			txtAccountNumber.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					if(!newValue.matches("\\d*")){
						txtAccountNumber.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}

			});

			txtCVV.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					// TODO Auto-generated method stub
					if(!newValue.matches("\\d*")){
						txtCVV.setText(newValue.replaceAll("[^\\d]", ""));
					}
				}

			});
		}

	}

	private void getCreditCard() {
		try {
			resultSet = loginDatabaseOperations.selectAllCards(AccountHolder.getAccountNumber());
			while(resultSet.next()){
				creditCard.setCreditcardnumber(resultSet.getLong(1));
				creditCard.setCvvnumber(resultSet.getString(5));
				creditCard.setAccountnumber(resultSet.getLong(6));
				listCreditCard.add(creditCard);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(checkForBlank()){

			try{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-DD-YYYY HH:MM:SS");

				String[] accountHolderName = labelWelcomeMessage.getText().split(", ");

				transaction.setTransactionFrom(accountHolderName[1]);
				transaction.setTransactionTo("SELF ACCOUNT");
				transaction.setFromAccountNo(String.valueOf(listCreditCard.get(0).getCreditcardnumber()));
				transaction.setToAccountNo(String.valueOf(listCreditCard.get(0).getAccountnumber()));
				transaction.setTransactionAmount(Double.parseDouble(txtRechargeAmount.getText()));
				transaction.setTransactionComment("Account Recharge Done using Credit Card");
				transaction.setTransactionStatus("PENDING");
				transaction.setTransactionType("RECHARGE");
				transaction.setAccountNumber(AccountHolder.getAccountNumber());

				resultSet = loginDatabaseOperations.selectBalance(listCreditCard.get(0).getAccountnumber());

				while(resultSet.next()){
					accountB = resultSet.getDouble(1);
				}

				accountB += Double.parseDouble(txtRechargeAmount.getText()); 

				if(loginDatabaseOperations.updateAccountBalance(accountB, listCreditCard.get(0).getAccountnumber()) == 1){

					if(loginDatabaseOperations.insertVirtualCardTransaction(transaction) == 1){
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Account");
						alert.setContentText("Account Balance Updated");
						Optional<ButtonType> result = alert.showAndWait();
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Account");
			alert.setContentText("Fields cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();			
		}
	}
	public void initData(String string) {
		// TODO Auto-generated method stub
		labelWelcomeMessage.setText(string);
	}	

	public Boolean checkForBlank(){
		if(txtAccountNumber.getText().toString().equals(""))
			return false;
		else if(txtRechargeAmount.getText().toString().equals(""))
			return false;
		else if(txtCVV.getText().toString().equals(""))
			return false;
		else if(comboCreditCard.getValue().equals(""))
			return false;
		else
			return true;
	}

}