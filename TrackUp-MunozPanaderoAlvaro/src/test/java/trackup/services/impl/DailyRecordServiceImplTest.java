package trackup.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import trackup.dto.request.DailyRecordRequestDTO;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.entity.DailyRecord;
import trackup.entity.Habit;
import trackup.repository.DailyRecordRepository;

/**
 * Test de la clase DailyRecordServiceImpl
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class DailyRecordServiceImplTest {

    @Mock
    private DailyRecordRepository dailyRecordRepository;

    @Mock
    private HabitServiceImpl habitService;

    @InjectMocks
    private DailyRecordServiceImpl dailyRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------------------
    // Tests para findDailyRecordById
    // ----------------------------

    @Test
    void testFindDailyRecordById_Success() {
        // Given: Un registro diario existente con un hábito válido
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(1L);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setCompleted(true);
        dailyRecord.setHabit(habit); // ✅ Asigna un Hábito válido

        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.of(dailyRecord));

        // When: Se busca el registro por ID
        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordById(1L);

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals(LocalDate.now(), result.get().getDate());
        assertTrue(result.get().getCompleted());
        assertEquals(1L, result.get().getHabitId()); // ✅ Verifica que el ID del hábito esté presente
        verify(dailyRecordRepository).findById(1L);
    }

    @Test
    void testFindDailyRecordById_NotFound() {
        // Given: El repositorio no encuentra el registro
        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se busca un registro inexistente
        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordById(1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(dailyRecordRepository).findById(1L);
    }

    // ----------------------------
    // Tests para findDailyRecordByCompleted
    // ----------------------------

    @Test
    void testFindDailyRecordByCompleted_Success() {
        // Given: Un registro diario completado existente
        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(1L);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setCompleted(true);
        dailyRecord.setHabit(habit); // ✅ Asigna un Hábito válido

        when(dailyRecordRepository.findByCompleted(eq(true))).thenReturn(Optional.of(dailyRecord));

        // When: Se busca el registro por estado de completado
        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByCompleted(true);

        // Then: El servicio devuelve el DTO con el estado actualizado
        assertTrue(result.isPresent());
        assertTrue(result.get().getCompleted());
        assertEquals(1L, result.get().getHabitId()); // ✅ Verifica que el ID del hábito esté presente
        verify(dailyRecordRepository).findByCompleted(eq(true));
    }

    @Test
    void testFindDailyRecordByCompleted_NotFound() {
        // Given: No hay registros diarios completados
        when(dailyRecordRepository.findByCompleted(eq(true))).thenReturn(Optional.empty());

        // When: Se busca un registro completado inexistente
        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByCompleted(true);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(dailyRecordRepository).findByCompleted(eq(true));
    }

    // ----------------------------
    // Tests para createDailyRecord
    // ----------------------------

    @Test
    void testCreateDailyRecord_Success() {
        // Given: Un DTO válido y un hábito existente
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now(), true, 1L, 1L
        );

        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        when(habitService.findHabitEntityById(1L)).thenReturn(Optional.of(habit));

        DailyRecord savedDailyRecord = new DailyRecord();
        savedDailyRecord.setId(1L);
        savedDailyRecord.setDate(LocalDate.now());
        savedDailyRecord.setCompleted(true);
        savedDailyRecord.setHabit(habit); // ✅ Asigna el Hábito al registro

        when(dailyRecordRepository.save(any(DailyRecord.class))).thenReturn(savedDailyRecord);

        // When: Se crea el registro diario
        DailyRecordResponseDTO result = dailyRecordService.createDailyRecord(requestDTO);

        // Then: El servicio devuelve el DTO y se verifican las llamadas al repositorio
        assertEquals(LocalDate.now(), result.getDate());
        assertTrue(result.getCompleted());
        assertEquals(1L, result.getHabitId());
        verify(habitService).findHabitEntityById(1L);
        verify(dailyRecordRepository).save(any(DailyRecord.class));
    }

    @Test
    void testCreateDailyRecord_HabitNotFound() {
        // Given: Un DTO válido pero el hábito no existe
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now(), true, 1L, 1L
        );
        when(habitService.findHabitEntityById(1L)).thenReturn(Optional.empty());

        // When: Se intenta crear un registro sin hábito válido
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dailyRecordService.createDailyRecord(requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("Habit not found with id: 1", exception.getMessage());
        verify(habitService).findHabitEntityById(1L);
        verify(dailyRecordRepository, never()).save(any(DailyRecord.class));
    }

    // ----------------------------
    // Tests para updateDailyRecord
    // ----------------------------

    @Test
    void testUpdateDailyRecord_Success() {
        // Given: Un DTO válido y un registro existente con hábito
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now().plusDays(1), false, 1L, 1L
        );

        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord existingDailyRecord = new DailyRecord();
        existingDailyRecord.setId(1L);
        existingDailyRecord.setDate(LocalDate.now());
        existingDailyRecord.setCompleted(true);
        existingDailyRecord.setHabit(habit); // ✅ Asigna el Hábito al registro

        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.of(existingDailyRecord));
        when(dailyRecordRepository.save(any(DailyRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: Se actualiza el registro diario
        DailyRecordResponseDTO result = dailyRecordService.updateDailyRecord(1L, requestDTO);

        // Then: El DTO tiene los valores actualizados y se verifica la llamada al repositorio
        assertEquals(LocalDate.now().plusDays(1), result.getDate());
        assertFalse(result.getCompleted());
        assertEquals(1L, result.getHabitId());
        verify(dailyRecordRepository).findById(1L);
        verify(dailyRecordRepository).save(existingDailyRecord);
    }

    @Test
    void testUpdateDailyRecord_DailyRecordNotFound() {
        // Given: El registro no existe
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now().plusDays(1), false, 1L, 1L
        );
        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta actualizar un registro inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dailyRecordService.updateDailyRecord(1L, requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("Registro diario no encontrado", exception.getMessage());
        verify(dailyRecordRepository).findById(1L);
        verify(dailyRecordRepository, never()).save(any(DailyRecord.class));
    }

    // ----------------------------
    // Tests para findDailyRecordByDate
    // ----------------------------

    @Test
    void testFindDailyRecordByDate_Success() {
        // Given: Un registro existente para una fecha específica
        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(1L);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setHabit(habit); // ✅ Asigna el Hábito al registro

        when(dailyRecordRepository.findByDate(LocalDate.now())).thenReturn(Optional.of(dailyRecord));

        // When: Se busca el registro por fecha
        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByDate(LocalDate.now());

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals(LocalDate.now(), result.get().getDate());
        assertEquals(1L, result.get().getHabitId()); // ✅ Verifica que el ID del hábito esté presente
        verify(dailyRecordRepository).findByDate(LocalDate.now());
    }

    @Test
    void testFindDailyRecordByDate_NotFound() {
        // Given: No hay registros para la fecha dada
        when(dailyRecordRepository.findByDate(LocalDate.now())).thenReturn(Optional.empty());

        // When: Se busca un registro para una fecha inexistente
        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByDate(LocalDate.now());

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(dailyRecordRepository).findByDate(LocalDate.now());
    }

    // ----------------------------
    // Test para getAllDailyRecords
    // ----------------------------

    @Test
    void testGetAllDailyRecords_DailyRecordsExist() {
        // Given: Dos registros diarios existentes con hábitos válidos
        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord dailyRecord1 = new DailyRecord();
        dailyRecord1.setId(1L);
        dailyRecord1.setDate(LocalDate.now());
        dailyRecord1.setHabit(habit); // ✅ Asigna el Hábito al registro

        DailyRecord dailyRecord2 = new DailyRecord();
        dailyRecord2.setId(2L);
        dailyRecord2.setDate(LocalDate.now().plusDays(1));
        dailyRecord2.setHabit(habit); // ✅ Asigna el Hábito al registro

        when(dailyRecordRepository.findAll()).thenReturn(Arrays.asList(dailyRecord1, dailyRecord2));

        // When: Se obtienen todos los registros diarios
        List<DailyRecordResponseDTO> result = dailyRecordService.getAllDailyRecords();

        // Then: Se devuelven los DTOs correctamente mapeados
        assertEquals(2, result.size());
        assertEquals(LocalDate.now(), result.get(0).getDate());
        assertEquals(LocalDate.now().plusDays(1), result.get(1).getDate());
        verify(dailyRecordRepository).findAll();
    }

    // ----------------------------
    // Tests para deleteDailyRecord
    // ----------------------------

    @Test
    void testDeleteDailyRecord_Success() {
        // Given: El registro existe
        when(dailyRecordRepository.existsById(1L)).thenReturn(true);

        // When: Se elimina el registro
        dailyRecordService.deleteDailyRecord(1L);

        // Then: Se llama a deleteById y se verifica la existencia
        verify(dailyRecordRepository).existsById(1L);
        verify(dailyRecordRepository).deleteById(1L);
    }

    @Test
    void testDeleteDailyRecord_DailyRecordNotFound() {
        // Given: El registro no existe
        when(dailyRecordRepository.existsById(1L)).thenReturn(false);

        // When: Se intenta eliminar un registro inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dailyRecordService.deleteDailyRecord(1L)
        );

        // Then: Se lanza la excepción correcta y no se llama a deleteById
        assertEquals("Registro diario no encontrado", exception.getMessage());
        verify(dailyRecordRepository).existsById(1L);
        verify(dailyRecordRepository, never()).deleteById(1L);
    }

}