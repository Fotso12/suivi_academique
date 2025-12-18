package com.suivi_academique.controllers;


import com.suivi_academique.dto.AuthRequest;
import com.suivi_academique.dto.AuthResponse;
import com.suivi_academique.dto.PersonnelDTO;
import com.suivi_academique.services.implementations.AuthentificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthentificationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody  PersonnelDTO request) {
        // Journalisation de la réception d'une requête de connexion
        logger.info("Requête POST reçue pour authentification avec login : {}", request.getLoginPersonnel());

        try {
            AuthResponse response = authenticationService.authenticate(request);
            // Journalisation du succès de l'authentification
            logger.info("Authentification réussie pour l'utilisateur : {}", request.getLoginPersonnel());
            return response;
        } catch (Exception e) {
            // Journalisation de l'erreur lors de l'authentification
            logger.error("Erreur lors de l'authentification pour l'utilisateur {} : {}", request.getLoginPersonnel(), e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody PersonnelDTO personnel
    ) {
        // Journalisation de la réception d'une requête d'inscription
        logger.info("Requête POST reçue pour inscription avec login : {}", personnel.getLoginPersonnel());

        try {
            AuthResponse response = authenticationService.register(personnel);
            // Journalisation du succès de l'inscription
            logger.info("Inscription réussie pour l'utilisateur : {}", personnel.getLoginPersonnel());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de l'inscription
            logger.error("Erreur lors de l'inscription pour l'utilisateur {} : {}", personnel.getLoginPersonnel(), e.getMessage(), e);
            throw e;
        }
    }
}
