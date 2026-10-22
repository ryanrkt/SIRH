package com.app.sirh.service;

import com.app.sirh.model.Role;
import com.app.sirh.model.Utilisateur;
import com.app.sirh.repository.RoleRepository;
import com.app.sirh.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Inscription d'un nouveau candidat.
     */
    @Transactional
public String registerCandidat(Utilisateur utilisateur) {

    // 1. Vérifier si l'email existe déjà
    if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
        return "Erreur : Cet email est déjà associé à un compte existant.";
    }

    // 2. Récupérer le rôle CANDIDAT
    Role candidatRole = roleRepository.findByCode("CANDIDAT")
        .orElse(null);

    if (candidatRole == null) {
        return "Erreur : Le rôle 'CANDIDAT' n'est pas initialisé en base de données.";
    }

    // 3. Associer le rôle au nouvel utilisateur
    utilisateur.setRole(candidatRole);

    // 4. Hacher le mot de passe
    utilisateur.setMotPasse(
        passwordEncoder.encode(utilisateur.getMotPasse())
    );

    // 5. Activer le compte
    utilisateur.setActif(true);

    // 6. Enregistrer l'utilisateur
    utilisateurRepository.save(utilisateur);

    return "Inscription réussie !";
}
}