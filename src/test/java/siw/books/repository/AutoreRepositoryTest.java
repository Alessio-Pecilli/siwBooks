
package siw.books.repository;
import org.springframework.test.annotation.DirtiesContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import siw.books.model.Autore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
@DisplayName("AutoreRepository Integration Tests")
class AutoreRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AutoreRepository autoreRepository;

    private Autore autore1;
    private Autore autore2;

    @BeforeEach
    void setUp() {
        autore1 = new Autore();
        autore1.setNome("Antoine");
        autore1.setCognome("de Saint-Exupéry");
        autore1.setDataNascita(LocalDate.of(1900, 6, 29));
        autore1.setNazionalita("Francese");

        autore2 = new Autore();
        autore2.setNome("Umberto");
        autore2.setCognome("Eco");
        autore2.setDataNascita(LocalDate.of(1932, 1, 5));
        autore2.setNazionalita("Italiana");
    }

    @Test
    @DisplayName("Dovrebbe salvare e recuperare un autore")
    void testSaveAndFindAutore() {
        // Given
        Autore savedAutore = entityManager.persistAndFlush(autore1);

        // When
        Autore foundAutore = autoreRepository.findById(savedAutore.getId()).orElse(null);

        // Then
        assertNotNull(foundAutore);
        assertEquals("Antoine", foundAutore.getNome());
        assertEquals("de Saint-Exupéry", foundAutore.getCognome());
        assertEquals("Francese", foundAutore.getNazionalita());
    }

    @Test
    @DisplayName("Dovrebbe trovare tutti gli autori")
    void testFindAll() {
        // Given
        entityManager.persist(autore1);
        entityManager.persist(autore2);
        entityManager.flush();

        // When
        Iterable<Autore> autori = autoreRepository.findAll();
        List<Autore> autoriList = new ArrayList<>();
        autori.forEach(autoriList::add);

        // Then
        assertEquals(2, autoriList.size());
    }

    @Test
    @DisplayName("Dovrebbe trovare autori per nome o cognome")
    void testFindAuthorByString() {
        // Given
        entityManager.persist(autore1);
        entityManager.persist(autore2);
        entityManager.flush();

        // When - Ricerca per nome
        List<Autore> risultatiNome = autoreRepository.findAuthorByString("Antoine");
        
        // When - Ricerca per cognome
        List<Autore> risultatiCognome = autoreRepository.findAuthorByString("Eco");
        
        // When - Ricerca per nome e cognome
        List<Autore> risultatiCompleto = autoreRepository.findAuthorByString("Umberto Eco");

        // Then
        assertEquals(1, risultatiNome.size());
        assertEquals("Antoine", risultatiNome.get(0).getNome());
        
        assertEquals(1, risultatiCognome.size());
        assertEquals("Eco", risultatiCognome.get(0).getCognome());
        
        assertEquals(1, risultatiCompleto.size());
        assertEquals("Umberto", risultatiCompleto.get(0).getNome());
        assertEquals("Eco", risultatiCompleto.get(0).getCognome());
    }

    @Test
    @DisplayName("Dovrebbe essere case-insensitive nella ricerca")
    void testFindAuthorByStringCaseInsensitive() {
        // Given
        entityManager.persist(autore1);
        entityManager.flush();

        // When
        List<Autore> risultatiLowercase = autoreRepository.findAuthorByString("antoine");
        List<Autore> risultatiUppercase = autoreRepository.findAuthorByString("ANTOINE");
        List<Autore> risultatiMixedCase = autoreRepository.findAuthorByString("AnToInE");

        // Then
        assertEquals(1, risultatiLowercase.size());
        assertEquals(1, risultatiUppercase.size());
        assertEquals(1, risultatiMixedCase.size());
        
        assertEquals("Antoine", risultatiLowercase.get(0).getNome());
        assertEquals("Antoine", risultatiUppercase.get(0).getNome());
        assertEquals("Antoine", risultatiMixedCase.get(0).getNome());
    }

    @Test
    @DisplayName("Dovrebbe restituire lista vuota se nessun autore corrisponde alla ricerca")
    void testFindAuthorByStringNoResults() {
        // Given
        entityManager.persist(autore1);
        entityManager.flush();

        // When
        List<Autore> risultati = autoreRepository.findAuthorByString("NonEsistente");

        // Then
        assertTrue(risultati.isEmpty());
    }

    @Test
    @DisplayName("Dovrebbe trovare autori con ricerca parziale")
    void testFindAuthorByStringPartialMatch() {
        // Given
        entityManager.persist(autore1);
        entityManager.persist(autore2);
        entityManager.flush();

        // When
        List<Autore> risultatiAnt = autoreRepository.findAuthorByString("Ant");
        List<Autore> risultatiUmb = autoreRepository.findAuthorByString("Umb");

        // Then
        assertEquals(1, risultatiAnt.size());
        assertEquals("Antoine", risultatiAnt.get(0).getNome());
        
        assertEquals(1, risultatiUmb.size());
        assertEquals("Umberto", risultatiUmb.get(0).getNome());
    }

    @Test
    @DisplayName("Dovrebbe eliminare un autore")
    void testDeleteAutore() {
        // Given
        Autore savedAutore = entityManager.persistAndFlush(autore1);
        Long autoreId = savedAutore.getId();

        // When
        autoreRepository.deleteById(autoreId);
        entityManager.flush();

        // Then
        assertFalse(autoreRepository.findById(autoreId).isPresent());
    }

    @Test
    @DisplayName("Dovrebbe gestire stringhe di ricerca vuote")
    void testFindAuthorByStringEmptySearch() {
        // Given
        entityManager.persist(autore1);
        entityManager.persist(autore2);
        entityManager.flush();

        // When
        List<Autore> risultatiStringaVuota = autoreRepository.findAuthorByString("");

        // Then
        assertEquals(2, risultatiStringaVuota.size());
    }
}
