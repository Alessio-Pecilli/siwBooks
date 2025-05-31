package siw.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import siw.books.model.Autore;
import siw.books.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    // puoi aggiungere query custom se ti servono
    public List<Libro> findByAutoriContaining(Autore autore);
    
}
