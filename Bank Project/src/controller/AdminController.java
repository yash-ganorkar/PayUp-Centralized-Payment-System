package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import models.LoginDatabaseOperations;

public class AdminController implements Initializable {
	@FXML
	private Label labelWelcomeMessage,labelAccountBalanceAmount;
	@FXML
	private MenuItem menuAddCurrencies,approvePayUpTransactions;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;

	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginDatabaseOperations = new LoginDatabaseOperations();
	}
	
	public void initData(){
			labelWelcomeMessage.setText("Welcome, Admin");
	}
	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException, SQLException{
		if(event.getSource()==menuAddCurrencies){
			//get reference to the button's stage         
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			//load up OTHER FXML document
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/AddTradableCurrencies.fxml"));
			root = fxmlLoader.load();
			AddCurrenciesController controller = fxmlLoader.<AddCurrenciesController>getController();		
		}
		else if(event.getSource()==approvePayUpTransactions){
			stage=(Stage) labelWelcomeMessage.getScene().getWindow();
			//load up OTHER FXML document
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PayUpTransactions.fxml"));
			root = fxmlLoader.load();
			ApproveTransactionsController controller = fxmlLoader.<ApproveTransactionsController>getController();
			controller.initData();
		}
		//create a new scene with root and set the stage
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}