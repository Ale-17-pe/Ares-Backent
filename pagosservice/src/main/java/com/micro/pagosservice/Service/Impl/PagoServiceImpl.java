package com.micro.pagosservice.Service.Impl;

import com.micro.pagosservice.Model.Transaccion;
import com.micro.pagosservice.Repository.TransaccionRepository;
import com.micro.pagosservice.Service.PagoService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PagoServiceImpl implements PagoService {

    private final TransaccionRepository transaccionRepository;

    public PagoServiceImpl(TransaccionRepository transaccionRepository1) {
        this.transaccionRepository = transaccionRepository1;
    }

    @Override
    public Transaccion iniciarPago(Transaccion nuevatransaccion) {
        nuevatransaccion.setEstado("PENDIENTE");
        Transaccion transaccionGuardada = transaccionRepository.save(nuevatransaccion);

        try {
            String idExterno = "GTW-" + System.currentTimeMillis();

            transaccionGuardada.setGatewayTxId(idExterno);
            transaccionGuardada.setEstado("COMPLETADO");
            transaccionGuardada.setFechaActualizacion(Instant.now());

        } catch (Exception e) {
            transaccionGuardada.setEstado("FALLIDO");
            transaccionGuardada.setFechaActualizacion(Instant.now());
        }

        return transaccionRepository.save(transaccionGuardada);
    }

    @Override
    public Transaccion obtenerTransaccion(Long id) {
        return transaccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transacción no encontrada"));
    }
}
