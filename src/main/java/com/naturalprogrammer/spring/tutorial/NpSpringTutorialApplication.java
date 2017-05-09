package com.naturalprogrammer.spring.tutorial;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//if you need to add components from other package
//@SpringBootApplication(scanBasePackageClasses={NpSpringTutorialApplication.class,SomeClassInTheOtherPackage.class})
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication
public class NpSpringTutorialApplication {

	private static final Log log = LogFactory.getLog(NpSpringTutorialApplication.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(NpSpringTutorialApplication.class, args);
		
		log.info("Beans in application context:");
		
		String beanNames[] = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		
		for(String beanName: beanNames)
			log.info(beanName);
	}
}
