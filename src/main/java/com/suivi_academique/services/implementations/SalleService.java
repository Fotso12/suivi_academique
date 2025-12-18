package com.suivi_academique.services.implementations;


import com.suivi_academique.dto.SalleDTO;
import com.suivi_academique.entities.Salle;
import com.suivi_academique.mappers.SalleMapper;
import com.suivi_academique.repositories.SalleRepository;
import com.suivi_academique.services.interfaces.SalleInterface;
import com.suivi_academique.utils.SalleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Service
@AllArgsConstructor
public class SalleService implements SalleInterface {

    // Logger pour journaliser les opérations du service Salle
    private static final Logger logger = LoggerFactory.getLogger(SalleService.class);

    private SalleRepository salleRepository;

    private SalleMapper salleMapper;


    @Override
    public SalleDTO save(SalleDTO salleDTO){
        // Journalisation du début de la sauvegarde d'une salle
        logger.info("Début de la sauvegarde de la salle avec code : {}", salleDTO.getCodeSalle());

        try {
            if(salleDTO.getCodeSalle().isEmpty() || salleDTO.getContenace()<10){
                // Journalisation des données incorrectes
                logger.warn("Données incorrectes pour la salle : code vide ou contenance < 10");
                throw new RuntimeException("Données incorret");
            }else{
                Salle salle = salleRepository.save(salleMapper.toEntity(salleDTO));
                // Journalisation du succès de la sauvegarde
                logger.info("Salle avec code {} sauvegardée avec succès", salleDTO.getCodeSalle());
                return salleMapper.toDTO(salle);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde de la salle avec code {} : {}", salleDTO.getCodeSalle(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<SalleDTO> getAll() {
        // Journalisation du début de la récupération de toutes les salles
        logger.info("Début de la récupération de toutes les salles");

        try {
            List<SalleDTO> salles = salleRepository.findAll().stream().map(
            salleMapper::toDTO).collect(Collectors.toList());
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} salles avec succès", salles.size());
            return salles;
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de toutes les salles : {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SalleDTO getById(String codeSalle) {
        Salle salle = salleRepository.findById(codeSalle).get();
        if(salle == null){
            throw new RuntimeException("Salle non trouvée");
        }else{
            return salleMapper.toDTO(salle);
        }
    }

    @Override
    public SalleDTO update(String codeSalle, SalleDTO salleDTO) {
        // Journalisation du début de la mise à jour d'une salle
        logger.info("Début de la mise à jour de la salle avec code : {}", codeSalle);

        try {
            Salle salle = salleRepository.findById(codeSalle).get();
            if(salle==null){
                // Journalisation si la salle n'est pas trouvée pour mise à jour
                logger.warn("Salle avec code {} non trouvée pour mise à jour", codeSalle);
                throw new RuntimeException("Salle introuvé");
            }else{
                salle.setContenance(salleDTO.getContenace());
                salle.setDescSalle(salleDTO.getDescSalle());
                salle.setStatutSalle(salleDTO.getStatutSalle());
                salleRepository.save(salle);
                // Journalisation du succès de la mise à jour
                logger.info("Salle avec code {} mise à jour avec succès", codeSalle);
                return salleMapper.toDTO(salle);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour de la salle avec code {} : {}", codeSalle, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(String codeSalle) {
        // Journalisation du début de la suppression d'une salle
        logger.info("Début de la suppression de la salle avec code : {}", codeSalle);

        try {
            boolean exist = salleRepository.existsById(codeSalle);
            if(!exist){
                // Journalisation si la salle n'existe pas pour suppression
                logger.warn("Salle avec code {} inexistante pour suppression", codeSalle);
                throw new RuntimeException("Salle inexistante");
            }else{
                salleRepository.deleteById(codeSalle);
                // Journalisation du succès de la suppression
                logger.info("Salle avec code {} supprimée avec succès", codeSalle);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression de la salle avec code {} : {}", codeSalle, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SalleDTO findSallesOccupe(SalleStatus salleStatus) {
        // Journalisation de l'appel à une méthode non implémentée
        logger.info("Méthode findSallesOccupe appelée avec statut : {} - Non implémentée", salleStatus);
        return null;
    }
}
