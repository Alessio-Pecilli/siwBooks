package siw.books.repository;
import org.springframework.test.annotation.DirtiesContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import siw.books.model.Autore;
import siw.books.model.Libro;
import siw.books.model.Recensione;
import siw.books.model.Utente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@Transactional
@Rollback
@DisplayName("LibroRepository Integration Tests")
class LibroRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibroRepository libroRepository;

    private Libro libro1;
    private Libro libro2;
    private Autore autore;
    private Utente utente;

    @BeforeEach
    void setUp() {
        // Crea autore
        autore = new Autore();
        autore.setNome("Antoine");
        autore.setCognome("de Saint-Exupéry");
        autore.setNazionalita("Francese");
        entityManager.persistAndFlush(autore);

        // Crea utente per le recensioni
        utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setEmail("mario.rossi@email.com");
        entityManager.persistAndFlush(utente);

        // Crea primo libro
        libro1 = new Libro();
        libro1.setTitolo("Il Piccolo Principe");
        libro1.setAnnoPubblicazione(1943);
        libro1.setAutori(new ArrayList<>());
        libro1.getAutori().add(autore);
        libro1.setRecensioni(new ArrayList<>());
        entityManager.persistAndFlush(libro1);

        // Crea secondo libro
        libro2 = new Libro();
        libro2.setTitolo("Il Nome della Rosa");
        libro2.setAnnoPubblicazione(1980);
        libro2.setAutori(new ArrayList<>());
        libro2.setRecensioni(new ArrayList<>());
        entityManager.persistAndFlush(libro2);

        // Crea recensioni per libro1
        Recensione recensione1 = new Recensione();
        recensione1.setTitolo("Ottimo libro");
        recensione1.setVoto(5);
        recensione1.setTesto("Un capolavoro della letteratura");
        recensione1.setAutore(utente);
        recensione1.setLibro(libro1);
        entityManager.persistAndFlush(recensione1);

        Recensione recensione2 = new Recensione();
        recensione2.setTitolo("Molto bello");
        recensione2.setVoto(4);
        recensione2.setTesto("Un libro che consiglio");
        recensione2.setAutore(utente);
        recensione2.setLibro(libro1);
        entityManager.persistAndFlush(recensione2);

        libro1.getRecensioni().add(recensione1);
        libro1.getRecensioni().add(recensione2);
        entityManager.persistAndFlush(libro1);

        entityManager.clear();
    }

    @Test
    @DisplayName("Dovrebbe trovare tutti i libri")
    void testFindAll() {
        List<Libro> libri = libroRepository.findAll();
        assertEquals(2, libri.size());
    }

    @Test
    @DisplayName("Dovrebbe trovare un libro per ID")
    void testFindById() {
        Optional<Libro> foundLibro = libroRepository.findById(libro1.getId());
        assertTrue(foundLibro.isPresent());
        assertEquals("Il Piccolo Principe", foundLibro.get().getTitolo());
        assertEquals(Integer.valueOf(1943), foundLibro.get().getAnnoPubblicazione());
    }

    @Test
    @DisplayName("Dovrebbe salvare un nuovo libro")
    void testSave() {
        Libro nuovoLibro = new Libro();
        nuovoLibro.setTitolo("1984");
        nuovoLibro.setAnnoPubblicazione(1949);

        Libro savedLibro = libroRepository.save(nuovoLibro);
        assertNotNull(savedLibro.getId());
        assertEquals("1984", savedLibro.getTitolo());

        Optional<Libro> foundLibro = libroRepository.findById(savedLibro.getId());
        assertTrue(foundLibro.isPresent());
        assertEquals("1984", foundLibro.get().getTitolo());
    }

    @Test
    @DisplayName("Dovrebbe eliminare un libro per ID")
    void testDeleteById() {
        Long id = libro2.getId();
        libroRepository.deleteById(id);

        Optional<Libro> deletedLibro = libroRepository.findById(id);
        assertFalse(deletedLibro.isPresent());
    }

    @Test
    @DisplayName("Dovrebbe trovare libri per autore")
    void testFindByAutoriContaining() {
        List<Libro> libri = libroRepository.findByAutoriContaining(autore);
        assertEquals(1, libri.size());
        assertEquals("Il Piccolo Principe", libri.get(0).getTitolo());
    }

    @Test
    @DisplayName("Dovrebbe trovare libri per titolo ignorando il case")
    void testFindByTitoloContainingIgnoreCase() {
        // Test con lowercase
        List<Libro> libri = libroRepository.findByTitoloContainingIgnoreCase("piccolo");
        assertEquals(1, libri.size());
        assertEquals("Il Piccolo Principe", libri.get(0).getTitolo());

        // Test con uppercase
        libri = libroRepository.findByTitoloContainingIgnoreCase("PICCOLO");
        assertEquals(1, libri.size());
        assertEquals("Il Piccolo Principe", libri.get(0).getTitolo());

        // Test con mixed case
        libri = libroRepository.findByTitoloContainingIgnoreCase("PiCcOlO");
        assertEquals(1, libri.size());
        assertEquals("Il Piccolo Principe", libri.get(0).getTitolo());

        // Test con stringa parziale
        libri = libroRepository.findByTitoloContainingIgnoreCase("principe");
        assertEquals(1, libri.size());
        assertEquals("Il Piccolo Principe", libri.get(0).getTitolo());
    }

    @Test
    @DisplayName("Dovrebbe restituire lista vuota quando nessun libro corrisponde al titolo")
    void testFindByTitoloContainingIgnoreCaseNoResults() {
        List<Libro> libri = libroRepository.findByTitoloContainingIgnoreCase("inesistente");
        assertTrue(libri.isEmpty());
    }

    @Test
    @DisplayName("Dovrebbe trovare i top libri per ID")
    void testFindTopLibriIds() {
        List<Long> topLibriIds = libroRepository.findTopLibriIds();
        assertFalse(topLibriIds.isEmpty());
        // Il libro1 dovrebbe essere tra i top (ha recensioni con voti 4 e 5)
        assertTrue(topLibriIds.contains(libro1.getId()));
    }

    @Test
    @DisplayName("Dovrebbe aggiornare un libro esistente")
    void testUpdate() {
        Optional<Libro> libroOpt = libroRepository.findById(libro1.getId());
        assertTrue(libroOpt.isPresent());

        Libro libro = libroOpt.get();
        libro.setTitolo("Il Piccolo Principe - Edizione Speciale");
        libro.setAnnoPubblicazione(2000);

        Libro updatedLibro = libroRepository.save(libro);
        assertEquals("Il Piccolo Principe - Edizione Speciale", updatedLibro.getTitolo());
        assertEquals(Integer.valueOf(2000), updatedLibro.getAnnoPubblicazione());

        // Verifica che sia stato aggiornato nel database
        Optional<Libro> foundLibro = libroRepository.findById(libro1.getId());
        assertTrue(foundLibro.isPresent());
        assertEquals("Il Piccolo Principe - Edizione Speciale", foundLibro.get().getTitolo());
        assertEquals(Integer.valueOf(2000), foundLibro.get().getAnnoPubblicazione());
    }

    @Test
    @DisplayName("Dovrebbe contare correttamente il numero di libri")
    void testCount() {
        long count = libroRepository.count();
        assertEquals(2, count);

        // Aggiungi un altro libro
        Libro nuovoLibro = new Libro();
        nuovoLibro.setTitolo("Test Libro");
        nuovoLibro.setAnnoPubblicazione(2023);
        libroRepository.save(nuovoLibro);

        count = libroRepository.count();
        assertEquals(3, count);
    }
}
