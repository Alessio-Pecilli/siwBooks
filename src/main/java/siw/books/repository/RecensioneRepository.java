package siw.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import siw.books.model.Libro;
import siw.books.model.Recensione;
import siw.books.model.Utente;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

List<Recensione> findByLibroId(Long id);

@Query("SELECT r.libro.id FROM Recensione r WHERE r.id = :id")
Long findLibroIdByRecensioneId(@Param("id") Long id);



List<Recensione> findByAutore(Utente autore);

}
