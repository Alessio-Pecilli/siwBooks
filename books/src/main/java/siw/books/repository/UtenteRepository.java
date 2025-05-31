package siw.books.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import siw.books.model.Utente;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Long> {
}
 