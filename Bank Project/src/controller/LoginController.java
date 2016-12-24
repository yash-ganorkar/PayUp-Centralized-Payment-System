package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.EmailSending;
import models.LoginDatabaseOperations;

public class LoginController extends EmailSending implements Initializable {

	@FXML
	private Label lblStatus;
	@FXML
	private TextField txtUsername,txtPassword;
	@FXML 
	private Alert alert;

	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	private static int no_of_attempts = 3;


	public void Login(ActionEvent event) {
		String password = "", name = "", email = "";
		char active_yn = 'N';
		int returnCode;
		if(checkForBlank()){
			try {
				resultSet = loginDatabaseOperations.selectData(txtUsername.getText().toString().trim());

				while(resultSet.next()){
					name = resultSet.getString(1) + " " + resultSet.getString(2);
					email = resultSet.getString(3);
					password = resultSet.getString(4);
					active_yn = resultSet.getString(5).charAt(0);
				}
				if(active_yn == 'Y' && !(txtUsername.getText().equals("admin"))){
					if(password.equals(LoginDatabaseOperations.getMD5(txtPassword.getText()))){

						Integer OTP = GenerateOTP();
						sendEmailUsingSSL(email, name, OTP);
						loginDatabaseOperations.updateLoginTableWithOTP(OTP, txtUsername.getText());

						new Alert(Alert.AlertType.CONFIRMATION, "Login Successful. Enter OTP sent to registered email address.").showAndWait();			 
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/OTP.fxml"));
						Parent root1 = (Parent) fxmlLoader.load();
						Stage stage = new Stage();
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.initStyle(StageStyle.UNDECORATED);
						stage.setScene(new Scene(root1));

						OTPController controller = fxmlLoader.<OTPController>getController();
						controller.initData(txtUsername.getText());
						stage.show();
					}
					else{
						--no_of_attempts;
						if(no_of_attempts > 0){
							new Alert(Alert.AlertType.ERROR, "Login Unsuccessful. You have " +no_of_attempts +" login attempts left.").showAndWait();
							txtPassword.setText("");
							txtPassword.requestFocus();
						}
						else
						{
							new Alert(Alert.AlertType.ERROR, "Your account is blocked. Please contact the bank representative.").showAndWait();
							returnCode = loginDatabaseOperations.updateLoginTableAfterMaxAttempts(txtUsername.getText().toString().trim());
						}
					}
				}
				else if(active_yn == 'Y' && (txtUsername.getText().equals("admin"))){
					if(password.equals(LoginDatabaseOperations.getMD5(txtPassword.getText()))){
						new Alert(Alert.AlertType.CONFIRMATION, "Login Successful...").showAndWait();			 
						FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AdminConsole.fxml"));
						Parent root1 = (Parent) fxmlLoader.load();
						Stage stage = new Stage();
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.initStyle(StageStyle.UNDECORATED);
						stage.setScene(new Scene(root1));

						AdminController controller = fxmlLoader.<AdminController>getController();
						controller.initData();
						stage.show();
					}
					else{
						--no_of_attempts;
						if(no_of_attempts > 0){
							new Alert(Alert.AlertType.ERROR, "Login Unsuccessful. You have " +no_of_attempts +" login attempts left.").showAndWait();
							txtPassword.setText("");
							txtPassword.requestFocus();
						}
						else
						{
							new Alert(Alert.AlertType.ERROR, "Your account is blocked. Please contact the bank representative.").showAndWait();
							returnCode = loginDatabaseOperations.updateLoginTableAfterMaxAttempts(txtUsername.getText().toString().trim());
						}
					}

				}
				else{
					new Alert(Alert.AlertType.ERROR, "Your account is blocked or you do not have online access. Please contact the bank representative.").showAndWait();
					System.exit(0);
				}
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			finally {
				loginDatabaseOperations.closeConnection();
			}
		}
		else{
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login");
			alert.setContentText("Field is Blank. Cannot Login.");
			Optional<ButtonType> result = alert.showAndWait();				
		}
	}
	public void CreateAccount(ActionEvent event) {
		// TODO Auto-generated method stub
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/CreateAccount.fxml"));
			Parent root1;
			root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("ABC");
			stage.setScene(new Scene(root1));  
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();
	}

	public Boolean checkForBlank(){
		if(txtUsername.getText().toString().equals(""))
			return false;
		else if(txtPassword.getText().toString().equals(""))
			return false;
		else
			return true;
	}

}