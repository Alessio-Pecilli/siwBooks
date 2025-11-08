package siw.books.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BasicIntegrationTest {

    @Test
    void contextLoads() {
        // Test banalissimo: verifica che il contesto Spring si avvii
        assertTrue(true);
    }
}
