package siw.books.controller;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import siw.books.model.Autore;
import siw.books.model.Credentials;
import siw.books.model.Immagine;
import siw.books.model.Libro;
import siw.books.services.AutoreService;
import siw.books.services.CredentialsService;
import siw.books.services.LibroService;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller

public class AutoreController {

    @Autowired
    private AutoreService autoreService;

    @Autowired
    private LibroService libroService;

    
    @Autowired
    private CredentialsService credentialsService;

    

    @GetMapping({"/autori", "/amministratori/autori"})
public String getAllAutori(Model model, Authentication authentication) {
    Iterable<Autore> iterable = autoreService.findAll();
    List<Autore> autori = new ArrayList<>();
    iterable.forEach(autori::add);
    model.addAttribute("autori", autori);

    if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/autori";
        }
    }

    return "autori";
}



    @PostMapping("/amministratori/eliminaAutore/{id}")
public String eliminaAutore(@PathVariable Long id) {
    Optional<Autore> optionalAutore = autoreService.findById(id);
    if (optionalAutore.isPresent()) {
        Autore autore = optionalAutore.get();
        
        // Rimuove l'autore da tutti i libri prima dell'eliminazione
        List<Libro> libri = libroService.findByAutore(autore);
        for (Libro libro : libri) {
            libro.getAutori().remove(autore);
            libroService.save(libro); // salva le modifiche sul libro
        }

        autoreService.deleteById(id);
    }
    return "redirect:/amministratori/autori";
}

@GetMapping("/amministratori/autori/new")
public String formNewAutore(Model model) {
    model.addAttribute("autore", new Autore());
    model.addAttribute("libri", libroService.findAll());
    return "amministratori/formAutore";
}

@PostMapping("amministratori/autori/crea")
public String saveAutore(
        @RequestParam("nome") String nome,
        @RequestParam("cognome") String cognome,
        @RequestParam(value = "dataNascita", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascita,
        @RequestParam(value = "dataMorte", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataMorte,
        @RequestParam(value = "nazionalita", required = false) String nazionalita,
        @RequestParam(value = "foto", required = false) MultipartFile foto,
        @RequestParam(value = "libriIds", required = false) List<Long> libriIds
) {
    System.out.println("Creazione nuovo autore: " + nome + " " + cognome);
    Autore autore = new Autore();
    autore.setNome(nome);
    autore.setCognome(cognome);
    autore.setDataNascita(dataNascita);
    autore.setDataMorte(dataMorte);
    autore.setNazionalita(nazionalita);
    
    if (libriIds != null) {
        List<Libro> libri = libroService.findAllById(libriIds);
        autore.setLibri(libri);
    }

     if (foto != null && !foto.isEmpty()) {
        try {
            System.out.println("Caricamento foto per l'autore: " + nome + " " + cognome);
            // Costruisci nome e path
            String nomeFile = (nome + "_" + cognome).toLowerCase().replace(" ", "_").replace(".", "") + ".jpg";
            System.out.println("Nome file: " + nomeFile);
            Path uploadDir = Paths.get("books/src/main/resources/static/images/authors");
            Path filePath = uploadDir.resolve(nomeFile);
            Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Foto caricata con successo: " + nomeFile);
            // Crea oggetto Immagine e collega all'autore
            Immagine immagine = new Immagine();
            immagine.setNomeFile(nomeFile);
            immagine.setPath("/images/authors/" + nomeFile);
            autore.setFotografia(immagine);

        } catch (IOException e) {
            System.err.println("Eccezione: " + e.getMessage());
            e.printStackTrace(); // o loggalo
        }
    }else{
        Immagine immagine = new Immagine();
            immagine.setNomeFile("default.jpg");
            immagine.setPath("/images/authors/default.jpg");
            autore.setFotografia(immagine);
    }

    autoreService.save(autore);
    return "redirect:/amministratori/autori";
}

@GetMapping("/amministratori/autori/modifica/{id}")
public String mostraFormModifica(@PathVariable Long id, Model model) {
    Optional<Autore> autoreOpt = autoreService.findById(id);
    if (autoreOpt.isPresent()) {
        Autore autore = autoreOpt.get();
        model.addAttribute("autore", autore);
        model.addAttribute("libri", libroService.findAll());

        return "amministratori/formAutore";
    }
    return "redirect:/amministratori/autori";
}


@PostMapping("/amministratori/autori/modifica/{id}")
public String modificaAutore(@PathVariable Long id,
                             @ModelAttribute Autore autoreModificato,
                             @RequestParam(name = "libriIds", required = false) List<Long> libroIds,
                             @RequestParam("foto") MultipartFile nuovaFoto,
                             Model model) throws IOException {

    Optional<Autore> opt = autoreService.findById(id);
    if (opt.isPresent()) {
        Autore autore = opt.get();

        // Aggiorna campi base
        autore.setNome(autoreModificato.getNome());
        autore.setCognome(autoreModificato.getCognome());
        autore.setDataNascita(autoreModificato.getDataNascita());
        autore.setDataMorte(autoreModificato.getDataMorte());
        autore.setNazionalita(autoreModificato.getNazionalita());
        List<Libro> libroSelezionati = new ArrayList<>();
    for (Long autoreId : libroIds) {
        libroService.findById(autoreId).ifPresent(libroSelezionati::add);
    }
    autore.setLibri(libroSelezionati);
    
if (nuovaFoto != null && !nuovaFoto.isEmpty()) {
        try {
            
            // Costruisci nome e path
            String nomeFile = autore.getFotografia().getNomeFile();
            Path uploadDir = Paths.get("books/src/main/resources/static/images/authors");
            Path filePath = uploadDir.resolve(nomeFile);
            Files.copy(nuovaFoto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            

        } catch (IOException e) {
            System.err.println("Eccezione: " + e.getMessage());
            e.printStackTrace(); // o loggalo
        }
    }

    autoreService.save(autore);
    }

    return "redirect:/amministratori/autori";
}

@GetMapping("/autori/ricerca")
public String cercaAutori(@RequestParam("nome") String nome, Model model, Authentication authentication) {
    List<Autore> autoriTrovati = autoreService.findByNomeOrCognome(nome);
    model.addAttribute("autori", autoriTrovati);
   if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/autori";
        }
    }

    return "autori";
}

@GetMapping("/autori/ordina/nome")
public String ordinaAutoriNome(Model model, Authentication authentication) {
    List<Autore> autoriTrovati = (List<Autore>) autoreService.findAll();
    autoriTrovati.sort((a1, a2) -> a1.getNome().compareToIgnoreCase(a2.getNome()));
    model.addAttribute("autori", autoriTrovati);
   if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/autori";
        }
    }

    return "autori";
}

@GetMapping("/autori/ordina/anno")
public String ordinaAutoriAnno(Model model, Authentication authentication) {
    List<Autore> autoriTrovati = (List<Autore>) autoreService.findAll();
    autoriTrovati.sort((a1, a2) -> {
        if (a1.getDataNascita() == null && a2.getDataNascita() == null) return 0;
        if (a1.getDataNascita() == null) return 1;
        if (a2.getDataNascita() == null) return -1;
        return a1.getDataNascita().compareTo(a2.getDataNascita());
    });
    model.addAttribute("autori", autoriTrovati);
   if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/autori";
        }
    }

    return "autori";
}

@GetMapping("/autori/ordina/voto")
public String ordinaAutoriVoto(Model model, Authentication authentication) {
    List<Autore> autoriTrovati = (List<Autore>) autoreService.findAll();
    autoriTrovati.sort((a1, a2) -> Double.compare(a2.getMediaTotale(), a1.getMediaTotale()));
    model.addAttribute("autori", autoriTrovati);
   if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/autori";
        }
    }

    return "autori";
}





    @GetMapping({"/autori/{id}", "/amministratori/autori/{id}"})
public String getAutore(@PathVariable Long id, Model model, Authentication authentication) {
    Optional<Autore> optionalAutore = autoreService.findById(id);
    if (optionalAutore.isEmpty()) {
        return "redirect:/autori";
    }

    Autore autore = optionalAutore.get();
    List<Libro> libri = libroService.findByAutore(autore);
    model.addAttribute("autore", autore);
    model.addAttribute("media", autore.getMediaTotale());
    model.addAttribute("libri", libri);
    
    if (authentication != null) {
        String username = authentication.getName();
        Credentials credentials = credentialsService.getCredentialsByUsername(username);
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "amministratori/dettaglioAutore";
        }
        
    }

    return "dettaglioAutore";
}

    @PostMapping("/amministratori/elimina/{id}")
    public String deleteAutore(@PathVariable Long id) {
        autoreService.deleteById(id);
        return "redirect:/amministratori/autori";
    }
}
