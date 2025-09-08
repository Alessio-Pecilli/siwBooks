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
        
        
        List<Libro> libri = libroService.findByAutore(autore);
        for (Libro libro : libri) {
            libro.getAutori().remove(autore);
            libroService.save(libro); 
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
    }else {
        autore.setLibri(new ArrayList<>());}

     if (foto != null && !foto.isEmpty()) {
        try {
            System.out.println("Caricamento foto per l'autore: " + nome + " " + cognome);
            
            String nomeFile = (nome + "_" + cognome).toLowerCase().replace(" ", "_").replace(".", "") + ".jpg";
            System.out.println("Nome file: " + nomeFile);
            Path uploadDir = Paths.get("books/src/main/resources/static/images/authors");
            Path filePath = uploadDir.resolve(nomeFile);
            Files.copy(foto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Foto caricata con successo: " + nomeFile);
           
            Immagine immagine = new Immagine();
            immagine.setNomeFile(nomeFile);
            immagine.setPath("/images/authors/" + nomeFile);
            autore.setFotografia(immagine);

        } catch (IOException e) {
            Immagine immagine = new Immagine();
            immagine.setNomeFile("default.jpg");
            immagine.setPath("/images/authors/default.jpg");
            autore.setFotografia(immagine);
            System.err.println("Eccezione: " + e.getMessage());
            e.printStackTrace(); 
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

        
        autore.setNome(autoreModificato.getNome());
        autore.setCognome(autoreModificato.getCognome());
        autore.setDataNascita(autoreModificato.getDataNascita());
        autore.setDataMorte(autoreModificato.getDataMorte());
        autore.setNazionalita(autoreModificato.getNazionalita());
        
        if (libroIds == null) libroIds = new ArrayList<>();

    for (Libro libro : autore.getLibri()) {
    List<Autore> autoriLibro = libro.getAutori();
    autoriLibro.remove(autore);
}

List<Libro> nuoviLibri = new ArrayList<>();
for (Long libroId : libroIds) {
    Optional<Libro> libroOpt = libroService.findById(libroId);
    if (libroOpt.isPresent()) {
        Libro libro = libroOpt.get();
        libro.getAutori().add(autore); 
        nuoviLibri.add(libro);
    }
}

autore.setLibri(nuoviLibri);
    
if (nuovaFoto != null && !nuovaFoto.isEmpty()) {
        try {
            
            
            Immagine foto = autore.getFotografia();
            String nomeFile = (foto != null) ? foto.getNomeFile() : "default.jpg"; 

            Path uploadDir = Paths.get("books/src/main/resources/static/images/authors");
            Path filePath = uploadDir.resolve(nomeFile);
            Files.copy(nuovaFoto.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            

        } catch (IOException e) {
            System.err.println("Eccezione: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    autoreService.save(autore);
    }

    return "redirect:/amministratori/autori";
}
@GetMapping("/autori/filtra")
public String filtraAutori(@RequestParam(required = false) String nome,
                           @RequestParam(required = false) String ordina,
                           Model model, Authentication authentication) {
    List<Autore> autori;
    
    if (nome != null && !nome.isEmpty()) {
        
        autori = autoreService.findByNomeOrCognome(nome);
    } else if ("nome".equalsIgnoreCase(ordina)) {
        autori = (List<Autore>) autoreService.findAll();
        autori.sort((a1, a2) -> a1.getNome().compareToIgnoreCase(a2.getNome()));
    } else if ("anno".equalsIgnoreCase(ordina)) {
        autori = (List<Autore>) autoreService.findAll();
        autori.sort((a1, a2) -> {
        if (a1.getDataNascita() == null && a2.getDataNascita() == null) return 0;
        if (a1.getDataNascita() == null) return 1;
        if (a2.getDataNascita() == null) return -1;
        return a1.getDataNascita().compareTo(a2.getDataNascita());
    });
    } else if ("voto".equalsIgnoreCase(ordina)) {
        autori = (List<Autore>) autoreService.findAll();
        autori.sort((a1, a2) -> Double.compare(a2.getMediaTotale(), a1.getMediaTotale()));
    } else {
        autori = (List<Autore>) autoreService.findAll(); 
    }
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
