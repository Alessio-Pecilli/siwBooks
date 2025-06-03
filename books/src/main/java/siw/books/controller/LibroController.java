package siw.books.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import siw.books.model.Autore;
import siw.books.model.Credentials;
import siw.books.model.Immagine;
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

   @GetMapping({"/libri", "/amministratori/libri"})
public String mostraLibri(Model model, Authentication authentication) {
    List<Libro> tuttiLibri = libroService.findAll();
    model.addAttribute("libri", tuttiLibri);
    List<String> pathsImmagini = new LinkedList<>();
    for (Libro libro : tuttiLibri) {
        if(libro.getImmagini() == null || libro.getImmagini().isEmpty()) {
            continue; // Skip libri without images
        }else{pathsImmagini.add(libro.getImmagini().get(0).getPath());}
        
    }
    model.addAttribute("copertine", pathsImmagini);
    
    for (Libro libro : tuttiLibri) {
        float mediaRecensioni = libro.getMediaVotiRecensioni();
        model.addAttribute("media_" + libro.getId(), mediaRecensioni);
    }
    

    if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        String ruolo = credentials.getRole();

        if (ruolo.equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/libri";
        }
    }

    return "libri";
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

@GetMapping({"/libri/{id}", "/utenti/libri/{id}", "/amministratori/libri/{id}"})
public String mostraSingoloLibro(@PathVariable Long id, Model model, Authentication authentication) {
    Optional<Libro> optionalLibro = libroService.findById(id);
    if (optionalLibro.isEmpty()) {
        return "redirect:/libri";
    }

    Libro libro = optionalLibro.get();
    model.addAttribute("libro", libro);
    model.addAttribute("recensioni", libro.getRecensioni());
    int mediaVoto = libro.getMediaVotiRecensioni();
    model.addAttribute("media", mediaVoto);
    // Gestione immagini
    List<Immagine> immagini = libro.getImmagini();
    List<String> pathsImmagini = new LinkedList<>();

    if (immagini == null || immagini.isEmpty()) {
        model.addAttribute("copertina", "/images/books/default.jpg");
    } else {
        model.addAttribute("copertina", immagini.get(0).getPath());
        for (int i = 1; i < immagini.size(); i++) {
            if (immagini.get(i) != null) {
                pathsImmagini.add(immagini.get(i).getPath());
                System.out.println("Path immagine: " + immagini.get(i).getPath());
            }
        }
    }

    model.addAttribute("copertine", pathsImmagini);

    // Gestione autenticazione
    if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        String ruolo = credentials.getRole();

        if (ruolo.equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/dettaglioLibro";
        } else if (ruolo.equals(Credentials.DEFAULT_ROLE)) {
            model.addAttribute("recensione", new Recensione());
            model.addAttribute("utente", credentials.getUtente());
            return "utentiRegistrati/dettaglioLibro";
        }
    }

    // Per utenti non loggati
    model.addAttribute("autori", libro.getAutori());
    return "dettaglioLibro";
}

@GetMapping("/amministratori/libri/modifica/{id}")
public String mostraFormModifica(@PathVariable Long id, Model model) {
    Optional<Libro> libroOpt = libroService.findById(id);
    if (libroOpt.isPresent()) {
        Libro libro = libroOpt.get();
        model.addAttribute("libro", libro);
        model.addAttribute("autori", autoreService.findAll());

        return "amministratori/formLibro";
    }
    return "redirect:/amministratori/libri";
}

    @GetMapping("/amministratori/libri/new")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autori", autoreService.findAll());
        return "amministratori/formLibro";
    }

  @PostMapping("/amministratori/libri/save")
public String saveLibro(@Valid @ModelAttribute Libro libro,
                        BindingResult libroBinding,
                        @RequestParam(name = "autoreIds", required = false) List<Long> autoreIds,
                        Model model) {

    boolean erroreAutori = (autoreIds == null || autoreIds.isEmpty());

    if (libroBinding.hasErrors() || erroreAutori) {
        model.addAttribute("libro", libro);
        model.addAttribute("autori", autoreService.findAll());

        if (erroreAutori) {
            model.addAttribute("erroreAutori", "Seleziona almeno un autore");
        }

        return "amministratori/formLibro";
    }

    List<Autore> autoriSelezionati = new ArrayList<>();
    for (Long id : autoreIds) {
        autoreService.findById(id).ifPresent(autoriSelezionati::add);
    }

    libro.setAutori(autoriSelezionati);
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
