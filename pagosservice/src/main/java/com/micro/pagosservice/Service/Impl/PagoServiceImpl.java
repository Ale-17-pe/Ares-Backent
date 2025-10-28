package com.micro.pagosservice.Service.Impl;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.DTO.TransaccionResponse;
import com.micro.pagosservice.Factory.PagoStrategyFactory;
import com.micro.pagosservice.Factory.TransaccionFactory;
import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Repository.TransaccionRepository;
import com.micro.pagosservice.Model.Transaccion;
import com.micro.pagosservice.Service.PagoService;
import com.micro.pagosservice.Strategy.PagoStrategy;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PagoServiceImpl implements PagoService {

    private final TransaccionRepository transaccionRepository;
    private final PagoStrategyFactory strategyFactory;


    public PagoServiceImpl(TransaccionRepository transaccionRepository, TransaccionFactory transaccionFactory, PagoStrategyFactory strategyFactory) {
        this.transaccionRepository = transaccionRepository;
        this.strategyFactory = strategyFactory;
    }
    // ===================================
    // 🔹 Procesar nuevo pago
    // ===================================
    @Override
    public TransaccionResponse procesarPago(TransaccionRequest request) {
        // Obtener la estrategia según el método de pago
        PagoStrategy estrategia = strategyFactory.obtenerEstrategia(request.getMetodoPago());

        // Procesar el pago
        Transaccion transaccion = estrategia.procesarPago(request);
        transaccionRepository.save(transaccion);

        log.info("✅ Pago procesado con éxito ({}) - Transacción ID {}", request.getMetodoPago(), transaccion.getId());

        return mapToResponse(transaccion);
    }

    private TransaccionResponse mapToResponse(Transaccion tx) {
        TransaccionResponse res = new TransaccionResponse();
        res.setId(tx.getId());
        res.setCodigoInterno(tx.getCodigoInterno());
        res.setUsuarioId(tx.getUsuarioId());
        res.setMonto(tx.getMonto());
        res.setMoneda(tx.getMoneda());
        res.setMetodoPago(tx.getMetodoPago());
        res.setEstado(tx.getEstado());
        res.setFechaCreacion(tx.getFechaCreacion());
        return res;
    }
    // ===================================
    // 🔹 Listar todas las transacciones
    // ===================================
    @Override
    public List<TransaccionResponse> listarTodas() {
        return transaccionRepository.findAll().stream()
                .map(TransaccionResponse::fromEntity)
                .toList();
    }

    // ===================================
    // 🔹 Obtener por ID
    // ===================================
    @Override
    public Optional<TransaccionResponse> obtenerPorId(Long id) {
        return transaccionRepository.findById(id).map(TransaccionResponse::fromEntity);
    }

    // ===================================
    // 🔹 Listar por usuario
    // ===================================
    @Override
    public List<TransaccionResponse> listarPorUsuario(Long usuarioId) {
        return transaccionRepository.findByUsuarioId(usuarioId).stream()
                .map(TransaccionResponse::fromEntity)
                .toList();
    }

    // ===================================
    // 🔹 Actualizar estado
    // ===================================
    @Override
    public TransaccionResponse actualizarEstado(Long id, EstadoTransaccion estado) {
        Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transacción no encontrada con ID: " + id));

        transaccion.setEstado(estado);
        log.info("🌀 Estado actualizado: Transacción {} -> {}", id, estado);
        transaccionRepository.save(transaccion);

        return TransaccionResponse.fromEntity(transaccion);
    }

    // ===================================
    // 🔹 Eliminar transacción
    // ===================================
    @Override
    public void eliminar(Long id) {
        if (!transaccionRepository.existsById(id)) {
            throw new EntityNotFoundException("Transacción no encontrada con ID: " + id);
        }
        transaccionRepository.deleteById(id);
        log.info("🗑️ Transacción {} eliminada correctamente", id);
    }

    // ===================================
    // 🔹 Listado paginado
    // ===================================
    @Override
    public Page<TransaccionResponse> listarPaginadas(Pageable pageable) {
        return transaccionRepository.findAll(pageable).map(TransaccionResponse::fromEntity);
    }
}
