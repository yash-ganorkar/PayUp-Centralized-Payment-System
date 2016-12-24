package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.naming.spi.ResolveResult;

import datamodels.Currency;
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
import javafx.stage.Stage;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class AddCurrenciesController implements Initializable, IActionMethods {

	@FXML
	private TextField txtCcy1,txtCcy2,txtBidRate,txtAskRate;
	@FXML
	private Label labelWelcomeMessage;
	@FXML
	private MenuItem menuAddCurrencies,approvePayUpTransactions;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private Button btnAdd, btnReset;
	@FXML 
	private Alert alert;

	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	
public void resetFields(){
	txtCcy1.setText("");
	txtCcy2.setText("");
	txtBidRate.setText("");
	txtAskRate.setText("");
}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginDatabaseOperations = new LoginDatabaseOperations();

		txtCcy1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					txtCcy1.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}

		});

		txtCcy2.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				if(!newValue.matches("\\D*")){
					txtCcy1.setText(newValue.replaceAll("[^\\D]", ""));
				}
			}

		});
	}

	@FXML
	public void handleButtonAction(ActionEvent event) throws IOException, SQLException{
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

	public void saveData(){
		int count=0;
		try{
if(checkForBlank())
{
			Currency currency = new Currency("", txtCcy1.getText().toString() + " - " + txtCcy2.getText().toString(), txtBidRate.getText().toString(), txtAskRate.getText().toString());
			resultSet = loginDatabaseOperations.selectParticularCurrencies(currency);
			while(resultSet.next()){
				count = resultSet.getInt(1);
			}
			if(count > 0)
			{
				new Alert(Alert.AlertType.ERROR, "Cannot Add Currency. Duplicate Currency Present").showAndWait();
			}
			else
			{
				int response = loginDatabaseOperations.insertCurrency(currency);
				if(response == 1)
				{
					new Alert(Alert.AlertType.CONFIRMATION, "Currency Added Successfully..").showAndWait();

				}
			}
}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Override
	public Boolean checkForBlank() {
		// TODO Auto-generated method stub
		if(txtCcy1.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Currency 1 cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtCcy1.requestFocus();
			return false;
		}
		else if(txtCcy2.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Currency 2 cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtCcy2.requestFocus();
			return false;
		}
		else if(txtBidRate.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Bid Rate cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtCcy2.requestFocus();
			return false;
		}

		else if(txtAskRate.getText().toString().equals(""))
		{
			alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Create Account");
			alert.setContentText("Ask Rate cannot be blank.");
			Optional<ButtonType> result = alert.showAndWait();

			txtCcy2.requestFocus();
			return false;
		}

		else
		return true;
	}
	@Override
	public void ExitForm(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
	}
}