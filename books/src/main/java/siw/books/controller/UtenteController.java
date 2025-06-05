package siw.books.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw.books.model.Credentials;
import siw.books.model.Libro;
import siw.books.model.Recensione;
import siw.books.model.Utente;
import siw.books.services.CredentialsService;
import siw.books.services.RecensioneService;
import siw.books.services.UtenteService;

@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private RecensioneService recensioneService;
    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/utente/{id}")
    public String getPaginaUtente(@PathVariable("id") Long id, Model model) {
        Utente utente = this.utenteService.getUtente(id);
        model.addAttribute("utente", utente);
        System.out.println("Utente ID: " + utente.getId());
        System.out.println("Nome: " + utente.getNome());
        System.out.println("Cognome: " + utente.getCognome());
        System.out.println("Email: " + utente.getEmail());
        List<Recensione> recensioni = recensioneService.findByAutore(utente);
        model.addAttribute("recensioniUtente", recensioni);

    List<String> copertine = new ArrayList<>();

    for (Recensione rec : recensioni) {
        Libro libro = rec.getLibro();
        String path = "/images/books/" + libro.getTitolo().toLowerCase().replace(" ", "_") + "_1.jpg";
        copertine.add(path);
    }
    model.addAttribute("copertineUtente", copertine);
        // Aggiungi altre informazioni se presenti nella classe Utente
        return "utentiRegistrati/utenteRegistrato.html"; // crea questo file
    }

    @GetMapping("/amministratori/{id}")
    public String getPaginaAdmin(@PathVariable("id") Long id, Model model) {
        Utente admin = this.utenteService.getUtente(id);
        model.addAttribute("utente", admin);
        return "amministratori/amministratore.html"; 
    }

    @GetMapping("/profilo")
public String showProfiloUtente(Authentication authentication, Model model) {
    if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        String ruolo = credentials.getRole();
        Utente utente = credentials.getUtente(); 

        model.addAttribute("utente", utente); // lo passi al template

        if (ruolo.equals(Credentials.ADMIN_ROLE)) {
            return "redirect:amministratori/" + utente.getId();
        } else if (ruolo.equals(Credentials.DEFAULT_ROLE)) {
            return "redirect:utente/" + utente.getId();
        }
    }
    return "redirect:/login";
}
}
