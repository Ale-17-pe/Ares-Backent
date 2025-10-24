package com.micro.reservasservices.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservaRequest {
    private Long usuarioId;
    private Long claseId;
}
