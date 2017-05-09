package com.naturalprogrammer.spring.tutorial.validators;

import javax.annotation.Resource;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.naturalprogrammer.spring.tutorial.dto.SignupForm;
import com.naturalprogrammer.spring.tutorial.entities.User;
import com.naturalprogrammer.spring.tutorial.repositories.UserRepository;

@Component
@Primary //Boot 1.5 throws error without one of the custom validators is primary
public class SignupFormValidator extends LocalValidatorFactoryBean {
	
	private UserRepository userRepository;
	
	@Resource
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(SignupForm.class);
	}
	
	@Override
	public void validate(Object obj, Errors errors, final Object... validationHints) {
		super.validate(obj,  errors,  validationHints);
		
		if(!errors.hasErrors()) {
			SignupForm signupForm = (SignupForm) obj;
			User user = userRepository.findByEmail(signupForm.getEmail());
			if(user != null)
				errors.rejectValue("email",  "emailNotUnique");
		}
	}
}
