package com.micro.pagosservice.Controller;

import com.micro.pagosservice.DTO.ApiResponse;
import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.DTO.TransaccionResponse;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Service.PagoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
public class TransaccionController {

    private final PagoService pagoService;

    public TransaccionController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    // ===============================
    // 🔹 Crear una transacción (pago)
    // ===============================
    @PostMapping("/procesar")
    public ResponseEntity<ApiResponse<TransaccionResponse>> procesarPago(@RequestBody TransaccionRequest request) {
        try {
            TransaccionResponse pago = pagoService.procesarPago(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Pago procesado exitosamente", pago));
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Error de validación: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (EntityNotFoundException e) {
            log.error("❌ Entidad no encontrada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("💥 Error inesperado al procesar el pago: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error interno al procesar el pago"));
        }
    }

    // ===============================
    // 🔹 Listar todas las transacciones
    // ===============================
    @GetMapping
    public ResponseEntity<ApiResponse<List<TransaccionResponse>>> listarTodas() {
        List<TransaccionResponse> lista = pagoService.listarTodas();
        return ResponseEntity.ok(ApiResponse.success("Listado de transacciones", lista));
    }

    // ===============================
    // 🔹 Obtener transacción por ID
    // ===============================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TransaccionResponse>> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id)
                .map(tx -> ResponseEntity.ok(ApiResponse.success("Transacción encontrada", tx)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("No se encontró la transacción con ID: " + id)));
    }

    // ===============================
    // 🔹 Listar por usuario
    // ===============================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<TransaccionResponse>>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<TransaccionResponse> lista = pagoService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Transacciones del usuario " + usuarioId, lista));
    }

    // ===============================
    // 🔹 Actualizar estado de transacción
    // ===============================
    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<TransaccionResponse>> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTransaccion estado
    ) {
        try {
            TransaccionResponse actualizada = pagoService.actualizarEstado(id, estado);
            return ResponseEntity.ok(ApiResponse.success("Estado actualizado", actualizada));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    // ===============================
    // 🔹 Eliminar transacción
    // ===============================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            pagoService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.success("Transacción eliminada correctamente", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    // ===============================
    // 🔹 Listado paginado
    // ===============================
    @GetMapping("/paginado")
    public ResponseEntity<ApiResponse<Page<TransaccionResponse>>> listarPaginadas(Pageable pageable) {
        Page<TransaccionResponse> pagina = pagoService.listarPaginadas(pageable);
        return ResponseEntity.ok(ApiResponse.success("Listado paginado de transacciones", pagina));
    }
}
