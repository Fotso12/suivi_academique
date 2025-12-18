package com.suivi_academique.services.implementations;

import com.suivi_academique.dto.CoursDTO;
import com.suivi_academique.entities.Cours;
import com.suivi_academique.mappers.CoursMapper;
import com.suivi_academique.repositories.CoursRepository;
import com.suivi_academique.services.interfaces.CoursInterface;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class CoursService implements CoursInterface {

    // Logger pour journaliser les opérations du service Cours
    private static final Logger logger = LoggerFactory.getLogger(CoursService.class);

    private CoursRepository coursRepository;

    private CoursMapper coursMapper;

    @Override
    public CoursDTO save(CoursDTO coursDTO) {
        if(coursDTO.getCodeCours().isEmpty()){

            throw new RuntimeException("Données incorret");

        }else{
            Cours cours = coursRepository.save(coursMapper.toEntity(coursDTO));
            return coursMapper.toDTO(cours);
        }
    }


    @Override
    public List<CoursDTO> getAll() {
        // Journalisation du début de la récupération de tous les cours
        logger.info("Début de la récupération de tous les cours");

        try {
            List<CoursDTO> cours = coursRepository.findAll().stream().map(
                    coursMapper::toDTO).collect(Collectors.toList());
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} cours avec succès", cours.size());
            return cours;
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de tous les cours : {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public CoursDTO getById(String codeCours) {
        // Journalisation du début de la récupération d'un cours par ID
        logger.info("Début de la récupération du cours avec code : {}", codeCours);

        try {
            Cours cours = coursRepository.findById(codeCours).get();
            if(cours==null){
                // Journalisation si le cours n'est pas trouvé
                logger.warn("Cours avec code {} non trouvé", codeCours);
                throw new RuntimeException("Salle non trouvée");
            }else{
                // Journalisation du succès de la récupération
                logger.info("Cours avec code {} récupéré avec succès", codeCours);
                return coursMapper.toDTO(cours);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération du cours avec code {} : {}", codeCours, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public CoursDTO update(String codeCours, CoursDTO coursDTO) {
        // Journalisation du début de la mise à jour d'un cours
        logger.info("Début de la mise à jour du cours avec code : {}", codeCours);

        try {
            Cours cours = coursRepository.findById(codeCours).get();

            if(cours==null){
                // Journalisation si le cours n'est pas trouvé pour mise à jour
                logger.warn("Cours avec code {} non trouvé pour mise à jour", codeCours);
                throw new RuntimeException("Cours introuvable");
            }else{
                cours.setDescCours(coursDTO.getDescCours());
                cours.setLabelCours(coursDTO.getLabelCours());
                cours.setNbCreditCours(coursDTO.getNbCreditCours());
                cours.setNbHeureCours(coursDTO.getNbHeureCours());
                coursRepository.save(cours);
                // Journalisation du succès de la mise à jour
                logger.info("Cours avec code {} mis à jour avec succès", codeCours);
                return coursMapper.toDTO(cours);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour du cours avec code {} : {}", codeCours, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void delete(String codeCours) {
        // Journalisation du début de la suppression d'un cours
        logger.info("Début de la suppression du cours avec code : {}", codeCours);

        try {
            if(!coursRepository.existsById(codeCours)){
                // Journalisation si le cours n'existe pas pour suppression
                logger.warn("Cours avec code {} inexistant pour suppression", codeCours);
                throw new RuntimeException("impossible de supprimer cours introuvable");
            }
            coursRepository.deleteById(codeCours);
            // Journalisation du succès de la suppression
            logger.info("Cours avec code {} supprimé avec succès", codeCours);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression du cours avec code {} : {}", codeCours, e.getMessage(), e);
            throw e;
        }
    }
}
