utilisateur{
    id_user
    user_name
    email
    mot_passe
    is_employe
    created_at

}

departement{
    id
    code
    libelle
}

role{
    id
    code "ADMIM","MANAGER","EMPLOYE"
}

employe{
    id_employe
    user_id
    departement_id
    role_id
    nom
    prenom
    date_naissance
    
}

poste{
    id
    departement_id
    titre
    description
}

contract{
    id
    employe_id
    poste_id
    type_contract "CDI","CDD","STAGE"
    salaire
    date_debut
    date_fin
    statut {ACTIF,TERMINE,ROMPU}
}

demande_recrutement{
    id
    departement_id
    demandeur_id
    poste_id
    nombre_poste
    created_at
    statut {EN_ATTENTE_RH,EN_ATTENTE_FINANCE,EN_ATTENTE,DIRECTION,APPROUVEE,REFUSEE}
}

approbation_recrutement{
    id
    demande_recrutement_id
    etape "RH","FINANCE","DIRECTION"
    valideur_id
    statut_etape EN_ATTENTE,VALIDER
    commentaire
    valider_at
}