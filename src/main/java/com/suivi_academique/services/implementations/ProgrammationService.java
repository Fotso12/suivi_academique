package com.suivi_academique.services.implementations;

import com.suivi_academique.dto.ProgrammationDTO;
import com.suivi_academique.entities.Programmation;
import com.suivi_academique.entities.Salle;
import com.suivi_academique.mappers.ProgrammationMapper;
import com.suivi_academique.mappers.SalleMapper;
import com.suivi_academique.repositories.ProgrammationRepository;
import com.suivi_academique.repositories.SalleRepository;
import com.suivi_academique.services.interfaces.ProgrammationInterface; // Interface supposée
import com.suivi_academique.utils.SalleStatus;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgrammationService implements ProgrammationInterface {

    // Logger pour journaliser les opérations du service Programmation
    private static final Logger logger = LoggerFactory.getLogger(ProgrammationService.class);

    private final ProgrammationRepository programmationRepository;
    private final ProgrammationMapper programmationMapper;

    private final SalleRepository salleRepository;

    private final SalleMapper salleMapper;

    @Override
    public ProgrammationDTO save(ProgrammationDTO programmationDTO){
        // Journalisation du début de la sauvegarde d'une programmation
        logger.info("Début de la sauvegarde de la programmation pour la salle : {}", programmationDTO.getSalle().getCodeSalle());

        try {
            // --- 1. Vérification de l'existence de la Salle ---
            String codeSalle = programmationDTO.getSalle().getCodeSalle();

            Salle salle = salleRepository.findById(codeSalle)
                    .orElseThrow(() -> new RuntimeException("Salle introuvable avec l'ID: " + codeSalle));

            if (programmationDTO.getNbHeure() <= 0) {
                // Journalisation de l'erreur de durée invalide
                logger.error("Durée de programmation invalide : {} heures", programmationDTO.getNbHeure());
                throw new RuntimeException("La durée de la programmation doit être supérieure à zéro.");
            }
            if (programmationDTO.getDateProgrammation() == null || programmationDTO.getFinProgrammation() == null) {
                // Journalisation de l'erreur de dates manquantes
                logger.error("Dates de programmation manquantes pour la salle : {}", codeSalle);
                throw new RuntimeException("Les dates de début et de fin de programmation sont obligatoires.");
            }
            if (salle.getStatutSalle() != SalleStatus.LIBRE) {
                String statutActuel = salle.getStatutSalle() != null ? salle.getStatutSalle().name() : "NON DÉFINI";
                // Journalisation de l'erreur de salle non disponible
                logger.error("Salle {} non disponible pour la programmation. Statut actuel: {}", codeSalle, statutActuel);
                throw new RuntimeException("La Salle " + codeSalle + " n'est pas disponible pour la programmation. Statut actuel: " + statutActuel);
            }

            Programmation programmation = programmationMapper.toEntity(programmationDTO);
            programmation = programmationRepository.save(programmation);
            // Journalisation du succès de la sauvegarde
            logger.info("Programmation sauvegardée avec succès pour la salle : {}", codeSalle);
            return programmationMapper.toDTO(programmation);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde de la programmation pour la salle {} : {}", programmationDTO.getSalle().getCodeSalle(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<ProgrammationDTO> getAll() {
        // Journalisation du début de la récupération de toutes les programmations
        logger.info("Début de la récupération de toutes les programmations");

        try {
            List<ProgrammationDTO> programmations = programmationRepository.findAll().stream().map(
                    programmationMapper::toDTO).collect(Collectors.toList());
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} programmations avec succès", programmations.size());
            return programmations;
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de toutes les programmations : {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ProgrammationDTO getById(int id) {
        Optional<Programmation> programmationOpt = programmationRepository.findById(id);

        if (programmationOpt.isEmpty()) {
            throw new RuntimeException("Programmation non trouvée avec l'ID: " + id);
        }

        return programmationMapper.toDTO(programmationOpt.get());
    }

    @Override
    public ProgrammationDTO update(int id, ProgrammationDTO programmationDTO) {

        Programmation existingProgrammation = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable pour la mise à jour."));

        existingProgrammation.setNbHeure(programmationDTO.getNbHeure());
        existingProgrammation.setDateProgrammation(programmationDTO.getDateProgrammation());
        existingProgrammation.setFinProgrammation(programmationDTO.getFinProgrammation());
        existingProgrammation.setStatutProgrammation(programmationDTO.getStatutProgrammation());

        // Relations: Nécessite de mapper les DTOs des entités liées vers leurs entités complètes
        // Note: Assurez-vous que les mappers imbriqués sont injectés dans ce service si vous en avez besoin
        /*
        existingProgrammation.setCours(coursMapper.toEntity(programmationDTO.getCours()));
        existingProgrammation.setSalle(salleMapper.toEntity(programmationDTO.getSalle()));
        existingProgrammation.setPersonnelProg(personnelMapper.toEntity(programmationDTO.getPersonnelProg()));
        existingProgrammation.setPersonnelVal(personnelMapper.toEntity(programmationDTO.getPersonnelVal()));
        */

        programmationRepository.save(existingProgrammation);
        return programmationMapper.toDTO(existingProgrammation);
    }

    @Override
    public void delete(int id) {
        boolean exist = programmationRepository.existsById(id);

        if (!exist) {
            throw new RuntimeException("Programmation inexistante pour la suppression.");
        }

        programmationRepository.deleteById(id);
    }
}