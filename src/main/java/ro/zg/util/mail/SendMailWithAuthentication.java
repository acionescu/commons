/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.util.mail;

/*
 Some SMTP servers require a username and password authentication before you
 can use their Server for Sending mail. This is most common with couple
 of ISP's who provide SMTP Address to Send Mail.

 This Program gives any example on how to do SMTP Authentication
 (User and Password verification)

 This is a free source code and is provided as it is without any warranties and
 it can be used in any your code for free.

 Author : Sudhir Ancha
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMailWithAuthentication {

    private String smtpPort = "465";
    private String sslFactory = "javax.net.ssl.SSLSocketFactory";
    private boolean useSSL;
    private boolean debug;

    public void postMail(List recipients, List cc, List bcc, String subject, String message, String from, String smtpHostName,final String username,
	    final String password) throws MessagingException {

	// Set the host smtp address
	Properties props = new Properties();
	props.put("mail.smtp.host", smtpHostName);
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.port", smtpPort);
	if (useSSL) {
	    props.put("mail.smtp.socketFactory.port", smtpPort);
	    props.put("mail.smtp.socketFactory.class", sslFactory);
	    props.put("mail.smtp.socketFactory.fallback", "false");
	}
	
//	    props.put("mail.smtp.starttls.enable", "true");
//	    props.put("mail.smtp.ssl.enable", "true");
	
	Authenticator auth = new javax.mail.Authenticator() {

	    public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	    }
	};

	Session session = Session.getDefaultInstance(props, auth);

	session.setDebug(debug);

	// create a message
	MimeMessage msg = new MimeMessage(session);

	// set the from and to address
	InternetAddress addressFrom = new InternetAddress(from);
	msg.setFrom(addressFrom);

	addRecipients(Message.RecipientType.TO, msg, recipients);
	addRecipients(Message.RecipientType.CC, msg, cc);
	addRecipients(Message.RecipientType.BCC, msg, bcc);

	// Setting the Subject and Content Type
	msg.setSubject(subject,"UTF-8");
	msg.setContent(message, "text/html; charset=UTF-8");
	msg.setHeader("Content-Type", "text/html; charset=UTF-8");
	Transport.send(msg);
    }

    private void addRecipients(RecipientType recType, Message msg, List recipients) throws MessagingException{
	if(recipients == null){
	    return;
	}
	InternetAddress[] addressTo = new InternetAddress[recipients.size()];
	for (int i = 0; i < recipients.size(); i++) {
	    addressTo[i] = new InternetAddress((String)recipients.get(i));
	}
	msg.setRecipients(recType, addressTo);
    }

    /**
     * @return the smtpPort
     */
    public String getSmtpPort() {
	return smtpPort;
    }

    /**
     * @return the sslFactory
     */
    public String getSslFactory() {
	return sslFactory;
    }

    

    /**
     * @param smtpPort
     *            the smtpPort to set
     */
    public void setSmtpPort(String smtpPort) {
	this.smtpPort = smtpPort;
    }

    /**
     * @param sslFactory
     *            the sslFactory to set
     */
    public void setSslFactory(String sslFactory) {
	this.sslFactory = sslFactory;
    }

    /**
     * @return the useSSL
     */
    public boolean isUseSSL() {
	return useSSL;
    }

    /**
     * @param useSSL
     *            the useSSL to set
     */
    public void setUseSSL(boolean useSSL) {
	this.useSSL = useSSL;
    }

}
