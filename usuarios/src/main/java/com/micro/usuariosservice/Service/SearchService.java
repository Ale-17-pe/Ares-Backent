package com.micro.usuariosservice.Service;

import com.micro.usuariosservice.Model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchService {
    List<Usuario> buscarPorNombre(String filtro);
    Page<Usuario> buscarPorNombreOApellido(String filtro, Pageable pageable);
    List<Usuario> buscarActivos();
    List<Usuario> buscarPorRol(String rol);
    List<Usuario> buscarPorDepartamento(String departamento);
}
