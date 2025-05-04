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

import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.entity.HabitType;
import trackup.repository.HabitTypeRepository;

/**
 * Test de la clase HabitTypeServiceImpl
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
public class HabitTypeServiceImplTest {

    @Mock
    private HabitTypeRepository habitTypeRepository;

    @InjectMocks
    private HabitTypeServiceImpl habitTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindHabitTypeById_Success() {
        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        when(habitTypeRepository.findById(1L)).thenReturn(Optional.of(habitType));

        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeById(1L);

        assertTrue(result.isPresent());
        assertEquals("Health", result.get().getName());
        verify(habitTypeRepository).findById(1L);
    }

    @Test
    void testFindHabitTypeById_NotFound() {
        when(habitTypeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeById(1L);

        assertFalse(result.isPresent());
        verify(habitTypeRepository).findById(1L);
    }


    @Test
    void testFindHabitTypeEntityById_Success() {
        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        when(habitTypeRepository.findById(1L)).thenReturn(Optional.of(habitType));

        Optional<HabitType> result = habitTypeService.findHabitTypeEntityById(1L);

        assertTrue(result.isPresent());
        assertEquals("Health", result.get().getName());
        verify(habitTypeRepository).findById(1L);
    }


    @Test
    void testFindHabitTypeByName_Success() {
        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.of(habitType));

        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeByName("Health");

        assertTrue(result.isPresent());
        assertEquals("Health", result.get().getName());
        verify(habitTypeRepository).findByName("Health");
    }

    @Test
    void testFindHabitTypeByName_NotFound() {
        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.empty());

        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeByName("Health");

        assertFalse(result.isPresent());
        verify(habitTypeRepository).findByName("Health");
    }


    @Test
    void testGetAllHabitTypes_HabitTypesExist() {
        HabitType habitType1 = new HabitType();
        habitType1.setId(1L);
        habitType1.setName("Health");

        HabitType habitType2 = new HabitType();
        habitType2.setId(2L);
        habitType2.setName("Productivity");

        when(habitTypeRepository.findAll()).thenReturn(Arrays.asList(habitType1, habitType2));

        List<HabitTypeResponseDTO> result = habitTypeService.getAllHabitTypes();

        assertEquals(2, result.size());
        assertEquals("Health", result.get(0).getName());
        assertEquals("Productivity", result.get(1).getName());
        verify(habitTypeRepository).findAll();
    }

    @Test
    void testGetAllHabitTypes_NoHabitTypes() {
        when(habitTypeRepository.findAll()).thenReturn(List.of());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.getAllHabitTypes()
        );
        assertEquals("No hay tipos de hábito registrados", exception.getMessage());
        verify(habitTypeRepository).findAll();
    }

    @Test
    void testCreateHabitType_Success() {
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health");

        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.empty());

        HabitType savedHabitType = new HabitType();
        savedHabitType.setId(1L);
        savedHabitType.setName("Health");

        when(habitTypeRepository.save(any(HabitType.class))).thenReturn(savedHabitType);

        HabitTypeResponseDTO result = habitTypeService.createHabitType(requestDTO);

        assertEquals("Health", result.getName());
        verify(habitTypeRepository).findByName("Health");
        verify(habitTypeRepository).save(any(HabitType.class));
    }

    @Test
    void testCreateHabitType_DuplicateName() {
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health");
        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.of(new HabitType()));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.createHabitType(requestDTO)
        );
        assertEquals("El tipo de hábito ya existe", exception.getMessage());
        verify(habitTypeRepository).findByName("Health");
        verify(habitTypeRepository, never()).save(any(HabitType.class));
    }

    @Test
    void testUpdateHabitType_Success() {
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health Updated");

        HabitType existingHabitType = new HabitType();
        existingHabitType.setId(1L);
        existingHabitType.setName("Health");

        when(habitTypeRepository.findById(1L)).thenReturn(Optional.of(existingHabitType));
        when(habitTypeRepository.save(any(HabitType.class))).thenReturn(existingHabitType);

        HabitTypeResponseDTO result = habitTypeService.updateHabitType(1L, requestDTO);

        assertEquals("Health Updated", result.getName());
        verify(habitTypeRepository).findById(1L);
        verify(habitTypeRepository).save(existingHabitType);
    }

    @Test
    void testUpdateHabitType_HabitTypeNotFound() {
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health Updated");
        when(habitTypeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.updateHabitType(1L, requestDTO)
        );
        assertEquals("Tipo de hábito no encontrado", exception.getMessage());
        verify(habitTypeRepository).findById(1L);
        verify(habitTypeRepository, never()).save(any(HabitType.class));
    }

    @Test
    void testDeleteHabitType_Success() {
        when(habitTypeRepository.existsById(1L)).thenReturn(true);

        habitTypeService.deleteHabitType(1L);

        verify(habitTypeRepository).existsById(1L);
        verify(habitTypeRepository).deleteById(1L);
    }

    @Test
    void testDeleteHabitType_HabitTypeNotFound() {
        when(habitTypeRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.deleteHabitType(1L)
        );
        assertEquals("Tipo de hábito no encontrado", exception.getMessage());
        verify(habitTypeRepository).existsById(1L);
        verify(habitTypeRepository, never()).deleteById(1L);
    }

}