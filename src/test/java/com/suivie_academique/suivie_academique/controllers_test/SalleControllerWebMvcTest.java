//package com.suivie_academique.suivie_academique.controllers_test;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//// Import des classes de configuration et de sécurité
//import com.suivi_academique.SuiviAcademiqueApplication; // 🔑 Rétablissement de l'import de la classe principale
//import com.suivi_academique.config.JwtAuthenticationEntryPoint;
//import com.suivi_academique.config.JwtAuthenticationFilter;
//import com.suivi_academique.config.SecurityConfig;
//import com.suivi_academique.config.JwtUtil;
//
//import com.suivi_academique.controllers.SalleController;
//import com.suivi_academique.dto.SalleDTO;
//import com.suivi_academique.services.implementations.PersonnelDetailsService;
//import com.suivi_academique.services.implementations.SalleService;
//import com.suivi_academique.utils.SalleStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.test.context.ContextConfiguration; // 🔑 Import nécessaire
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//// =================================================================
//// 🚀 CORRECTION CLÉ : Rétablissement de ContextConfiguration pour trouver la classe principale
//// =================================================================
//
//@WebMvcTest(
//        controllers = SalleController.class,
//        excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class
//)
//// 🔑 Rétablissement de cette ligne qui permet à Spring de localiser la configuration principale
//@ContextConfiguration(classes = SuiviAcademiqueApplication.class)
//// 🔑 Maintien des Imports pour que Spring Security trouve les beans spécifiques du filtre et de l'entrypoint
//@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtAuthenticationEntryPoint.class})
//@DisplayName("Tests d'intégration du SalleController avec Sécurité JWT")
//class SalleControllerWebMvcTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private SalleService salleService;
//
//    @MockitoBean
//    private JwtUtil jwtUtils;
//
//    @MockitoBean
//    private PersonnelDetailsService personnelDetailsService;
//
//    // Mockage des dépendances utilisées par SecurityConfig
//    @MockitoBean
//    private AuthenticationProvider authenticationProvider;
//
//    @MockitoBean
//    private AuthenticationManager authenticationManager;
//
//    @MockitoBean
//    private AuthenticationConfiguration authenticationConfiguration;
//
//    // Définition d'un utilisateur de test pour la simulation avec .with(user())
//    private User testUser;
//
//    private static final String API_BASE = "/salle";
//    private SalleDTO salleDto;
//
//    @BeforeEach
//    void setup() {
//        // Initialisation du DTO
//        salleDto = new SalleDTO();
//        salleDto.setCodeSalle("S001");
//        salleDto.setContenace(50);
//        salleDto.setDescSalle("Salle de conférence");
//        salleDto.setStatutSalle(SalleStatus.LIBRE);
//
//        // Utilisateur que nous utiliserons pour simuler l'authentification réussie (Rôle ADMIN)
//        testUser = new User(
//                "TEST_USER",
//                "password",
//                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
//        );
//    }
//
//    // =================================================================
//    // TESTS CRUD DE BASE AVEC AUTHENTIFICATION (Succès)
//    // =================================================================
//
//    @Test
//    @DisplayName("POST /salle - crée une salle avec succès (201)")
//    void save_Success_ReturnsCreated() throws Exception {
//        when(salleService.save(any(SalleDTO.class))).thenReturn(salleDto);
//
//        mockMvc.perform(post(API_BASE)
//                        .with(user(testUser))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(salleDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.codeSalle").value("S001"));
//    }
//
//    @Test
//    @DisplayName("POST /salle - échoue avec données invalides (400)")
//    void save_InvalidData_ReturnsBadRequest() throws Exception {
//        SalleDTO invalidDTO = new SalleDTO();
//
//        // Simuler l'échec métier du service (ou de la validation)
//        when(salleService.save(any(SalleDTO.class))).thenThrow(new RuntimeException("Données incorret"));
//
//        mockMvc.perform(post(API_BASE)
//                        .with(user(testUser))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        // Le DTO vide cause l'exception que nous avons mockée ci-dessus
//                        .content(objectMapper.writeValueAsString(invalidDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Données incorret"));
//    }
//
//    @Test
//    @DisplayName("GET /salle - retourne toutes les salles (200)")
//    void getAll_ReturnsAllSalles() throws Exception {
//        List<SalleDTO> sallesList = List.of(salleDto);
//        when(salleService.getAll()).thenReturn(sallesList);
//
//        mockMvc.perform(get(API_BASE)
//                        .with(user(testUser)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].codeSalle").value("S001"));
//    }
//
//    @Test
//    @DisplayName("GET /salle/{codeSalle} - trouve salle existante (200)")
//    void show_ExistingSalle_ReturnsOk() throws Exception {
//        when(salleService.getById("S001")).thenReturn(salleDto);
//
//        mockMvc.perform(get(API_BASE + "/S001")
//                        .with(user(testUser)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.codeSalle").value("S001"));
//    }
//
//    @Test
//    @DisplayName("PUT /salle/{codeSalle} - met à jour salle existante (200)")
//    void update_ExistingSalle_ReturnsUpdated() throws Exception {
//        SalleDTO updatedDTO = new SalleDTO();
//        updatedDTO.setCodeSalle("S001");
//        updatedDTO.setContenace(100);
//        updatedDTO.setDescSalle("Salle mise à jour");
//        updatedDTO.setStatutSalle(SalleStatus.OCCUPE);
//
//        when(salleService.update(eq("S001"), any(SalleDTO.class))).thenReturn(updatedDTO);
//
//        mockMvc.perform(put(API_BASE + "/S001")
//                        .with(user(testUser))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.contenace").value(100));
//    }
//
//    @Test
//    @DisplayName("DELETE /salle/{codeSalle} - supprime salle existante (200)")
//    void delete_ExistingSalle_ReturnsOk() throws Exception {
//        doNothing().when(salleService).delete("S001");
//
//        mockMvc.perform(delete(API_BASE + "/S001")
//                        .with(user(testUser)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("DELETE /salle/{codeSalle} - salle inexistante (400)")
//    void delete_NonExistingSalle_ReturnsBadRequest() throws Exception {
//        doThrow(new RuntimeException("Salle inexistante")).when(salleService).delete("S999");
//
//        mockMvc.perform(delete(API_BASE + "/S999")
//                        .with(user(testUser)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Salle inexistante"));
//    }
//
//    @Test
//    @DisplayName("GET /salle/{codeSalle} - salle inexistante (400)")
//    void show_NonExistingSalle_ReturnsBadRequest() throws Exception {
//        when(salleService.getById("S999")).thenThrow(new RuntimeException("Salle non trouvée"));
//
//        mockMvc.perform(get(API_BASE + "/S999")
//                        .with(user(testUser)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Salle non trouvée"));
//    }
//
//
//    // =================================================================
//    // TESTS DE SÉCURITÉ (Doivent renvoyer 401 Unauthorized)
//    // =================================================================
//
//    @Test
//    @DisplayName("Sécurité : Refus d'accès sans token (401)")
//    void access_without_token_is_unauthorized() throws Exception {
//        mockMvc.perform(get(API_BASE)
//                        .with(anonymous())) // Simule un utilisateur non authentifié (pas de token)
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @DisplayName("Sécurité : Refus d'accès avec token invalide (401)")
//    void access_with_invalid_token_is_unauthorized() throws Exception {
//        // Simuler que le JwtUtil renvoie false (token invalide)
//        when(personnelDetailsService.loadUserByUsername(anyString())).thenReturn(testUser); // Pour que le filtre essaie de valider
//        when(jwtUtils.isTokenValid(eq("invalid-token"), any())).thenReturn(false);
//
//        mockMvc.perform(get(API_BASE)
//                        .header("Authorization", "Bearer invalid-token"))
//                .andExpect(status().isUnauthorized());
//    }
//}