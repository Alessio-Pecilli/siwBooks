package siw.books.controller;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import siw.books.model.Autore;
import siw.books.model.Libro;
import siw.books.services.AutoreService;
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

    @GetMapping("/autori")
    public String getAllAutori(Model model) {
        Iterable<Autore> iterable = autoreService.findAll();
        List<Autore> autori = new ArrayList<>();
        iterable.forEach(autori::add);

        model.addAttribute("autori", autori);
        return "autori";
    }

    @GetMapping("amministratori/autori")
    public String getAllAutoriAdmin(Model model) {
        Iterable<Autore> iterable = autoreService.findAll();
        List<Autore> autori = new ArrayList<>();
        iterable.forEach(autori::add);

        model.addAttribute("autori", autori);
        return "amministratori/autori";
    }

    @GetMapping("amministratori/autori/{id}")
    public String getAutoreAdmin(@PathVariable Long id, Model model) {
        Optional<Autore> optionalAutore = autoreService.findById(id);
        if (optionalAutore.isPresent()) {
            Autore autore = optionalAutore.get();
            List<Libro> libri = libroService.findByAutore(autore);
            model.addAttribute("autore", autore);
            model.addAttribute("libri", libri);
            return "amministratori/dettaglioAutore";
        } else {
            return "redirect:amministratori/autori"; // oppure una pagina 404
        }
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

        // Costruisci anche il nome del file immagine da mostrare
        String nomeFile = (autore.getNome() + " " + autore.getCognome())
                .toLowerCase().replace(" ", "_").replace(".", "") + ".jpg";
        model.addAttribute("fotoAutore", "/images/authors/" + nomeFile);

        return "amministratori/formAutore";
    }
    return "redirect:/amministratori/autori";
}


@PostMapping("/amministratori/autori/modifica/{id}")
public String aggiornaAutore(
        @PathVariable("id") Long id,
        @RequestParam("nome") String nome,
        @RequestParam("cognome") String cognome,
        @RequestParam(value = "dataNascita", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascita,
        @RequestParam(value = "dataMorte", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataMorte,
        @RequestParam(value = "nazionalita", required = false) String nazionalita,
        @RequestParam(value = "foto", required = false) MultipartFile foto,
        @RequestParam(value = "libriIds", required = false) List<Long> libroIds
) {
    Optional<Autore> optionalAutore = autoreService.findById(id);
    if (optionalAutore.isPresent()) {
        Autore autore = optionalAutore.get();
        autore.setNome(nome);
        autore.setCognome(cognome);
        autore.setDataNascita(dataNascita);
        autore.setDataMorte(dataMorte);
        autore.setNazionalita(nazionalita);

        if (libroIds != null) {
            List<Libro> libriAssociati = libroService.findAllById(libroIds);
            autore.setLibri(libriAssociati);
        } else {
            autore.setLibri(List.of());
        }

        if (foto != null && !foto.isEmpty()) {
            try {
                String nomeFile = (nome + " " + cognome).toLowerCase().replace(" ", "_").replace(".", "") + ".jpg";
                Path path = Paths.get("src/main/resources/static/images/authors/", nomeFile);
                Files.copy(foto.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ignored) {}
        }

        autoreService.save(autore);
    }

    return "redirect:/amministratori/autori";
}


    @GetMapping("autori/{id}")
    public String getAutore(@PathVariable Long id, Model model) {
        Optional<Autore> optionalAutore = autoreService.findById(id);
        if (optionalAutore.isPresent()) {
            Autore autore = optionalAutore.get();
            List<Libro> libri = libroService.findByAutore(autore);
            model.addAttribute("autore", autore);
            model.addAttribute("libri", libri);
            return "dettaglioAutore";
        } else {
            return "redirect:/autori"; // oppure una pagina 404
        }
    }


    @GetMapping("/amministratori/elimina/{id}")
    public String deleteAutore(@PathVariable Long id) {
        autoreService.deleteById(id);
        return "redirect:/autori";
    }
}
