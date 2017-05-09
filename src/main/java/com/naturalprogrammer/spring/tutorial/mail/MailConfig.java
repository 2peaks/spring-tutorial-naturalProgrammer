package com.naturalprogrammer.spring.tutorial.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

	@Bean
	//@Profile("dev") //in application.properties file
	@ConditionalOnProperty(name="spring.mail.host",
			havingValue="foo",
			matchIfMissing=true)
	public MailSender mockMailSender() {
		return new MockMailSender();
	}
	
	@Bean
	//@Profile("!dev")
	@ConditionalOnProperty(name="spring.mail.host")// it says, if spring.mail.host exist in application.properties file, use this bean
	public MailSender smtpMailSender() {
		return new SmtpMailSender();
	}
}
