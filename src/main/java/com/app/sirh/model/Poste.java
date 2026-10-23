package com.app.sirh.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "poste", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"departement_id", "titre"})
})
public class Poste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    @Column(name = "titre", nullable = false, length = 150)
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Poste() {
    }

    public Poste(Departement departement, String titre, String description) {
        this.departement = departement;
        this.titre = titre;
        this.description = description;
    }

    // GETTERS et SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // equals et hashCode basés sur l'ID (bonnes pratiques JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poste poste = (Poste) o;
        return id != null && id.equals(poste.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}