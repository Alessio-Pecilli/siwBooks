package siw.books.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siw.books.model.Immagine;
import siw.books.model.Autore;

@Repository
public interface ImmagineRepository extends JpaRepository<Immagine, Long> {
    
    List<Immagine> findByAutore(Autore autore); // per delete autore
    
    List<Immagine> findByLibroId(Long libroId); // utile se ti serve mostrare tutte le immagini di un libro
}
