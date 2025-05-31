package siw.books.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import siw.books.model.Autore;
import siw.books.model.Credentials;
import siw.books.model.Libro;
import siw.books.model.Recensione;
import siw.books.model.Utente;
import siw.books.services.AutoreService;
import siw.books.services.CredentialsService;
import siw.books.services.LibroService;
import siw.books.services.RecensioneService;

@Controller
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private RecensioneService recensioneService;

    @Autowired
    private AutoreService autoreService;

    @Autowired
    private CredentialsService credentialsService;

    // 🔓 PUBBLICO
    @GetMapping("/libri")
    public String mostraLibri(Model model) {
        List<Libro> tuttiLibri = libroService.findAll();
        model.addAttribute("libri", tuttiLibri);
        return "libri";
    }

    @GetMapping("/libri/{id}")
    public String mostraSingoloLibro(@PathVariable Long id, Model model) {
        Optional<Libro> optionalLibro = libroService.findById(id);
        if (optionalLibro.isPresent()) {
            Libro libro = optionalLibro.get();
            model.addAttribute("libro", libro);
            model.addAttribute("recensioni", libro.getRecensioni());
            model.addAttribute("autori", libro.getAutori());
            return "dettaglioLibro";
        } else {
            return "redirect:/libri";
        }
    }

    
    @GetMapping("/utenti/libri/{id}")
    public String getLibroUtente(@PathVariable Long id, Model model) {
        Optional<Libro> optionalLibro = libroService.findById(id);
        if (optionalLibro.isPresent()) {
            Libro libro = optionalLibro.get();
            model.addAttribute("libro", libro);
            model.addAttribute("recensioni", libro.getRecensioni());
            model.addAttribute("recensione", new Recensione());
            
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Credentials credentials = credentialsService.getCredentialsByUsername(username);
            Utente utente = credentials.getUtente();
            model.addAttribute("utente", utente);

            return "utentiRegistrati/dettaglioLibro";
        } else {
            return "redirect:/libri";
        }
    }
@PostMapping("/utenti/libri/{id}/aggiungiRecensione")
public String aggiungiRecensione(@PathVariable Long id,
                                  @ModelAttribute("recensione") Recensione recensione) {

    Optional<Libro> optionalLibro = libroService.findById(id);
    if (optionalLibro.isPresent()) {
        Libro libro = optionalLibro.get();

        // Recupera username dell'utente loggato
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }

        // Recupera l'oggetto Utente associato
        Utente autore = credentialsService.getCredentialsByUsername(username).getUtente();

        // Collega i dati
        recensione.setId(null);
        recensione.setLibro(libro);
        recensione.setAutore(autore); 
        libro.getRecensioni().add(recensione);
        recensioneService.salvaRecensione(recensione);

        return "redirect:/utenti/libri/" + id;
    } else {
        return "redirect:/libri";
    }
}

    // 🛡️ AMMINISTRATORI
    @GetMapping("/amministratori/libri")
    public String mostraLibriAdmin(Model model) {
        List<Libro> tuttiLibri = libroService.findAll();
        model.addAttribute("libri", tuttiLibri);
        return "amministratori/libri";
    }

    @GetMapping("/amministratori/libri/{id}")
    public String getLibroAdmin(@PathVariable Long id, Model model) {
        Optional<Libro> optionalLibro = libroService.findById(id);
        if (optionalLibro.isPresent()) {
            Libro libro = optionalLibro.get();
            model.addAttribute("libro", libro);
            model.addAttribute("recensioni", libro.getRecensioni());
            return "amministratori/dettaglioLibro";
        } else {
            return "redirect:/amministratori/libri";
        }
    }

    @GetMapping("/amministratori/libri/new")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autori", autoreService.findAll());
        return "amministratori/formLibro";
    }

    @PostMapping("/amministratori/libri/save")
    public String saveLibro(@ModelAttribute Libro libro) {
        libroService.save(libro);
        return "redirect:/amministratori/libri";
    }

    @PostMapping("/amministratori/libri/elimina/{id}")
    public String eliminaLibro(@PathVariable Long id) {
        libroService.deleteById(id);
        return "redirect:/amministratori/libri";
    }

    @PostMapping({"/amministratori/recensioni/elimina/{id}", "/utenti/recensioni/elimina/{id}"})
public String eliminaRecensione(@PathVariable Long id, Authentication authentication) {
    Long idLibro = recensioneService.getLibroIdFromRecensione(id);
    recensioneService.eliminaRecensione(id);

    
    String username = authentication.getName();
    String ruolo = credentialsService.getCredentialsByUsername(username).getRole();

    if (ruolo.equals(Credentials.ADMIN_ROLE)) {
        return "redirect:/amministratori/libri/" + idLibro;
    } else {
        return "redirect:/utenti/libri/" + idLibro;
    }
}

}
