package com.micro.pagosservice.Model.Repository;

import com.micro.pagosservice.Model.Enum.EstadoTransaccion;
import com.micro.pagosservice.Model.Transaccion;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
@Repository
public interface TransaccionRepositoryCustom {
    List<Transaccion> buscarFiltradas(Long usuarioId, EstadoTransaccion estado, Instant desde, Instant hasta);
}
