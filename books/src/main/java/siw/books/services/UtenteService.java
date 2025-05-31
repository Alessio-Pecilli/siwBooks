package siw.books.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import siw.books.model.Utente;
import siw.books.repository.UtenteRepository;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente getUtente(Long id) {
        Optional<Utente> result = utenteRepository.findById(id);
        return result.orElse(null);
    }

    public void saveUtente(Utente utente) {
        utenteRepository.save(utente);
    }
    
}
