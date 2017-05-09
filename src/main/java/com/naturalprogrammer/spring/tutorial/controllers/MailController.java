package com.naturalprogrammer.spring.tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naturalprogrammer.spring.tutorial.mail.MailSender;

@RestController
public class MailController {

	/*
	//private MailSender mailSender = new MockMailSender();
	@Resource
	private MailSender mailSender;
	*/
	
	private MailSender mailSender;
	
	@Autowired
	public MailController(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@RequestMapping("/mail")
	public String sendMail() {
		mailSender.send("abc@hotmail.com", "some subject", "the body");
		return "Mail sent";
	}
}
