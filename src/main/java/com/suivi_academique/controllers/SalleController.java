package com.suivi_academique.controllers;


import com.suivi_academique.dto.SalleDTO;
import com.suivi_academique.services.implementations.SalleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/salle")
public class SalleController {

    // Logger pour journaliser les opérations du contrôleur Salle
    private static final Logger logger = LoggerFactory.getLogger(SalleController.class);

    private SalleService salleService;
    public SalleController(SalleService salleService) {
        this.salleService = salleService;
    }



    @PostMapping
    public ResponseEntity<?> save(@RequestBody SalleDTO salleDTO) {
        // Journalisation de la réception d'une requête de sauvegarde de salle
        logger.info("Requête POST reçue pour sauvegarder une salle avec code : {}", salleDTO.getCodeSalle());

        try{
            SalleDTO savedSalle = salleService.save(salleDTO);
            // Journalisation du succès de la sauvegarde
            logger.info("Salle avec code {} sauvegardée avec succès via API", salleDTO.getCodeSalle());
            return new ResponseEntity<>(savedSalle, HttpStatus.CREATED);
        }catch(Exception e){
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde de la salle avec code {} via API : {}", salleDTO.getCodeSalle(), e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<SalleDTO>> getAll() {
        // Journalisation de la réception d'une requête de récupération de toutes les salles
        logger.info("Requête GET reçue pour récupérer toutes les salles");

        try{
            List<SalleDTO> salles = salleService.getAll();
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} salles avec succès via API", salles.size());
            return new  ResponseEntity<>(salles, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de toutes les salles via API : {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{codeSalle}")
    public ResponseEntity<?> update(@PathVariable("codeSalle") String codeSalle, @RequestBody SalleDTO salleDTO) {
        // Journalisation de la réception d'une requête de mise à jour de salle
        logger.info("Requête PUT reçue pour mettre à jour la salle avec code : {}", codeSalle);

        try {
            SalleDTO updatedSalle = salleService.update(codeSalle, salleDTO);
            // Journalisation du succès de la mise à jour
            logger.info("Salle avec code {} mise à jour avec succès via API", codeSalle);
            return  new ResponseEntity<>(updatedSalle, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour de la salle avec code {} via API : {}", codeSalle, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{codeSalle}")
    public ResponseEntity<?> delete(@PathVariable("codeSalle") String codeSalle) {
        // Journalisation de la réception d'une requête de suppression de salle
        logger.info("Requête DELETE reçue pour supprimer la salle avec code : {}", codeSalle);

        try {
            salleService.delete(codeSalle);
            // Journalisation du succès de la suppression
            logger.info("Salle avec code {} supprimée avec succès via API", codeSalle);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression de la salle avec code {} via API : {}", codeSalle, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{codeSalle}")
    public ResponseEntity<?> Show(@PathVariable("codeSalle") String codeSalle) {
        // Journalisation de la réception d'une requête d'affichage d'une salle
        logger.info("Requête GET reçue pour afficher la salle avec code : {}", codeSalle);

        try{
            SalleDTO salle = salleService.getById(codeSalle);
            // Journalisation du succès de l'affichage
            logger.info("Salle avec code {} affichée avec succès via API", codeSalle);
            return new ResponseEntity<>(salle, HttpStatus.OK);
        }catch(Exception e){
            // Journalisation de l'erreur lors de l'affichage
            logger.error("Erreur lors de l'affichage de la salle avec code {} via API : {}", codeSalle, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



}
