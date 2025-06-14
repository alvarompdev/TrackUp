package trackup.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import trackup.dto.request.GoalRequestDTO;
import trackup.dto.response.GoalResponseDTO;
import trackup.entity.Goal;
import trackup.entity.User;
import trackup.repository.GoalRepository;
import trackup.repository.UserRepository;

/**
 * Test de la clase GoalServiceImpl
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------
    // Tests para findGoalById
    // ----------------------------

    @Test
    void testFindGoalById_Success() {
        // Given: Un objetivo existente
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Run a marathon");

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        // When: Se busca el objetivo por ID
        Optional<GoalResponseDTO> result = goalService.findGoalById(1L);

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals("Run a marathon", result.get().getName());
        verify(goalRepository).findById(1L);
    }

    @Test
    void testFindGoalById_NotFound() {
        // Given: El repositorio no encuentra el objetivo
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se busca un objetivo inexistente
        Optional<GoalResponseDTO> result = goalService.findGoalById(1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(goalRepository).findById(1L);
    }

    // ----------------------------
    // Tests para findGoalByName
    // ----------------------------

    @Test
    void testFindGoalByName_Success() {
        // Given: Un objetivo existente
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Run a marathon");

        when(goalRepository.findByName("Run a marathon")).thenReturn(Optional.of(goal));

        // When: Se busca el objetivo por nombre
        Optional<GoalResponseDTO> result = goalService.findGoalByName("Run a marathon");

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals("Run a marathon", result.get().getName());
        verify(goalRepository).findByName("Run a marathon");
    }

    @Test
    void testFindGoalByName_NotFound() {
        // Given: El repositorio no encuentra el objetivo por nombre
        when(goalRepository.findByName("Run a marathon")).thenReturn(Optional.empty());

        // When: Se busca un objetivo inexistente
        Optional<GoalResponseDTO> result = goalService.findGoalByName("Run a marathon");

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(goalRepository).findByName("Run a marathon");
    }

    // ----------------------------
    // Tests para findGoalByNameAndUserId
    // ----------------------------

    @Test
    void testFindGoalByNameAndUserId_Success() {
        // Given: Un objetivo existente para un usuario específico
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Run a marathon");

        User user = new User();
        user.setId(1L);
        goal.setUser(user); // ✅ Asigna el usuario al objetivo

        when(goalRepository.findGoalByNameAndUserId("Run a marathon", 1L)).thenReturn(Optional.of(goal));

        // When: Se busca el objetivo por nombre y usuario
        Optional<GoalResponseDTO> result = goalService.findGoalByNameAndUserId("Run a marathon", 1L);

        // Then: El servicio devuelve el DTO con el ID del usuario
        assertTrue(result.isPresent());
        assertEquals("Run a marathon", result.get().getName());
        assertEquals(1L, result.get().getUserId());
        verify(goalRepository).findGoalByNameAndUserId("Run a marathon", 1L);
    }

    @Test
    void testFindGoalByNameAndUserId_NotFound() {
        // Given: No hay objetivos para el usuario y nombre dados
        when(goalRepository.findGoalByNameAndUserId("Run a marathon", 1L)).thenReturn(Optional.empty());

        // When: Se busca un objetivo inexistente
        Optional<GoalResponseDTO> result = goalService.findGoalByNameAndUserId("Run a marathon", 1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(goalRepository).findGoalByNameAndUserId("Run a marathon", 1L);
    }

    // ----------------------------
    // Tests para getAllGoals
    // ----------------------------

    @Test
    void testGetAllGoals_UsersExist() {
        // Given: Dos objetivos existentes
        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setName("Run a marathon");

        Goal goal2 = new Goal();
        goal2.setId(2L);
        goal2.setName("Read 50 books");

        when(goalRepository.findAll()).thenReturn(Arrays.asList(goal1, goal2));

        // When: Se obtienen todos los objetivos
        List<GoalResponseDTO> result = goalService.getAllGoals();

        // Then: Se devuelven los DTOs correctamente mapeados
        assertEquals(2, result.size());
        assertEquals("Run a marathon", result.get(0).getName());
        assertEquals("Read 50 books", result.get(1).getName());
        verify(goalRepository).findAll();
    }

    // ----------------------------
    // Tests para createGoal
    // ----------------------------

    @Test
    void testCreateGoal_Success() {
        // Given: Un DTO válido y usuario existente
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon", "Complete a marathon in 2 hours", 1L);

        User user = new User();
        user.setId(1L); // ✅ Crea un usuario válido

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Goal savedGoal = new Goal();
        savedGoal.setId(1L);
        savedGoal.setName("Run a marathon");
        savedGoal.setUser(user); // ✅ Asigna el usuario al objetivo

        when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);

        // When: Se crea el objetivo
        GoalResponseDTO result = goalService.createGoal(requestDTO);

        // Then: El DTO tiene el ID del usuario y se verifican las llamadas al repositorio
        assertEquals("Run a marathon", result.getName());
        assertEquals(1L, result.getUserId());
        verify(userRepository).findById(1L);
        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void testCreateGoal_UserNotFound() {
        // Given: Un DTO válido pero el usuario no existe
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon", "Complete a marathon in 2 hours", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta crear un objetivo con usuario inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> goalService.createGoal(requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    // ----------------------------
    // Tests para updateGoal
    // ----------------------------

    @Test
    void testUpdateGoal_Success() {
        // Given: Un DTO válido y un objetivo existente
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon updated", "Complete a marathon in 1.5 hours", 1L);

        Goal existingGoal = new Goal();
        existingGoal.setId(1L);
        existingGoal.setName("Run a marathon");

        when(goalRepository.findById(1L)).thenReturn(Optional.of(existingGoal));
        when(goalRepository.save(any(Goal.class))).thenReturn(existingGoal);

        // When: Se actualiza el objetivo
        GoalResponseDTO result = goalService.updateGoal(1L, requestDTO);

        // Then: El DTO tiene el nombre actualizado y se verifica la llamada al repositorio
        assertEquals("Run a marathon updated", result.getName());
        verify(goalRepository).findById(1L);
        verify(goalRepository).save(existingGoal);
    }

    @Test
    void testUpdateGoal_GoalNotFound() {
        // Given: El objetivo no existe
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon updated", "Complete a marathon in 1.5 hours", 1L);
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta actualizar un objetivo inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> goalService.updateGoal(1L, requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(1L);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    // ----------------------------
    // Tests para deleteGoal
    // ----------------------------

    @Test
    void testDeleteGoal_Success() {
        // Given: El objetivo existe
        Goal goal = new Goal();
        goal.setId(1L);

        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        // When: Se elimina el objetivo
        goalService.deleteGoal(1L);

        // Then: Se llama a deleteById y se verifica la llamada al repositorio
        verify(goalRepository).findById(1L);
        verify(goalRepository).delete(goal);
    }

    @Test
    void testDeleteGoal_GoalNotFound() {
        // Given: El objetivo no existe
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta eliminar un objetivo inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> goalService.deleteGoal(1L)
        );

        // Then: Se lanza la excepción correcta y no se llama a delete
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(1L);
        verify(goalRepository, never()).delete(any(Goal.class));
    }

}