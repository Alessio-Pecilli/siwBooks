package siw.books.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import siw.books.model.Utente;
import siw.books.repository.UtenteRepository;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
	private CredentialsService credentialsService;

    public Utente getUtente(Long id) {
        Optional<Utente> result = utenteRepository.findById(id);
        return result.orElse(null);
    }

    public void saveUtente(Utente utente) {
        utenteRepository.save(utente);
    }

    public Utente getUtenteCorrente() {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return this.credentialsService.getCredentialsByUsername(userDetails.getUsername()).getUtente();
    }
    
}
