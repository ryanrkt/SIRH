package com.app.sirh.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "demande_recrutement")
public class DemandeRecrutement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demandeur_id", nullable = false)
    private Employe demandeur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "poste_id", nullable = false)
    private Poste poste;

    @Column(name = "nombre_poste", nullable = false)
    private Integer nombrePoste = 1;

     @Column(name = "salaire", nullable = false)
    private Integer salaire;

    @Column(name = "date_limite")
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "statut",
        nullable = false,
        columnDefinition = "demande_statut_enum"
    )
    private DemandeStatutEnum statut = DemandeStatutEnum.EN_ATTENTE_RH;

    @Column(
        name = "created_at",
        nullable = false,
        columnDefinition = "TIMESTAMPTZ"
    )
    private OffsetDateTime createdAt;

    public DemandeRecrutement() {
    }

    @PrePersist
    public void avantEnregistrement() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }

        if (statut == null) {
            statut = DemandeStatutEnum.EN_ATTENTE_RH;
        }

        if (nombrePoste == null) {
            nombrePoste = 1;
        }
    }

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

    public Employe getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Employe demandeur) {
        this.demandeur = demandeur;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public Integer getNombrePoste() {
        return nombrePoste;
    }

    public void setNombrePoste(Integer nombrePoste) {
        this.nombrePoste = nombrePoste;
    }

  public DemandeStatutEnum getStatut() {
    return statut;
}

public void setStatut(DemandeStatutEnum statut) {
    this.statut = statut;
}

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getSalaire() {
        return salaire;
    }
    public void setSalaire(Integer salaire) {
        this.salaire = salaire;
    }

    public LocalDate getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(LocalDate dateLimite) {
        this.dateLimite = dateLimite;
    }
}