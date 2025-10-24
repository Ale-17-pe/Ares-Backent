package com.micro.pagosservice.Service;

import com.micro.pagosservice.Model.Transaccion;

public interface PagoService {
    Transaccion iniciarPago(Transaccion transaccion);
    Transaccion obtenerTransaccion(Long id);
}
