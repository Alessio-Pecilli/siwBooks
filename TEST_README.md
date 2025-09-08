# Test Suite per SiwBooks

Questa è una suite completa di test di unità e integrazione per l'applicazione SiwBooks, implementata utilizzando JUnit 5 e Mockito.

## Struttura dei Test

### 📁 Test dei Modelli (`src/test/java/siw/books/model/`)
- **LibroTest.java**: Test per la classe Libro, inclusi i test per il calcolo della media voti
- **AutoreTest.java**: Test per la classe Autore, inclusi i test per il calcolo della media totale
- **RecensioneTest.java**: Test per la classe Recensione con validazione dei campi

### 📁 Test dei Servizi (`src/test/java/siw/books/services/`)
- **LibroServiceTest.java**: Test del service layer per i libri usando Mockito
- **AutoreServiceTest.java**: Test del service layer per gli autori con mock delle dipendenze

### 📁 Test dei Controller (`src/test/java/siw/books/controller/`)
- **AuthControllerTest.java**: Test del controller di autenticazione con MockMvc

### 📁 Test di Integrazione (`src/test/java/siw/books/repository/`)
- **LibroRepositoryTest.java**: Test di integrazione per la repository dei libri con database H2 in-memory

## Tecnologie Utilizzate

- **JUnit 5**: Framework per i test di unità
- **Mockito**: Framework per la creazione di mock objects
- **Spring Boot Test**: Per test di integrazione e test slice
- **MockMvc**: Per test dei controller web
- **@DataJpaTest**: Per test delle repository con database in-memory

## Come Eseguire i Test

### Eseguire tutti i test
```bash
./mvnw test
```

### Eseguire test specifici per categoria

#### Test dei modelli
```bash
./mvnw test -Dtest="siw.books.model.**"
```

#### Test dei servizi
```bash
./mvnw test -Dtest="siw.books.services.**"
```

#### Test dei controller
```bash
./mvnw test -Dtest="siw.books.controller.**"
```

#### Test delle repository
```bash
./mvnw test -Dtest="siw.books.repository.**"
```

### Eseguire la test suite completa
```bash
./mvnw test -Dtest="SiwBooksTestSuite"
```

## Report di Copertura

Per generare un report di copertura del codice, aggiungi il plugin JaCoCo al `pom.xml`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Poi esegui:
```bash
./mvnw clean test jacoco:report
```

Il report sarà disponibile in `target/site/jacoco/index.html`.

## Tipologie di Test Implementate

### 1. Test di Unità (Unit Tests)
- Test dei singoli metodi delle classi model
- Test dei service layer con mock delle dipendenze
- Test delle business logic senza dipendenze esterne

### 2. Test di Integrazione (Integration Tests)
- Test delle repository con database reale (H2 in-memory)
- Test delle query JPA personalizzate
- Test delle relazioni tra entità

### 3. Test dei Controller (Web Layer Tests)
- Test degli endpoint REST/MVC
- Test della validazione dei form
- Test della gestione degli errori
- Test dell'autenticazione e autorizzazione

## Esempi di Test

### Test di Unità con Mock
```java
@ExtendWith(MockitoExtension.class)
class LibroServiceTest {
    @Mock
    private LibroRepository libroRepository;
    
    @InjectMocks
    private LibroService libroService;
    
    @Test
    void testFindAll() {
        // Given
        when(libroRepository.findAll()).thenReturn(expectedLibri);
        
        // When
        List<Libro> result = libroService.findAll();
        
        // Then
        assertEquals(expectedLibri, result);
    }
}
```

### Test di Integrazione
```java
@DataJpaTest
class LibroRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Test
    void testFindByTitoloContaining() {
        // Test con database reale
    }
}
```

### Test del Controller
```java
@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CredentialsService credentialsService;
    
    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login.html"));
    }
}
```

## Best Practices Implementate

1. **Arrange-Act-Assert Pattern**: Struttura chiara dei test
2. **Test Isolation**: Ogni test è indipendente dagli altri
3. **Descriptive Names**: Nomi dei test che descrivono chiaramente il comportamento
4. **Mock Objects**: Uso di mock per isolare le unità sotto test
5. **Edge Cases**: Test dei casi limite e degli errori
6. **Data Setup**: Uso di `@BeforeEach` per la preparazione dei dati di test

## Copertura dei Test

I test coprono:
- ✅ Metodi getter/setter dei model
- ✅ Business logic (calcolo medie, validazioni)
- ✅ CRUD operations nei service
- ✅ Query personalizzate nelle repository
- ✅ Endpoint del controller
- ✅ Gestione degli errori
- ✅ Validazione dei dati
- ✅ Relazioni tra entità JPA

## Note

- I test utilizzano un database H2 in-memory per i test di integrazione
- Le dipendenze esterne sono mockate per garantire l'isolamento
- I test sono eseguiti in parallelo per migliorare le performance
- Ogni test ha un nome descrittivo e un display name per facilitare la lettura dei report
