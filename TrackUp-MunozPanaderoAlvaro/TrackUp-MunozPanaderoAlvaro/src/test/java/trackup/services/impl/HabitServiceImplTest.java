package trackup.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import trackup.dto.request.HabitRequestDTO;
import trackup.dto.response.HabitResponseDTO;
import trackup.entity.Habit;
import trackup.entity.HabitType;
import trackup.entity.User;
import trackup.repository.HabitRepository;
import trackup.services.UserService;
import trackup.services.HabitTypeService;

/**
 * Test de la clase HabitServiceImpl
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class HabitServiceImplTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserService userService;

    @Mock
    private HabitTypeService habitTypeService;

    @InjectMocks
    private HabitServiceImpl habitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindHabitById_Success() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));

        Optional<HabitResponseDTO> result = habitService.findHabitById(1L);

        assertTrue(result.isPresent());
        assertEquals("Exercise", result.get().getName());
        verify(habitRepository).findById(1L);
    }

    @Test
    void testFindHabitById_NotFound() {
        when(habitRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<HabitResponseDTO> result = habitService.findHabitById(1L);

        assertFalse(result.isPresent());
        verify(habitRepository).findById(1L);
    }

    @Test
    void testFindHabitEntityById_Success() {
        Habit habit = new Habit();
        habit.setId(1L);
        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));

        Optional<Habit> result = habitService.findHabitEntityById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(habitRepository).findById(1L);
    }

    @Test
    void testFindHabitByName_Success() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");
        when(habitRepository.findByName("Exercise")).thenReturn(Optional.of(habit));

        Optional<Habit> result = habitService.findHabitByName("Exercise");

        assertTrue(result.isPresent());
        assertEquals("Exercise", result.get().getName());
        verify(habitRepository).findByName("Exercise");
    }

    @Test
    void testFindHabitByName_NotFound() {
        when(habitRepository.findByName("Exercise")).thenReturn(Optional.empty());

        Optional<Habit> result = habitService.findHabitByName("Exercise");

        assertFalse(result.isPresent());
        verify(habitRepository).findByName("Exercise");
    }

    @Test
    void testFindHabitByNameAndUserId_Success() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");
        when(habitRepository.findHabitByNameAndUserId("Exercise", 1L)).thenReturn(Optional.of(habit));

        Optional<HabitResponseDTO> result = habitService.findHabitByNameAndUserId("Exercise", 1L);

        assertTrue(result.isPresent());
        assertEquals("Exercise", result.get().getName());
        verify(habitRepository).findHabitByNameAndUserId("Exercise", 1L);
    }

    @Test
    void testFindHabitByNameAndUserId_NotFound() {
        when(habitRepository.findHabitByNameAndUserId("Exercise", 1L)).thenReturn(Optional.empty());

        Optional<HabitResponseDTO> result = habitService.findHabitByNameAndUserId("Exercise", 1L);

        assertFalse(result.isPresent());
        verify(habitRepository).findHabitByNameAndUserId("Exercise", 1L);
    }

    @Test
    void testGetAllHabits_UsersExist() {
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setName("Exercise");

        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setName("Reading");

        when(habitRepository.findAll()).thenReturn(Arrays.asList(habit1, habit2));

        List<HabitResponseDTO> result = habitService.getAllHabits();

        assertEquals(2, result.size());
        assertEquals("Exercise", result.get(0).getName());
        assertEquals("Reading", result.get(1).getName());
        verify(habitRepository).findAll();
    }

    @Test
    void testGetAllHabits_NoHabits() {
        when(habitRepository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.getAllHabits()
        );
        assertEquals("No hay usuarios registrados", exception.getMessage());
        verify(habitRepository).findAll();
    }

    @Test
    void testGetAllHabitsByUserId_UsersExist() {
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setName("Exercise");

        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setName("Reading");

        when(habitRepository.findAllHabitsByUserId(1L)).thenReturn(Arrays.asList(habit1, habit2));

        List<HabitResponseDTO> result = habitService.getAllHabitsByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("Exercise", result.get(0).getName());
        assertEquals("Reading", result.get(1).getName());
        verify(habitRepository).findAllHabitsByUserId(1L);
    }

    @Test
    void testGetAllHabitsByUserId_NoHabits() {
        when(habitRepository.findAllHabitsByUserId(1L)).thenReturn(List.of());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.getAllHabitsByUserId(1L)
        );
        assertEquals("No hay usuarios registrados", exception.getMessage());
        verify(habitRepository).findAllHabitsByUserId(1L);
    }

    @Test
    void testCreateHabit_Success() {
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(habitTypeService.findHabitTypeEntityById(1L)).thenReturn(Optional.of(habitType));
        when(userService.findUserEntityById(1L)).thenReturn(Optional.of(user));

        Habit savedHabit = new Habit();
        savedHabit.setId(1L);
        savedHabit.setName("Exercise");
        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);

        HabitResponseDTO result = habitService.createHabit(requestDTO);

        assertEquals("Exercise", result.getName());
        verify(habitTypeService).findHabitTypeEntityById(1L);
        verify(userService).findUserEntityById(1L);
        verify(habitRepository).save(any(Habit.class));
    }

    @Test
    void testCreateHabit_HabitTypeNotFound() {
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        when(habitTypeService.findHabitTypeEntityById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.createHabit(requestDTO)
        );
        assertEquals("Tipo de hábito no encontrado con ID: 1", exception.getMessage());
        verify(habitTypeService).findHabitTypeEntityById(1L);
        verify(userService, never()).findUserEntityById(anyLong());
        verify(habitRepository, never()).save(any(Habit.class));
    }

    @Test
    void testUpdateHabit_Success() {
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise Updated", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        Habit existingHabit = new Habit();
        existingHabit.setId(1L);
        existingHabit.setName("Exercise");

        when(habitRepository.findById(1L)).thenReturn(Optional.of(existingHabit));
        when(habitRepository.save(any(Habit.class))).thenReturn(existingHabit);

        HabitResponseDTO result = habitService.updateHabit(1L, requestDTO);

        assertEquals("Exercise Updated", result.getName());
        assertEquals("Daily", result.getFrequency());
        verify(habitRepository).findById(1L);
        verify(habitRepository).save(existingHabit);
    }

    @Test
    void testUpdateHabit_HabitNotFound() {
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise Updated", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        when(habitRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.updateHabit(1L, requestDTO)
        );
        assertEquals("Habit not found", exception.getMessage());
        verify(habitRepository).findById(1L);
        verify(habitRepository, never()).save(any(Habit.class));
    }

    @Test
    void testDeleteHabit_Success() {
        when(habitRepository.existsById(1L)).thenReturn(true);

        habitService.deleteHabit(1L);

        verify(habitRepository).existsById(1L);
        verify(habitRepository).deleteById(1L);
    }

    @Test
    void testDeleteHabit_HabitNotFound() {
        when(habitRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.deleteHabit(1L)
        );
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(habitRepository).existsById(1L);
        verify(habitRepository, never()).deleteById(1L);
    }

}