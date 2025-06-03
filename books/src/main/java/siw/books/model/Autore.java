package siw.books.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

@Entity
public class Autore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascita;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataMorte;
    private String nazionalita;

    @ManyToMany(mappedBy = "autori")
    private List<Libro> libri;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Immagine fotografia; 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public LocalDate getDataMorte() {
        return dataMorte;
    }

    public void setDataMorte(LocalDate dataMorte) {
        this.dataMorte = dataMorte;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public Immagine getFotografia() {
        return fotografia;
    }

    public void setFotografia(Immagine fotografia) {
        this.fotografia = fotografia;
    }

    public List<Libro> getLibri() {
    return libri;
}

public void setLibri(List<Libro> libri) {
    this.libri = libri;
}
    
}
