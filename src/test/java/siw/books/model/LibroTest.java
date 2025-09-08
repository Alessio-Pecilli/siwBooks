package siw.books.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Libro Model Tests")
class LibroTest {

    private Libro libro;
    private List<Recensione> recensioni;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        recensioni = new ArrayList<>();
    }

    @Test
    @DisplayName("Dovrebbe creare un libro con valori predefiniti")
    void testLibroCreation() {
        assertNotNull(libro);
        assertNull(libro.getId());
        assertNull(libro.getTitolo());
        assertNull(libro.getAnnoPubblicazione());
        assertNotNull(libro.getImmagini());
        assertTrue(libro.getImmagini().isEmpty());
        assertNotNull(libro.getRecensioni());
        assertTrue(libro.getRecensioni().isEmpty());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il titolo")
    void testSetAndGetTitolo() {
        String titolo = "Il Piccolo Principe";
        libro.setTitolo(titolo);
        assertEquals(titolo, libro.getTitolo());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente l'anno di pubblicazione")
    void testSetAndGetAnnoPubblicazione() {
        Integer anno = 1943;
        libro.setAnnoPubblicazione(anno);
        assertEquals(anno, libro.getAnnoPubblicazione());
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la media voti quando non ci sono recensioni")
    void testGetMediaVotiRecensioniWithoutReviews() {
        int media = libro.getMediaVotiRecensioni();
        assertEquals(0, media);
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la media voti con una recensione")
    void testGetMediaVotiRecensioniWithOneReview() {
        Recensione recensione = new Recensione();
        recensione.setVoto(5);
        recensioni.add(recensione);
        libro.setRecensioni(recensioni);

        int media = libro.getMediaVotiRecensioni();
        assertEquals(5, media);
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la media voti con più recensioni")
    void testGetMediaVotiRecensioniWithMultipleReviews() {
        Recensione recensione1 = new Recensione();
        recensione1.setVoto(4);
        Recensione recensione2 = new Recensione();
        recensione2.setVoto(5);
        Recensione recensione3 = new Recensione();
        recensione3.setVoto(3);
        
        recensioni.add(recensione1);
        recensioni.add(recensione2);
        recensioni.add(recensione3);
        libro.setRecensioni(recensioni);

        int media = libro.getMediaVotiRecensioni();
        assertEquals(4, media); // (4+5+3)/3 = 4
    }

    @Test
    @DisplayName("Dovrebbe gestire correttamente lista di recensioni null")
    void testGetMediaVotiRecensioniWithNullReviews() {
        libro.setRecensioni(null);
        int media = libro.getMediaVotiRecensioni();
        assertEquals(0, media);
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la lista di autori")
    void testSetAndGetAutori() {
        List<Autore> autori = new ArrayList<>();
        Autore autore = new Autore();
        autore.setNome("Antoine");
        autore.setCognome("de Saint-Exupéry");
        autori.add(autore);

        libro.setAutori(autori);
        assertEquals(autori, libro.getAutori());
        assertEquals(1, libro.getAutori().size());
        assertEquals("Antoine", libro.getAutori().get(0).getNome());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la lista di immagini")
    void testSetAndGetImmagini() {
        List<Immagine> immagini = new ArrayList<>();
        Immagine immagine = new Immagine();
        immagine.setNomeFile("copertina.jpg");
        immagini.add(immagine);

        libro.setImmagini(immagini);
        assertEquals(immagini, libro.getImmagini());
        assertEquals(1, libro.getImmagini().size());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente l'ID")
    void testSetAndGetId() {
        Long id = 1L;
        libro.setId(id);
        assertEquals(id, libro.getId());
    }
}
