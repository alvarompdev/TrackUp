package trackup.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackup.dto.request.HabitTypeRequestDTO;
import trackup.dto.response.HabitTypeResponseDTO;
import trackup.entity.HabitType;
import trackup.repository.HabitTypeRepository;
import trackup.services.HabitTypeService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de tipos de hábito
 * Se encarga de la lógica de negocio relacionada con los tipos de hábito,
 * incluyendo la creación, actualización, eliminación y obtención de tipos de hábito
 */
@Service
public class HabitTypeServiceImpl implements HabitTypeService {

    private final HabitTypeRepository habitTypeRepository; // Repositorio de tipos de hábito

    /**
     * Constructor con inyección de dependencias
     *
     * @param habitTypeRepository Repositorio de tipos de hábito
     */
    @Autowired
    public HabitTypeServiceImpl(HabitTypeRepository habitTypeRepository) {
        this.habitTypeRepository = habitTypeRepository;
    }

    @Override
    public Optional<HabitTypeResponseDTO> getHabitTypeById(Long id) {
        return habitTypeRepository.findById(id) // Busca el tipo de hábito por ID, y si lo encuentra lo transforma a un DTO
                .map(this::mapToDTO);
    }

    @Override
    public Optional<HabitType> getHabitTypeEntityById(Long id) {
        return habitTypeRepository.findById(id); // Retorna directamente la entidad HabitType en el caso de que exista
    }

    @Override
    public Optional<HabitTypeResponseDTO> getHabitTypeByName(String name) {
        return habitTypeRepository.findByName(name) // Busca el tipo de hábito por nombre
                .map(this::mapToDTO);
    }

    @Override
    public List<HabitTypeResponseDTO> getAllHabitTypes() {
        List<HabitType> habitTypes = habitTypeRepository.findAll(); // Obtiene todos los tipos de hábito existentes en la base de datos

        if (habitTypes.isEmpty()) { // Si no hay tipos de hábito, lanza una excepción
            throw new RuntimeException("No hay tipos de hábito registrados");
        }

        return habitTypes.stream() // Transforma cada entidad HabitType a un HabitTypeResponseDTO y lo devuelve
                .map(this::mapToDTO) // Transforma cada entidad HabitType a un HabitTypeResponseDTO
                .collect(Collectors.toList()); // Recoge todos los DTOs en una listas
    }

    @Override
    public HabitTypeResponseDTO createHabitType(HabitTypeRequestDTO habitTypeRequestDTO) {
        if (habitTypeRepository.findByName(habitTypeRequestDTO.getName()).isPresent()) { // Comprueba si el tipo de hábito ya existe
            throw new RuntimeException("El tipo de hábito ya existe");
        }

        HabitType habitType = new HabitType(); // Crea una nueva entidad HabitType
        habitType.setName(habitTypeRequestDTO.getName()); // Asigna el nombre del tipo de hábito desde el DTO

        HabitType savedHabitType = habitTypeRepository.save(habitType); // Guarda el nuevo tipo de hábito en la base de datos
        return mapToDTO(savedHabitType); // Convierte la entidad guardada a un DTO y lo devuelve
    }

    @Override
    public HabitTypeResponseDTO updateHabitType(Long id, HabitTypeRequestDTO habitTypeRequestDTO) {
        HabitType habitType = habitTypeRepository.findById(id) // Busca el tipo de hábito por ID
                .orElseThrow(() -> new RuntimeException("Tipo de hábito no encontrado"));

        habitType.setName(habitTypeRequestDTO.getName()); // Actualiza el nombre del tipo de hábito con el nuevo valor del DTO

        HabitType updatedHabitType = habitTypeRepository.save(habitType); // Guarda los cambios en la base de datos
        return mapToDTO(updatedHabitType); // Convierte la entidad actualizada a un DTO y lo devuelve
    }

    @Override
    public void deleteHabitType(Long id) {
        if (!habitTypeRepository.existsById(id)) { // Verifica si el tipo de hábito existe
            throw new RuntimeException("Tipo de hábito no encontrado");
        }

        habitTypeRepository.deleteById(id); // Elimina el tipo de hábito por ID
    }

    /**
     * Convierte una entidad HabitType a un DTO HabitTypeResponseDTO
     *
     * @param habitType Entidad HabitType a convertir
     * @return DTO HabitTypeResponseDTO
     */
    private HabitTypeResponseDTO mapToDTO(HabitType habitType) {
        return new HabitTypeResponseDTO( // Crea un nuevo DTO HabitTypeResponseDTO
                habitType.getId(), // ID del tipo de hábito
                habitType.getName() // Nombre del tipo de hábito
        );
    }

    /**
     * Convierte un DTO HabitTypeRequestDTO a una entidad HabitType
     *
     * NO USADO AL MOMENTO, MARCADO ASÍ POR INTELLIJ
     *
     * @param habitTypeRequestDTO DTO a convertir
     * @return Entidad HabitType
     */
    /*private HabitType mapToEntity(HabitTypeRequestDTO habitTypeRequestDTO) {
        HabitType habitType = new HabitType();
        habitType.setName(habitTypeRequestDTO.getName());
        return habitType;
    }*/

}