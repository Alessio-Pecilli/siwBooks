package siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import siw.books.model.Credentials;
import siw.books.model.Recensione;
import siw.books.model.Utente;
import siw.books.services.CredentialsService;
@ControllerAdvice
public class GlobalController {

	@Autowired
	private CredentialsService credentialsService;

	@ModelAttribute("userDetails")
	public UserDetails getUser() {
	    UserDetails user = null;
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (!(authentication instanceof AnonymousAuthenticationToken)) {
	      user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    }
	    return user;
	  }

	@GetMapping("/profilo")
public String showProfiloUtente(Authentication authentication, Model model) {
    if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        String ruolo = credentials.getRole();
        Utente utente = credentials.getUtente(); // prendi l'oggetto Utente

        model.addAttribute("utente", utente); // lo passi al template

        if (ruolo.equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/amministratore";
        } else if (ruolo.equals(Credentials.DEFAULT_ROLE)) {
            return "utentiRegistrati/utenteRegistrato";
        }
    }
    return "redirect:/login";
}

}

