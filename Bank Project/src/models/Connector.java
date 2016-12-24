package models;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class Connector {

	public static Connection conn =  null;

	public Connection getConn() {
		return conn;
	}

	public Connector(){
		try {
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://www.papademas.net:3306/fp","dbfp","510"); //DB_URL,Username,Password
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
