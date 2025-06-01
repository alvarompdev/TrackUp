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

    // ----------------------------
    // Tests para findHabitTypeById
    // ----------------------------

    @Test
    void testFindHabitTypeById_Success() {
        // Given: Un tipo de hábito existente
        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        when(habitTypeRepository.findById(1L)).thenReturn(Optional.of(habitType));

        // When: Se busca el tipo de hábito por ID
        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeById(1L);

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals("Health", result.get().getName());
        verify(habitTypeRepository).findById(1L);
    }

    @Test
    void testFindHabitTypeById_NotFound() {
        // Given: El repositorio no encuentra el tipo de hábito
        when(habitTypeRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se busca un tipo de hábito inexistente
        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeById(1L);

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(habitTypeRepository).findById(1L);
    }

    // ----------------------------
    // Tests para findHabitTypeEntityById
    // ----------------------------

    @Test
    void testFindHabitTypeEntityById_Success() {
        // Given: Un tipo de hábito existente
        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        when(habitTypeRepository.findById(1L)).thenReturn(Optional.of(habitType));

        // When: Se busca la entidad HabitType por ID
        Optional<HabitType> result = habitTypeService.findHabitTypeEntityById(1L);

        // Then: El servicio devuelve la entidad sin mapear
        assertTrue(result.isPresent());
        assertEquals("Health", result.get().getName());
        verify(habitTypeRepository).findById(1L);
    }

    // ----------------------------
    // Tests para findHabitTypeByName
    // ----------------------------

    @Test
    void testFindHabitTypeByName_Success() {
        // Given: Un tipo de hábito existente
        HabitType habitType = new HabitType();
        habitType.setId(1L);
        habitType.setName("Health");

        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.of(habitType));

        // When: Se busca el tipo de hábito por nombre
        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeByName("Health");

        // Then: El servicio devuelve el DTO correctamente mapeado
        assertTrue(result.isPresent());
        assertEquals("Health", result.get().getName());
        verify(habitTypeRepository).findByName("Health");
    }

    @Test
    void testFindHabitTypeByName_NotFound() {
        // Given: El repositorio no encuentra el tipo de hábito por nombre
        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.empty());

        // When: Se busca un tipo de hábito inexistente
        Optional<HabitTypeResponseDTO> result = habitTypeService.findHabitTypeByName("Health");

        // Then: No hay resultado y se verifica la llamada al repositorio
        assertFalse(result.isPresent());
        verify(habitTypeRepository).findByName("Health");
    }

    // ----------------------------
    // Tests para getAllHabitTypes
    // ----------------------------

    @Test
    void testGetAllHabitTypes_HabitTypesExist() {
        // Given: Dos tipos de hábito existentes
        HabitType habitType1 = new HabitType();
        habitType1.setId(1L);
        habitType1.setName("Health");

        HabitType habitType2 = new HabitType();
        habitType2.setId(2L);
        habitType2.setName("Productivity");

        when(habitTypeRepository.findAll()).thenReturn(Arrays.asList(habitType1, habitType2));

        // When: Se obtienen todos los tipos de hábito
        List<HabitTypeResponseDTO> result = habitTypeService.getAllHabitTypes();

        // Then: Se devuelven los DTOs correctamente mapeados
        assertEquals(2, result.size());
        assertEquals("Health", result.get(0).getName());
        assertEquals("Productivity", result.get(1).getName());
        verify(habitTypeRepository).findAll();
    }

    @Test
    void testGetAllHabitTypes_NoHabitTypes() {
        // Given: El repositorio devuelve una lista vacía
        when(habitTypeRepository.findAll()).thenReturn(List.of());

        // When: Se intenta obtener todos los tipos de hábito vacíos
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.getAllHabitTypes()
        );

        // Then: Se lanza la excepción correcta y se verifica la llamada al repositorio
        assertEquals("No hay tipos de hábito registrados", exception.getMessage());
        verify(habitTypeRepository).findAll();
    }

    // ----------------------------
    // Tests para createHabitType
    // ----------------------------

    @Test
    void testCreateHabitType_Success() {
        // Given: Un DTO válido y repositorio vacío (sin duplicados)
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health");
        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.empty());

        // When: Se crea un nuevo tipo de hábito
        HabitType savedHabitType = new HabitType();
        savedHabitType.setId(1L);
        savedHabitType.setName("Health");

        when(habitTypeRepository.save(any(HabitType.class))).thenReturn(savedHabitType);

        // Then: El servicio devuelve el DTO y se verifican las llamadas al repositorio
        HabitTypeResponseDTO result = habitTypeService.createHabitType(requestDTO);
        assertEquals("Health", result.getName());
        verify(habitTypeRepository).findByName("Health");
        verify(habitTypeRepository).save(any(HabitType.class));
    }

    @Test
    void testCreateHabitType_DuplicateName() {
        // Given: Un nombre de tipo de hábito duplicado
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health");
        when(habitTypeRepository.findByName("Health")).thenReturn(Optional.of(new HabitType()));

        // When: Se intenta crear un tipo de hábito con nombre duplicado
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.createHabitType(requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("El tipo de hábito ya existe", exception.getMessage());
        verify(habitTypeRepository).findByName("Health");
        verify(habitTypeRepository, never()).save(any(HabitType.class));
    }

    // ----------------------------
    // Tests para updateHabitType
    // ----------------------------

    @Test
    void testUpdateHabitType_Success() {
        // Given: Un DTO válido y un tipo de hábito existente
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health Updated");

        HabitType existingHabitType = new HabitType();
        existingHabitType.setId(1L);
        existingHabitType.setName("Health");

        when(habitTypeRepository.findById(1L)).thenReturn(Optional.of(existingHabitType));
        when(habitTypeRepository.save(any(HabitType.class))).thenReturn(existingHabitType);

        // When: Se actualiza el tipo de hábito
        HabitTypeResponseDTO result = habitTypeService.updateHabitType(1L, requestDTO);

        // Then: El servicio devuelve el DTO actualizado y se verifican las llamadas al repositorio
        assertEquals("Health Updated", result.getName());
        verify(habitTypeRepository).findById(1L);
        verify(habitTypeRepository).save(existingHabitType);
    }

    @Test
    void testUpdateHabitType_HabitTypeNotFound() {
        // Given: El tipo de hábito no existe
        HabitTypeRequestDTO requestDTO = new HabitTypeRequestDTO("Health Updated");
        when(habitTypeRepository.findById(1L)).thenReturn(Optional.empty());

        // When: Se intenta actualizar un tipo de hábito inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.updateHabitType(1L, requestDTO)
        );

        // Then: Se lanza la excepción correcta y no se llama al save
        assertEquals("Tipo de hábito no encontrado", exception.getMessage());
        verify(habitTypeRepository).findById(1L);
        verify(habitTypeRepository, never()).save(any(HabitType.class));
    }

    // ----------------------------
    // Tests para deleteHabitType
    // ----------------------------

    @Test
    void testDeleteHabitType_Success() {
        // Given: El tipo de hábito existe
        when(habitTypeRepository.existsById(1L)).thenReturn(true);

        // When: Se elimina el tipo de hábito
        habitTypeService.deleteHabitType(1L);

        // Then: Se llama a deleteById y se verifica la existencia
        verify(habitTypeRepository).existsById(1L);
        verify(habitTypeRepository).deleteById(1L);
    }

    @Test
    void testDeleteHabitType_HabitTypeNotFound() {
        // Given: El tipo de hábito no existe
        when(habitTypeRepository.existsById(1L)).thenReturn(false);

        // When: Se intenta eliminar un tipo de hábito inexistente
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> habitTypeService.deleteHabitType(1L)
        );

        // Then: Se lanza la excepción correcta y no se llama a deleteById
        assertEquals("Tipo de hábito no encontrado", exception.getMessage());
        verify(habitTypeRepository).existsById(1L);
        verify(habitTypeRepository, never()).deleteById(1L);
    }

}