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

    @Test
    void testFindDailyRecordById_Success() {
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(1L);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setCompleted(true);
        dailyRecord.setHabit(habit);

        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.of(dailyRecord));

        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordById(1L);

        assertTrue(result.isPresent());
        assertEquals(LocalDate.now(), result.get().getDate());
        assertTrue(result.get().getCompleted());
        verify(dailyRecordRepository).findById(1L);
    }

    @Test
    void testFindDailyRecordByCompleted_Success() {
        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(1L);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setCompleted(true);
        dailyRecord.setHabit(habit);

        when(dailyRecordRepository.findByCompleted(eq(true))).thenReturn(Optional.of(dailyRecord));

        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByCompleted(true);

        assertTrue(result.isPresent());
        assertTrue(result.get().getCompleted());
        verify(dailyRecordRepository).findByCompleted(eq(true));
    }

    @Test
    void testFindDailyRecordByCompleted_NotFound() {
        when(dailyRecordRepository.findByCompleted(eq(true))).thenReturn(Optional.empty());

        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByCompleted(true);

        assertFalse(result.isPresent());
        verify(dailyRecordRepository).findByCompleted(eq(true));
    }

    @Test
    void testCreateDailyRecord_Success() {
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now(), true, 1L
        );

        Habit habit = new Habit();
        habit.setId(1L);
        habit.setName("Exercise");

        when(habitService.findHabitEntityById(1L)).thenReturn(Optional.of(habit));

        DailyRecord savedDailyRecord = new DailyRecord();
        savedDailyRecord.setId(1L);
        savedDailyRecord.setDate(LocalDate.now());
        savedDailyRecord.setCompleted(true);
        savedDailyRecord.setHabit(habit);

        when(dailyRecordRepository.save(any(DailyRecord.class))).thenReturn(savedDailyRecord);

        DailyRecordResponseDTO result = dailyRecordService.createDailyRecord(requestDTO);

        assertEquals(LocalDate.now(), result.getDate());
        assertTrue(result.getCompleted());
        assertEquals(1L, result.getHabitId());
        verify(habitService).findHabitEntityById(1L);
        verify(dailyRecordRepository).save(any(DailyRecord.class));
    }

    @Test
    void testUpdateDailyRecord_Success() {
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now().plusDays(1), false, 1L
        );

        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord existingDailyRecord = new DailyRecord();
        existingDailyRecord.setId(1L);
        existingDailyRecord.setDate(LocalDate.now());
        existingDailyRecord.setCompleted(true);
        existingDailyRecord.setHabit(habit);

        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.of(existingDailyRecord));
        when(dailyRecordRepository.save(any(DailyRecord.class))).thenAnswer(invocation -> {
            DailyRecord updatedRecord = invocation.getArgument(0);
            System.out.println("Valor actualizado: " + updatedRecord.getCompleted());
            return updatedRecord;
        });

        DailyRecordResponseDTO result = dailyRecordService.updateDailyRecord(1L, requestDTO);

        assertEquals(LocalDate.now().plusDays(1), result.getDate());
        assertFalse(result.getCompleted());
        assertEquals(1L, result.getHabitId());
        verify(dailyRecordRepository).findById(1L);
        verify(dailyRecordRepository).save(existingDailyRecord);
    }

    @Test
    void testFindDailyRecordById_NotFound() {
        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordById(1L);

        assertFalse(result.isPresent());
        verify(dailyRecordRepository).findById(1L);
    }

    @Test
    void testFindDailyRecordByDate_Success() {
        Habit habit = new Habit();
        habit.setId(1L);

        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setId(1L);
        dailyRecord.setDate(LocalDate.now());
        dailyRecord.setHabit(habit);

        when(dailyRecordRepository.findByDate(LocalDate.now())).thenReturn(Optional.of(dailyRecord));

        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByDate(LocalDate.now());

        assertTrue(result.isPresent());
        assertEquals(LocalDate.now(), result.get().getDate());
        verify(dailyRecordRepository).findByDate(LocalDate.now());
    }

    @Test
    void testFindDailyRecordByDate_NotFound() {
        when(dailyRecordRepository.findByDate(LocalDate.now())).thenReturn(Optional.empty());

        Optional<DailyRecordResponseDTO> result = dailyRecordService.findDailyRecordByDate(LocalDate.now());

        assertFalse(result.isPresent());
        verify(dailyRecordRepository).findByDate(LocalDate.now());
    }

    @Test
    void testGetAllDailyRecords_DailyRecordsExist() {
        Habit habit = new Habit();
        habit.setId(1L); // ✅ Crea un Habit válido

        DailyRecord dailyRecord1 = new DailyRecord();
        dailyRecord1.setId(1L);
        dailyRecord1.setDate(LocalDate.now());
        dailyRecord1.setHabit(habit); // ✅ Asigna el Habit

        DailyRecord dailyRecord2 = new DailyRecord();
        dailyRecord2.setId(2L);
        dailyRecord2.setDate(LocalDate.now().plusDays(1));
        dailyRecord2.setHabit(habit); // ✅ Asigna el Habit

        when(dailyRecordRepository.findAll()).thenReturn(Arrays.asList(dailyRecord1, dailyRecord2));

        List<DailyRecordResponseDTO> result = dailyRecordService.getAllDailyRecords();

        assertEquals(2, result.size());
        assertEquals(LocalDate.now(), result.get(0).getDate());
        assertEquals(LocalDate.now().plusDays(1), result.get(1).getDate());
        verify(dailyRecordRepository).findAll();
    }

    @Test
    void testCreateDailyRecord_HabitNotFound() {
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now(), true, 1L
        );

        when(habitService.findHabitEntityById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dailyRecordService.createDailyRecord(requestDTO)
        );
        assertEquals("Habit not found with id: 1", exception.getMessage());
        verify(habitService).findHabitEntityById(1L);
        verify(dailyRecordRepository, never()).save(any(DailyRecord.class));
    }

    @Test
    void testUpdateDailyRecord_DailyRecordNotFound() {
        DailyRecordRequestDTO requestDTO = new DailyRecordRequestDTO(
                LocalDate.now().plusDays(1), false, 1L
        );

        when(dailyRecordRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dailyRecordService.updateDailyRecord(1L, requestDTO)
        );
        assertEquals("Registro diario no encontrado", exception.getMessage());
        verify(dailyRecordRepository).findById(1L);
        verify(dailyRecordRepository, never()).save(any(DailyRecord.class));
    }

    @Test
    void testDeleteDailyRecord_Success() {
        when(dailyRecordRepository.existsById(1L)).thenReturn(true);

        dailyRecordService.deleteDailyRecord(1L);

        verify(dailyRecordRepository).existsById(1L);
        verify(dailyRecordRepository).deleteById(1L);
    }

    @Test
    void testDeleteDailyRecord_DailyRecordNotFound() {
        when(dailyRecordRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> dailyRecordService.deleteDailyRecord(1L)
        );
        assertEquals("Registro diario no encontrado", exception.getMessage());
        verify(dailyRecordRepository).existsById(1L);
        verify(dailyRecordRepository, never()).deleteById(1L);
    }

}