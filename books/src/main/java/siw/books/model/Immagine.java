package siw.books.model;

import jakarta.persistence.*;
@Entity
public class Immagine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeFile;
    private String path;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @OneToOne
    @JoinColumn(name = "autore_id")
    private Autore autore;

    // Getter e Setter
    public Long getId() { return id; }

    public String getNomeFile() { return nomeFile; }
    public void setNomeFile(String nomeFile) { this.nomeFile = nomeFile; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }

    public Autore getAutore() { return autore; }
    public void setAutore(Autore autore) { this.autore = autore; }
}
