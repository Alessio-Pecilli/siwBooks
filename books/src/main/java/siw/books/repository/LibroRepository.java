package siw.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import siw.books.model.Autore;
import siw.books.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    // puoi aggiungere query custom se ti servono
    public List<Libro> findByAutoriContaining(Autore autore);
    
    @Query(value = "SELECT r.libro_id " +
               "FROM recensione r " +
               "GROUP BY r.libro_id " +
               "ORDER BY AVG(r.voto) DESC " +
               "LIMIT 2", nativeQuery = true)
List<Long> findTopLibriIds();
List<Libro> findByTitoloContainingIgnoreCase(String titolo);


    
}
