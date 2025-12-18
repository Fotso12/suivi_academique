package com.suivi_academique.services.implementations;


import com.suivi_academique.repositories.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonnelDetailsService implements UserDetailsService {

    // Logger pour journaliser les opérations du service PersonnelDetails
    private static final Logger logger = LoggerFactory.getLogger(PersonnelDetailsService.class);

    private final PersonnelRepository personnelRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Journalisation du début du chargement de l'utilisateur
        logger.info("Début du chargement de l'utilisateur avec login : {}", username);

        try {
            UserDetails userDetails = personnelRepository.findByLoginPersonnel(username).orElseThrow(() -> new UsernameNotFoundException("Personnel introuvable avec le login: " + username));
            // Journalisation du succès du chargement
            logger.info("Utilisateur avec login {} chargé avec succès", username);
            return userDetails;
        } catch (UsernameNotFoundException e) {
            // Journalisation de l'erreur si l'utilisateur n'est pas trouvé
            logger.error("Utilisateur avec login {} non trouvé : {}", username, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            // Journalisation de toute autre erreur
            logger.error("Erreur lors du chargement de l'utilisateur avec login {} : {}", username, e.getMessage(), e);
            throw e;
        }
    }

}
