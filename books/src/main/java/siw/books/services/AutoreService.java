package siw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import siw.books.model.Autore;
import siw.books.model.Immagine;
import siw.books.model.Libro;
import siw.books.repository.AutoreRepository;
import siw.books.repository.LibroRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AutoreService {

    @Autowired
    private AutoreRepository autoreRepository;

    @Autowired
    private LibroService libroService;

    @Autowired
    private ImmagineService immagineService;


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



    @Transactional
public void deleteById(Long id) {
    Autore autore = autoreRepository.findById(id).orElseThrow();

    // Rimuovi autore da tutti i libri associati
    for (Libro libro : libroService.findByAutore(autore)) {
        libro.getAutori().remove(autore);
        libroService.save(libro);
    }

    // Rimuovi autore da immagini associate
    List<Immagine> immagini = immagineService.findByAutore(autore);
    for (Immagine img : immagini) {
        img.setAutore(null);
        immagineService.save(img);
    }

    autoreRepository.delete(autore);
}

    


public List<Autore> findTopAutori() {
    List<Long> ids = autoreRepository.findTopAutoriIds();
    Iterable<Autore> iterable = autoreRepository.findAllById(ids);
    
    List<Autore> autori = new ArrayList<>();
    iterable.forEach(autori::add);

    return autori;
}

public List<Autore> findByNomeOrCognome(String nome) {
    return autoreRepository.findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCase(nome, nome);
}


}
