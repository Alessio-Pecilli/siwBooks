package siw.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import siw.books.model.Libro;
import siw.books.model.Recensione;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {

    @Query("SELECT r FROM Recensione r LEFT JOIN FETCH r.autore WHERE r.libro.id = :id")
List<Recensione> findByLibroId(@Param("id") Long libroId);

@Query("SELECT r.libro.id FROM Recensione r WHERE r.id = :recensioneId")
Long findLibroIdByRecensioneId(@Param("recensioneId") Long recensioneId);

}
