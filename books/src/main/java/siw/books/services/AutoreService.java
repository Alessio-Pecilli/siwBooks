package siw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siw.books.model.Autore;
import siw.books.model.Libro;
import siw.books.repository.AutoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AutoreService {

    @Autowired
    private AutoreRepository autoreRepository;

    @Autowired
    private LibroService libroService;

    public Iterable<Autore> findAll() {
        return autoreRepository.findAll();
    }

    public Optional<Autore> findById(Long id) {
        return autoreRepository.findById(id);
    }

    public Autore save(Autore autore) {
    if (autore.getLibri() != null) {
        for (Libro libro : autore.getLibri()) {
            if (!libro.getAutori().contains(autore)) {
                libro.getAutori().add(autore);  // Aggiorna il lato opposto della relazione
            }
        }
    }
    return autoreRepository.save(autore);
}



    public void deleteById(Long autoreId) {
    Optional<Autore> optional = autoreRepository.findById(autoreId);
    if (optional.isPresent()) {
        Autore autore = optional.get();
        List<Libro> libri = libroService.findByAutore(autore);

        for (Libro libro : libri) {
            libro.getAutori().remove(autore);
            libroService.save(libro); // salva il libro aggiornato senza quell'autore
        }

        autoreRepository.deleteById(autoreId);
    }
}
}
