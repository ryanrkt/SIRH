package com.app.sirh.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "offre_emploi")
public class OffreEmploi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demande_recrutement_id", nullable = false)
    private DemandeRecrutement demandeRecrutement;

    @Column(name = "titre", nullable = false, length = 200)
    private String titre;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "profil_recherche", columnDefinition = "TEXT")
    private String profilRecherche;

    @Column(name = "salaire_propose_min", precision = 12, scale = 2)
    private BigDecimal salaireProposeMin;

    @Column(name = "salaire_propose_max", precision = 12, scale = 2)
    private BigDecimal salaireProposeMax;

    @Column(name = "heures_par_semaine_proposees")
    private Integer heuresParSemaineProposees = 40;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 50)
    private OffreStatutEnum statut = OffreStatutEnum.BROUILLON;

    @Column(name = "date_publication")
    private OffsetDateTime datePublication;

    @Column(name = "date_expiration")
    private OffsetDateTime dateExpiration;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public OffreEmploi() {}

    public enum OffreStatutEnum {
        BROUILLON, PUBLIEE, FERMEE, ANNULEE
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DemandeRecrutement getDemandeRecrutement() { return demandeRecrutement; }
    public void setDemandeRecrutement(DemandeRecrutement demandeRecrutement) { this.demandeRecrutement = demandeRecrutement; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProfilRecherche() { return profilRecherche; }
    public void setProfilRecherche(String profilRecherche) { this.profilRecherche = profilRecherche; }
    public BigDecimal getSalaireProposeMin() { return salaireProposeMin; }
    public void setSalaireProposeMin(BigDecimal salaireProposeMin) { this.salaireProposeMin = salaireProposeMin; }
    public BigDecimal getSalaireProposeMax() { return salaireProposeMax; }
    public void setSalaireProposeMax(BigDecimal salaireProposeMax) { this.salaireProposeMax = salaireProposeMax; }
    public Integer getHeuresParSemaineProposees() { return heuresParSemaineProposees; }
    public void setHeuresParSemaineProposees(Integer heuresParSemaineProposees) { this.heuresParSemaineProposees = heuresParSemaineProposees; }
    public OffreStatutEnum getStatut() { return statut; }
    public void setStatut(OffreStatutEnum statut) { this.statut = statut; }
    public OffsetDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(OffsetDateTime datePublication) { this.datePublication = datePublication; }
    public OffsetDateTime getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(OffsetDateTime dateExpiration) { this.dateExpiration = dateExpiration; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffreEmploi that = (OffreEmploi) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}