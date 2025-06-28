package siw.books.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import siw.books.model.Autore;
import siw.books.model.Immagine;
import siw.books.model.Libro;
import siw.books.services.AutoreService;
import siw.books.services.LibroService;

@Controller
public class MainController {
    
    @Autowired
    private LibroService libroService;

    @Autowired
    private AutoreService autoreService;

    @GetMapping("/")
    public String home(Model model,Authentication authentication) {
        List<Libro> topLibri = libroService.findTopLibri();
        List<Autore> topAutori = autoreService.findTopAutori();

        List<String> copertineLibri = new ArrayList<>();
        // Assicurati che topLibri non sia null o vuoto prima di accedere a getImmagini()
        for (Libro libro : topLibri) {
        if (!libro.getImmagini().isEmpty()) {
            copertineLibri.add(libro.getImmagini().get(0).getPath());
        } else {
            copertineLibri.add("/images/books/default.jpg");
        }
    }


    model.addAttribute("topLibri", topLibri);
    model.addAttribute("copertineLibri", copertineLibri);
    model.addAttribute("topAutori", topAutori);
    boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
    
    model.addAttribute("isAuthenticated", isAuthenticated);

        return "home"; 
    }
}
