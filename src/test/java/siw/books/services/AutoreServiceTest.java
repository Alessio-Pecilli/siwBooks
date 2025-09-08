package siw.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import siw.books.model.Autore;
import siw.books.model.Immagine;
import siw.books.model.Libro;
import siw.books.repository.AutoreRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutoreService Tests")
class AutoreServiceTest {

    @Mock
    private AutoreRepository autoreRepository;

    @Mock
    private LibroService libroService;

    @Mock
    private ImmagineService immagineService;

    @InjectMocks
    private AutoreService autoreService;

    private Autore autore1;
    private Autore autore2;
    private Libro libro1;
    private List<Immagine> immagini;

    @BeforeEach
    void setUp() {
        autore1 = new Autore();
        autore1.setId(1L);
        autore1.setNome("Antoine");
        autore1.setCognome("de Saint-Exupéry");

        autore2 = new Autore();
        autore2.setId(2L);
        autore2.setNome("Umberto");
        autore2.setCognome("Eco");

        libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitolo("Il Piccolo Principe");
        libro1.setAutori(new ArrayList<>());

        immagini = new ArrayList<>();
        Immagine immagine = new Immagine();
        immagine.setAutore(autore1);
        immagini.add(immagine);
    }

    @Test
    @DisplayName("Dovrebbe restituire tutti gli autori")
    void testFindAll() {
        // Given
        List<Autore> expectedAutori = Arrays.asList(autore1, autore2);
        when(autoreRepository.findAll()).thenReturn(expectedAutori);

        // When
        Iterable<Autore> actualAutori = autoreService.findAll();

        // Then
        assertEquals(expectedAutori, actualAutori);
        verify(autoreRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Dovrebbe trovare un autore per ID quando esiste")
    void testFindByIdWhenExists() {
        // Given
        Long id = 1L;
        when(autoreRepository.findById(id)).thenReturn(Optional.of(autore1));

        // When
        Optional<Autore> result = autoreService.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(autore1, result.get());
        assertEquals("Antoine", result.get().getNome());
        verify(autoreRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Dovrebbe restituire Optional vuoto quando l'autore non esiste")
    void testFindByIdWhenNotExists() {
        // Given
        Long id = 999L;
        when(autoreRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Autore> result = autoreService.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(autoreRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Dovrebbe salvare un autore senza libri")
    void testSaveAutoreWithoutBooks() {
        // Given
        autore1.setLibri(null);
        when(autoreRepository.save(autore1)).thenReturn(autore1);

        // When
        Autore savedAutore = autoreService.save(autore1);

        // Then
        assertEquals(autore1, savedAutore);
        verify(autoreRepository, times(1)).save(autore1);
    }

    @Test
    @DisplayName("Dovrebbe salvare un autore e aggiornare le relazioni con i libri")
    void testSaveAutoreWithBooks() {
        // Given
        List<Libro> libri = Arrays.asList(libro1);
        autore1.setLibri(libri);
        
        when(autoreRepository.save(autore1)).thenReturn(autore1);

        // When
        Autore savedAutore = autoreService.save(autore1);

        // Then
        assertEquals(autore1, savedAutore);
        assertTrue(libro1.getAutori().contains(autore1));
        verify(autoreRepository, times(1)).save(autore1);
    }

    @Test
    @DisplayName("Non dovrebbe aggiungere duplicati nella lista autori del libro")
    void testSaveAutoreWithBooksNoDuplicates() {
        // Given
        List<Libro> libri = Arrays.asList(libro1);
        autore1.setLibri(libri);
        libro1.getAutori().add(autore1); // Autore già presente
        
        when(autoreRepository.save(autore1)).thenReturn(autore1);

        // When
        Autore savedAutore = autoreService.save(autore1);

        // Then
        assertEquals(autore1, savedAutore);
        assertEquals(1, libro1.getAutori().size()); // Non dovrebbe essere duplicato
        verify(autoreRepository, times(1)).save(autore1);
    }

    @Test
    @DisplayName("Dovrebbe eliminare un autore e aggiornare le relazioni")
    void testDeleteById() {
        // Given
        Long id = 1L;
        List<Libro> libriAutore = Arrays.asList(libro1);
        libro1.getAutori().add(autore1);
        
        when(autoreRepository.findById(id)).thenReturn(Optional.of(autore1));
        when(libroService.findByAutore(autore1)).thenReturn(libriAutore);
        when(immagineService.findByAutore(autore1)).thenReturn(immagini);
        when(libroService.save(any(Libro.class))).thenReturn(libro1);
        doNothing().when(immagineService).save(any(Immagine.class));

        // When
        autoreService.deleteById(id);

        // Then
        assertFalse(libro1.getAutori().contains(autore1));
        assertNull(immagini.get(0).getAutore());
        verify(autoreRepository, times(1)).findById(id);
        verify(libroService, times(1)).findByAutore(autore1);
        verify(immagineService, times(1)).findByAutore(autore1);
        verify(libroService, times(1)).save(libro1);
        verify(immagineService, times(1)).save(immagini.get(0));
        verify(autoreRepository, times(1)).delete(autore1);
    }

    @Test
    @DisplayName("Dovrebbe lanciare eccezione quando si tenta di eliminare un autore inesistente")
    void testDeleteByIdWhenAutoreNotExists() {
        // Given
        Long id = 999L;
        when(autoreRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(Exception.class, () -> autoreService.deleteById(id));
        verify(autoreRepository, times(1)).findById(id);
        verify(autoreRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Dovrebbe trovare i top autori mantenendo l'ordine")
    void testFindTopAutori() {
        // Given
        List<Long> topAutoriIds = Arrays.asList(1L, 2L);
        List<Autore> autori = Arrays.asList(autore1, autore2);
        
        when(autoreRepository.findTopAutoriIds()).thenReturn(topAutoriIds);
        when(autoreRepository.findAllById(topAutoriIds)).thenReturn(autori);

        // When
        List<Autore> result = autoreService.findTopAutori();

        // Then
        assertEquals(2, result.size());
        assertEquals(autore1, result.get(0));
        assertEquals(autore2, result.get(1));
        verify(autoreRepository, times(1)).findTopAutoriIds();
        verify(autoreRepository, times(1)).findAllById(topAutoriIds);
    }

    @Test
    @DisplayName("Dovrebbe gestire correttamente autori null nella lista dei top autori")
    void testFindTopAutoriWithNullValues() {
        // Given
        List<Long> topAutoriIds = Arrays.asList(1L, 2L, 3L);
        List<Autore> autori = Arrays.asList(autore1, null, autore2);
        
        when(autoreRepository.findTopAutoriIds()).thenReturn(topAutoriIds);
        when(autoreRepository.findAllById(topAutoriIds)).thenReturn(autori);

        // When
        List<Autore> result = autoreService.findTopAutori();

        // Then
        assertEquals(2, result.size()); // Solo autori non-null
        assertEquals(autore1, result.get(0));
        assertEquals(autore2, result.get(1));
        verify(autoreRepository, times(1)).findTopAutoriIds();
        verify(autoreRepository, times(1)).findAllById(topAutoriIds);
    }

    @Test
    @DisplayName("Dovrebbe trovare autori per nome o cognome")
    void testFindByNomeOrCognome() {
        // Given
        String searchString = "Antoine";
        List<Autore> expectedAutori = Arrays.asList(autore1);
        when(autoreRepository.findAuthorByString(searchString)).thenReturn(expectedAutori);

        // When
        List<Autore> result = autoreService.findByNomeOrCognome(searchString);

        // Then
        assertEquals(expectedAutori, result);
        assertEquals(1, result.size());
        assertEquals(autore1, result.get(0));
        verify(autoreRepository, times(1)).findAuthorByString(searchString);
    }

    @Test
    @DisplayName("Dovrebbe restituire lista vuota quando nessun autore corrisponde alla ricerca")
    void testFindByNomeOrCognomeNoResults() {
        // Given
        String searchString = "NonEsistente";
        when(autoreRepository.findAuthorByString(searchString)).thenReturn(new ArrayList<>());

        // When
        List<Autore> result = autoreService.findByNomeOrCognome(searchString);

        // Then
        assertTrue(result.isEmpty());
        verify(autoreRepository, times(1)).findAuthorByString(searchString);
    }
}
