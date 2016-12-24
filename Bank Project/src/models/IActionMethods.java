package models;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public interface IActionMethods {
	
	@FXML
	public void handleButtonAction(ActionEvent event) throws IOException, SQLException;
	
	public Boolean checkForBlank();
	
	@FXML
	public void ExitForm(ActionEvent event) throws IOException;

}
