package com.suivi_academique.controllers;

import com.suivi_academique.dto.ProgrammationDTO;
import com.suivi_academique.services.interfaces.ProgrammationInterface; // Utilisation de l'interface
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programmations")
@AllArgsConstructor
public class ProgrammationController {

    // Logger pour journaliser les opérations du contrôleur Programmation
    private static final Logger logger = LoggerFactory.getLogger(ProgrammationController.class);

    private final ProgrammationInterface programmationService;


    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProgrammationDTO programmationDTO) {
        // Journalisation de la réception d'une requête de sauvegarde de programmation
        logger.info("Requête POST reçue pour sauvegarder une programmation avec id : {}", programmationDTO.getId());

        try {
            ProgrammationDTO savedProgrammation = programmationService.save(programmationDTO);
            // Journalisation du succès de la sauvegarde
            logger.info("Programmation avec id {} sauvegardée avec succès via API", programmationDTO.getId());
            return new ResponseEntity<>(savedProgrammation, HttpStatus.CREATED);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde de la programmation avec id {} via API : {}", programmationDTO.getId(), e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProgrammationDTO>> getAll() {
        // Journalisation de la réception d'une requête de récupération de toutes les programmations
        logger.info("Requête GET reçue pour récupérer toutes les programmations");

        try {
            List<ProgrammationDTO> programmations = programmationService.getAll();
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} programmations avec succès via API", programmations.size());
            return new ResponseEntity<>(programmations, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de toutes les programmations via API : {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody ProgrammationDTO programmationDTO) {
        // Journalisation de la réception d'une requête de mise à jour de programmation
        logger.info("Requête PUT reçue pour mettre à jour la programmation avec id : {}", id);

        try {
            ProgrammationDTO updatedProgrammation = programmationService.update(id, programmationDTO);
            // Journalisation du succès de la mise à jour
            logger.info("Programmation avec id {} mise à jour avec succès via API", id);
            return new ResponseEntity<>(updatedProgrammation, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour de la programmation avec id {} via API : {}", id, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        // Journalisation de la réception d'une requête de suppression de programmation
        logger.info("Requête DELETE reçue pour supprimer la programmation avec id : {}", id);

        try {
            programmationService.delete(id);
            // Journalisation du succès de la suppression
            logger.info("Programmation avec id {} supprimée avec succès via API", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression de la programmation avec id {} via API : {}", id, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        // Journalisation de la réception d'une requête d'affichage d'une programmation
        logger.info("Requête GET reçue pour afficher la programmation avec id : {}", id);

        try {
            ProgrammationDTO programmation = programmationService.getById(id);
            // Journalisation du succès de l'affichage
            logger.info("Programmation avec id {} affichée avec succès via API", id);
            return new ResponseEntity<>(programmation, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de l'affichage
            logger.error("Erreur lors de l'affichage de la programmation avec id {} via API : {}", id, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}