package siw.books.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import siw.books.model.Immagine;
import siw.books.model.Autore;
import siw.books.repository.ImmagineRepository;

@Service
public class ImmagineService {

    @Autowired
    private ImmagineRepository immagineRepository;

    public List<Immagine> findAll() {
        return immagineRepository.findAll();
    }

    public Immagine findById(Long id) {
        return immagineRepository.findById(id).orElse(null);
    }

    public List<Immagine> findByAutore(Autore autore) {
        return immagineRepository.findByAutore(autore);
    }

    @Transactional
    public void delete(Immagine immagine) {
        immagineRepository.delete(immagine);
    }

    @Transactional
    public void save(Immagine immagine) {
        immagineRepository.save(immagine);
    }
}
