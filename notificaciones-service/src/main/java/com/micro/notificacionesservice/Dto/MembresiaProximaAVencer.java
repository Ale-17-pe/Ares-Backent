package com.micro.notificacionesservice.Dto;

import java.time.LocalDate;

public record MembresiaProximaAVencer(
        Long idMembresia,
        Long idUsuario,
        String emailDestino,
        LocalDate fechaVencimiento
) {}
