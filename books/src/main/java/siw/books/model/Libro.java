package siw.books.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private Integer annoPubblicazione;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL)
    private List<Immagine> immagini;

    @ManyToMany
@JoinTable(
    name = "libro_autori",
    joinColumns = @JoinColumn(name = "libro_id"),
    inverseJoinColumns = @JoinColumn(name = "autori_id")
)
    private List<Autore> autori;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Recensione> recensioni = new ArrayList<>();

    // GETTER
    public Long getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public Integer getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public List<Immagine> getImmagini() {
        return immagini;
    }

    public void setImmagini(List<Immagine> immagini) {
        this.immagini = immagini;
    }

    public List<Autore> getAutori() {
        return autori;
    }

    public List<Recensione> getRecensioni() {
        return recensioni;
    }
}
