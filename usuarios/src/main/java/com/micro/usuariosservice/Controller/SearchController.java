package com.micro.usuariosservice.Controller;

import com.micro.usuariosservice.DTO.ApiResponse;
import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // ====================================
    // 🔍 BÚSQUEDA GENERAL (inteligente)
    // ====================================
    @GetMapping
    public ResponseEntity<ApiResponse<?>> buscarUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false, defaultValue = "false") boolean activos,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        // Configurar paginación y orden
        String[] sortParams = sort.split(",");
        Sort sortConfig = Sort.by(Sort.Direction.fromString(sortParams.length > 1 ? sortParams[1] : "asc"), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortConfig);

        // 1️⃣ Si hay nombre → búsqueda paginada
        if (nombre != null && !nombre.isBlank()) {
            Page<Usuario> pagedResult = searchService.buscarPorNombreOApellido(nombre, pageable);
            pagedResult.forEach(u -> u.setPassword(null));
            return ResponseEntity.ok(ApiResponse.success("Búsqueda paginada realizada correctamente", pagedResult));
        }

        // 2️⃣ Si hay rol, departamento o activos → búsqueda simple
        List<Usuario> resultado;
        if (role != null && !role.isBlank()) {
            resultado = searchService.buscarPorRol(role);
        } else if (departamento != null && !departamento.isBlank()) {
            resultado = searchService.buscarPorDepartamento(departamento);
        } else if (activos) {
            resultado = searchService.buscarActivos();
        } else {
            resultado = List.of();
        }

        resultado.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(ApiResponse.success("Búsqueda realizada correctamente", resultado));
    }

    // ====================================
    // 🔹 BÚSQUEDA PAGINADA DIRECTA
    // ====================================
    @GetMapping("/paginado")
    public ResponseEntity<ApiResponse<Page<Usuario>>> buscarPorNombrePaginado(
            @RequestParam(required = false) String nombre,
            @PageableDefault(size = 10, sort = "apellido") Pageable pageable) {

        Page<Usuario> pagina = searchService.buscarPorNombreOApellido(nombre, pageable);
        pagina.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(ApiResponse.success("Resultados paginados", pagina));
    }

    // ====================================
    // ✅ LISTAR SOLO ACTIVOS
    // ====================================
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<Usuario>>> listarActivos() {
        List<Usuario> activos = searchService.buscarActivos();
        activos.forEach(u -> u.setPassword(null));

        if (activos.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No hay usuarios activos", activos));
        }
        return ResponseEntity.ok(ApiResponse.success("Usuarios activos encontrados", activos));
    }

    // ====================================
    // 👥 BUSCAR POR ROL
    // ====================================
    @GetMapping("/rol")
    public ResponseEntity<ApiResponse<List<Usuario>>> buscarPorRol(@RequestParam String rol) {
        List<Usuario> porRol = searchService.buscarPorRol(rol);
        porRol.forEach(u -> u.setPassword(null));

        if (porRol.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No se encontraron usuarios con ese rol", porRol));
        }
        return ResponseEntity.ok(ApiResponse.success("Usuarios con rol " + rol + " encontrados", porRol));
    }

    // ====================================
    // 🏢 BUSCAR POR DEPARTAMENTO (Admins)
    // ====================================
    @GetMapping("/departamento")
    public ResponseEntity<ApiResponse<List<Usuario>>> buscarPorDepartamento(@RequestParam String departamento) {
        List<Usuario> admins = searchService.buscarPorDepartamento(departamento);
        admins.forEach(u -> u.setPassword(null));

        if (admins.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No hay administradores en ese departamento", admins));
        }
        return ResponseEntity.ok(ApiResponse.success("Administradores del departamento '" + departamento + "'", admins));
    }
}
