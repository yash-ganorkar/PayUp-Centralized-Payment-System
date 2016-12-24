package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import datamodels.AccountHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.IActionMethods;
import models.LoginDatabaseOperations;

public class AccountHolderController implements Initializable,IActionMethods {

	@FXML
	private Label labelWelcomeMessage,labelAccountBalanceAmount;
	@FXML
	private MenuItem rechargeAccountBalance,menuForexAccount, menuProfile,viewPayUpTransactions,payFriend,linkCreditCards,unlinkCreditCards,addPayee,removePayee,addVirtualCard,addVirtualCash;
	@FXML
	private Stage stage;
	@FXML
	private Parent root;
	@FXML
	private PieChart pieChart = new PieChart();
	@FXML
	private Button btnExit;
	
	private ResultSet resultSet = null;
	private LoginDatabaseOperations loginDatabaseOperations;
	private HashMap<String, Double> chartData;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginDatabaseOperations = new LoginDatabaseOperations();
		
	}
	public void setDataToDataModels(){
		chartData = new HashMap();
		try{
			resultSet = loginDatabaseOperations.selectParticularRecord(AccountHolder.getAccountNumber());
			
			while(resultSet.next()){
				AccountHolder.setAccountHolderName(resultSet.getString(1) + " " +resultSet.getString(2));
				AccountHolder.setAccountBalance((resultSet.getDouble(3)));
			}
			labelWelcomeMessage.setText("Welcome, "+AccountHolder.getAccountHolderName());
			labelAccountBalanceAmount.setText(String.valueOf(AccountHolder.getAccountBalance()));

			pieChart.setData(loadPieChart());
			pieChart.setTitle("Number of Transactions");
			pieChart.setLegendSide(Side.LEFT);
			pieChart.setClockwise(false);
			pieChart.setLabelsVisible(false);

			final Label caption = new Label("");
			caption.setTextFill(Color.DARKORANGE);
			caption.setStyle("-fx-font: 24 arial;");

			for (final PieChart.Data data : pieChart.getData()) {
				data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
						new EventHandler<MouseEvent>() {
					@Override public void handle(MouseEvent e) {
						caption.setTranslateX(e.getSceneX());
						caption.setTranslateY(e.getSceneY());
						caption.setText(String.valueOf(data.getPieValue()));
					}
				});
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public void initData(String username){
		try {
			resultSet = loginDatabaseOperations.getAccountNumber(username.trim());
			
			while(resultSet.next()){
				AccountHolder.setAccountNumber(resultSet.getLong(1));
			}
			setDataToDataModels();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private ObservableList<Data> loadPieChart() throws SQLException {
		// TODO Auto-generated method stub

		resultSet = loginDatabaseOperations.selectDistinctTransactionType(AccountHolder.getAccountNumber());
		while(resultSet.next()){
			chartData.put(resultSet.getString(2),resultSet.getDouble(1));
		}
		ObservableList<Data> answer = FXCollections.observableArrayList();
		
		Iterator it =  chartData.entrySet().iterator();
		while(it.hasNext()){
			
			Map.Entry<String, Double> pair = (Map.Entry) it.next();
			
			answer.add(new PieChart.Data(pair.getKey(),pair.getValue().doubleValue()));
			
		}
		
		
		
		if(answer.size() == 0)
		{
		answer.addAll(
				new PieChart.Data("FOREX", 0)
				,new PieChart.Data("RECHARGE", 0)
				,new PieChart.Data("TRANSFER", 0)
				);
		}
		return answer;
	}

	public void ExitForm(){
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}
	
	@Override
	public void handleButtonAction(ActionEvent event) throws IOException, SQLException {
		// TODO Auto-generated method stub
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
	@Override
	public Boolean checkForBlank() {
		// TODO Auto-generated method stub
		//do nothing
		return null;
	}
	@Override
	public void ExitForm(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		//do nothing
	}
}
