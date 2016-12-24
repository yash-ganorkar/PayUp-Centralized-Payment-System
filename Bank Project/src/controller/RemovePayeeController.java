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
import datamodels.Currency;
import datamodels.Payee;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.LoginDatabaseOperations;

public class RemovePayeeController implements Initializable{

	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private Label labelWelcomeMessage;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private ObservableList<Payee> payeeList;
	@FXML
	private TableColumn<Payee, String> payeeId;
	@FXML
	private TableColumn<Payee, String> payeeName;
	@FXML
	private TableColumn<Payee, String> payeeAccountNo;
	@FXML
	private TableColumn<Payee, String> payeeContactNo;
	@FXML
	private TableColumn<Payee, String> payeeAlias;
	@FXML
	private TableColumn<Payee, String> payerAccountNo;

	@FXML 
	private TableView<Payee> tableView;


	private LoginDatabaseOperations loginDatabaseOperations;
	private ResultSet resultSet;
	private Payee payee;
	private List<Payee> listPayees;


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
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		loginDatabaseOperations = new LoginDatabaseOperations();

		tableView.setRowFactory(new Callback<TableView<Payee>, TableRow<Payee>>() {

			@Override
			public TableRow<Payee> call(TableView<Payee> tv) {
				TableRow<Payee> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
						//	new Alert(Alert.AlertType.CONFIRMATION, "Account Created for Mr. "+ textLastName.getText()+ ". Please note down account number " +accountNumber+ " for future reference." ).showAndWait();
						payee = row.getItem();
						Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
						alert.setTitle("Confirm Remove selected Payee?");
						alert.setContentText("Do you want to remove "+ payee.getPayeeName() +" as a Payee?");

						Optional<ButtonType> result = alert.showAndWait();

						if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
							loginDatabaseOperations.deletePayeeRecord(Long.parseLong(payee.getPayeeID().toString()));
							parseUserList();
						}
					}
				});
				return row;
			}
		});
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
	public void parseUserList() {
		// TODO Auto-generated method stub		
		try {
			resultSet = loginDatabaseOperations.selectAllPayees(AccountHolder.getAccountNumber());
			listPayees = new ArrayList<>();
			payeeList = FXCollections.observableArrayList();
			Payee payee;
			while(resultSet.next()){
				payee =  new Payee(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),"","");				
				payeeList.add(payee);
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		payeeId.setCellValueFactory(new PropertyValueFactory<Payee,String>("payeeID"));
		payeeName.setCellValueFactory(new PropertyValueFactory<Payee,String>("payeeName"));
		payeeAccountNo.setCellValueFactory(new PropertyValueFactory<Payee,String>("payeeAccountNo"));
		payeeContactNo.setCellValueFactory(new PropertyValueFactory<Payee,String>("payeeContactNo"));
		payeeAlias.setCellValueFactory(new PropertyValueFactory<Payee,String>("payeeAlias"));
		payerAccountNo.setCellValueFactory(new PropertyValueFactory<Payee,String>("payerAccountNo"));
		tableView.setItems(payeeList);
	}

	public void initData(String string) {
		// TODO Auto-generated method stub
		labelWelcomeMessage.setText(string);
		parseUserList();
	}
}