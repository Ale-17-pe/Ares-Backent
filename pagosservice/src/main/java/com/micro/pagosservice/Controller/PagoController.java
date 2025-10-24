package com.micro.pagosservice.Controller;

import com.micro.pagosservice.Model.Transaccion;
import com.micro.pagosservice.Service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/pagos")
public class PagoController {
    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<Transaccion> crearPago(@RequestBody Transaccion transaccion) {
        Transaccion resultado = pagoService.iniciarPago(transaccion);
        if ("COMPLETADO".equals(resultado.getEstado())) {
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(resultado, HttpStatus.ACCEPTED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> obtenerPago(@PathVariable Long id) {
        Transaccion transaccion = pagoService.obtenerTransaccion(id);
        return ResponseEntity.ok(transaccion);
    }
}
