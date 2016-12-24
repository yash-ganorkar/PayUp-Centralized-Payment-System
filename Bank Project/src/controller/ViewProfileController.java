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
import datamodels.User;
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
import javafx.scene.control.DatePicker;
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


public class ViewProfileController extends EmailSending implements Initializable,IActionMethods {
	
	@FXML
	private TextField textFirstName
						,textMiddleName
						,textLastName
						,textContact
						,textSSN
						,textEmail
						,textAddressLine1
						,textAddressLine2
						,textCity
						,dpDOB
						,textGender;
	
	@FXML
	private Label labelWelcomeMessage;
		
	@FXML
	private ComboBox<String> comboState = new ComboBox();

	@FXML
	private Button btnReset;
	@FXML
	private Button btnSave;
	@FXML 
	private Alert alert;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;

	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;

	
	private List<String> screenData = new ArrayList<>();
	private LoginDatabaseOperations loginDatabaseOperations;
	private ResultSet resultSet = null;
	private User user;
	
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

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();
		getAccountProfile();
	}
	
	public void getAccountProfile(){
		
		try{
			resultSet = loginDatabaseOperations.getAccountProfile(AccountHolder.getAccountNumber());
			while(resultSet.next())
			{
				textFirstName.setText(resultSet.getString(1));
				textMiddleName.setText(resultSet.getString(2));
				textLastName.setText(resultSet.getString(3));
				textContact.setText(resultSet.getString(4));
				textSSN.setText(resultSet.getString(5));
				textEmail.setText(resultSet.getString(6));
				
				String split[] = resultSet.getString(7).split(",");
				
				textAddressLine1.setText(split[0]);
				textAddressLine2.setText(split[1]);
				textCity.setText(split[2]);
				
				textGender.setText(resultSet.getString(8));
				comboState.setValue(resultSet.getString(9));
				dpDOB.setText(resultSet.getString(10));
				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void updateProfile(){
		try{
			if(checkForBlank()){
			user = new User();
			
			user.setContact(textContact.getText());
			user.setEmail(textEmail.getText());
			user.setAddressLine1(textAddressLine1.getText());
			user.setAddressLine2(textAddressLine2.getText());
			user.setCity(textCity.getText());
			user.setState(comboState.getValue());
			
			if(loginDatabaseOperations.updateRecords(AccountHolder.getAccountNumber(), user) == 1){
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Profile");
				alert.setContentText("Profile Updated.");
				Optional<ButtonType> result = alert.showAndWait();
			}
			}else{
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Profile");
				alert.setContentText("Fields cannot be blank.");
				Optional<ButtonType> result = alert.showAndWait();

			}
			
		}catch(Exception ex){
			
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
		else if(event.getSource()==addPayee){
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
	
	public Boolean checkForBlank(){
		if(textContact.getText().toString().equals(""))
			return false;
		else if(textEmail.getText().toString().equals(""))
			return false;
		else if(textAddressLine1.getText().equals(""))
			return false;
		else if(textCity.getText().equals(""))
			return false;
		else if(comboState.getValue().equals(""))
			return false;

		else
			return true;
	}
}