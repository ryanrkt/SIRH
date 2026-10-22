package com.app.sirh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.sirh.model.Employe;
import com.app.sirh.model.Utilisateur;
import com.app.sirh.repository.EmployeRepository;
import com.app.sirh.repository.UtilisateurRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur trouvé avec l'email : " + email));

        Employe employe = employeRepository.findByUtilisateurId(utilisateur.getId()).orElse(null);

        return new CustomUserDetails(utilisateur, employe);
    }
}