package siw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import siw.books.model.Credentials;
import siw.books.model.Utente;
import siw.books.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	private CredentialsRepository credentialsRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public Credentials getCredentialsByUsername(String username) {
		return credentialsRepository.findByUsername(username).get();
	}

	public boolean existsByUsername(String username) {
		return this.credentialsRepository.existsByUsername(username);
	}

	@Transactional
	public void saveCredentials(@Valid Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		this.credentialsRepository.save(credentials);
	}

	public Credentials getCredentialsByUtente(Utente utente) {
		try {
			return this.credentialsRepository.findByUtente(utente).get();
		} catch (Exception e) {
			return null;
		}
	}
	
}
