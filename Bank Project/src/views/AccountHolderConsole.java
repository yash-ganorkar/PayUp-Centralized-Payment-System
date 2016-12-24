package views;

import java.io.IOException;

import controller.AccountHolderController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AccountHolderConsole extends Application {
	@FXML
	private Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		try {
/*			BorderPane root = new BorderPane();*/			
			
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/AccountHolderConsole.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}