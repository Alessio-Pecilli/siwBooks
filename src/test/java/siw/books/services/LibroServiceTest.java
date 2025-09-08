package siw.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import siw.books.model.Autore;
import siw.books.model.Libro;
import siw.books.repository.LibroRepository;
import siw.books.repository.RecensioneRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LibroService Tests")
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private RecensioneRepository recensioneRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro1;
    private Libro libro2;
    private Autore autore;

    @BeforeEach
    void setUp() {
        libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitolo("Il Piccolo Principe");
        libro1.setAnnoPubblicazione(1943);

        libro2 = new Libro();
        libro2.setId(2L);
        libro2.setTitolo("Il Nome della Rosa");
        libro2.setAnnoPubblicazione(1980);

        autore = new Autore();
        autore.setId(1L);
        autore.setNome("Antoine");
        autore.setCognome("de Saint-Exupéry");
    }

    @Test
    @DisplayName("Dovrebbe restituire tutti i libri")
    void testFindAll() {
        // Given
        List<Libro> expectedLibri = Arrays.asList(libro1, libro2);
        when(libroRepository.findAll()).thenReturn(expectedLibri);

        // When
        List<Libro> actualLibri = libroService.findAll();

        // Then
        assertEquals(expectedLibri, actualLibri);
        assertEquals(2, actualLibri.size());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Dovrebbe trovare un libro per ID quando esiste")
    void testFindByIdWhenExists() {
        // Given
        Long id = 1L;
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro1));

        // When
        Optional<Libro> result = libroService.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(libro1, result.get());
        assertEquals("Il Piccolo Principe", result.get().getTitolo());
        verify(libroRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Dovrebbe restituire Optional vuoto quando il libro non esiste")
    void testFindByIdWhenNotExists() {
        // Given
        Long id = 999L;
        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Libro> result = libroService.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(libroRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Dovrebbe salvare un libro")
    void testSave() {
        // Given
        when(libroRepository.save(libro1)).thenReturn(libro1);

        // When
        Libro savedLibro = libroService.save(libro1);

        // Then
        assertEquals(libro1, savedLibro);
        assertEquals("Il Piccolo Principe", savedLibro.getTitolo());
        verify(libroRepository, times(1)).save(libro1);
    }

    @Test
    @DisplayName("Dovrebbe eliminare un libro per ID")
    void testDeleteById() {
        // Given
        Long id = 1L;
        doNothing().when(libroRepository).deleteById(id);

        // When
        libroService.deleteById(id);

        // Then
        verify(libroRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Dovrebbe trovare libri per autore")
    void testFindByAutore() {
        // Given
        List<Libro> expectedLibri = Arrays.asList(libro1);
        when(libroRepository.findByAutoriContaining(autore)).thenReturn(expectedLibri);

        // When
        List<Libro> actualLibri = libroService.findByAutore(autore);

        // Then
        assertEquals(expectedLibri, actualLibri);
        assertEquals(1, actualLibri.size());
        assertEquals(libro1, actualLibri.get(0));
        verify(libroRepository, times(1)).findByAutoriContaining(autore);
    }

    @Test
    @DisplayName("Dovrebbe trovare tutti i libri per lista di ID")
    void testFindAllById() {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L);
        List<Libro> expectedLibri = Arrays.asList(libro1, libro2);
        when(libroRepository.findAllById(ids)).thenReturn(expectedLibri);

        // When
        List<Libro> actualLibri = libroService.findAllById(ids);

        // Then
        assertEquals(expectedLibri, actualLibri);
        assertEquals(2, actualLibri.size());
        verify(libroRepository, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Dovrebbe trovare i top libri")
    void testFindTopLibri() {
        // Given
        List<Long> topLibriIds = Arrays.asList(1L, 2L);
        List<Libro> expectedLibri = Arrays.asList(libro1, libro2);
        when(libroRepository.findTopLibriIds()).thenReturn(topLibriIds);
        when(libroRepository.findAllById(topLibriIds)).thenReturn(expectedLibri);

        // When
        List<Libro> actualLibri = libroService.findTopLibri();

        // Then
        assertEquals(expectedLibri, actualLibri);
        assertEquals(2, actualLibri.size());
        verify(libroRepository, times(1)).findTopLibriIds();
        verify(libroRepository, times(1)).findAllById(topLibriIds);
    }

    @Test
    @DisplayName("Dovrebbe trovare libri per titolo contenente il testo ignorando il case")
    void testFindByTitoloContainingIgnoreCase() {
        // Given
        String titolo = "piccolo";
        List<Libro> expectedLibri = Arrays.asList(libro1);
        when(libroRepository.findByTitoloContainingIgnoreCase(titolo)).thenReturn(expectedLibri);

        // When
        List<Libro> actualLibri = libroService.findByTitoloContainingIgnoreCase(titolo);

        // Then
        assertEquals(expectedLibri, actualLibri);
        assertEquals(1, actualLibri.size());
        assertEquals(libro1, actualLibri.get(0));
        verify(libroRepository, times(1)).findByTitoloContainingIgnoreCase(titolo);
    }

    @Test
    @DisplayName("Dovrebbe restituire lista vuota quando nessun libro corrisponde al titolo")
    void testFindByTitoloContainingIgnoreCaseNoResults() {
        // Given
        String titolo = "nonEsiste";
        when(libroRepository.findByTitoloContainingIgnoreCase(titolo)).thenReturn(Arrays.asList());

        // When
        List<Libro> actualLibri = libroService.findByTitoloContainingIgnoreCase(titolo);

        // Then
        assertTrue(actualLibri.isEmpty());
        verify(libroRepository, times(1)).findByTitoloContainingIgnoreCase(titolo);
    }

    @Test
    @DisplayName("Dovrebbe gestire correttamente ID null in findById")
    void testFindByIdWithNullId() {
        // Given
        Long id = null;
        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Libro> result = libroService.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(libroRepository, times(1)).findById(id);
    }
}
