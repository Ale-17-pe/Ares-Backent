package com.micro.membresiasservice.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuscripcionRequest {
    private Long usuarioId;
    private Long planId;
}
