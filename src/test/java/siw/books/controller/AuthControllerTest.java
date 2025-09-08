package siw.books.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import siw.books.model.Credentials;
import siw.books.model.Utente;
import siw.books.services.CredentialsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CredentialsService credentialsService;

    private Utente utente;
    private Credentials credentials;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setId(1L);
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setEmail("mario.rossi@email.com");

        credentials = new Credentials();
        credentials.setUsername("mario123");
        credentials.setPassword("password123");
        credentials.setUtente(utente);
    }

    @Test
    @DisplayName("Dovrebbe mostrare la pagina di login")
    void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"));
    }

    @Test
    @DisplayName("Dovrebbe mostrare la pagina di login con errore")
    void testShowLoginPageWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"))
                .andExpect(model().attribute("msgError", "Username o password errati"));
    }

    @Test
    @DisplayName("Dovrebbe mostrare il form di registrazione")
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"))
                .andExpect(model().attributeExists("utente"))
                .andExpect(model().attributeExists("credentials"));
    }

    @Test
    @DisplayName("Dovrebbe registrare un utente con successo")
    void testRegisterUserSuccess() throws Exception {
        // Given
        when(credentialsService.existsByUsername(anyString())).thenReturn(false);
        doNothing().when(credentialsService).saveCredentials(any(Credentials.class));

        // When & Then
        mockMvc.perform(post("/register")
                .param("utente.nome", "Mario")
                .param("utente.cognome", "Rossi")
                .param("utente.email", "mario.rossi@email.com")
                .param("credentials.username", "mario123")
                .param("credentials.password", "password123")
                .param("confermaPwd", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/utente/*"));

        verify(credentialsService, times(1)).existsByUsername("mario123");
        verify(credentialsService, times(1)).saveCredentials(any(Credentials.class));
    }

    @Test
    @DisplayName("Dovrebbe fallire la registrazione se username già esistente")
    void testRegisterUserFailsUsernameExists() throws Exception {
        // Given
        when(credentialsService.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/register")
                .param("utente.nome", "Mario")
                .param("utente.cognome", "Rossi")
                .param("utente.email", "mario.rossi@email.com")
                .param("credentials.username", "mario123")
                .param("credentials.password", "password123")
                .param("confermaPwd", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"))
                .andExpect(model().attribute("msgError", "Username già in uso"));

        verify(credentialsService, times(1)).existsByUsername("mario123");
        verify(credentialsService, never()).saveCredentials(any(Credentials.class));
    }

    @Test
    @DisplayName("Dovrebbe fallire la registrazione se le password non coincidono")
    void testRegisterUserFailsPasswordMismatch() throws Exception {
        // Given
        when(credentialsService.existsByUsername(anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/register")
                .param("utente.nome", "Mario")
                .param("utente.cognome", "Rossi")
                .param("utente.email", "mario.rossi@email.com")
                .param("credentials.username", "mario123")
                .param("credentials.password", "password123")
                .param("confermaPwd", "password456"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"))
                .andExpect(model().attribute("msgError", "Le 2 password scritte non sono uguali"));

        verify(credentialsService, times(1)).existsByUsername("mario123");
        verify(credentialsService, never()).saveCredentials(any(Credentials.class));
    }

    @Test
    @DisplayName("Dovrebbe gestire errori di validazione")
    void testRegisterUserValidationErrors() throws Exception {
        // When & Then (nome vuoto dovrebbe causare errore di validazione)
        mockMvc.perform(post("/register")
                .param("utente.nome", "")
                .param("utente.cognome", "Rossi")
                .param("utente.email", "mario.rossi@email.com")
                .param("credentials.username", "mario123")
                .param("credentials.password", "password123")
                .param("confermaPwd", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"));

        verify(credentialsService, never()).saveCredentials(any(Credentials.class));
    }

    @Test
    @DisplayName("Dovrebbe gestire eccezioni durante la registrazione")
    void testRegisterUserException() throws Exception {
        // Given
        when(credentialsService.existsByUsername(anyString())).thenReturn(false);
        doThrow(new RuntimeException("Database error")).when(credentialsService).saveCredentials(any(Credentials.class));

        // When & Then
        mockMvc.perform(post("/register")
                .param("utente.nome", "Mario")
                .param("utente.cognome", "Rossi")
                .param("utente.email", "mario.rossi@email.com")
                .param("credentials.username", "mario123")
                .param("credentials.password", "password123")
                .param("confermaPwd", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register.html"))
                .andExpect(model().attribute("msgError", "Errore durante la registrazione"));

        verify(credentialsService, times(1)).existsByUsername("mario123");
        verify(credentialsService, times(1)).saveCredentials(any(Credentials.class));
    }
}
