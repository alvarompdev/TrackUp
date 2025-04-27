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

    private final HabitTypeRepository habitTypeRepository;

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
        return habitTypeRepository.findById(id)
                .map(this::mapToDTO);
    }

    @Override
    public Optional<HabitType> getHabitTypeEntityById(Long id) {
        return habitTypeRepository.findById(id);
    }

    @Override
    public Optional<HabitTypeResponseDTO> getHabitTypeByName(String name) {
        return habitTypeRepository.findByName(name)
                .map(this::mapToDTO);
    }

    @Override
    public List<HabitTypeResponseDTO> getAllHabitTypes() {
        List<HabitType> habitTypes = habitTypeRepository.findAll();

        if (habitTypes.isEmpty()) {
            throw new RuntimeException("No hay tipos de hábito registrados");
        }

        return habitTypes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public HabitTypeResponseDTO createHabitType(HabitTypeRequestDTO habitTypeRequestDTO) {
        if (habitTypeRepository.findByName(habitTypeRequestDTO.getName()).isPresent()) {
            throw new RuntimeException("El tipo de hábito ya existe");
        }

        HabitType habitType = new HabitType();
        habitType.setName(habitTypeRequestDTO.getName());

        HabitType savedHabitType = habitTypeRepository.save(habitType);
        return mapToDTO(savedHabitType);
    }

    @Override
    public HabitTypeResponseDTO updateHabitType(Long id, HabitTypeRequestDTO habitTypeRequestDTO) {
        HabitType habitType = habitTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de hábito no encontrado"));

        habitType.setName(habitTypeRequestDTO.getName());

        HabitType updatedHabitType = habitTypeRepository.save(habitType);
        return mapToDTO(updatedHabitType);
    }

    @Override
    public void deleteHabitType(Long id) {
        if (!habitTypeRepository.existsById(id)) {
            throw new RuntimeException("Tipo de hábito no encontrado");
        }
        habitTypeRepository.deleteById(id);
    }

    /**
     * Convierte una entidad HabitType a un DTO HabitTypeResponseDTO
     *
     * @param habitType Entidad HabitType a convertir
     * @return DTO HabitTypeResponseDTO
     */
    private HabitTypeResponseDTO mapToDTO(HabitType habitType) {
        return new HabitTypeResponseDTO(
                habitType.getId(),
                habitType.getName()
        );
    }

    /**
     * Convierte un DTO HabitTypeRequestDTO a una entidad HabitType
     *
     * @param habitTypeRequestDTO DTO a convertir
     * @return Entidad HabitType
     */
    private HabitType mapToEntity(HabitTypeRequestDTO habitTypeRequestDTO) {
        HabitType habitType = new HabitType();
        habitType.setName(habitTypeRequestDTO.getName());
        return habitType;
    }

}