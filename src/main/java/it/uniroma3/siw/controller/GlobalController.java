package it.uniroma3.siw.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

//mette a disposizione di ogni vista un attributo
@ControllerAdvice
public class GlobalController {
	
	@Autowired
	private CredentialsService credentialsService;

	//dettagli sull'utente loggato
	@ModelAttribute("userDetails")
	public UserDetails getUser() {
		UserDetails user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		return user;
	}
	
	//booleano se sei admin
	@ModelAttribute("isAdmin")
	public boolean isAdmin() {
		boolean isAdmin = false;
		UserDetails user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(user.getUsername());
			isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);
			return isAdmin;
		}
		return isAdmin;
	}
	
	//booleano se sei loggato
	@ModelAttribute("isAuthenticated")
	public boolean isAuthenticated() {
		boolean isAuthenticated=false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			isAuthenticated=true;
		}
		return isAuthenticated;
	}
}
