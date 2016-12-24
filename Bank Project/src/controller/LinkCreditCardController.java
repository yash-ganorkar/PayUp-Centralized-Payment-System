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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class LinkCreditCardController implements Initializable,IActionMethods {

	@FXML
	private TextField txtCreditCardNumber,txtCVVNumber,txtNameOnCard,txtMonth,txtYear,txtAlias,txtCreditCardType;
	@FXML
	private Label labelWelcomeMessage;	
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private Stage stage;
	@FXML
	private Button btnLink;
	@FXML
	private Parent root;
	@FXML
	private ListView<String> listView;
	@FXML 
	private Alert alert;
	
	private List<String> cardList;
	
	ObservableList<String> observableList =  FXCollections.observableArrayList();
	
	private LoginDatabaseOperations loginDatabaseOperations;

	private List<String> creditCard;
	private ResultSet resultSet;
	
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
		txtCreditCardNumber.setText("");
		txtCVVNumber.setText("");
		txtNameOnCard.setText("");
		txtMonth.setText("");
		txtYear.setText("");
		txtAlias.setText("");
		txtCreditCardType.setText("");
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginDatabaseOperations = new LoginDatabaseOperations();
		try {
		resultSet = loginDatabaseOperations.selectAllCards(AccountHolder.getAccountNumber());
		cardList = new ArrayList<>();
			while(resultSet.next()){
				cardList.add(0,resultSet.getString(1));
				cardList.add(1,resultSet.getString(2));
				cardList.add(2,resultSet.getString(3));
				cardList.add(3,resultSet.getString(4));
			}
			
			observableList.setAll(cardList);
			listView.setItems(observableList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		txtCreditCardNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtCreditCardNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
			
		});

		txtCVVNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtCVVNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
			
		});
		
		txtCVVNumber.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if (newValue.intValue() > oldValue.intValue()) {
					if (txtCVVNumber.getText().length() > 3) {
						txtCVVNumber.setText(txtCVVNumber.getText().substring(0, 3));
					}
				}
			}

		});

		txtNameOnCard.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					txtNameOnCard.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					txtNameOnCard.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}
			
		});
		
		txtAlias.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					txtAlias.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					txtAlias.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});


		txtMonth.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtMonth.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
			
		});
		
		txtMonth.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if (newValue.intValue() > oldValue.intValue()) {
					if (txtMonth.getText().length() > 2) {
						txtMonth.setText(txtMonth.getText().substring(0, 2));
					}
				}
			}

		});


		txtYear.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtYear.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
			
		});
		
		txtYear.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if (newValue.intValue() > oldValue.intValue()) {
					if (txtYear.getText().length() > 4) {
						txtYear.setText(txtYear.getText().substring(0, 4));
					}
				}
			}

		});
	}

	public void setCreditCardType(){
		try{
			System.out.println(txtCreditCardNumber.getLength());
			if(txtCreditCardNumber.getLength() >= 16){
				System.out.println(txtCreditCardNumber.getText());
				String result = getCCType(txtCreditCardNumber.getText().trim());
				System.out.println(result);
				if(result == "NA")
					new Alert(Alert.AlertType.ERROR, "Invalid Card").showAndWait();
				else
				txtCreditCardType.setText(result);	
			}
			else
			{
				//do nothing
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private String getCCType(String ccNumber){

		String visaRegex        = "^4[0-9]{12}(?:[0-9]{3})?$";
		String masterRegex      = "^5[1-5][0-9]{14}$";
		String discoverRegex    = "^6(?:011|5[0-9]{2})[0-9]{12}$";
		String CCType 			= "";

		String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
				"(?<mastercard>5[1-5][0-9]{14})|" +
				"(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
				"(?<amex>3[47][0-9]{13})|" +
				"(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
				"(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";

		try {
			/*            ccNumber = ccNumber.replaceAll("\\D", "");
            	if(ccNumber.matches(visaRegex))
            		return "VISA";
            	else if(ccNumber.matches(masterRegex))
            		return "MASTER CARD";
            	else if(ccNumber.matches(discoverRegex))
            		return "DISCOVER";
            	else 
            		return "";
			 */            	
			Pattern pattern = Pattern.compile(regex);
			ccNumber = ccNumber.replaceAll("\\D", "");
			Matcher matcher = pattern.matcher(ccNumber);
			System.out.println(matcher.matches());
			if(matcher.matches()) {
				//If card is valid then verify which group it belong 
				if(matcher.group("mastercard")!= null){
					System.out.println("mastercard");
					CCType = "MASTER CARD";
				}
				else if(matcher.group("visa") != null){
					System.out.println("visa");
					CCType = "VISA";
				}
				else if(matcher.group("discover") != null){
					System.out.println("discover");
					CCType = "DISCOVER";
				}
				else if(matcher.group("amex") != null){
					System.out.println("amex");
					CCType = "AMEX";
				}
				else if(matcher.group("diners") != null){
					System.out.println("diners");
					CCType = "DINERS";
				}
			}
			else{
				CCType = "NA";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CCType;
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
				
			
			creditCard = new ArrayList<>();
			
			creditCard.add(0, txtCreditCardNumber.getText().toString());
			creditCard.add(1, txtCVVNumber.getText().toString());
			creditCard.add(2, txtNameOnCard.getText().toString());
			creditCard.add(3, txtMonth.getText().toString()+"/"+txtYear.getText().toString());
			creditCard.add(4, txtAlias.getText().toString());
			creditCard.add(5, txtCreditCardType.getText().toString());
			creditCard.add(6, AccountHolder.getAccountNumber().toString());

			if(loginDatabaseOperations.insertCreditCard(creditCard) == 1){
					System.out.println("Linked...");
					alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Credit Card");
					alert.setContentText("Credit Card Linked.");
					Optional<ButtonType> result = alert.showAndWait();
				}
				else
				{
					System.out.println("Not Linked...");
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Credit Card");
					alert.setContentText("Unable to link credit card. Please contact Support Desk for more help.");
					Optional<ButtonType> result = alert.showAndWait();
				}
			}else{
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Credit Card");
			alert.setContentText("Unable to link credit card. Fields blank.");
			Optional<ButtonType> result = alert.showAndWait();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void initData(String string) {
		labelWelcomeMessage.setText(string);
	}
	
	public Boolean checkForBlank(){
		if(txtCreditCardNumber.getText().toString().equals(""))		
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Link Credit Card");
			alert.setContentText("Credit Card Number cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtCreditCardNumber.requestFocus();
			return false;
		}

		else if(txtCVVNumber.getText().toString().equals("")){
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Link Credit Card");
			alert.setContentText("Credit Card CVV Number cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtCVVNumber.requestFocus();
			return false;
		}

		else if(txtNameOnCard.getText().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Link Credit Card");
			alert.setContentText("Credit Card Name cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtNameOnCard.requestFocus();
			return false;
		}
		else if(txtMonth.getText().equals("") || txtMonth.getText().length() < 2 || txtMonth.getText().length() > 2)
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Link Credit Card");
			alert.setContentText("Month cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtMonth.requestFocus();
			return false;
		}
		else if(txtYear.getText().equals("") || txtYear.getText().length() < 4 || txtYear.getText().length() > 4)
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Link Credit Card");
			alert.setContentText("Year cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtYear.requestFocus();
			return false;
		}
		
		else if(txtAlias.getText().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Link Credit Card");
			alert.setContentText("Alias cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtAlias.requestFocus();
			return false;
		}
		else
			return true;
	}

}