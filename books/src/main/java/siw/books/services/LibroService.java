package siw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import siw.books.model.Autore;
import siw.books.model.Libro;
import siw.books.repository.LibroRepository;
import siw.books.repository.RecensioneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private RecensioneRepository recensioneRepository;

    public List<Libro> findAll() {
        return libroRepository.findAll();
    }

    public Optional<Libro> findById(Long id) {
        return libroRepository.findById(id);
    }

    public Libro save(Libro libro) {
        return libroRepository.save(libro);
    }

    public void deleteById(Long id) {
        libroRepository.deleteById(id);
    }

    public List<Libro> findByAutore(Autore autore) {
    return this.libroRepository.findByAutoriContaining(autore);
}


public List<Libro> findAllById(List<Long> ids) {
    return this.libroRepository.findAllById(ids);
}

public List<Libro> findTopLibri() {
    List<Long> ids = libroRepository.findTopLibriIds();
    return libroRepository.findAllById(ids);
}


public List<Libro> findByTitoloContainingIgnoreCase(String titolo) {
    return libroRepository.findByTitoloContainingIgnoreCase(titolo);
}

}
