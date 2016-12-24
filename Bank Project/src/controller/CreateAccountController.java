package controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import datamodels.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.LoginDatabaseOperations;
import models.EmailSending;

public class CreateAccountController extends EmailSending implements Initializable {

	@FXML
	private TextField 
	textFirstName
	,textMiddleName
	,textLastName
	,textContact
	,textSSN
	,textEmail
	,textAddressLine1
	,textAddressLine2
	,textCity
	,textUsername
	,textPassword;

	@FXML
	private ComboBox<String> comboGender = new ComboBox();

	@FXML
	private DatePicker dpDOB;

	@FXML
	private ComboBox<String> comboState = new ComboBox();

	@FXML
	private Button btnReset;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnExit;
	@FXML 
	private Alert alert;

	private ResultSet resultSet = null;
	private List<String> screenData = new ArrayList<>();
	private LoginDatabaseOperations loginDatabaseOperations;
	private User user;

	public void resetFields(){
		textFirstName.setText("");
		textMiddleName.setText("");
		textLastName.setText("");
		textContact.setText("");
		textSSN.setText("");
		textEmail.setText("");
		textAddressLine1.setText("");
		textAddressLine2.setText("");
		textCity.setText("");
		textUsername.setText("");
		textPassword.setText("");
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();		
		user = new User();
		List<String> list = new ArrayList<String>();
		list.add("Alabama");
		list.add("Alaska");
		list.add("Arizona");
		list.add("Arkansas");
		list.add("California");
		list.add("Colorado");
		list.add("Connecticut");
		list.add("Delaware");
		list.add("Florida");
		list.add("Georgia");
		list.add("Hawaii");
		list.add("Idaho");
		list.add("Illinois");
		list.add("Indiana");
		list.add("Iowa");
		list.add("Kansas");
		list.add("Kentucky");
		list.add("Louisiana");
		list.add("Maine");
		list.add("Maryland");
		list.add("Massachusetts");
		list.add("Michigan");
		list.add("Minnesota");
		list.add("Mississippi");
		list.add("Missouri");

		ObservableList<String> obList = FXCollections.observableList(list);
		comboState.getItems().clear();
		comboState.setItems(obList);

		List<String> list2 = new ArrayList<String>();
		list2.add("Male");
		list2.add("Female");

		ObservableList<String> obList2 = FXCollections.observableList(list2);
		comboGender.getItems().clear();
		comboGender.setItems(obList2);

		textFirstName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					textFirstName.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					textFirstName.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

		textMiddleName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					textMiddleName.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					textMiddleName.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}

		});

		textLastName.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					textLastName.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					textLastName.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});

		textContact.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					textContact.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		textContact.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if (newValue.intValue() > oldValue.intValue()) {
					if (textContact.getText().length() >= 10) {
						textContact.setText(textContact.getText().substring(0, 10));
					}
				}
			}

		});
		textSSN.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					textSSN.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}

		});
		textSSN.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				if (newValue.intValue() > oldValue.intValue()) {
					if (textSSN.getText().length() >= 9) {
						textSSN.setText(textSSN.getText().substring(0, 9));
					}
				}
			}
		});
		textCity.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					textCity.setText(newValue.replaceAll("[^\\D]", ""));
				}
				else if(!newValue.matches("[^~`@#$()!,./|%&*-_=+<>?'\";:]*")){
					textLastName.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
		
		textUsername.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("[^~`@#$\\s()!,./|%&*-_=+<>?'\";:]*")){
					textUsername.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}	
	public void saveData(ActionEvent event){
		try{
			if(checkForBlank()){
				user.setFirstName(textFirstName.getText().toString());
				user.setMiddleName(textMiddleName.getText().toString());
				user.setLastName(textLastName.getText().toString());
				user.setGender(comboGender.getValue());
				user.setState(comboState.getValue());
				user.setSSN(textSSN.getText().toString());
				user.setContact(textContact.getText().toString());
				user.setCity(textCity.getText().toString());
				user.setAddressLine1(textAddressLine1.getText().toString());
				user.setAddressLine2(textAddressLine2.getText().toString());
				user.setEmail(textEmail.getText().toString());
				user.setUsername(textUsername.getText().toString());
				user.setPassword(textPassword.getText().toString());
				user.setDateofbirth(dpDOB.getValue().toString());
				if(checkAllRecords(user)){
					if(emailValid(user.getEmail()))
					{
						int accountNumber = loginDatabaseOperations.saveData(user);

						System.out.println(" Account Created... Account Number : "+accountNumber);
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Account Created");
						if(user.getGender().equalsIgnoreCase("Male"))
							alert.setContentText("Account Created for Mr. "+ textLastName.getText()+ ". Please note down account number " +accountNumber+ " for future reference. Use OTP sent via email to login." );
						else if(user.getGender().equalsIgnoreCase("Female"))
							alert.setContentText("Account Created for Miss "+ textLastName.getText()+ ". Please note down account number " +accountNumber+ " for future reference. Use OTP sent via email to login." );

						Optional<ButtonType> result = alert.showAndWait();

						if(result.get() == ButtonType.OK){
							ExitForm();
						}
						ExitForm();
					}
					else{
						alert = new Alert(AlertType.CONFIRMATION);
						alert.setTitle("Create Account");
						alert.setContentText("Email Address Invalid. Please enter valid email address.");
						Optional<ButtonType> result = alert.showAndWait();
					}
				}
				else{
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Create Account");
					alert.setContentText("Duplicate Record found Or Field is Blank. Cannot Save Data.");
					Optional<ButtonType> result = alert.showAndWait();
				}
			
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void ExitForm(){
		Stage stage = (Stage) btnExit.getScene().getWindow();		
		stage.close();
	}

	public Boolean checkAllRecords(User user){
		List<User> tempList = new ArrayList<>();
		try{
			resultSet = loginDatabaseOperations.selectAllUsers();
			while(resultSet.next()){
				User tempUser = new User();
				tempUser.setSSN(resultSet.getString(3));
				tempUser.setUsername(resultSet.getString(2));
				tempUser.setContact(resultSet.getString(1));
				tempList.add(tempUser);
			}

			for(User u : tempList){
				if(user.getSSN().equals(u.getSSN())){
					return false;
				} 
				else if(user.getUsername().equals(u.getUsername())){
					return false;
				}
				else if(user.getContact().equals(u.getContact())){
					return false;
				}
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
		return true;
	}

	public Boolean checkForBlank(){
		if(textFirstName.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("First Name cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textFirstName.requestFocus();
			return false;
		}
		else if(textLastName.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Last Name cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textLastName.requestFocus();
			return false;
		}
		else if(comboGender.getValue()==null)
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Gender cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			comboGender.requestFocus();
			return false;
		}
		else if(comboState.getValue()==null)
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("State cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			comboState.requestFocus();
			return false;
		}
		else if(textSSN.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("SSN cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textSSN.requestFocus();
			return false;
		}
		else if(textCity.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("City cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textCity.requestFocus();
			return false;
		}
		else if(textAddressLine1.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Address Line 1 cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textAddressLine1.requestFocus();
			return false;
		}
		else if(textEmail.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Email cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textEmail.requestFocus();
			return false;
		}
		else if(textPassword.getText().toString().equals("")){
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Password cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			textPassword.requestFocus();
			return false;
		}
		else if(dpDOB.getValue()==null)
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Date of birth cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			dpDOB.requestFocus();
			return false;
		}
		else if(textContact.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Contact cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			dpDOB.requestFocus();
			return false;
		}
		else if(textUsername.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Username cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			dpDOB.requestFocus();
			return false;
		}

		else if(textSSN.getText().length() < 9){
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("SSN cannot be less than 9");
			Optional<ButtonType> result = alert.showAndWait();

			textSSN.requestFocus();
			return false;
		}
		else
			return true;
	}
}
