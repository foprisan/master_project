package com.dizeratie.forum.tools;



import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


import org.apache.log4j.Logger;

public class SendMail {
	/**
	 * Variable that gets a list of properties associated with the given
	 * configuration key
	 */
	
	 
	private static String pathSessionLogger;
        
        private static String objetMail;
        private static String pj;
        private static String corpsMail;
        private static String destinataires;
        private static String message;

	/**
	 * Starts the application from command line
	 * 
	 * @param path
	 *            to the mail configuration file
	 */
	
        
        
        
        @SuppressWarnings({ "rawtypes" })
	public static void sendMail(String objetMail, String corpsMail,String destinataires, String messageText,String pjPath ,String sender) throws IOException {
            
            System.out.println("ObjetMail: " + objetMail);
            System.out.println("corpsMail: " + corpsMail);
            System.out.println("destinataires: " + destinataires);
            System.out.println("messageText: " + objetMail);            

// Creates a new file at the specified path
		try  {
			// Initializes and writes to the logger file

			System.out.println("Beginning of mail initialisation...");

			// Creates an empty property list named props
			Properties props = new Properties();

			// Information about the host and sender
			String smtpHost = "ITEM-AX30177";
			

			// Adds properties to the props list
			props.put("mail.smtp.host", smtpHost);
			//props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", 25);
//                        props.put("mail.smtp.starttls.enable","true");
//                        props.put("mail.smtp.ssl.enable","true");
//                        props.put("mail.smtp.ssl.enable","true");
                       // props.put("mail.smtp.auth.ntlm.domain","axway");
                       // props.put("mail.imap.auth.ntlm.disable", "true");
                        
			// Creates an instance of a session, based on the props list and
			// sets Debug mode to true, writing the output to a file
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);
			

			// Variable used for the mail message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));

			// Makes a list of all the recipientsF
			if (destinataires.contains(",")) {
				String[] destinataire =  destinataires.split(",");
                                
				
				for (int i = 0; i < destinataire.length; ++i) {
					message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(destinataire[i]));
				}
			} else {
				// Adds only one recipient
				String receiver = destinataires;
				message.addRecipient(Message.RecipientType.TO,
					new InternetAddress((String) receiver));
			}
			// Adds a subject to the mail
			message.setSubject(objetMail);

			// Verifies the attachment state
			if (pjPath==null) {
				// Information about the mail body without attachment
                            
                            StringBuffer sbMailMessageSansPJ = new StringBuffer(
					corpsMail.length());

				for (int j = 0; j < corpsMail.length(); ++j) {
					sbMailMessageSansPJ.append(corpsMail.charAt(j));
				}
				System.out.println("Mail message"+sbMailMessageSansPJ);
				message.setContent(sbMailMessageSansPJ.toString(), "text/html");

			} else if (pjPath!=null && !pjPath.equals("")) {
				// Information about the mail body with attachment
				StringBuffer sbMailMessage = new StringBuffer(
					corpsMail.length());
				// Creates a multipart message (mail body + attachment)
				MimeMultipart mp = new MimeMultipart();
				MimeBodyPart mbp1 = new MimeBodyPart();

				for (int i = 0; i < corpsMail.length(); ++i) {
					sbMailMessage.append(corpsMail.charAt(i));
				}
				// Creates a list of all the attached files
				if (pjPath.contains(",")) {
                                    String [] files=pjPath.split(",");
                                    
					for (int i = 0; i < files.length; ++i) {
						String filename = new File((String) files[i])
							.getName();
						File file = new File((String) files[i]);

						if (file.exists()) {
							BodyPart mbp2 = new MimeBodyPart();
							DataSource source = new FileDataSource(file);
//							mbp2.setDataHandler(new DataHandler(source));
//							mbp2.setFileName(filename);
							mp.addBodyPart(mbp2);
						} else {
							System.out.println("The file '" + file + "' does not exist.");
						}
					}
				} else {
					// Attaches only one file
					File file = new File(pjPath);

					if (file.exists()) {
						MimeBodyPart mbp2 = new MimeBodyPart();
						mbp2.attachFile(file);
						mp.addBodyPart(mbp2);
					} else {
						System.out.println("The file '" + file + "' does not exist.");
					}
				}
				mbp1.setContent(sbMailMessage.toString(), "text/html");
				mp.addBodyPart(mbp1);
				message.setContent(mp);
			}

			// Sends the mail and closes the connection
			Transport tr = session.getTransport("smtp");
			tr.connect(smtpHost, "", "");
                        
			tr.sendMessage(message, message.getAllRecipients());

			tr.close();
		
			System.out.println("End of processing mail.");
		}
		// Thrown when Session attempts to instantiate a Provider that doesn't
		// exist.
		catch (NoSuchProviderException e1) {
			System.out.println("No available provider for this protocol: " +e1);
		}
		// Thrown when a wrongly formatted address is encountered.
		catch (AddressException e2) {
			System.out.println("Invalid address: "+ e2);
		}
		// Thrown by the Messaging classes
		catch (MessagingException e3) {
			System.out.println("Mail message error: "+ e3);
		} 
	}
        
    
	
}