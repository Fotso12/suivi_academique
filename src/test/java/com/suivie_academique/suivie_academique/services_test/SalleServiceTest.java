package com.suivie_academique.suivie_academique.services_test;

import com.suivi_academique.dto.SalleDTO;
import com.suivi_academique.entities.Salle;
import com.suivi_academique.mappers.SalleMapper;
import com.suivi_academique.repositories.SalleRepository;
import com.suivi_academique.services.implementations.SalleService;
import com.suivi_academique.utils.SalleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalleServiceTest {

    @Mock
    private SalleRepository salleRepository;

    @Mock
    private SalleMapper salleMapper;

    @InjectMocks
    private SalleService salleService;

    private Salle salle;
    private SalleDTO salleDTO;

    /**
     * Initialisation des objets communs utilisés
     * dans les différents tests
     */
    @BeforeEach
    void initialisation() {
        salle = new Salle();
        salle.setCodeSalle("S1");
        salle.setContenance(30);
        salle.setDescSalle("Salle informatique");
        salle.setStatutSalle(SalleStatus.LIBRE);

        salleDTO = new SalleDTO();
        salleDTO.setCodeSalle("S1");
        salleDTO.setContenace(30);
        salleDTO.setDescSalle("Salle informatique");
        salleDTO.setStatutSalle(SalleStatus.LIBRE);
    }

    /**
     * Test de la sauvegarde d'une salle
     * avec des données valides
     */
    @Test
    void doit_enregistrer_une_salle_valide() {
        when(salleMapper.toEntity(salleDTO)).thenReturn(salle);
        when(salleRepository.save(salle)).thenReturn(salle);
        when(salleMapper.toDTO(salle)).thenReturn(salleDTO);

        SalleDTO resultat = salleService.save(salleDTO);

        assertNotNull(resultat);
        assertEquals("S1", resultat.getCodeSalle());
        verify(salleRepository, times(1)).save(salle);
    }

    /**
     * Test de la sauvegarde d'une salle
     * avec des données invalides (contenance < 10)
     */
    @Test
    void doit_lever_exception_si_donnees_invalides_lors_enregistrement() {
        salleDTO.setContenace(5);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> salleService.save(salleDTO)
        );

        assertEquals("Données incorret", exception.getMessage());
        verify(salleRepository, never()).save(any());
    }

    /**
     * Test de la récupération de toutes les salles
     */
    @Test
    void doit_retourner_liste_des_salles() {
        when(salleRepository.findAll()).thenReturn(Arrays.asList(salle));
        when(salleMapper.toDTO(salle)).thenReturn(salleDTO);

        List<SalleDTO> resultat = salleService.getAll();

        assertEquals(1, resultat.size());
        verify(salleRepository, times(1)).findAll();
    }

    /**
     * Test de la récupération d'une salle existante par son code
     */
    @Test
    void doit_retourner_salle_par_code() {
        when(salleRepository.findById("S1")).thenReturn(Optional.of(salle));
        when(salleMapper.toDTO(salle)).thenReturn(salleDTO);

        SalleDTO resultat = salleService.getById("S1");

        assertNotNull(resultat);
        assertEquals("S1", resultat.getCodeSalle());
    }

    /**
     * Test de la mise à jour d'une salle existante
     */
    @Test
    void doit_mettre_a_jour_une_salle_existante() {
        when(salleRepository.findById("S1")).thenReturn(Optional.of(salle));
        when(salleRepository.save(salle)).thenReturn(salle);
        when(salleMapper.toDTO(salle)).thenReturn(salleDTO);

        SalleDTO resultat = salleService.update("S1", salleDTO);

        assertNotNull(resultat);
        verify(salleRepository, times(1)).save(salle);
    }

    /**
     * Test de la suppression d'une salle existante
     */
    @Test
    void doit_supprimer_une_salle_existante() {
        when(salleRepository.existsById("S1")).thenReturn(true);

        salleService.delete("S1");

        verify(salleRepository, times(1)).deleteById("S1");
    }

    /**
     * Test de la suppression d'une salle inexistante
     * → une exception doit être levée
     */
    @Test
    void doit_lever_exception_si_salle_inexistante_lors_suppression() {
        when(salleRepository.existsById("S1")).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> salleService.delete("S1")
        );

        assertEquals("Salle inexistante", exception.getMessage());
        verify(salleRepository, never()).deleteById(any());
    }
}

