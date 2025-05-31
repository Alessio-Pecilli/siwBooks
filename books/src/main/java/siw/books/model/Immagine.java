package siw.books.model;

import jakarta.persistence.*;

@Entity
public class Immagine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeFile; // es. copertina1.jpg
    private String tipoMime; // es. image/jpeg

    @Lob
    @Column(length = 16777215) // 16 MB (puoi adattare)
    private byte[] dati;

    @ManyToOne
    private Libro libro;
}
