package com.suivi_academique.controllers;


import com.suivi_academique.dto.AffectationDTO;
import com.suivi_academique.entities.AffectationId;
import com.suivi_academique.services.implementations.AffectationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/affectation")
public class AffectationController {

    // Logger pour journaliser les opérations du contrôleur Affectation
    private static final Logger logger = LoggerFactory.getLogger(AffectationController.class);

    private AffectationService affectationService;

    public AffectationController(AffectationService affectationService) {
        this.affectationService = affectationService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody AffectationDTO affectationDTO) {
        // Journalisation de la réception d'une requête de sauvegarde d'affectation
        logger.info("Requête POST reçue pour sauvegarder une affectation avec code cours : {} et code personnel : {}",
                affectationDTO.getCours().getCodeCours(),
                affectationDTO.getPersonnel().getCodePersonnel());
        try{
            AffectationDTO savedAffectation = affectationService.save(affectationDTO);
            // Journalisation du succès de la sauvegarde
            logger.info("Affectation avec code cours {} et code personnel {} sauvegardée avec succès via API", affectationDTO.getCours().getCodeCours(), affectationDTO.getPersonnel().getCodePersonnel());
            return new ResponseEntity<>(savedAffectation, HttpStatus.CREATED);
        }catch(Exception e){
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde de l'affectation avec code cours {} et code personnel {} via API : {}", affectationDTO.getCours().getCodeCours(), affectationDTO.getPersonnel().getCodePersonnel(), e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<AffectationDTO>> getAll() {
        // Journalisation de la réception d'une requête de récupération de toutes les affectations
        logger.info("Requête GET reçue pour récupérer toutes les affectations");

        try{
            List<AffectationDTO> affectations = affectationService.getAll();
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} affectations avec succès via API", affectations.size());
            return new  ResponseEntity<>(affectations, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de toutes les affectations via API : {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{codeCours}/{codePersonnel}")
    public ResponseEntity<?> update(@PathVariable String codeCours, @PathVariable String codePersonnel, @RequestBody AffectationDTO affectationDTO) {
        try {
            AffectationId id =  new AffectationId(codeCours, codePersonnel);
            return  new ResponseEntity<>(affectationService.update(id, affectationDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{codeCours}/{codePersonnel}")
    public ResponseEntity<?> delete(@PathVariable String codeCours, @PathVariable String codePersonnel) {
        try {
            affectationService.delete(codeCours, codePersonnel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{codeCours}/{codePersonnel}")
    public ResponseEntity<?> Show(@PathVariable String codeCours, @PathVariable String codePersonnel) {
        try{
            AffectationId id =  new AffectationId(codeCours, codePersonnel);
            return new ResponseEntity<>(affectationService.getById(id), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}
