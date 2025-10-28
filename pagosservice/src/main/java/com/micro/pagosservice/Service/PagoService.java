package com.micro.pagosservice.Service;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.DTO.TransaccionResponse;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PagoService {

    TransaccionResponse procesarPago(TransaccionRequest request);

    List<TransaccionResponse> listarTodas();

    Optional<TransaccionResponse> obtenerPorId(Long id);

    List<TransaccionResponse> listarPorUsuario(Long usuarioId);

    TransaccionResponse actualizarEstado(Long id, EstadoTransaccion estado);

    void eliminar(Long id);

    Page<TransaccionResponse> listarPaginadas(Pageable pageable);
}
