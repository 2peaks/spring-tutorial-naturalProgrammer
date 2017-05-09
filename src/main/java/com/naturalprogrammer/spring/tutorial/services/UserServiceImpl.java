package com.naturalprogrammer.spring.tutorial.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.validation.BindingResult;

import com.naturalprogrammer.spring.tutorial.dto.ForgotPasswordForm;
import com.naturalprogrammer.spring.tutorial.dto.ResetPasswordForm;
import com.naturalprogrammer.spring.tutorial.dto.SignupForm;
import com.naturalprogrammer.spring.tutorial.dto.UserDetailsImpl;
import com.naturalprogrammer.spring.tutorial.dto.UserEditForm;
import com.naturalprogrammer.spring.tutorial.entities.User;
import com.naturalprogrammer.spring.tutorial.entities.User.Role;
import com.naturalprogrammer.spring.tutorial.mail.MailSender;
import com.naturalprogrammer.spring.tutorial.repositories.UserRepository;
import com.naturalprogrammer.spring.tutorial.util.MyUtil;

@Service
@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private MailSender mailSender;
	
	private static final Log log = LogFactory.getLog(UserServiceImpl.class);
	
	@Autowired //constructor based injection
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailSender = mailSender;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void signup(SignupForm signupForm) {
		final User user = new User();
		user.setEmail(signupForm.getEmail());
		user.setName(signupForm.getName());
		user.setPassword(passwordEncoder.encode(signupForm.getPassword()));
		user.getRoles().add(Role.UNVERIFIED);
		user.setVerificationCode(RandomStringUtils.randomAlphanumeric(16));
		userRepository.save(user);
		
		//make sure email is sent only after the user is successfully created. ie. after commit
		TransactionSynchronizationManager.registerSynchronization( new TransactionSynchronizationAdapter() {
			@Override
			public void afterCommit() {
				String verifyLink = MyUtil.hostUrl() + "/users/" + user.getVerificationCode() + "/verify";
				mailSender.send(user.getEmail(), MyUtil.getMessage("verifySubject"), MyUtil.getMessage("verifyEmail", verifyLink));
				log.info(verifyLink);
			}
		});		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if(user == null)
			throw new UsernameNotFoundException(username);
		
		return new UserDetailsImpl(user);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void verify(String verificationCode) {
		long loggedInUserId = MyUtil.getSessionUser().getId();
		User user = userRepository.findOne(loggedInUserId);
		
		MyUtil.validate(user.getRoles().contains(Role.UNVERIFIED), "alreadyVerified"); //"alreadyVerified" in messages.properties
		MyUtil.validate(user.getVerificationCode().equals(verificationCode), "incorrect", "verification code"); //"incorrect" in messages.properties
		
		user.getRoles().remove(Role.UNVERIFIED);
		user.setVerificationCode(null);
		userRepository.save(user);
	}

	@Override
	public void forgotPassword(ForgotPasswordForm form) {
		final User user = userRepository.findByEmail(form.getEmail());
		final String forgotPasswordCode = RandomStringUtils.randomAlphanumeric(User.RANDOM_CODE_LENGTH);
		
		user.setForgotPasswordCode(forgotPasswordCode);
		final User savedUser = userRepository.save(user);
		
		TransactionSynchronizationManager.registerSynchronization(
			new TransactionSynchronizationAdapter() {
				@Override
				public void afterCommit() {
					mailForgotPasswordLink(savedUser);
				}
			});
	}
	
	private void mailForgotPasswordLink(User user) {
		
		String forgotPasswordLink = MyUtil.hostUrl() + "/reset-password/" + user.getForgotPasswordCode();
		mailSender.send(user.getEmail(), MyUtil.getMessage("forgotPasswordSubject"), MyUtil.getMessage("forgotPasswordEmail", forgotPasswordLink));
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm, BindingResult result) {
		User user = userRepository.findByForgotPasswordCode(forgotPasswordCode);
		if(user == null)
			result.reject("invalidForgotPassword");
		
		if(result.hasErrors())
			return;
		
		user.setForgotPasswordCode(null);
		user.setPassword(passwordEncoder.encode(resetPasswordForm.getPassword().trim()));
		userRepository.save(user);
		
	}

	@Override
	public User findOne(long userId) {
		
		User loggedIn = MyUtil.getSessionUser();
		User user = userRepository.findOne(userId);
		
		if(loggedIn == null || loggedIn.getId() != user.getId() && !loggedIn.isAdmin())
			user.setEmail("Confidential");
		
		return user;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public void update(long userId, UserEditForm userEditForm) {
		User loggedIn = MyUtil.getSessionUser();
		MyUtil.validate(loggedIn.isAdmin() || loggedIn.getId() == userId, "noPermission");
		User user = userRepository.findOne(userId);
		user.setName(userEditForm.getName());
		if(loggedIn.isAdmin())
			user.setRoles(userEditForm.getRoles());
		userRepository.save(user);
		
	}
}
