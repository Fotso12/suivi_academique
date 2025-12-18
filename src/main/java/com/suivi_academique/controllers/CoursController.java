package com.suivi_academique.controllers;


import com.suivi_academique.dto.CoursDTO;
import com.suivi_academique.services.implementations.CoursService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/cours")
public class CoursController {

    // Logger pour journaliser les opérations du contrôleur Cours
    private static final Logger logger = LoggerFactory.getLogger(CoursController.class);

    private CoursService coursService;

    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CoursDTO coursDTO) {
        // Journalisation de la réception d'une requête de sauvegarde de cours
        logger.info("Requête POST reçue pour sauvegarder un cours avec code : {}", coursDTO.getCodeCours());

        try{
            CoursDTO savedCours = coursService.save(coursDTO);
            // Journalisation du succès de la sauvegarde
            logger.info("Cours avec code {} sauvegardé avec succès via API", coursDTO.getCodeCours());
            return new ResponseEntity<>(savedCours, HttpStatus.CREATED);
        }catch(Exception e){
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde du cours avec code {} via API : {}", coursDTO.getCodeCours(), e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping()
    public ResponseEntity<List<CoursDTO>> getAll() {
        // Journalisation de la réception d'une requête de récupération de tous les cours
        logger.info("Requête GET reçue pour récupérer tous les cours");

        try{
            List<CoursDTO> cours = coursService.getAll();
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} cours avec succès via API", cours.size());
            return new  ResponseEntity<>(cours, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de tous les cours via API : {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    @PutMapping("/{codeCours}")
    public ResponseEntity<?> update(@PathVariable String codeCours, @RequestBody CoursDTO coursDTO) {
        // Journalisation de la réception d'une requête de mise à jour de cours
        logger.info("Requête PUT reçue pour mettre à jour le cours avec code : {}", codeCours);

        try {
            CoursDTO updatedCours = coursService.update(codeCours, coursDTO);
            // Journalisation du succès de la mise à jour
            logger.info("Cours avec code {} mis à jour avec succès via API", codeCours);
            return  new ResponseEntity<>(updatedCours, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour du cours avec code {} via API : {}", codeCours, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{codeCours}")
    public ResponseEntity<?> delete(@PathVariable String codeCours) {
        // Journalisation de la réception d'une requête de suppression de cours
        logger.info("Requête DELETE reçue pour supprimer le cours avec code : {}", codeCours);

        try {
            coursService.delete(codeCours);
            // Journalisation du succès de la suppression
            logger.info("Cours avec code {} supprimé avec succès via API", codeCours);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression du cours avec code {} via API : {}", codeCours, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{codeCours}")
    public ResponseEntity<?> get(@PathVariable("codeCours") String codeCours) {
        // Journalisation de la réception d'une requête d'affichage d'un cours
        logger.info("Requête GET reçue pour afficher le cours avec code : {}", codeCours);

        try{
            CoursDTO cours = coursService.getById(codeCours);
            // Journalisation du succès de l'affichage
            logger.info("Cours avec code {} affiché avec succès via API", codeCours);
            return new ResponseEntity<>(cours, HttpStatus.OK);
        }catch(Exception e){
            // Journalisation de l'erreur lors de l'affichage
            logger.error("Erreur lors de l'affichage du cours avec code {} via API : {}", codeCours, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
