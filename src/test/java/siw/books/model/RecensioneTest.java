package siw.books.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Recensione Model Tests")
class RecensioneTest {

    private Recensione recensione;

    @BeforeEach
    void setUp() {
        recensione = new Recensione();
    }

    @Test
    @DisplayName("Dovrebbe creare una recensione con valori predefiniti")
    void testRecensioneCreation() {
        assertNotNull(recensione);
        assertNull(recensione.getId());
        assertNull(recensione.getTitolo());
        assertEquals(0, recensione.getVoto());
        assertNull(recensione.getTesto());
        assertNull(recensione.getAutore());
        assertNull(recensione.getLibro());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente l'ID")
    void testSetAndGetId() {
        Long id = 1L;
        recensione.setId(id);
        assertEquals(id, recensione.getId());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il titolo")
    void testSetAndGetTitolo() {
        String titolo = "Ottimo libro!";
        recensione.setTitolo(titolo);
        assertEquals(titolo, recensione.getTitolo());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il voto")
    void testSetAndGetVoto() {
        int voto = 5;
        recensione.setVoto(voto);
        assertEquals(voto, recensione.getVoto());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il testo")
    void testSetAndGetTesto() {
        String testo = "Un libro fantastico che consiglio a tutti. La storia è coinvolgente e i personaggi ben sviluppati.";
        recensione.setTesto(testo);
        assertEquals(testo, recensione.getTesto());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente l'autore")
    void testSetAndGetAutore() {
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        
        recensione.setAutore(utente);
        assertEquals(utente, recensione.getAutore());
        assertEquals("Mario", recensione.getAutore().getNome());
        assertEquals("Rossi", recensione.getAutore().getCognome());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il libro")
    void testSetAndGetLibro() {
        Libro libro = new Libro();
        libro.setTitolo("Il Piccolo Principe");
        libro.setAnnoPubblicazione(1943);
        
        recensione.setLibro(libro);
        assertEquals(libro, recensione.getLibro());
        assertEquals("Il Piccolo Principe", recensione.getLibro().getTitolo());
        assertEquals(Integer.valueOf(1943), recensione.getLibro().getAnnoPubblicazione());
    }

    @Test
    @DisplayName("Dovrebbe gestire correttamente i valori limite per il voto")
    void testVotoLimits() {
        // Test voto minimo
        recensione.setVoto(1);
        assertEquals(1, recensione.getVoto());
        
        // Test voto massimo
        recensione.setVoto(5);
        assertEquals(5, recensione.getVoto());
        
        // Test voto fuori range (il modello non ha validazione, ma testiamo comunque)
        recensione.setVoto(0);
        assertEquals(0, recensione.getVoto());
        
        recensione.setVoto(6);
        assertEquals(6, recensione.getVoto());
    }

    @Test
    @DisplayName("Dovrebbe gestire correttamente stringhe vuote")
    void testEmptyStrings() {
        recensione.setTitolo("");
        assertEquals("", recensione.getTitolo());
        
        recensione.setTesto("");
        assertEquals("", recensione.getTesto());
    }

    @Test
    @DisplayName("Dovrebbe gestire correttamente valori null")
    void testNullValues() {
        recensione.setTitolo(null);
        assertNull(recensione.getTitolo());
        
        recensione.setTesto(null);
        assertNull(recensione.getTesto());
        
        recensione.setAutore(null);
        assertNull(recensione.getAutore());
        
        recensione.setLibro(null);
        assertNull(recensione.getLibro());
    }
}
