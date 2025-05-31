package siw.books.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw.books.model.Utente;
import siw.books.services.UtenteService;

@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/utente/{id}")
    public String getPaginaUtente(@PathVariable("id") Long id, Model model) {
        Utente utente = this.utenteService.getUtente(id);
        model.addAttribute("utente", utente);
        System.out.println("Utente ID: " + utente.getId());
        System.out.println("Nome: " + utente.getNome());
        System.out.println("Cognome: " + utente.getCognome());
        System.out.println("Email: " + utente.getEmail());
        // Aggiungi altre informazioni se presenti nella classe Utente
        return "utentiRegistrati/utenteRegistrato.html"; // crea questo file
    }

    @GetMapping("/amministratori/{id}")
    public String getPaginaAdmin(@PathVariable("id") Long id, Model model) {
        Utente admin = this.utenteService.getUtente(id);
        model.addAttribute("utente", admin);
        return "amministratori/amministratore.html"; 
    }
}
