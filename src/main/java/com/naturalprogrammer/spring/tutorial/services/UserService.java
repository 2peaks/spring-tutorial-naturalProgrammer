package com.naturalprogrammer.spring.tutorial.services;

import org.springframework.validation.BindingResult;

import com.naturalprogrammer.spring.tutorial.dto.ForgotPasswordForm;
import com.naturalprogrammer.spring.tutorial.dto.ResetPasswordForm;
import com.naturalprogrammer.spring.tutorial.dto.SignupForm;
import com.naturalprogrammer.spring.tutorial.dto.UserEditForm;
import com.naturalprogrammer.spring.tutorial.entities.User;

public interface UserService {

	public abstract void signup(SignupForm signupForm);
	
	public abstract void verify(String verificationCode);

	public abstract void forgotPassword(ForgotPasswordForm forgotPasswordForm);

	public abstract void resetPassword(String forgotPasswordCode, ResetPasswordForm resetPasswordForm,
			BindingResult result);

	public abstract User findOne(long userId);

	public abstract void update(long userId, UserEditForm userEditForm);
}
