package models;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSending {
	
	public void sendEmailUsingSSL(String emailAddress, String Name, Integer OTP){
		Properties smtpServerProperties = new Properties();
		
		smtpServerProperties.put("mail.smtp.host", "smtp.gmail.com");
		smtpServerProperties.put("mail.smtp.socketFactory.port", "465");
		smtpServerProperties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		smtpServerProperties.put("mail.smtp.auth", "true");
		smtpServerProperties.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(smtpServerProperties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("payupsystem@gmail.com","payupsystem12345");
					}
				});
		
		try {

			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("payupsystem@gmail.com"));
			mimeMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(emailAddress));
			mimeMessage.setSubject("Welcome Message");
			mimeMessage.setText("Dear " + Name +
					"\n\n PayUp Team welcomes you. For security reasons, please keep your username and password secret."
					+ " \n\n You will be required to enter OTP as " + OTP +". "
							+ " \n\n For other queries please call our support desk at "
							+ " +1 (312)383-8806. ");

			Transport.send(mimeMessage);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public Integer GenerateOTP() {
		Random rand = new Random();
		String id = String.format("%04d", rand.nextInt(10000));
		return Integer.parseInt(id);
	}

	public boolean emailValid(String email){
		//String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		 
		Pattern pattern = Pattern.compile(regex);
//		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		
		if(matcher.matches())
			return true;
		else
			return false;
	}

}
