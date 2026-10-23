package com.app.sirh.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class DemandeRecrutementForm {

    @NotBlank(message = "Veuillez saisir ou sélectionner un poste")
    private String posteTitre;

    @Min(value = 1, message = "Le nombre de postes doit être au moins de 1")
    private int nombrePoste = 1;
   

    private Integer salaire;

    // Getters et Setters
    public String getPosteTitre() { return posteTitre; }
    public void setPosteTitre(String posteTitre) { this.posteTitre = posteTitre; }

    public int getNombrePoste() { return nombrePoste; }
    public void setNombrePoste(int nombrePoste) { this.nombrePoste = nombrePoste; }

    public Integer getSalaire() { return salaire; }
    public void setSalaire(Integer salaire) { this.salaire = salaire; }
}