package trackup.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.services.DailyRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/daily-records") // Prefijo para todas las rutas de este controlador
public class DailyRecordController {

    private final DailyRecordService dailyRecordService; // Servicio de registros diarios

    /**
     * Constructor con inyección de dependencias
     *
     * @param dailyRecordService Servicio de registros diarios
     */
    public DailyRecordController(DailyRecordService dailyRecordService) {
        this.dailyRecordService = dailyRecordService; // Inyección de dependencias del servicio
    }

    /**
     * Obtiene todos los registros diarios
     *
     * FUNCIONA
     *
     * @return Lista de registros diarios
     */
    @GetMapping("/daily-records")
    public ResponseEntity<List<DailyRecordResponseDTO>> findAllDailyRecords() {
        List<DailyRecordResponseDTO> dailyRecordsList = dailyRecordService.getAllDailyRecords(); // Obtiene todos los registros diarios
        if (dailyRecordsList.isEmpty()) { // Si la lista está vacía, devuelve un código 204 No Content
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dailyRecordsList); // Devuelve la lista de registros diarios con un código 200 OK
    }

    /**
     * Obtiene un registro diario por su ID
     *
     * FUNCIONA
     *
     * @param id ID del registro diario
     * @return Registro diario encontrado
     */
    @GetMapping("/daily-record/{id}")
    public ResponseEntity<DailyRecordResponseDTO> findDailyRecordById(@PathVariable  Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.getDailyRecordById(id); // Obtiene el usuario de acuerdo al ID proporcionado
        return dailyRecordOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un registro diario por su fecha
     *
     * FUNCIONA CON EL METODO TEMPORAL QUE LE PERMITE PONER UNA FECHA EN LA URL
     *
     * @param date Fecha del registro diario
     * @return Registro diario encontrado
     */
    /*@GetMapping("/daily-record/date/{date}")
    public ResponseEntity<DailyRecordResponseDTO> findDailyRecordByDate(@PathVariable LocalDate date) {
        if (date == null) { // Verifica si la fecha es nula o vacía
            return ResponseEntity.badRequest().build();  // Devuelve error si la fecha es nula o vacía
        }

        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.getDailyRecordByDate(date); // Obtiene el usuario de acuerdo al ID proporcionado
        return dailyRecordOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }*/

    @GetMapping("/daily-record/date/{date}") //TEMPORAL
    public ResponseEntity<DailyRecordResponseDTO> findDailyRecordByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.getDailyRecordByDate(date);
        return dailyRecordOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Obtiene un registro diario por su estado de completado
     *
     * FUNCIONA
     *
     * @param completed Estado de completado del registro diario
     * @return Registro diario encontrado
     */
    @GetMapping("/daily-record/completed/{completed}")
    public ResponseEntity<DailyRecordResponseDTO> findDailyRecordByCompleted(@PathVariable Boolean completed) {
        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.getDailyRecordByCompleted(completed); // Obtiene el usuario de acuerdo al ID proporcionado
        return dailyRecordOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo registro diario
     *
     * FUNCIONA
     *
     * @param dailyRecordResponseDTO Datos del registro diario que se va a crear
     * @return Registro diario creado
     */
    @PostMapping("/daily-record")
    public ResponseEntity<DailyRecordResponseDTO> createDailyRecord(@RequestBody DailyRecordResponseDTO dailyRecordResponseDTO) {
        if (dailyRecordResponseDTO == null) { // Verifica si el objeto es nulo
            return ResponseEntity.badRequest().build();  // Devuelve error si el objeto es nulo
        }

        DailyRecordResponseDTO createdDailyRecord = dailyRecordService.createDailyRecord(dailyRecordResponseDTO); // Crea un nuevo registro diario
        return ResponseEntity.ok(createdDailyRecord); // Devuelve el registro diario creado con un código 200 OK
    }

    /**
     * Actualiza un registro diario existente
     *
     * FUNCIONA
     *
     * @param id ID del registro diario que se va a actualizar
     * @param dailyRecordResponseDTO Datos del registro diario que se va a actualizar
     * @return Registro diario actualizado
     */
    @PutMapping("/daily-record/{id}")
    public ResponseEntity<DailyRecordResponseDTO> updateDailyRecord(@PathVariable Long id, @RequestBody DailyRecordResponseDTO dailyRecordResponseDTO) {
        if (id < 0 || dailyRecordResponseDTO == null) { // Verifica si el ID es negativo o el objeto es nulo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo o el objeto es nulo
        }

        DailyRecordResponseDTO updatedDailyRecord = dailyRecordService.updateDailyRecord(id, dailyRecordResponseDTO); // Actualiza el registro diario
        return ResponseEntity.ok(updatedDailyRecord); // Devuelve el registro diario actualizado con un código 200 OK
    }

    /**
     * Elimina un registro diario existente
     *
     * FUNCIONA
     *
     * @param id ID del registro diario que se va a eliminar
     * @return Código de respuesta HTTP
     */
    @DeleteMapping("/daily-record/{id}")
    public ResponseEntity<Void> deleteDailyRecord(@PathVariable Long id) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        dailyRecordService.deleteDailyRecord(id); // Elimina el registro diario
        return ResponseEntity.noContent().build(); // Devuelve un código 204 No Content
    }

}