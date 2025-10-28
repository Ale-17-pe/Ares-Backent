package com.micro.notificacionesservice.Service;

import com.micro.notificacionesservice.Service.Strategy.NotificacionStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificacionContext {
    private final java.util.List<NotificacionStrategy> estrategias;
    private final Map<String, NotificacionStrategy> registry = new ConcurrentHashMap<>();

    @Value("${notificaciones.default-channel}")
    private String canalPorDefecto;

    @PostConstruct
    void init() {
        estrategias.forEach(e -> registry.put(e.getNombre(), e));
        log.info("🔌 Estrategias registradas: {}", registry.keySet());
        log.info("📬 Canal por defecto: {}", canalPorDefecto);

    }
    public void enviarDefault(String destino, String asunto, String mensaje) {
        enviar(canalPorDefecto, destino, asunto, mensaje);
    }
    public void enviar(String canal, String destino, String asunto, String mensaje) {
        NotificacionStrategy strategy = registry.getOrDefault(canal, registry.get(canalPorDefecto));
        if (strategy == null) {
            throw new IllegalStateException("No hay estrategia disponible para canal: " + canal);
        }
        strategy.enviar(destino, asunto, mensaje);
    }
}
