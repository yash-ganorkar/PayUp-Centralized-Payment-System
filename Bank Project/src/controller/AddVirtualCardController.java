package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import datamodels.Currency;
import datamodels.VirtualCard;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.LoginDatabaseOperations;

public class AddVirtualCardController implements Initializable {

	@FXML
	private Label labelDigits1,labelDigits2,labelDigits3,labelDigits4,labelCardHolderName,labelCVV,labelWelcomeMessage;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private Button btnAdd,btnShowHideDetails;
	@FXML
	private Alert alert;

	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	private VirtualCard vCard;

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
		vCard = new VirtualCard();
	}

	public void initData(String string) {
		// TODO Auto-generated method stub
		labelWelcomeMessage.setText(string);
		parseUserList();
	}

	public void parseUserList() {
		// TODO Auto-generated method stub		
		ResultSet rs;
		try {
			resultSet = loginDatabaseOperations.getVirtualCardYN(AccountHolder.getAccountNumber());
			while(resultSet.next()){
				if(resultSet.getString(1).equalsIgnoreCase("N"))
				{
					//getNewVirtualCardNumber()
					rs = loginDatabaseOperations.getNewVirtualCardNumber(AccountHolder.getAccountNumber());
					while(rs.next()){
						vCard.setNumber1(rs.getLong(1));
						vCard.setNumber2(rs.getLong(2));
						vCard.setNumber3(rs.getLong(3));
						vCard.setNumber4(rs.getLong(4));
						vCard.setCvv(rs.getLong(5));
						vCard.setFirstName(rs.getString(6).toUpperCase());
						}
				}
				else if(resultSet.getString(1).equalsIgnoreCase("Y")){
					//getVirtualCardNumber()

					rs = loginDatabaseOperations.getVirtualCardNumber(AccountHolder.getAccountNumber());
					while(rs.next()){
						System.out.println(rs.getString(2)+""+rs.getString(3));
						vCard.setNumber1(rs.getLong(2));
						vCard.setNumber2(rs.getLong(3));
						vCard.setNumber3(rs.getLong(4));
						vCard.setNumber4(rs.getLong(5));
						vCard.setCvv(rs.getLong(6));
						vCard.setFirstName(rs.getString(7).toUpperCase());

						labelDigits1.setText(vCard.getNumber1().toString());
						labelDigits2.setText(vCard.getNumber2().toString());
						labelDigits3.setText(vCard.getNumber3().toString());
						labelDigits4.setText(vCard.getNumber4().toString());
						labelCVV.setText(vCard.getCvv().toString());
						labelCardHolderName.setText(vCard.getFirstName().toUpperCase());
						btnAdd.setText("Remove");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public void saveCard(ActionEvent event){
		try{
			if(btnAdd.getText().equals("Add")){
				vCard.setMonth("02");
				vCard.setYear("2022");
				vCard.setAccountNumber(AccountHolder.getAccountNumber());
				
				if(loginDatabaseOperations.insertVirtualCardNumber(vCard) == 1){
					if(loginDatabaseOperations.updateVirtualCardYN(AccountHolder.getAccountNumber(),"Y") == 0){
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Virtual Card");
						alert.setContentText("Virtual Card added successfully.");
						Optional<ButtonType> result = alert.showAndWait();
					}
					parseUserList();
				}			
				btnAdd.setText("Remove");
			}
			else if(btnAdd.getText().equals("Remove")){
				if(loginDatabaseOperations.deleteVirtualCard(AccountHolder.getAccountNumber()) == 1){
					if(loginDatabaseOperations.updateVirtualCardYN(AccountHolder.getAccountNumber(),"N") == 1){
					alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Virtual Card");
					alert.setContentText("Virtual Card deleted successfully.");
					Optional<ButtonType> result = alert.showAndWait();
					}
					parseUserList();
				}
				btnAdd.setText("Add");				
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void showHideDetails()
	{
		if(btnShowHideDetails.getText().toString().equals("Hide Details")){
			labelDigits1.setText("....");
			labelDigits2.setText("....");
			labelDigits3.setText("....");
			labelCVV.setText("...");
			btnShowHideDetails.setText("Show Details");
		}
		else if(btnShowHideDetails.getText().toString().equals("Show Details")){
			labelDigits1.setText(vCard.getNumber1().toString());
			labelDigits2.setText(vCard.getNumber2().toString());
			labelDigits3.setText(vCard.getNumber3().toString());
			labelCVV.setText(vCard.getCvv().toString());
			btnShowHideDetails.setText("Hide Details");
		}
		
	}
}
