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

    // ----------------------------
    // Tests para findHabitById
    // ----------------------------

    @Test
    void testFindHabitById_Success() {
        // Given: Un hábito existente
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));

        // When: Se busca el hábito por ID
        Optional<HabitResponseDTO> result = habitService.findHabitById(1L);

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals("Exercise", result.get().getName());
        verify(habitRepository).findById(1L);
    }

    @Test
    void testFindHabitById_NotFound() {
        // Given: El repositorio no encuentra el hábito
        when(habitRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se busca un hábito inexistente
        Optional<HabitResponseDTO> result = habitService.findHabitById(1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(habitRepository).findById(1L);
    }

    // ----------------------------
    // Test para findHabitEntityById
    // ----------------------------

    @Test
    void testFindHabitEntityById_Success() {
        // Given: Un hábito existente
        Habit habit = new Habit();
        habit.setId(1L);

        when(habitRepository.findById(1L)).thenReturn(Optional.of(habit));

        // When: Se busca la entidad Habit por ID
        Optional<Habit> result = habitService.findHabitEntityById(1L);

        // Then: El servicio devuelve la entidad sin mapear
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(habitRepository).findById(1L);
    }

    // ----------------------------
    // Tests para findHabitByName
    // ----------------------------

    @Test
    void testFindHabitByName_Success() {
        // Given: Un hábito existente
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        when(habitRepository.findByName("Exercise")).thenReturn(Optional.of(habit));

        // When: Se busca el hábito por nombre
        Optional<Habit> result = habitService.findHabitByName("Exercise");

        // Then: El servicio devuelve la entidad con el nombre correcto
        assertTrue(result.isPresent());
        assertEquals("Exercise", result.get().getName());
        verify(habitRepository).findByName("Exercise");
    }

    @Test
    void testFindHabitByName_NotFound() {
        // Given: El repositorio no encuentra el hábito por nombre
        when(habitRepository.findByName("Exercise")).thenReturn(Optional.empty());

        // When: Se busca un hábito inexistente
        Optional<Habit> result = habitService.findHabitByName("Exercise");

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(habitRepository).findByName("Exercise");
    }

    // ----------------------------
    // Tests para findHabitByNameAndUserId
    // ----------------------------

    @Test
    void testFindHabitByNameAndUserId_Success() {
        // Given: Un hábito existente para un usuario específico
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        when(habitRepository.findHabitByNameAndUserId("Exercise", 1L)).thenReturn(Optional.of(habit));

        // When: Se busca el hábito por nombre y usuario
        Optional<HabitResponseDTO> result = habitService.findHabitByNameAndUserId("Exercise", 1L);

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals("Exercise", result.get().getName());
        verify(habitRepository).findHabitByNameAndUserId("Exercise", 1L);
    }

    @Test
    void testFindHabitByNameAndUserId_NotFound() {
        // Given: No hay hábitos para el usuario y nombre dados
        when(habitRepository.findHabitByNameAndUserId("Exercise", 1L)).thenReturn(Optional.empty());

        // When: Se busca un hábito inexistente
        Optional<HabitResponseDTO> result = habitService.findHabitByNameAndUserId("Exercise", 1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(habitRepository).findHabitByNameAndUserId("Exercise", 1L);
    }

    // ----------------------------
    // Tests para getAllHabits
    // ----------------------------

    @Test
    void testGetAllHabits_UsersExist() {
        // Given: Dos hábitos existentes
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setName("Exercise");

        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setName("Reading");

        when(habitRepository.findAll()).thenReturn(Arrays.asList(habit1, habit2));

        // When: Se obtienen todos los hábitos
        List<HabitResponseDTO> result = habitService.getAllHabits();

        // Then: Se devuelven los hábitos correctamente mapeados
        assertEquals(2, result.size());
        assertEquals("Exercise", result.get(0).getName());
        assertEquals("Reading", result.get(1).getName());
        verify(habitRepository).findAll();
    }

    @Test
    void testGetAllHabits_NoHabits() {
        // Given: El repositorio devuelve una lista vacía
        when(habitRepository.findAll()).thenReturn(List.of());

        // When: Se intenta obtener todos los hábitos
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.getAllHabits()
        );

        // Then: Se lanza la excepción correcta y se verifica la llamada al repositorio
        assertEquals("No hay usuarios registrados", exception.getMessage());
        verify(habitRepository).findAll();
    }

    // ----------------------------
    // Tests para getAllHabitsByUserId
    // ----------------------------

    @Test
    void testGetAllHabitsByUserId_UsersExist() {
        // Given: Dos hábitos existentes para el usuario
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setName("Exercise");

        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setName("Reading");

        when(habitRepository.findAllHabitsByUserId(1L)).thenReturn(Arrays.asList(habit1, habit2));

        // When: Se obtienen todos los hábitos de un usuario
        List<HabitResponseDTO> result = habitService.getAllHabitsByUserId(1L);

        // Then: Se devuelven los hábitos correctamente mapeados
        assertEquals(2, result.size());
        assertEquals("Exercise", result.get(0).getName());
        assertEquals("Reading", result.get(1).getName());
        verify(habitRepository).findAllHabitsByUserId(1L);
    }

    @Test
    void testGetAllHabitsByUserId_NoHabits() {
        // Given: No hay hábitos para el usuario dado
        when(habitRepository.findAllHabitsByUserId(1L)).thenReturn(List.of());

        // When: Se intenta obtener hábitos de un usuario sin registros
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.getAllHabitsByUserId(1L)
        );

        // Then: Se lanza la excepción correcta y se verifica la llamada al repositorio
        assertEquals("No hay usuarios registrados", exception.getMessage());
        verify(habitRepository).findAllHabitsByUserId(1L);
    }

    // ----------------------------
    // Tests para createHabit
    // ----------------------------

    @Test
    void testCreateHabit_Success() {
        // Given: Un DTO válido y repositorio vacío (sin duplicados)
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        // When: Se crean las dependencias y se guarda el hábito
        when(habitTypeService.findHabitTypeEntityById(1L)).thenReturn(Optional.of(habitType));
        when(userService.findUserEntityById(1L)).thenReturn(Optional.of(user));

        Habit savedHabit = new Habit();
        savedHabit.setId(1L);
        savedHabit.setName("Exercise");

        when(habitRepository.save(any(Habit.class))).thenReturn(savedHabit);

        // Then: El servicio devuelve el DTO y se verifican las llamadas al repositorio
        HabitResponseDTO result = habitService.createHabit(requestDTO);
        assertEquals("Exercise", result.getName());
        verify(habitTypeService).findHabitTypeEntityById(1L);
        verify(userService).findUserEntityById(1L);
        verify(habitRepository).save(any(Habit.class));
    }

    @Test
    void testCreateHabit_HabitTypeNotFound() {
        // Given: Un DTO válido pero el tipo de hábito no existe
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        when(habitTypeService.findHabitTypeEntityById(1L)).thenReturn(Optional.empty());

        // When: Se intenta crear un hábito con un tipo inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.createHabit(requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llaman métodos innecesarios
        assertEquals("Tipo de hábito no encontrado con ID: 1", exception.getMessage());
        verify(habitTypeService).findHabitTypeEntityById(1L);
        verify(userService, never()).findUserEntityById(anyLong());
        verify(habitRepository, never()).save(any(Habit.class));
    }

    // ----------------------------
    // Tests para updateHabit
    // ----------------------------

    @Test
    void testUpdateHabit_Success() {
        // Given: Un DTO válido y un hábito existente
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise Updated", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        Habit existingHabit = new Habit();
        existingHabit.setId(1L);
        existingHabit.setName("Exercise");

        when(habitRepository.findById(1L)).thenReturn(Optional.of(existingHabit));
        when(habitRepository.save(any(Habit.class))).thenReturn(existingHabit);

        // When: Se actualizan los campos del hábito
        HabitResponseDTO result = habitService.updateHabit(1L, requestDTO);

        // Then: El servicio devuelve el DTO actualizado y se verifica la llamada al repositorio
        assertEquals("Exercise Updated", result.getName());
        assertEquals("Daily", result.getFrequency());
        verify(habitRepository).findById(1L);
        verify(habitRepository).save(existingHabit);
    }

    @Test
    void testUpdateHabit_HabitNotFound() {
        // Given: El hábito no existe
        HabitRequestDTO requestDTO = new HabitRequestDTO(
                "Exercise Updated", "Daily exercise", "Daily", LocalDate.now(), LocalDate.now().plusDays(30), 1L, 1L
        );

        when(habitRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta actualizar un hábito inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.updateHabit(1L, requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("Habit not found", exception.getMessage());
        verify(habitRepository).findById(1L);
        verify(habitRepository, never()).save(any(Habit.class));
    }

    // ----------------------------
    // Tests para deleteHabit
    // ----------------------------

    @Test
    void testDeleteHabit_Success() {
        // Given: El hábito existe
        when(habitRepository.existsById(1L)).thenReturn(true);

        // When: Se elimina el hábito
        habitService.deleteHabit(1L);

        // Then: Se llama a deleteById y se verifica la existencia
        verify(habitRepository).existsById(1L);
        verify(habitRepository).deleteById(1L);
    }

    @Test
    void testDeleteHabit_HabitNotFound() {
        // Given: El hábito no existe
        when(habitRepository.existsById(1L)).thenReturn(false);

        // When: Se intenta eliminar un hábito inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitService.deleteHabit(1L)
        );

        // Then: Se lanza la excepción correcta y no se llama a deleteById
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(habitRepository).existsById(1L);
        verify(habitRepository, never()).deleteById(1L);
    }

}