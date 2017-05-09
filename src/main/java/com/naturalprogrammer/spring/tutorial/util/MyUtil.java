package com.naturalprogrammer.spring.tutorial.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naturalprogrammer.spring.tutorial.dto.UserDetailsImpl;
import com.naturalprogrammer.spring.tutorial.entities.User;

@Component
public class MyUtil {
	
	@Autowired
	public MyUtil(MessageSource messageSource) {
		MyUtil.messageSource = messageSource;
	}
	
	private static MessageSource messageSource;
	
	public static void flash(RedirectAttributes redirectAttributes, String kind, String messageKey) {
		redirectAttributes.addFlashAttribute("flashKind", kind);
		redirectAttributes.addFlashAttribute("flashMessage", MyUtil.getMessage(messageKey));
	}
	
	private static String hostAndPort;
	
	@Value("${hostAndPort}") // the value is in application.properties
	public void setHostAndPort(String hostAndPort) {
		MyUtil.hostAndPort = hostAndPort;
	}
	
	private static String activeProfiles;
	
	@Value("${spring.profiles.active}")
	public void setActiveProfile(String activeProfiles) {
		MyUtil.activeProfiles = activeProfiles;
	}
	
	public static boolean isDev() {
		return activeProfiles.equals("dev");
	}
	
	public static String hostUrl() {
		return (isDev() ? "http://" : "https://") + hostAndPort;
	}
	
	public static String getMessage(String messageKey, Object... args) {
		//messageSource.getMessage(key, args, Locale.getDefault());
		return messageSource.getMessage(messageKey, args, Locale.getDefault());
	}

	public static void validate(boolean valid, String msgContent, Object... args) {
		if(!valid)
			throw new RuntimeException(getMessage(msgContent, args));
	}

	public static User getSessionUser() {
		UserDetailsImpl auth = getAuth();
		return auth == null ? null : auth.getUser();
	}
	
	public static UserDetailsImpl getAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			Object principal = auth.getPrincipal();
			if(principal instanceof UserDetailsImpl) {
				return(UserDetailsImpl) principal;
			}
		}
		return null;
	}
}
