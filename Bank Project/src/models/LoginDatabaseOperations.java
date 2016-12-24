package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import datamodels.Currency;
import datamodels.ForexTransaction;
import datamodels.Payee;
import datamodels.Transaction;
import datamodels.User;
import datamodels.VirtualCard;

public class LoginDatabaseOperations {

	private int returnCode = -1;
	private PreparedStatement preparedStatement;
	private	ResultSet resultSet = null;
	private StringBuilder queryBuilder = new StringBuilder();
	private Connection conn;
	private Connector connection;
	private Boolean returnType;

	public LoginDatabaseOperations() {
		connection = new Connector();
	}
	
	public ResultSet selectData(String username){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select payup_account.firstname, payup_account.lastname, payup_account.email, payup_login.password, payup_login.active_yn from payup_login,payup_account ");
				queryBuilder.append(" where payup_account.username = payup_login.username and payup_login.username = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, username);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public ResultSet selectParticularRecord(Long accountNumber){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select FirstName,LastName,accountbalance from payup_account");
				queryBuilder.append(" where AccountNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNumber);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public ResultSet getAccountNumber(String username){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select AccountNo from payup_account");
				queryBuilder.append(" where username = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, username);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public int updateLoginTableAfterMaxAttempts(String username){
		try{
			returnCode = -1;
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" update payup_LOGIN ");
				queryBuilder.append(" set active_yn = 'N', updated_by = 'sys_admin', updated_at = (SELECT CURRENT_TIMESTAMP)");
				queryBuilder.append(" where username = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, username);

				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}
	
	public int updateLoginTableToActivate(String username){
		try{
			returnCode = -1;
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" update payup_LOGIN ");
				queryBuilder.append(" set active_yn = 'Y', updated_by = 'admin', updated_at = (SELECT CURRENT_TIMESTAMP)");
				queryBuilder.append(" where username = ?");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, username);
				returnCode = preparedStatement.executeUpdate(queryBuilder.toString());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}
	
	public int saveData(User user){
		try{
			returnCode = -1;
			conn = connection.getConn();
			resultSet = null;
			if(conn != null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_account (FirstName,MiddleName,LastName,Contact,SSN,Email,Address,Gender,State,Username,dateofbirth) values (?,?,?,?,?,?,?,?,?,?,?) ");
				preparedStatement = (PreparedStatement)conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, user.getFirstName());
				preparedStatement.setString(2, user.getMiddleName());
				preparedStatement.setString(3, user.getLastName());
				preparedStatement.setString(4, user.getContact());
				preparedStatement.setString(5, user.getSSN());
				preparedStatement.setString(6, user.getEmail());
				preparedStatement.setString(7, user.getAddressLine1()+ ", "+user.getAddressLine2()+", " + user.getCity());
				preparedStatement.setString(8, user.getGender());
				preparedStatement.setString(9, user.getState());
				preparedStatement.setString(10, user.getUsername());
				preparedStatement.setString(11, user.getDateofbirth());

				preparedStatement.execute();
				preparedStatement.close();
				queryBuilder = new StringBuilder();

				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
			    Date now = new Date();
			    String strDate = sdfDate.format(now);

				queryBuilder.append("INSERT INTO payup_LOGIN (username,password,no_of_attempts,created_at,created_by,updated_by,updated_at,active_yn,otp) values (?,?,?,?,?,?,?,?,?) ");
				preparedStatement = (PreparedStatement)conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, user.getUsername());
				preparedStatement.setString(2, getMD5(user.getPassword()));
				preparedStatement.setInt(3, 3);
				preparedStatement.setString(4, strDate);
				preparedStatement.setString(5, "admin");
				preparedStatement.setString(6, "admin");
				preparedStatement.setString(7, strDate);
				preparedStatement.setString(8, "Y");
				preparedStatement.setInt(9, 0000);
				preparedStatement.execute();
				preparedStatement.close();
				
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select AccountNo from payup_Account");
				queryBuilder.append(" where ssn = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, user.getSSN());
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()){
					returnCode = resultSet.getInt(1);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
		
	}
	public static String getMD5(String input) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] message = messageDigest.digest(input.getBytes());
			BigInteger number = new BigInteger(1, message);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	public int insertCreditCard(List<String> creditCard){
		returnCode = 0;
		try{		
			conn = connection.getConn();
			resultSet = null;
			if(conn != null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_carddetails (creditcardnumber,cvvnumber,nameoncard,expiry,alias,cardtype,accountno) values (?,?,?,?,?,?,?) ");
				preparedStatement = (PreparedStatement)conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, Long.parseLong(creditCard.get(0)));
				preparedStatement.setString(2,  creditCard.get(1));
				preparedStatement.setString(3, creditCard.get(2));
				preparedStatement.setString(4, creditCard.get(3));
				preparedStatement.setString(5, creditCard.get(4));
				preparedStatement.setString(6, creditCard.get(5));
				preparedStatement.setInt(7, Integer.parseInt(creditCard.get(6)));

				returnCode = preparedStatement.executeUpdate();
				preparedStatement.close();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}
	
	public ResultSet selectAllCards(Long accountNo){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select creditcardnumber,alias,cardtype,expiry,cvvnumber,accountno from payup_carddetails");
				queryBuilder.append(" where accountno = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNo);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public int insertPayee(Payee payee){
		returnCode = 0;
		try{		
			conn = connection.getConn();
			resultSet = null;
			if(conn != null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_payee (PayeeName,PayeeAccountNo,PayeeNickName,PayeeEmailAddress,PayerAccountNo) values (?,?,?,?,?) ");
				preparedStatement = (PreparedStatement)conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, payee.getPayeeName());
				preparedStatement.setLong(2,  Long.parseLong(payee.getPayeeAccountNo()));
				preparedStatement.setString(3, payee.getPayeeAlias());
				preparedStatement.setString(4, payee.getPayeeContactNo());
				preparedStatement.setLong(5, Long.parseLong(payee.getPayerAccountNo()));

				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}
	
	public ResultSet selectAllCurrencies(){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select CcyPairID,CcyPair,CcyPairBidRate,CcyPairAskRate from payup_rates");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public ResultSet selectAllPayees(Long accountNo){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select PayeeId,PayeeName,PayeeAccountNo,PayeeEmailAddress from payup_payee where PayerAccountNo = ?");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNo);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public void deletePayeeRecord(Long payeeID){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" delete from payup_payee where PayeeID = ?");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, payeeID);
				preparedStatement.execute();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public ResultSet getNewVirtualCardNumber(Long accountNo)
	{
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" SELECT FLOOR(RAND() * 10000) AS VIRTUALCARDNUMBER1,FLOOR(RAND() * 10000) AS VIRTUALCARDNUMBER2,FLOOR(RAND() * 10000) AS VIRTUALCARDNUMBER3,FLOOR(RAND() * 10000) AS VIRTUALCARDNUMBER4, FLOOR(RAND() * 1000) AS CVV, FirstName from payup_account ");
				queryBuilder.append(" where accountNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNo);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultSet;
	}
	public ResultSet getVirtualCardNumber(Long accountNo){
		try{
			conn = connection.getConn();
			if(conn!= null){
				
				queryBuilder = new StringBuilder();
				queryBuilder.append(" SELECT count(*) as Count,virtualcard1,virtualcard2,virtualcard3,virtualcard4,cvv,FirstName from payup_virtualcards ");
				queryBuilder.append(" where AccNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNo);
				resultSet = preparedStatement.executeQuery();					
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultSet;
	}
	
	public ResultSet getVirtualCardYN(Long accountNo){
		try{
			conn = connection.getConn();
			if(conn!= null){
				
				queryBuilder = new StringBuilder();
				queryBuilder.append(" SELECT virtualcardyn from payup_account ");
				queryBuilder.append(" where AccountNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNo);
				resultSet = preparedStatement.executeQuery();					
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultSet;
	}
	
	public int updateVirtualCardYN(Long accountNo, String virtualcardyn){
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				
				queryBuilder = new StringBuilder();
				queryBuilder.append(" update payup_account set virtualcardyn = ?  ");
				queryBuilder.append(" where AccountNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, virtualcardyn);
				preparedStatement.setLong(2, accountNo);
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}
	public int insertVirtualCardNumber(VirtualCard vCard){
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_virtualcards (virtualcard1,virtualcard2,virtualcard3,virtualcard4,cvv,expirymonth,expiryyear,FirstName,AccNo) VALUES (?,?,?,?,?,?,?,?,?)");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, vCard.getNumber1());
				preparedStatement.setLong(2, vCard.getNumber2());
				preparedStatement.setLong(3, vCard.getNumber3());
				preparedStatement.setLong(4, vCard.getNumber4());
				preparedStatement.setLong(5, vCard.getCvv());
				preparedStatement.setString(6, vCard.getMonth());
				preparedStatement.setString(7, vCard.getYear());
				preparedStatement.setString(8, vCard.getFirstName());
				preparedStatement.setLong(9, vCard.getAccountNumber());
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}

	public int insertVirtualCardTransaction(Transaction transaction){
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_transactions (transactionfrom,transactionto,fromaccountno,toaccountno,transactionamount,transactioncomment,transactiontype,transactionstatus, Account_Number) VALUES (?,?,?,?,?,?,?,?,?)");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, transaction.getTransactionFrom());
				preparedStatement.setString(2, transaction.getTransactionTo());
				preparedStatement.setString(3, transaction.getFromAccountNo());
				preparedStatement.setString(4, transaction.getToAccountNo());
				preparedStatement.setDouble(5, transaction.getTransactionAmount());
				preparedStatement.setString(6, transaction.getTransactionComment());
				preparedStatement.setString(7, transaction.getTransactionType());
				preparedStatement.setString(8, transaction.getTransactionStatus());
				preparedStatement.setLong(9, transaction.getAccountNumber());
				returnCode = preparedStatement.executeUpdate();			
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	public int updateAccountBalance(Double accountBalance, Long accountNo){
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("UPDATE payup_account ");
				queryBuilder.append(" SET accountbalance = ? ");
				queryBuilder.append(" where accountNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setDouble(1, accountBalance);
				preparedStatement.setLong(2, accountNo);
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	public int updateVirtualCardBalance(Double accountBalance, Long accountNo){
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("UPDATE payup_virtualcards ");
				queryBuilder.append(" SET balance = ? ");
				queryBuilder.append(" where AccNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setDouble(1, accountBalance);
				preparedStatement.setLong(2, accountNo);
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}
	
	public int insertForexTransaction(ForexTransaction transaction){
		try{
			returnCode = -1;
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_forextransactions (ccy1,ccy2,direction,ccy1amt,ccy2amt,exchangerate,UserAccNo) VALUES (?,?,?,?,?,?,?)");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, transaction.getCcy1());
				preparedStatement.setString(2, transaction.getCcy2());
				preparedStatement.setString(3, transaction.getDirection());
				preparedStatement.setDouble(4, transaction.getCcy1amt());
				preparedStatement.setDouble(5, transaction.getCcy2amt());
				preparedStatement.setDouble(6, transaction.getExchangerate());
				preparedStatement.setLong(7, transaction.getAccountNo());
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;
	}
	
	public int insertCurrency(Currency currency){
		try{
			returnCode = -1;
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("INSERT INTO payup_rates (CcyPair,CcyPairBidRate,CcyPairAskRate) VALUES (?,?,?)");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, currency.getCurrencyPair());
				preparedStatement.setDouble(2, Double.parseDouble(currency.getCurrencyBuyRate()));
				preparedStatement.setDouble(3, Double.parseDouble(currency.getCurrencySellRate()));
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}

	public ResultSet selectParticularCurrencies(Currency currency){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select count(CcyPair) as presentYN  from payup_rates where CcyPair = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, currency.getCurrencyPair());
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public int updateLoginTableWithOTP(Integer oTP, String username) {
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("UPDATE payup_login ");
				queryBuilder.append(" SET OTP = ? ");
				queryBuilder.append(" where username = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setDouble(1, oTP);
				preparedStatement.setString(2, username);
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return returnCode;		
	}
	
	public ResultSet selectOTP(String username) {
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("select OTP ");
				queryBuilder.append(" from payup_login ");
				queryBuilder.append(" where username = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, username);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultSet;		
	}

	public ResultSet selectBalance(Long accountNumber) {
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("select accountbalance ");
				queryBuilder.append(" from payup_account ");
				queryBuilder.append(" where AccountNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNumber);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return resultSet;		
	}
	
	public ResultSet selectAllTransactions(){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select transactionid,fromaccountno,toaccountno,transactionamount,transactiontype,transactionstatus from payup_transactions");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public int deleteVirtualCard(Long accountNumber){
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" delete from payup_virtualcards where AccNo = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNumber);
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;
	}

	public ResultSet getAccountProfile(Long accountNumber){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select FirstName,MiddleName,LastName,Contact,SSN,Email,Address,Gender,State,dateofbirth from payup_account");
				queryBuilder.append(" where accountno = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNumber);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}

	public ResultSet selectTransactionsPerUser(Long accountNumber){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select transactionid,fromaccountno,toaccountno,transactionamount,transactiontype,transactionstatus from payup_transactions");
				queryBuilder.append(" where account_number = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNumber);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}

	public int updateRecords(Long accountNumber, User user) {
		returnCode = 0;
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append("UPDATE payup_account ");
				queryBuilder.append(" SET Contact = ?, Email = ?, Address = ?, State = ? ");
				queryBuilder.append(" where accountno = ? ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setString(1, user.getContact());
				preparedStatement.setString(2, user.getEmail());
				preparedStatement.setString(3, user.getAddressLine1() + "," + user.getAddressLine2()+ "," + user.getCity());
				preparedStatement.setString(4, user.getState());
				preparedStatement.setLong(5, accountNumber);
				returnCode = preparedStatement.executeUpdate();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnCode;		
	}

	public ResultSet selectDistinctTransactionType(Long accountNumber){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select COUNT(transactiontype),transactiontype from payup_transactions");
				queryBuilder.append(" where account_number = ?  group by transactiontype ");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNumber);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public ResultSet selectAllUsers(){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select contact,username,ssn from payup_account");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}
	
	public ResultSet selectAllPayee(){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select PayeeId,PayeeName,PayeeAccountNo,PayeeEmailAddress from payup_payee");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}

	public ResultSet selectAllAccounts(Long accountNo){
		try{
			conn = connection.getConn();
			if(conn!= null){
				queryBuilder = new StringBuilder();
				queryBuilder.append(" select count(accountNo) from payup_account where accountNo = ?");
				preparedStatement = (PreparedStatement) conn.prepareStatement(queryBuilder.toString());
				preparedStatement.setLong(1, accountNo);
				resultSet = preparedStatement.executeQuery();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return resultSet;
	}

	
	public void closeConnection(){
		try{
			conn.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}