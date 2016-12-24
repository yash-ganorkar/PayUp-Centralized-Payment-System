package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import datamodels.Currency;
import datamodels.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class ViewTransactionsController implements Initializable {

	@FXML
	private Label labelWelcomeMessage,labelAccountBalanceAmount;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML 
	private TableView<Transaction> tableView;
	@FXML
	private ObservableList<Transaction> transactionObservableList;
	@FXML
	private TableColumn<Transaction, String> transactionID;
	@FXML
	private TableColumn<Transaction, String> transactionFromNumber;
	@FXML
	private TableColumn<Transaction, String> transactionToNumber;
	@FXML
	private TableColumn<Transaction, String> transactionAmount;
	@FXML
	private TableColumn<Transaction, String> transactionType;
	@FXML
	private TableColumn<Transaction, String> transactionStatus;

	@FXML
	private Stage stage;
	@FXML
	private Parent root;

	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	private List<Transaction> transactionList;
	
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
		loginDatabaseOperations = new LoginDatabaseOperations();
		
		tableView.setRowFactory(new Callback<TableView<Transaction>, TableRow<Transaction>>() {

			@Override
			public TableRow<Transaction> call(TableView<Transaction> tv) {
				TableRow<Transaction> row = new TableRow<>();
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
/*						rowData = row.getItem();
						//System.out.println(rowData.getCurrencyPair());
						String ccyPair = rowData.getCurrencyPair();
						String currency[] = ccyPair.split(" - ");

						txtCcy1.setText(currency[0]);
						txtCcy2.setText(currency[1]);

*/						/*labelCcyDetails.setText("1 " +currency[0]+" = ");*/
					}
				});
				return row;
			}
		});

	}
	
	public void initData(){
			fetchAllTransactions();
	}
	private void fetchAllTransactions() {
		// TODO Auto-generated method stub
		try {
			resultSet = loginDatabaseOperations.selectTransactionsPerUser(AccountHolder.getAccountNumber());
			transactionList = new ArrayList<>();
			transactionObservableList = FXCollections.observableArrayList();
			Transaction transact;
			while(resultSet.next()){
				transact =  new Transaction(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6));				
				transactionObservableList.add(transact);
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transactionID.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactId"));
		transactionFromNumber.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactFromAccountNo"));
		transactionToNumber.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactToAccountNo"));
		transactionAmount.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactAmount"));
		transactionType.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactType"));
		transactionStatus.setCellValueFactory(new PropertyValueFactory<Transaction,String>("transactStatus"));
		tableView.setItems(transactionObservableList);
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
}
