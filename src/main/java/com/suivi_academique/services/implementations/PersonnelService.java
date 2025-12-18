package com.suivi_academique.services.implementations;

import com.suivi_academique.dto.PersonnelDTO;
import com.suivi_academique.entities.Personnel;
import com.suivi_academique.mappers.PersonnelMapper;
import com.suivi_academique.repositories.PersonnelRepository;
import com.suivi_academique.services.interfaces.Personnelnterface;
import com.suivi_academique.utils.CodeGenerator;
import com.suivi_academique.utils.RolePersonnel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonnelService implements Personnelnterface {

        // Logger pour journaliser les opérations du service Personnel
        private static final Logger logger = LoggerFactory.getLogger(PersonnelService.class);

        private PersonnelRepository personnelRepository;

        private PersonnelMapper personnelMapper;

        private CodeGenerator codeGenerator;
    private PasswordEncoder passwordEncoder;


    @Override
    public List<PersonnelDTO> getAll() {
        // Journalisation du début de la récupération de tous les personnels
        logger.info("Début de la récupération de tous les personnels");

        try {
            List<PersonnelDTO> personnels = personnelRepository.findAll().stream().map(
                    personnelMapper::toDTO).collect(Collectors.toList());
            // Journalisation du succès de la récupération
            logger.info("Récupération de {} personnels avec succès", personnels.size());
            return personnels;
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération de tous les personnels : {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PersonnelDTO getById(String codePersonnel) {
        // Journalisation du début de la récupération d'un personnel par ID
        logger.info("Début de la récupération du personnel avec code : {}", codePersonnel);

        try {
            Personnel personnel = personnelRepository.findById(codePersonnel).get();
            if(personnel==null){
                // Journalisation si le personnel n'est pas trouvé
                logger.warn("Personnel avec code {} non trouvé", codePersonnel);
                throw new RuntimeException("Personnel non trouvée");
            }else{
                // Journalisation du succès de la récupération
                logger.info("Personnel avec code {} récupéré avec succès", codePersonnel);
                return personnelMapper.toDTO(personnel);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la récupération
            logger.error("Erreur lors de la récupération du personnel avec code {} : {}", codePersonnel, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PersonnelDTO update(String codePersonnel, PersonnelDTO personnelDTO) {
        // Journalisation du début de la mise à jour d'un personnel
        logger.info("Début de la mise à jour du personnel avec code : {}", codePersonnel);

        try {
            Personnel personnel = personnelRepository.findById(codePersonnel).get();

            if(personnel==null){
                // Journalisation si le personnel n'est pas trouvé pour mise à jour
                logger.warn("Personnel avec code {} non trouvé pour mise à jour", codePersonnel);
                throw new RuntimeException("Cours introuvable");
            }else{
                personnel.setNomPersonnel(personnelDTO.getNomPersonnel());
                personnel.setLoginPersonnel(personnelDTO.getLoginPersonnel());
                personnel.setPadPersonnel(personnelDTO.getPadPersonnel());
                personnel.setPhonePersonnel(personnelDTO.getPhonePersonnel());
                personnel.setRolePersonnel(personnelDTO.getRolePersonnel());
                personnel.setSexePersonnel(personnelDTO.getSexePersonnel());
                personnelRepository.save(personnel);
                // Journalisation du succès de la mise à jour
                logger.info("Personnel avec code {} mis à jour avec succès", codePersonnel);
                return personnelMapper.toDTO(personnel);

            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la mise à jour
            logger.error("Erreur lors de la mise à jour du personnel avec code {} : {}", codePersonnel, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public PersonnelDTO getByRole(String roleString) {
        return personnelRepository.searchByRole(roleString).stream().map(
                personnelMapper::toDTO).collect(Collectors.toList()).get(0);
    }

    @Override
    public void delete(String codePersonnel) {
        // Journalisation du début de la suppression d'un personnel
        logger.info("Début de la suppression du personnel avec code : {}", codePersonnel);

        try {
            boolean exist = personnelRepository.existsById(codePersonnel);

            if (!exist) {
                // Journalisation si le personnel n'existe pas pour suppression
                logger.warn("Personnel avec code {} inexistant pour suppression", codePersonnel);
                // Cette erreur devrait renvoyer 400 Bad Request
                throw new RuntimeException("Personnel inexistant.");
            } else {
                // C'est ici que l'échec se produit probablement
                personnelRepository.deleteById(codePersonnel);
                // Journalisation du succès de la suppression
                logger.info("Personnel avec code {} supprimé avec succès", codePersonnel);
            }
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la suppression
            logger.error("Erreur lors de la suppression du personnel avec code {} : {}", codePersonnel, e.getMessage(), e);
            throw e;
        }

    }

    @Override
    public PersonnelDTO save(PersonnelDTO personnelDTO) {
        // Journalisation du début de la sauvegarde d'un personnel
        logger.info("Début de la sauvegarde du personnel avec nom : {}", personnelDTO.getNomPersonnel());

        try {
            // Generate code based on role
            String generatedCode = codeGenerator.generate(personnelDTO.getRolePersonnel().name());
            if (generatedCode == null) {
                // Journalisation de l'erreur de génération de code
                logger.error("Impossible de générer un code pour le rôle : {}", personnelDTO.getRolePersonnel());
                throw new RuntimeException("Impossible de générer un code pour le rôle: " + personnelDTO.getRolePersonnel());
            }
            personnelDTO.setCodePersonnel(generatedCode);

            // Convert DTO to entity and ensure code is set
            Personnel personnel = new Personnel();
            personnel.setCodePersonnel(generatedCode);
            personnel.setNomPersonnel(personnelDTO.getNomPersonnel());
            personnel.setLoginPersonnel(personnelDTO.getLoginPersonnel());
            personnel.setPadPersonnel(passwordEncoder.encode(personnelDTO.getPadPersonnel()));
            personnel.setSexePersonnel(personnelDTO.getSexePersonnel());
            personnel.setPhonePersonnel(personnelDTO.getPhonePersonnel());
            if (personnelDTO.getRolePersonnel() != null) {
                personnel.setRolePersonnel(RolePersonnel.valueOf(personnelDTO.getRolePersonnel().name()));
            }

            Personnel savedPersonnel = personnelRepository.save(personnel);
            // Journalisation du succès de la sauvegarde
            logger.info("Personnel avec code {} sauvegardé avec succès", generatedCode);
            return personnelMapper.toDTO(savedPersonnel);
        } catch (Exception e) {
            // Journalisation de l'erreur lors de la sauvegarde
            logger.error("Erreur lors de la sauvegarde du personnel avec nom {} : {}", personnelDTO.getNomPersonnel(), e.getMessage(), e);
            throw e;
        }
    }
}

