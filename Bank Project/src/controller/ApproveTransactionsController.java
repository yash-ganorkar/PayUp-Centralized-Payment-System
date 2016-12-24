package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.LoginDatabaseOperations;

public class ApproveTransactionsController implements Initializable {

	@FXML
	private Label labelWelcomeMessage,labelAccountBalanceAmount;
	@FXML
	private MenuItem menuAddCurrencies,approvePayUpTransactions;
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
	private Stage stage;
	@FXML
	private Parent root;

	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	private List<Transaction> transactionList;
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
			labelWelcomeMessage.setText("Welcome, Admin");
			fetchAllTransactions();
	}
	private void fetchAllTransactions() {
		// TODO Auto-generated method stub
		try {
			resultSet = loginDatabaseOperations.selectAllTransactions();
			transactionList = new ArrayList<>();
			transactionObservableList = FXCollections.observableArrayList();
			Transaction transact;
			while(resultSet.next()){
				/*				currencies.add(0,resultSet.getString(1));
				currencies.add(1,resultSet.getString(2));
				cardList.add(2,resultSet.getString(3));
				cardList.add(3,resultSet.getString(4));
				 */			
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
		tableView.setItems(transactionObservableList);
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
