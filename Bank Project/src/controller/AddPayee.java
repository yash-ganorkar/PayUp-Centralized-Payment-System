package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import datamodels.AccountHolder;
import datamodels.Payee;
import datamodels.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.EmailSending;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class AddPayee extends EmailSending implements Initializable, IActionMethods{
	@FXML
	private Label labelWelcomeMessage;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private TextField txtPayeeName,txtPayeeAccNumber,txtPayeeAlias,txtPayeeEmailAddress,txtPayerAccNumber;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private Alert alert;
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
	private List<String> payee;
	private ResultSet resultSet = null;
	private Payee beneficiary;
	private List<Payee> beneficiaryList;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		beneficiary = new Payee("","","","","","");
		beneficiaryList =  new ArrayList<>();
		txtPayerAccNumber.setText(AccountHolder.getAccountNumber().toString());
		loginDatabaseOperations = new LoginDatabaseOperations();

		txtPayeeName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					txtPayeeName.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					txtPayeeName.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}

		});

		txtPayeeAccNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtPayeeAccNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					txtPayeeAccNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

		txtPayeeAlias.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					txtPayeeAlias.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					txtPayeeAlias.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
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
		int records = 0;
		try{
			if(checkForBlank()){
				if(emailValid(txtPayeeEmailAddress.getText().toString()))
				{
					resultSet = loginDatabaseOperations.selectAllAccounts(Long.parseLong(txtPayeeAccNumber.getText()));
					while(resultSet.next()){
						records = resultSet.getInt(1);
					}
					if(records == 0)
					{
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Payee");
						alert.setContentText("No such account exists.");
						Optional<ButtonType> result = alert.showAndWait();
					}
					else if(Long.parseLong(txtPayeeAccNumber.getText()) == Long.parseLong(txtPayerAccNumber.getText())){
						alert = new Alert(AlertType.ERROR);
						alert.setTitle("Payee");
						alert.setContentText("Cannot add self as Payee");
						Optional<ButtonType> result = alert.showAndWait();
					}
					else{
						payee = new ArrayList<>();

						beneficiary.setPayeeName(txtPayeeName.getText().toString());
						beneficiary.setPayeeAccountNo(txtPayeeAccNumber.getText().toString());	
						beneficiary.setPayeeAlias(txtPayeeAlias.getText().toString());
						beneficiary.setPayeeContactNo(txtPayeeEmailAddress.getText().toString());
						beneficiary.setPayerAccountNumber(txtPayerAccNumber.getText().toString());

						if(loginDatabaseOperations.insertPayee(beneficiary) == 1){
							alert = new Alert(AlertType.CONFIRMATION);
							alert.setTitle("Payee");
							alert.setContentText("Payee Added Successfully.");
							Optional<ButtonType> result = alert.showAndWait();
						}
					}
				}
				else
				{
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Payee");
					alert.setContentText("Invalid Email Address.");
					Optional<ButtonType> result = alert.showAndWait();
				}
			}
			else
			{
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Payee");
				alert.setContentText("Fields are blank.");
				Optional<ButtonType> result = alert.showAndWait();
			}


		}catch(Exception ex){
			ex.printStackTrace();
		}

	}	
	public void resetFields(){
		txtPayeeName.setText("");
		txtPayeeAccNumber.setText("");
		txtPayeeAlias.setText("");
		txtPayeeEmailAddress.setText("");
	}

	public void initData(String string) {
		labelWelcomeMessage.setText(string);
	}

	public Boolean checkForBlank(){
		if(txtPayeeName.getText().toString().equals("")){			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Payee");
			alert.setContentText("Payee Name cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtPayeeName.requestFocus();
			return false;
		}
		else if(txtPayeeAccNumber.getText().toString().equals("")){			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Payee");
			alert.setContentText("Payee Account Number cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtPayeeAccNumber.requestFocus();
			return false;
		}

		else if(txtPayeeAlias.getText().equals("")){			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Payee");
			alert.setContentText("Payee Alias cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtPayeeAlias.requestFocus();
			return false;
		}

		else if(txtPayeeEmailAddress.getText().equals("")){			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Payee");
			alert.setContentText("Payee Email Address cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtPayeeEmailAddress.requestFocus();
			return false;
		}

		else
			return true;
	}

}