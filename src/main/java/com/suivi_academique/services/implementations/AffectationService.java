package com.suivi_academique.services.implementations;

import com.suivi_academique.dto.AffectationDTO;
import com.suivi_academique.entities.Affectation;
import com.suivi_academique.entities.AffectationId;
import com.suivi_academique.mappers.AffectationMapper;
import com.suivi_academique.mappers.CoursMapper;
import com.suivi_academique.mappers.PersonnelMapper;
import com.suivi_academique.repositories.AffectationRepository;
import com.suivi_academique.repositories.CoursRepository;
import com.suivi_academique.repositories.PersonnelRepository;
import com.suivi_academique.services.interfaces.AffectationInterface;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AffectationService implements AffectationInterface {

    // Logger pour journaliser les opérations du service Affectation
    private static final Logger logger = LoggerFactory.getLogger(AffectationService.class);

    private AffectationRepository affectationRepository;

    private AffectationMapper affectationMapper;

    private CoursRepository coursRepository;

    private PersonnelRepository personnelRepository;

    private PersonnelMapper personnelMapper;

    private CoursMapper coursMapper;


    @Override
    public AffectationDTO save(AffectationDTO affectationDTO) {

        String codeCours = affectationDTO.getCours().getCodeCours();
        String codePersonnel = affectationDTO.getPersonnel().getCodePersonnel();

        if (!coursRepository.existsById(codeCours)) {
            throw new RuntimeException("Le Cours avec l'ID " + codeCours + " n'existe pas.");
        }
        if (!personnelRepository.existsById(codePersonnel)) {
            throw new RuntimeException("Le Personnel avec l'ID " + codePersonnel + " n'existe pas.");
        }

        AffectationId affectationId = new AffectationId(codeCours, codePersonnel);


        if (affectationRepository.existsById(affectationId)) {
            throw new RuntimeException("Cette Affectation existe déjà pour ce Cours et ce Personnel.");
        }

        // Assignation de l'ID composite au DTO avant le mapping
        // Ceci est nécessaire pour que le mapper (toEntity) puisse créer l'entité complète
        affectationDTO.setCodeAffectation(affectationId);

        Affectation affectation = affectationRepository.save(affectationMapper.toEntity(affectationDTO));
        return affectationMapper.toDTO(affectation);
    }

    @Override
    public List<AffectationDTO> getAll() {
        // Journalisation du début de la récupération de toutes les affectations
        logger.info("Début de la récupération de toutes les affectations");

        try {
            List<AffectationDTO> affectations = affectationRepository.findAll().stream().map(
                    affectationMapper::toDTO).collect(Collectors.toList());
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} affectations avec succès", affectations.size());
            return affectations;
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de toutes les affectations : {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AffectationDTO getById(AffectationId affectationId) {
        // Journalisation du début de la récupération d'une affectation par ID
        logger.info("Début de la récupération de l'affectation avec ID : cours {}, personnel {}", affectationId.getCodeCours(), affectationId.getCodePersonnel());

        try {
            Affectation affectation = affectationRepository.findById(affectationId).orElse(null);
            if (affectation == null) {
                // Journalisation si l'affectation n'est pas trouvée
                logger.warn("Affectation non trouvée pour cours {} et personnel {}", affectationId, affectationId.getCodePersonnel());
                throw new RuntimeException("Affectation non trouvé");
            }else{
                // Journalisation du succès de la récupération
                logger.info("Affectation récupérée avec succès pour cours {} et personnel {}", affectationId.getCodeCours(), affectationId.getCodePersonnel());
                return  affectationMapper.toDTO(affectation);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de l'affectation avec ID : cours {}, personnel {} : {}", affectationId.getCodeCours(), affectationId.getCodePersonnel(), e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public AffectationDTO update(AffectationId affectationId, AffectationDTO affectationDTO) {
    Affectation affectation = affectationRepository.findById(affectationId).orElse(null);
        if(affectation == null){
            throw new RuntimeException("AffectationId non trouvé");
        }
        affectation.setPersonnel(personnelMapper.toEntity(affectationDTO.getPersonnel()));
        affectation.setCours(coursMapper.toEntity(affectationDTO.getCours()));
        affectationRepository.save(affectation);
        return affectationMapper.toDTO(affectation);
    }

    @Override
    public void delete(String codeCours, String codePersonnel ) {
        // Journalisation du début de la suppression d'une affectation
        logger.info("Début de la suppression de l'affectation pour cours {} et personnel {}", codeCours, codePersonnel);

        try {
            AffectationId id = new AffectationId(codeCours, codePersonnel);

            boolean exist = affectationRepository.existsById(id);

            if (!exist) {
                // Journalisation si l'affectation n'existe pas pour suppression
                logger.warn("Affectation inexistante pour suppression : cours {}, personnel {}", codeCours, codePersonnel);
                throw new RuntimeException("Affectation inexistante pour la suppression.");
            } else {
                affectationRepository.deleteById(id);
                // Journalisation du succès de la suppression
                logger.info("Affectation supprimée avec succès pour cours {} et personnel {}", codeCours, codePersonnel);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression de l'affectation pour cours {} et personnel {} : {}", codeCours, codePersonnel, e.getMessage(), e);
            throw e;
        }
    }
}
