package siw.books.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Autore Model Tests")
class AutoreTest {

    private Autore autore;

    @BeforeEach
    void setUp() {
        autore = new Autore();
    }

    @Test
    @DisplayName("Dovrebbe creare un autore con valori predefiniti")
    void testAutoreCreation() {
        assertNotNull(autore);
        assertNull(autore.getId());
        assertNull(autore.getNome());
        assertNull(autore.getCognome());
        assertNull(autore.getDataNascita());
        assertNull(autore.getDataMorte());
        assertNull(autore.getNazionalita());
        assertNull(autore.getFotografia());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il nome")
    void testSetAndGetNome() {
        String nome = "Antoine";
        autore.setNome(nome);
        assertEquals(nome, autore.getNome());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente il cognome")
    void testSetAndGetCognome() {
        String cognome = "de Saint-Exupéry";
        autore.setCognome(cognome);
        assertEquals(cognome, autore.getCognome());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la data di nascita")
    void testSetAndGetDataNascita() {
        LocalDate dataNascita = LocalDate.of(1900, 6, 29);
        autore.setDataNascita(dataNascita);
        assertEquals(dataNascita, autore.getDataNascita());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la data di morte")
    void testSetAndGetDataMorte() {
        LocalDate dataMorte = LocalDate.of(1944, 7, 31);
        autore.setDataMorte(dataMorte);
        assertEquals(dataMorte, autore.getDataMorte());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la nazionalità")
    void testSetAndGetNazionalita() {
        String nazionalita = "Francese";
        autore.setNazionalita(nazionalita);
        assertEquals(nazionalita, autore.getNazionalita());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la fotografia")
    void testSetAndGetFotografia() {
        Immagine fotografia = new Immagine();
        fotografia.setNomeFile("autore.jpg");
        autore.setFotografia(fotografia);
        assertEquals(fotografia, autore.getFotografia());
        assertEquals("autore.jpg", autore.getFotografia().getNomeFile());
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente la lista di libri")
    void testSetAndGetLibri() {
        List<Libro> libri = new ArrayList<>();
        Libro libro = new Libro();
        libro.setTitolo("Il Piccolo Principe");
        libri.add(libro);

        autore.setLibri(libri);
        assertEquals(libri, autore.getLibri());
        assertEquals(1, autore.getLibri().size());
        assertEquals("Il Piccolo Principe", autore.getLibri().get(0).getTitolo());
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la media totale quando non ci sono libri")
    void testGetMediaTotaleWithoutBooks() {
        autore.setLibri(new ArrayList<>());
        int media = autore.getMediaTotale();
        assertEquals(0, media);
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la media totale quando non ci sono libri con recensioni")
    void testGetMediaTotaleWithBooksButNoReviews() {
        List<Libro> libri = new ArrayList<>();
        Libro libro1 = new Libro();
        libro1.setRecensioni(new ArrayList<>());
        Libro libro2 = new Libro();
        libro2.setRecensioni(null);
        libri.add(libro1);
        libri.add(libro2);

        autore.setLibri(libri);
        int media = autore.getMediaTotale();
        assertEquals(0, media);
    }

    @Test
    @DisplayName("Dovrebbe calcolare correttamente la media totale con libri che hanno recensioni")
    void testGetMediaTotaleWithBooksAndReviews() {
        List<Libro> libri = new ArrayList<>();
        
        // Primo libro con media 4
        Libro libro1 = new Libro();
        List<Recensione> recensioni1 = new ArrayList<>();
        Recensione rec1 = new Recensione();
        rec1.setVoto(4);
        Recensione rec2 = new Recensione();
        rec2.setVoto(4);
        recensioni1.add(rec1);
        recensioni1.add(rec2);
        libro1.setRecensioni(recensioni1);
        
        // Secondo libro con media 5
        Libro libro2 = new Libro();
        List<Recensione> recensioni2 = new ArrayList<>();
        Recensione rec3 = new Recensione();
        rec3.setVoto(5);
        recensioni2.add(rec3);
        libro2.setRecensioni(recensioni2);
        
        // Terzo libro senza recensioni (non dovrebbe essere contato)
        Libro libro3 = new Libro();
        libro3.setRecensioni(new ArrayList<>());
        
        libri.add(libro1);
        libri.add(libro2);
        libri.add(libro3);

        autore.setLibri(libri);
        int media = autore.getMediaTotale();
        assertEquals(4, media); // (4+5)/2 = 4
    }

    @Test
    @DisplayName("Dovrebbe impostare e ottenere correttamente l'ID")
    void testSetAndGetId() {
        Long id = 1L;
        autore.setId(id);
        assertEquals(id, autore.getId());
    }
}
