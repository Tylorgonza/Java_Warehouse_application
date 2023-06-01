package billing_services;

import javax.mail.*;
import javax.mail.internet.*;

import java.text.MessageFormat;
import java.util.*;

public class Email {
	
	String senderEmail="";
	String senderPass="";
	final String emailServerPort="465";
	final String smtpServerPort="smtp.gmail.com";
	static String receiver=null;
	static String subjet;
	static String emailBody;
	static String invoice="";
	static String filePath="";

	
	Email(String sender, String senderPassword,String path){
		senderEmail=sender;
		senderPass=senderPassword;
		filePath=path;
		
	}

	
	public void sendEmail(String receiver, String subject, String body) {
		Email.receiver=receiver;
		Email.subjet=subject;
		Email.emailBody=body;		
		
		Properties props = new Properties();
		props.put("mail.smtp.user", senderEmail);
		props.put("mail.smtp.host", smtpServerPort);
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.starttls", "true");
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.socketFactory.port",smtpServerPort);
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback","false");

		try {
			Authenticator auth=new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
			MimeMessage msg = new MimeMessage(session);
//			msg.setText(emailBody);
			msg.setSubject(subject);
			Multipart emailContent=new MimeMultipart();
			MimeBodyPart textContent=new MimeBodyPart();
			textContent.setText(emailBody);
			MimeBodyPart attachments=new MimeBodyPart();
			attachments.attachFile(filePath);
			System.out.println(filePath);
			emailContent.addBodyPart(textContent);
			emailContent.addBodyPart(attachments);
			msg.setContent(emailContent);			
			msg.setFrom(new InternetAddress(senderEmail));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			Transport.send(msg);
			System.out.println("Email sent");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	
	public class SMTPAuthenticator extends Authenticator {
		public SMTPAuthenticator() {

		    super();
		}

		@Override
		public PasswordAuthentication getPasswordAuthentication() {
		 String username = senderEmail;
		 String password = senderPass;
		    if ((username != null) && (username.length() > 0) && (password != null) 
		      && (password.length   () > 0)) {

		        return new PasswordAuthentication(username, password);
		    }

		    return null;
		}
	}

}
