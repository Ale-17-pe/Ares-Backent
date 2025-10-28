package com.micro.notificacionesservice.Controller;

import com.micro.notificacionesservice.Service.EmailService;
import com.micro.notificacionesservice.Service.NotificacionContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionContext context;

    public NotificacionController(NotificacionContext context) {
        this.context = context;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(
            @RequestParam String destino,
            @RequestParam(defaultValue = "simulada") String canal) {
        context.enviar(canal, destino, "Prueba de Notificación", "Hola! Este es un correo de prueba.");
        return ResponseEntity.ok("Notificación enviada por canal: " + canal);
    }
}
