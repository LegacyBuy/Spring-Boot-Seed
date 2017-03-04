package com.legacybuy.examples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class EmailExample {

	@Autowired
	private MailSender mailSender;

	void emailDemo() {
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setFrom("hirahulgoyal@gmail.com");
		msg.setTo("hideepakgoyal@gmail.com");

		msg.setSubject("Hello Testing " + System.currentTimeMillis());
		msg.setText("Dear , thank you for placing order. Your order number is ");

		try {
			this.mailSender.send(msg);
		} catch (MailException ex) {
			// simply log it and go on...
			System.err.println(ex.getMessage());
		}
	}
}
