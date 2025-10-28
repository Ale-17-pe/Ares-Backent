package com.micro.pagosservice.Strategy;

import com.micro.pagosservice.DTO.TransaccionRequest;
import com.micro.pagosservice.Model.Transaccion;

public interface PagoStrategy {
    Transaccion procesarPago(TransaccionRequest request);
    boolean soportaMetodo(String metodo);

}
