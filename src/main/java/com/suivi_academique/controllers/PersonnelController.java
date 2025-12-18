package com.suivi_academique.controllers;


import com.suivi_academique.dto.PersonnelDTO;
import com.suivi_academique.dto.SalleDTO;
import com.suivi_academique.services.implementations.PersonnelService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/personnel")
public class PersonnelController {

    // Logger pour journaliser les opérations du contrôleur Personnel
    private static final Logger logger = LoggerFactory.getLogger(PersonnelController.class);

    private PersonnelService personnelService;

    public PersonnelController(PersonnelService personnelService) {
        this.personnelService = personnelService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody PersonnelDTO personnelDTO) {
        // Journalisation de la réception d'une requête de sauvegarde de personnel
        logger.info("Requête POST reçue pour sauvegarder un personnel avec code : {}", personnelDTO.getCodePersonnel());

        try{
            PersonnelDTO savedPersonnel = personnelService.save(personnelDTO);
            // Journalisation du succès de la sauvegarde
            logger.info("Personnel avec code {} sauvegardé avec succès via API", personnelDTO.getCodePersonnel());
            return new ResponseEntity<>(savedPersonnel, HttpStatus.CREATED);
        }catch(Exception e){
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde du personnel avec code {} via API : {}", personnelDTO.getCodePersonnel(), e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<PersonnelDTO>> getAll() {
        return new  ResponseEntity<>(personnelService.getAll(), HttpStatus.OK);

    }

    @PutMapping("/{codePersonnel}")
    public ResponseEntity<?> update(@PathVariable("codePersonnel") String codePersonnel, @RequestBody PersonnelDTO personnelDTO) {
        // Journalisation de la réception d'une requête de mise à jour de personnel
        logger.info("Requête PUT reçue pour mettre à jour le personnel avec code : {}", codePersonnel);

        try {
            PersonnelDTO updatedPersonnel = personnelService.update(codePersonnel, personnelDTO);
            // Journalisation du succès de la mise à jour
            logger.info("Personnel avec code {} mis à jour avec succès via API", codePersonnel);
            return  new ResponseEntity<>(updatedPersonnel, HttpStatus.OK);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour du personnel avec code {} via API : {}", codePersonnel, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{codePersonnel}")
    public ResponseEntity<?> delete(@PathVariable("codePersonnel") String codePersonnel) {
        // Journalisation de la réception d'une requête de suppression de personnel
        logger.info("Requête DELETE reçue pour supprimer le personnel avec code : {}", codePersonnel);

        try {
            personnelService.delete(codePersonnel);
            // Journalisation du succès de la suppression
            logger.info("Personnel avec code {} supprimé avec succès via API", codePersonnel);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression du personnel avec code {} via API : {}", codePersonnel, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{codePersonnel}")
    public ResponseEntity<?> get(@PathVariable("codePersonnel") String codePersonnel) {
        // Journalisation de la réception d'une requête d'affichage d'un personnel
        logger.info("Requête GET reçue pour afficher le personnel avec code : {}", codePersonnel);

        try{
            PersonnelDTO personnel = personnelService.getById(codePersonnel);
            // Journalisation du succès de l'affichage
            logger.info("Personnel avec code {} affiché avec succès via API", codePersonnel);
            return new ResponseEntity<>(personnel, HttpStatus.OK);
        }catch(Exception e){
            // Journalisation de l'erreur lors de l'affichage
            logger.error("Erreur lors de l'affichage du personnel avec code {} via API : {}", codePersonnel, e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
