package com.app.sirh.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(
    name = "approbation_recrutement"  
)
public class ApprobationRecrutement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "demande_recrutement_id",
        nullable = false
    )
    private DemandeRecrutement demandeRecrutement;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "etape",
        nullable = false,
        columnDefinition = "etape_approbation_enum"
    )
    private EtapeApprobationEnum etape;

    @ManyToOne
    @JoinColumn(name = "valideur_id")
    private Employe valideur;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
        name = "statut_etape",
        nullable = false,
        columnDefinition = "statut_etape_enum"
    )
    private StatutEtapeEnum statutEtape = StatutEtapeEnum.EN_ATTENTE;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "valider_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime validerAt;

    public ApprobationRecrutement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemandeRecrutement getDemandeRecrutement() {
        return demandeRecrutement;
    }

    public void setDemandeRecrutement(
            DemandeRecrutement demandeRecrutement) {
        this.demandeRecrutement = demandeRecrutement;
    }

 public EtapeApprobationEnum getEtape() {
    return etape;
}

public void setEtape(EtapeApprobationEnum etape) {
    this.etape = etape;
}

    public Employe getValideur() {
        return valideur;
    }

    public void setValideur(Employe valideur) {
        this.valideur = valideur;
    }

public StatutEtapeEnum getStatutEtape() {
    return statutEtape;
}

public void setStatutEtape(StatutEtapeEnum statutEtape) {
    this.statutEtape = statutEtape;
}

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public OffsetDateTime getValiderAt() {
        return validerAt;
    }

    public void setValiderAt(OffsetDateTime validerAt) {
        this.validerAt = validerAt;
    }
}