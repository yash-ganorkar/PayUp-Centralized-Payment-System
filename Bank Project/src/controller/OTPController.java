package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert.AlertType;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class OTPController implements Initializable, IActionMethods{

	@FXML
	private PasswordField txtOTP;

	@FXML
	private Button btnVerify;
	@FXML 
	private Alert alert;

	private LoginDatabaseOperations loginDatabaseOperations;
	private ResultSet resultSet = null;

	private String username;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();
		
		txtOTP.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\d*")){
					txtOTP.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
			
		});
	}

	public void verifyOTP(){
		Integer OTP = null;
		if(checkForBlank()){
		try{
			resultSet = loginDatabaseOperations.selectOTP(username);
			while(resultSet.next()){
				OTP = resultSet.getInt(1);
			}

			if(OTP == Integer.parseInt(txtOTP.getText()))
			{
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("OTP Verified");
				alert.setContentText("OTP matched with system. Logging you into system.");
				Optional<ButtonType> result = alert.showAndWait();

				if(result.get() == ButtonType.OK){
					if(username.equals("admin")){
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AdminConsole.fxml"));
						Parent root1 = (Parent) fxmlLoader.load();
						Stage stage = new Stage();
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.initStyle(StageStyle.UNDECORATED);
						stage.setTitle("Account Console");
						stage.setScene(new Scene(root1));

						AdminController controller = fxmlLoader.<AdminController>getController();
						controller.initData();
						stage.show();
					}
					else{
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AccountHolderConsole.fxml"));
						Parent root1 = (Parent) fxmlLoader.load();
						Stage stage = new Stage();
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.initStyle(StageStyle.UNDECORATED);
						stage.setTitle("Account Console");
						stage.setScene(new Scene(root1));

						AccountHolderController controller = fxmlLoader.<AccountHolderController>getController();
						controller.initData(username);
						stage.show();

					}
					ExitForm();

				}	
			}
			else
			{
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("OTP Verification failed.");
				alert.setContentText("OTP did not match with system. Please enter valid OTP.");
				Optional<ButtonType> result1 = alert.showAndWait();
				ExitForm();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		}else{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("OTP Verification");
			alert.setContentText("Fields cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();
		}
	}

	public void initData(String uname) {
		// TODO Auto-generated method stub
		username = uname;
	}
	
	public void ExitForm(){
		Stage stage = (Stage) btnVerify.getScene().getWindow();
		
		stage.close();
	}
	public Boolean checkForBlank(){
		if(txtOTP.getText().toString().equals(""))
			return false;
		else
			return true;
	}
	@Override
	public void handleButtonAction(ActionEvent event) throws IOException, SQLException {
		// TODO Auto-generated method stub
		//do nothing
	}
	@Override
	public void ExitForm(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		//do nothing
	}
}