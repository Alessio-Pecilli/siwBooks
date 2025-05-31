package siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import siw.books.model.Credentials;
import siw.books.model.Utente;
import siw.books.services.CredentialsService;

@Controller
public class AuthController {

	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping("/login")
	public String showlogingUtente(@RequestParam(value = "error", required = false) boolean error, Model model) {

	    if (error)
	        model.addAttribute("msgError", "Username o password errati");

	    return "login.html";
	}

	@GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("utente", new Utente());
        model.addAttribute("credentials", new Credentials());
        return "register.html";
    }
	
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("utente") Utente utente,
                             BindingResult utenteBindingResult,
                             @Valid @ModelAttribute("credentials") Credentials credentials,
                             @Valid @RequestParam(name = "confermaPwd") String confermaPwd,
                             BindingResult credentialsBindingResult,
                             Model model) {
        
        // Validation check for both objects
        if (utenteBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
            model.addAttribute("msgError", "Errore nella validazione dei dati");
            return "register.html";
        }
        // Check if username already exists
        if (credentialsService.existsByUsername(credentials.getUsername())) {
            model.addAttribute("msgError", "Username già in uso");
            return "register.html";
        }
        if(!credentials.getPassword().equals(confermaPwd)) {
        	model.addAttribute("msgError", "Le 2 password scritte non sono uguali");
            return "register.html";
        }
        try {
            
            // Link utente to credentials
            credentials.setUtente(utente);
            
            // Save credentials (which will cascade to utente due to @OneToOne relationship)
            credentialsService.saveCredentials(credentials);
            
            model.addAttribute("msgSuccess", "Registrazione completata con successo");
            return "redirect:/utente/"+ utente.getId();
            
        } catch (Exception e) {
            model.addAttribute("msgError", "Errore durante la registrazione");
            return "register.html";
        }
    }

}
