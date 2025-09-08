package siw.books.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import siw.books.model.Recensione;
import siw.books.model.Utente;
import siw.books.repository.RecensioneRepository;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;
    @Transactional
    public List<Recensione> findByLibroId(Long idLibro) {
    return this.recensioneRepository.findByLibroId(idLibro);
}
    public void salvaRecensione(Recensione recensione) {
       
        recensioneRepository.save(recensione); 
    }

    public void eliminaRecensione(Long id) {
        recensioneRepository.deleteById(id);
    }

    public Long getLibroIdFromRecensione(Long recensioneId) {
    return recensioneRepository.findLibroIdByRecensioneId(recensioneId);
}

public List<Recensione> findByAutore(Utente utente) {
    return recensioneRepository.findByAutore(utente);
}



}
