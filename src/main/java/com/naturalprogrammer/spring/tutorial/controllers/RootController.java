package com.naturalprogrammer.spring.tutorial.controllers;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring.tutorial.dto.ForgotPasswordForm;
import com.naturalprogrammer.spring.tutorial.dto.ResetPasswordForm;
import com.naturalprogrammer.spring.tutorial.dto.SignupForm;
import com.naturalprogrammer.spring.tutorial.mail.SmtpMailSender;
import com.naturalprogrammer.spring.tutorial.services.UserService;
import com.naturalprogrammer.spring.tutorial.util.MyUtil;
import com.naturalprogrammer.spring.tutorial.validators.ForgotPasswordFormValidator;
import com.naturalprogrammer.spring.tutorial.validators.ResetPasswordFormValidator;
import com.naturalprogrammer.spring.tutorial.validators.SignupFormValidator;

@Controller
public class RootController {

	private static final Log log = LogFactory.getLog(SmtpMailSender.class);
	
	/*
	@Value("${app.name}")
	private String appName;
	*/

	/* check MvcConfig.java
	 * 
	@RequestMapping("/")
	// if you add @ResponseBody, it will return "home" string to the browser. it will be like putting @RestController in the class level
	public String home() {
		return "home";
	}
	*/
	

	private UserService userService;
	private SignupFormValidator signupFormValidator;
	private ForgotPasswordFormValidator forgotPasswordFormValidator;
	private ResetPasswordFormValidator resetPasswordFormValidator;

	
	@Autowired
	public RootController(UserService userService,
			SignupFormValidator signupFormValidator,
			ForgotPasswordFormValidator forgotPasswordFormValidator,
			ResetPasswordFormValidator resetPasswordFormValidator) {
		this.userService = userService;
		this.signupFormValidator = signupFormValidator;
		this.forgotPasswordFormValidator = forgotPasswordFormValidator;
		this.resetPasswordFormValidator = resetPasswordFormValidator;
	}

	/*
	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("signupFormValidator")
	private SignupFormValidator signupFormValidator;
	
	@Autowired
	@Qualifier("forgotPasswordFormValidator")
	private ForgotPasswordFormValidator forgotPasswordFormValidator;
	
	*/
	
	//set it to use custom form validator.
	@InitBinder("signupForm") 
	protected void initSignupBinder(WebDataBinder binder) {
		binder.setValidator(signupFormValidator);
	}
	
	//set it to use custom form validator.
	@InitBinder("forgotPasswordForm") 
	protected void initForgotPasswordBinder(WebDataBinder binder) {
		binder.setValidator(forgotPasswordFormValidator);
	}
	
	//set it to use custom form validator.
	@InitBinder("resetPasswordForm") 
	protected void initResetPasswordBinder(WebDataBinder binder) {
		binder.setValidator(resetPasswordFormValidator);
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		
		//model.addAttribute("signupForm", new SignupForm()); // if the name is not provided, Spring gets its name from the class name in camel case
		model.addAttribute(new SignupForm());
		return "signup";
	}
	
	/* without flash message
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result) {

		if(result.hasErrors())
			return "signup";
		
		userService.signup(signupForm);
		
		return "redirect:/";
	}
	*/
	
	// with flash message
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {

		if(result.hasErrors())
			return "signup";
		
		userService.signup(signupForm);
		
		//redirectAttributes.addFlashAttribute("flashKind", "success");
		//redirectAttributes.addFlashAttribute("flashMessage", "Signup successful. Please check your mailbox to verify yourself");
		
		MyUtil.flash(redirectAttributes, "success", "signupSuccess");
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/forgot-password", method=RequestMethod.GET)
	public String forgotPassword(Model model) {
		model.addAttribute(new ForgotPasswordForm());
		return "forgot-password";
	}
	
	@RequestMapping(value="/forgot-password", method = RequestMethod.POST)
	public String forgotPassword(@ModelAttribute("forgotPasswordForm") @Valid ForgotPasswordForm forgotPasswordForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if(result.hasErrors())
			return "forgot-password";
		
		userService.forgotPassword(forgotPasswordForm);
		MyUtil.flash(redirectAttributes,  "info", "checkMailResetPassword");
		
		return "redirect:/";
	}
	
	@RequestMapping(value="/reset-password/{forgotPasswordCode}")
	public String resetPassword(@PathVariable("forgotPasswordCode") String forgotPasswordCode, Model model) {
		model.addAttribute(new ResetPasswordForm());
		return "reset-password";
	}
	
	@RequestMapping(value="/reset-password/{forgotPasswordCode}", method = RequestMethod.POST)
	public String resetPassword(
			@PathVariable("forgotPasswordCode") String forgotPasswordCode,
			@ModelAttribute("resetPasswordForm")
			@Valid ResetPasswordForm resetPasswordForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		
		userService.resetPassword(forgotPasswordCode, resetPasswordForm, result);
		if(result.hasErrors())
			return "reset-password";
		
		MyUtil.flash(redirectAttributes,  "success", "passwordChanged");
		
		return "redirect:/login";
	}
}
