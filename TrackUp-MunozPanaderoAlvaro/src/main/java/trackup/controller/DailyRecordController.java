package trackup.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trackup.dto.request.DailyRecordRequestDTO;
import trackup.dto.response.DailyRecordResponseDTO;
import trackup.services.DailyRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar los registros diarios
 *
 * Acceso: <a href="http://localhost:8080/api/daily-records">...</a>
 *
 * @author Álvaro Muñoz Panadero - alvaromp.dev@gmail.com
 */
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/daily-records") // Prefijo para todas las rutas de este controlador
@Tag(name = "Daily Records", description = "API para gestión de registros diarios") // Anotación Swagger
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
     * GET <a href="http://localhost:8080/api/daily-records/daily-records">...</a>
     *
     * @return Lista de registros diarios
     */
    @Operation(summary = "Obtener todos los registros", description = "Retorna una lista completa de registros diarios")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de registros encontrada", content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay registros disponibles", content = @Content)
    })
    @GetMapping("/daily-records")
    public ResponseEntity<List<DailyRecordResponseDTO>> getAllDailyRecords() {
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
     * GET <a href="http://localhost:8080/api/daily-records/daily-record/id/1">...</a>
     *
     * @param id ID del registro diario
     * @return Registro diario encontrado
     */
    @Operation(summary = "Obtener registro por ID", description = "Busca un registro diario por su ID único")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado", content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content)
    })
    @GetMapping("/daily-record/{id}")
    public ResponseEntity<DailyRecordResponseDTO> getDailyRecordById(
            @Parameter(description = "ID del registro (debe ser positivo)", required = true, schema = @Schema(minimum = "0"))
            @PathVariable Long id
    ) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.findDailyRecordById(id); // Obtiene el usuario de acuerdo al ID proporcionado
        return dailyRecordOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un registro diario por su fecha
     *
     * FUNCIONA CON EL METODO TEMPORAL QUE LE PERMITE PONER UNA FECHA EN LA URL
     *
     * GET <a href="http://localhost:8080/api/daily-records/daily-record/date/2023-10-01">...</a>
     *
     * @param date Fecha del registro diario
     * @return Registro diario encontrado
     */
    @Operation(summary = "Buscar registro por fecha", description = "Busca un registro diario por su fecha en formato ISO (yyyy-MM-dd)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro encontrado", content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Fecha inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content)
    })
    @GetMapping("/daily-record/date/{date}")
    public ResponseEntity<DailyRecordResponseDTO> getDailyRecordByDate(
            @Parameter(description = "Fecha en formato ISO (ej: 2023-10-01)", required = true)
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (date == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.findDailyRecordByDate(date);
        return dailyRecordOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un registro diario por su estado de completado
     *
     * FUNCIONA
     *
     * GET <a href="http://localhost:8080/api/daily-records/daily-record/completed/true">...</a>
     *
     * @param completed Estado de completado del registro diario
     * @return Registro diario encontrado
     */
    @Operation(summary = "Buscar registros por estado", description = "Busca registros diarios por su estado de completado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro(s) encontrado(s)", content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron registros", content = @Content)
    })
    @GetMapping("/daily-record/completed/{completed}")
    public ResponseEntity<DailyRecordResponseDTO> getDailyRecordByCompleted(
            @Parameter(description = "Estado de completado (true/false)", required = true)
            @PathVariable Boolean completed
    ) {
        Optional<DailyRecordResponseDTO> dailyRecordOpt = dailyRecordService.findDailyRecordByCompleted(completed); // Obtiene el usuario de acuerdo al ID proporcionado
        return dailyRecordOpt.map(ResponseEntity::ok) // Si el usuario existe, devuelve el objeto UserResponseDTO
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo registro diario
     *
     * FUNCIONA
     *
     * POST <a href="http://localhost:8080/api/daily-records/daily-record">...</a>
     *
     * @param dailyRecordRequestDTO Datos del registro diario que se va a crear
     * @return Registro diario creado
     */
    @Operation(summary = "Crear nuevo registro", description = "Registra un nuevo registro diario en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro creado exitosamente", content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/daily-record")
    public ResponseEntity<DailyRecordResponseDTO> createDailyRecord(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo registro",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DailyRecordRequestDTO.class))
            )
            @RequestBody DailyRecordRequestDTO dailyRecordRequestDTO
    ) {
        if (dailyRecordRequestDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        DailyRecordResponseDTO createdDailyRecord = dailyRecordService.createDailyRecord(dailyRecordRequestDTO);
        return ResponseEntity.ok(createdDailyRecord);
    }

    /**
     * Actualiza un registro diario existente
     *
     * FUNCIONA
     *
     * PUT <a href="http://localhost:8080/api/daily-records/daily-record/id/1">...</a>
     *
     * @param id ID del registro diario que se va a actualizar
     * @param dailyRecordRequestDTO Datos del registro diario que se va a actualizar
     * @return Registro diario actualizado
     */
    @Operation(summary = "Actualizar registro", description = "Modifica los datos de un registro diario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente", content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content)
    })
    @PutMapping("/daily-record/{id}")
    public ResponseEntity<DailyRecordResponseDTO> updateDailyRecord(
            @Parameter(description = "ID del registro a actualizar", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados del registro",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DailyRecordResponseDTO.class))
            )
            @RequestBody DailyRecordRequestDTO dailyRecordRequestDTO
    ) {
        if (id < 0 || dailyRecordRequestDTO == null) { // Verifica si el ID es negativo o el objeto es nulo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo o el objeto es nulo
        }

        DailyRecordResponseDTO updatedDailyRecord = dailyRecordService.updateDailyRecord(id, dailyRecordRequestDTO); // Actualiza el registro diario
        return ResponseEntity.ok(updatedDailyRecord); // Devuelve el registro diario actualizado con un código 200 OK
    }

    /**
     * Elimina un registro diario existente
     *
     * FUNCIONA
     *
     * DELETE <a href="http://localhost:8080/api/daily-records/daily-record/id/1">...</a>
     *
     * @param id ID del registro diario que se va a eliminar
     * @return Código de respuesta HTTP
     */
    @Operation(summary = "Eliminar registro", description = "Elimina un registro diario del sistema por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado", content = @Content)
    })
    @DeleteMapping("/daily-record/{id}")
    public ResponseEntity<Void> deleteDailyRecord(
            @Parameter(description = "ID del registro a eliminar", required = true) @PathVariable Long id
    ) {
        if (id < 0) { // Verifica si el ID es negativo
            return ResponseEntity.badRequest().build();  // Devuelve error si el ID es negativo
        }

        dailyRecordService.deleteDailyRecord(id); // Elimina el registro diario
        return ResponseEntity.noContent().build(); // Devuelve un código 204 No Content
    }

}