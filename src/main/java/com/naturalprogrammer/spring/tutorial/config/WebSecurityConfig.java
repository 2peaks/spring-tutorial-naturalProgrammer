package com.naturalprogrammer.spring.tutorial.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
//@EnableWebMvcSecurity is deprecated. It's not needed Spring Boot 1.3 or above anyway
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${rememberMe.privateKey}")
	private String rememberMeKey;
	
	@Resource
	private UserDetailsService userService;
	
	@Bean
	public RememberMeServices rememberMeServices() {
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(rememberMeKey, userService);
		return rememberMeServices;
	}
	
	@Bean //Bean inside @Configuration, so it will use existing bean if there is one, instead of creating new beans
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/",
				"/home",
				"/error",
				"/signup",
				"/forgot-password",
				"/reset-password/*",
				"/public/**",
				"/users/*").permitAll() //the list about are permit all
		.anyRequest().authenticated(); // other than those, the user needs to be authenticated.
		
		//Chrome browser still remembers withtout "remember me" checkbox checked.. it worked on Firefox though..
		http.formLogin()
			.loginPage("/login")
		.permitAll().and()
		.rememberMe().key(rememberMeKey).rememberMeServices(rememberMeServices()).and()
		.logout().permitAll();
	}
	
	@Autowired
	@Override
	//Spring calls this to get an anthencationManager
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}
}
