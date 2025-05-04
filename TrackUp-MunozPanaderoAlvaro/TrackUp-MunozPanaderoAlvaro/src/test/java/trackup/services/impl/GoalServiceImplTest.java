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

    @Test
    void testFindGoalById_Success() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Run a marathon");
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        Optional<GoalResponseDTO> result = goalService.findGoalById(1L);

        assertTrue(result.isPresent());
        assertEquals("Run a marathon", result.get().getName());
        verify(goalRepository).findById(1L);
    }

    @Test
    void testFindGoalById_NotFound() {
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<GoalResponseDTO> result = goalService.findGoalById(1L);

        assertFalse(result.isPresent());
        verify(goalRepository).findById(1L);
    }

    @Test
    void testFindGoalByName_Success() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Run a marathon");
        when(goalRepository.findByName("Run a marathon")).thenReturn(Optional.of(goal));

        Optional<GoalResponseDTO> result = goalService.findGoalByName("Run a marathon");

        assertTrue(result.isPresent());
        assertEquals("Run a marathon", result.get().getName());
        verify(goalRepository).findByName("Run a marathon");
    }

    @Test
    void testFindGoalByName_NotFound() {
        when(goalRepository.findByName("Run a marathon")).thenReturn(Optional.empty());

        Optional<GoalResponseDTO> result = goalService.findGoalByName("Run a marathon");

        assertFalse(result.isPresent());
        verify(goalRepository).findByName("Run a marathon");
    }

    @Test
    void testFindGoalByNameAndUserId_Success() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setName("Run a marathon");

        User user = new User();
        user.setId(1L);
        goal.setUser(user);

        when(goalRepository.findGoalByNameAndUserId("Run a marathon", 1L)).thenReturn(Optional.of(goal));

        Optional<GoalResponseDTO> result = goalService.findGoalByNameAndUserId("Run a marathon", 1L);

        assertTrue(result.isPresent());
        assertEquals("Run a marathon", result.get().getName());
        assertEquals(1L, result.get().getUserId());
        verify(goalRepository).findGoalByNameAndUserId("Run a marathon", 1L);
    }

    @Test
    void testFindGoalByNameAndUserId_NotFound() {
        when(goalRepository.findGoalByNameAndUserId("Run a marathon", 1L)).thenReturn(Optional.empty());

        Optional<GoalResponseDTO> result = goalService.findGoalByNameAndUserId("Run a marathon", 1L);

        assertFalse(result.isPresent());
        verify(goalRepository).findGoalByNameAndUserId("Run a marathon", 1L);
    }

    @Test
    void testGetAllGoals_UsersExist() {
        Goal goal1 = new Goal();
        goal1.setId(1L);
        goal1.setName("Run a marathon");

        Goal goal2 = new Goal();
        goal2.setId(2L);
        goal2.setName("Read 50 books");

        when(goalRepository.findAll()).thenReturn(Arrays.asList(goal1, goal2));

        List<GoalResponseDTO> result = goalService.getAllGoals();

        assertEquals(2, result.size());
        assertEquals("Run a marathon", result.get(0).getName());
        assertEquals("Read 50 books", result.get(1).getName());
        verify(goalRepository).findAll();
    }


    @Test
    void testCreateGoal_Success() {
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon", "Complete a marathon in 2 hours", 1L);

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Goal savedGoal = new Goal();
        savedGoal.setId(1L);
        savedGoal.setName("Run a marathon");
        savedGoal.setUser(user);
        when(goalRepository.save(any(Goal.class))).thenReturn(savedGoal);

        GoalResponseDTO result = goalService.createGoal(requestDTO);

        assertEquals("Run a marathon", result.getName());
        assertEquals(1L, result.getUserId());
        verify(userRepository).findById(1L);
        verify(goalRepository).save(any(Goal.class));
    }

    @Test
    void testCreateGoal_UserNotFound() {
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon", "Complete a marathon in 2 hours", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> goalService.createGoal(requestDTO)
        );
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void testUpdateGoal_Success() {
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon updated", "Complete a marathon in 1.5 hours", 1L);

        Goal existingGoal = new Goal();
        existingGoal.setId(1L);
        existingGoal.setName("Run a marathon");

        when(goalRepository.findById(1L)).thenReturn(Optional.of(existingGoal));
        when(goalRepository.save(any(Goal.class))).thenReturn(existingGoal);

        GoalResponseDTO result = goalService.updateGoal(1L, requestDTO);

        assertEquals("Run a marathon updated", result.getName());
        verify(goalRepository).findById(1L);
        verify(goalRepository).save(existingGoal);
    }

    @Test
    void testUpdateGoal_GoalNotFound() {
        GoalRequestDTO requestDTO = new GoalRequestDTO("Run a marathon updated", "Complete a marathon in 1.5 hours", 1L);
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> goalService.updateGoal(1L, requestDTO)
        );
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(1L);
        verify(goalRepository, never()).save(any(Goal.class));
    }

    @Test
    void testDeleteGoal_Success() {
        Goal goal = new Goal();
        goal.setId(1L);
        when(goalRepository.findById(1L)).thenReturn(Optional.of(goal));

        goalService.deleteGoal(1L);

        verify(goalRepository).findById(1L);
        verify(goalRepository).delete(goal);
    }

    @Test
    void testDeleteGoal_GoalNotFound() {
        when(goalRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> goalService.deleteGoal(1L)
        );
        assertEquals("Goal not found", exception.getMessage());
        verify(goalRepository).findById(1L);
        verify(goalRepository, never()).delete(any(Goal.class));
    }

}