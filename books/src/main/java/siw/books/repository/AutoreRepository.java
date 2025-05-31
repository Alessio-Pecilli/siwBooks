package siw.books.repository;

import org.springframework.data.repository.CrudRepository;
import siw.books.model.Autore;

public interface AutoreRepository extends CrudRepository<Autore, Long> {
}