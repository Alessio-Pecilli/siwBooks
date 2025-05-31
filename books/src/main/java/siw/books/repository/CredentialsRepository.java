package siw.books.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import siw.books.model.Credentials;
import siw.books.model.Utente;


public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
	
	public Optional<Credentials> findByUsername(String username);
	
	public Optional<Credentials> findByUtente(Utente utente);
	
	public boolean existsByUsername(String username);
}
