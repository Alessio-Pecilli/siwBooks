package siw.books.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import siw.books.model.Autore;
import siw.books.model.Credentials;
import siw.books.model.Immagine;
import siw.books.model.Libro;
import siw.books.model.Recensione;
import siw.books.model.Utente;
import siw.books.services.AutoreService;
import siw.books.services.CredentialsService;
import siw.books.services.ImmagineService;
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

    @Autowired
    private ImmagineService immagineService;

   @GetMapping({"/libri", "/amministratori/libri"})
public String mostraLibri(Model model, Authentication authentication) {
    List<Libro> tuttiLibri = libroService.findAll();
    model.addAttribute("libri", tuttiLibri);
    List<String> pathsImmagini = new LinkedList<>();
    for (Libro libro : tuttiLibri) {
        if(libro.getImmagini() == null || libro.getImmagini().isEmpty()) {
            System.out.println("Libro senza immagini: " + libro.getTitolo());
            continue; // Skip libri without images
        }else{pathsImmagini.add(libro.getImmagini().get(0).getPath());
        System.out.println("Path immagine: " + libro.getImmagini().get(0).getPath());
        }
        
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
        model.addAttribute("copertina", libro.getImmagini().get(0).getPath());
        model.addAttribute("autori", autoreService.findAll());

        List<Immagine> immagini = libro.getImmagini();
    List<String> pathsImmagini = new LinkedList<>();

    if (immagini == null || immagini.isEmpty()) {
        
    } else {
        
        for (int i = 1; i < immagini.size(); i++) {
            if (immagini.get(i) != null) {
                pathsImmagini.add(immagini.get(i).getPath());
                
            }
        }
    }

    model.addAttribute("copertine", pathsImmagini);

        return "amministratori/formLibro";
    }
    return "redirect:/amministratori/libri";
}



@PostMapping("/amministratori/libri/modifica/{id}")
public String modificaLibro(@PathVariable Long id,
                            @RequestParam("titolo") String titolo,
                            @RequestParam("annoPubblicazione") int annoPubblicazione,
                            @RequestParam(name = "autoreIds", required = false) List<Long> autoreIds,
                            @RequestParam(name = "copertina", required = false) MultipartFile copertina,
                            @RequestParam(name = "altreImmagini", required = false) MultipartFile[] altreImmagini,
                            Model model) throws IOException {

    Optional<Libro> libroOpt = libroService.findById(id);
    if (libroOpt.isEmpty()) return "redirect:/amministratori/libri";

    Libro libro = libroOpt.get();

    // Validazione: almeno un autore
    boolean erroreAutori = (autoreIds == null || autoreIds.isEmpty());
    if (erroreAutori || titolo == null || titolo.isBlank()) {
        model.addAttribute("libro", libro);
        model.addAttribute("autori", autoreService.findAll());
        if (erroreAutori)
            model.addAttribute("erroreAutori", "Seleziona almeno un autore");
        if (titolo == null || titolo.isBlank())
            model.addAttribute("erroreTitolo", "Il titolo non può essere vuoto");
        return "amministratori/formLibro";
    }

    // Aggiorna i campi modificabili
    libro.setTitolo(titolo);
    libro.setAnnoPubblicazione(annoPubblicazione);

    // Aggiorna autori
    List<Autore> autoriSelezionati = new ArrayList<>();
    for (Long autoreId : autoreIds) {
        autoreService.findById(autoreId).ifPresent(autoriSelezionati::add);
    }
    libro.setAutori(autoriSelezionati);

    // Salva nuova copertina (opzionale)
    if (copertina != null && !copertina.isEmpty()) {
        String nomeFile = titolo.toLowerCase().replace(" ", "_") + "_1.jpg";
        Path path = Paths.get("books/src/main/resources/static/images/books/");
        Files.createDirectories(path);
        Path filePath = path.resolve(nomeFile);
        Files.copy(copertina.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Immagine nuovaCopertina = new Immagine();
        nuovaCopertina.setNomeFile(nomeFile);
        nuovaCopertina.setPath("/images/books/" + nomeFile);
        nuovaCopertina.setLibro(libro);
        libro.getImmagini().add(nuovaCopertina);
    }

    // Salva altre immagini (opzionali)
    if (altreImmagini != null) {
        for (int i = 0; i < altreImmagini.length; i++) {
            MultipartFile img = altreImmagini[i];
            if (!img.isEmpty()) {
                String nomeFile = titolo.toLowerCase().replace(" ", "_") + "_" + (i + 2) + ".jpg";
                Path path = Paths.get("books/src/main/resources/static/images/books/");
                Files.createDirectories(path);
                Path filePath = path.resolve(nomeFile);
                Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                Immagine immagineExtra = new Immagine();
                immagineExtra.setNomeFile(nomeFile);
                immagineExtra.setPath("/images/books/" + nomeFile);
                immagineExtra.setLibro(libro);
                libro.getImmagini().add(immagineExtra);
            }
        }
    }

    libroService.save(libro);
    return "redirect:/amministratori/libri";
}

@PostMapping("/amministratori/libri/rimuoviImmagine/{idImg}")
public String rimuoviImmagine(@PathVariable Long idImg, RedirectAttributes redirectAttributes) {
    Optional<Immagine> immagineOpt = immagineService.findById(idImg);
    if (immagineOpt.isPresent()) {
        Immagine immagine = immagineOpt.get();
        Libro libro = immagine.getLibro();

        // Rimuove dal libro
        libro.getImmagini().removeIf(img -> img.getId().equals(idImg));
        libroService.save(libro); // Necessario se orphanRemoval è attivo

        // Cancella anche dal DB
        immagineService.deleteById(idImg);

        // Redirect alla pagina di modifica del libro
        return "redirect:/amministratori/libri/modifica/" + libro.getId();
    }

    // Se non trovata, torna alla lista
    return "redirect:/amministratori/libri";
}
    @GetMapping("/amministratori/libri/new")
    public String formNewLibro(Model model) {
        Libro a = new Libro();
        a.setImmagini(new ArrayList<>());
        model.addAttribute("libro", a);
        model.addAttribute("autori", autoreService.findAll());
        return "amministratori/formLibro";
    }
@PostMapping("/amministratori/libri/salva")
public String saveLibro(@RequestParam("titolo") String titolo,
                        @RequestParam("annoPubblicazione") int annoPubblicazione,
                        @RequestParam(name = "autoreIds", required = false) List<Long> autoreIds,
                        @RequestParam("copertina") MultipartFile copertina,
                         @RequestParam(name = "altreImmagini", required = false) MultipartFile[] altreImmagini,
                        Model model) throws IOException {

    
    if (autoreIds == null || autoreIds.isEmpty()) {
        model.addAttribute("erroreAutori", "Seleziona almeno un autore");
        model.addAttribute("autori", autoreService.findAll());

        
        Libro libro = new Libro();
        libro.setTitolo(titolo);
        libro.setAnnoPubblicazione(annoPubblicazione);
        model.addAttribute("libro", libro);

        return "amministratori/formLibro";
    }

    
    Libro libro = new Libro();
    libro.setTitolo(titolo);
    libro.setAnnoPubblicazione(annoPubblicazione);
    System.out.println("Arriva fino a qui: ");
    // Salva copertina
    try{
    System.out.println("Inizio salvataggio copertina...");
    if (!copertina.isEmpty()) {
        System.out.println("File copertina ricevuto: " + copertina.getOriginalFilename());
        String nomeFile = titolo.toLowerCase().replace(" ", "_").replace(".", "") + "_1.jpg";
        System.out.println("Nome file generato: " + nomeFile);
        Path path = Paths.get("books/src/main/resources/static/images/books/");
        System.out.println("Percorso destinazione: " + path.toAbsolutePath());
        Files.createDirectories(path);
        Path filePath = path.resolve(nomeFile);
        System.out.println("Percorso file completo: " + filePath.toAbsolutePath());
        Files.copy(copertina.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        Immagine immagine = new Immagine();
        immagine.setNomeFile(nomeFile);
        immagine.setPath("/images/books/" + nomeFile);
        libro.getImmagini().add(immagine);
        System.out.println("Copertina salvata: " + immagine.getPath());
        immagine.setLibro(libro);
    } else {
        Immagine immagine = new Immagine();
        immagine.setNomeFile("default.jpg");
            immagine.setPath("/images/books/default.jpg");
        libro.getImmagini().add(immagine); 
        immagine.setLibro(libro);
    }
    System.out.println("Fine salvataggio copertina.");
    } catch (Exception e) {
        System.out.println("Errore durante il salvataggio della copertina: " + e.getMessage());
    }

    if (altreImmagini != null) {
    for (int i = 0; i < altreImmagini.length; i++) {
        MultipartFile img = altreImmagini[i];
        if (!img.isEmpty()) {
            String nomeFile = titolo.toLowerCase().replace(" ", "_").replace(".", "") + "_" + (i + 2) + ".jpg";
            Path path = Paths.get("books/src/main/resources/static/images/books/");
            Files.createDirectories(path);
            Path filePath = path.resolve(nomeFile);
            Files.copy(img.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Immagine immagineExtra = new Immagine();
            immagineExtra.setNomeFile(nomeFile);
            immagineExtra.setPath("/images/books/" + nomeFile);
            immagineExtra.setLibro(libro); // Associa il libro
            libro.getImmagini().add(immagineExtra);
        }
    }
}


    // Setta gli autori
    List<Autore> autoriSelezionati = new ArrayList<>();
    for (Long id : autoreIds) {
        autoreService.findById(id).ifPresent(autoriSelezionati::add);
    }
    libro.setAutori(autoriSelezionati);
    System.out.println("Autori selezionati: " + autoriSelezionati.size());
    System.err.println("Titolo del libro: " + titolo + " con immagine: " + libro.getImmagini().get(0).getPath());
    libroService.save(libro);
    return "redirect:/amministratori/libri";
}



    @PostMapping("/amministratori/libri/elimina/{id}")
    public String eliminaLibro(@PathVariable Long id) {
        libroService.deleteById(id);
        return "redirect:/amministratori/libri";
    }

    @GetMapping("/libri/ricerca")
public String cercaLibri(@RequestParam("titolo") String titolo, Model model,Authentication authentication) {
    List<Libro> tuttiLibri = libroService.findByTitoloContainingIgnoreCase(titolo);
    
    model.addAttribute("libri", tuttiLibri);
    List<String> pathsImmagini = new LinkedList<>();
    for (Libro libro : tuttiLibri) {
        if(libro.getImmagini() == null || libro.getImmagini().isEmpty()) {
            System.out.println("Libro senza immagini: " + libro.getTitolo());
            continue; // Skip libri without images
        }else{pathsImmagini.add(libro.getImmagini().get(0).getPath());
        System.out.println("Path immagine: " + libro.getImmagini().get(0).getPath());
        }
        
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

@GetMapping("/libri/ordina/titolo")
public String ordinaLibriNome(Model model,Authentication authentication) {
    List<Libro> tuttiLibri = libroService.findAll();
    tuttiLibri.sort((l1, l2) -> l1.getTitolo().compareToIgnoreCase(l2.getTitolo()));
    
    model.addAttribute("libri", tuttiLibri);
    List<String> pathsImmagini = new LinkedList<>();
    for (Libro libro : tuttiLibri) {
        if(libro.getImmagini() == null || libro.getImmagini().isEmpty()) {
            System.out.println("Libro senza immagini: " + libro.getTitolo());
            continue; // Skip libri without images
        }else{pathsImmagini.add(libro.getImmagini().get(0).getPath());
        System.out.println("Path immagine: " + libro.getImmagini().get(0).getPath());
        }
        
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
    

@GetMapping("/libri/ordina/anno")
public String ordinaLibriAnno(Model model,Authentication authentication) {
    List<Libro> tuttiLibri = libroService.findAll();
    tuttiLibri.sort((l1, l2) -> Integer.compare(l1.getAnnoPubblicazione(), l2.getAnnoPubblicazione()));
    
    model.addAttribute("libri", tuttiLibri);
    List<String> pathsImmagini = new LinkedList<>();
    for (Libro libro : tuttiLibri) {
        if(libro.getImmagini() == null || libro.getImmagini().isEmpty()) {
            System.out.println("Libro senza immagini: " + libro.getTitolo());
            continue; // Skip libri without images
        }else{pathsImmagini.add(libro.getImmagini().get(0).getPath());
        System.out.println("Path immagine: " + libro.getImmagini().get(0).getPath());
        }
        
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

@GetMapping("/libri/ordina/voti")
public String ordinaLibriVoti(Model model,Authentication authentication) {
    List<Libro> tuttiLibri = libroService.findAll();
    tuttiLibri.sort((l1, l2) -> Integer.compare(l1.getMediaVotiRecensioni(), l2.getMediaVotiRecensioni()));
    java.util.Collections.reverse(tuttiLibri);
    model.addAttribute("libri", tuttiLibri);
    List<String> pathsImmagini = new LinkedList<>();
    for (Libro libro : tuttiLibri) {
        if(libro.getImmagini() == null || libro.getImmagini().isEmpty()) {
            System.out.println("Libro senza immagini: " + libro.getTitolo());
            continue; // Skip libri without images
        }else{pathsImmagini.add(libro.getImmagini().get(0).getPath());
        System.out.println("Path immagine: " + libro.getImmagini().get(0).getPath());
        }
        
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
