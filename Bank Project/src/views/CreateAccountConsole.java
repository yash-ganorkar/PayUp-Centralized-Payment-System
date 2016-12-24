package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CreateAccountConsole extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
/*			BorderPane root = new BorderPane();*/			
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateAccountController.fxml"));
			Parent root = loader.load();
			
			/*CreateAccountController createAccountController = loader.getController();*/
			
			Scene scene = new Scene(root);
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