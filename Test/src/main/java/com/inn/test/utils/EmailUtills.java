package com.inn.test.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


//this class is created to send update to other admins that a user is approved
@Service
public class EmailUtills {
	
	@Autowired
	private JavaMailSender emailSender;//JavaMailSender interface used to send and recive msg
	
	public void sendSimpleMesssage(String to, String subject, String text, List<String>list){
		
//SimpleMailMessage class: used to create a simple mail message including the from, to, cc, subject and text fields
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("shivsam.k@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if(list != null && list.size() > 0)
		{
			message.setCc(getCcArray(list));
		}
		emailSender.send(message);
	}
	private String[]getCcArray(List<String>ccList)
	{
		String[]cc = new String[ccList.size()];
		
		for(int i = 0; i < ccList.size(); i++)
		{
		cc[i] = ccList.get(i);	
		}
		return cc;
	}
	//MessagingException: thrown when the connect method on a Store or Transport object fails due to an authentication failure
	public void forgotMail(String to, String subject, String password) throws MessagingException
	{
	MimeMessage message = emailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message, true);
	helper.setFrom("shivsam.k@Gmail.com");
	helper.setTo(to);
	helper.setSubject(subject);
String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
message.setContent(htmlMsg, "text/html");
emailSender.send(message);
	}

}
