package com.micro.usuariosservice.Service.Impl;

import com.micro.usuariosservice.Model.Administrador;
import com.micro.usuariosservice.Model.Cliente;
import com.micro.usuariosservice.Model.Recepcionista;
import com.micro.usuariosservice.Model.Usuario;
import com.micro.usuariosservice.Repository.AdministradorRepository;
import com.micro.usuariosservice.Repository.ClienteRepository;
import com.micro.usuariosservice.Repository.RecepcionistaRepository;
import com.micro.usuariosservice.Service.SearchService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final ClienteRepository clienteRepository;
    private final RecepcionistaRepository recepcionistaRepository;
    private final AdministradorRepository administradorRepository;

    public SearchServiceImpl(ClienteRepository clienteRepository,
                             RecepcionistaRepository recepcionistaRepository,
                             AdministradorRepository administradorRepository) {
        this.clienteRepository = clienteRepository;
        this.recepcionistaRepository = recepcionistaRepository;
        this.administradorRepository = administradorRepository;
    }

    // =========================
    //  BÚSQUEDA NO PAGINADA
    // =========================
    @Override
    public List<Usuario> buscarPorNombre(String filtro) {
        List<Usuario> resultado = new ArrayList<>();
        resultado.addAll(clienteRepository.buscarPorNombreOApellido(filtro));
        resultado.addAll(recepcionistaRepository.buscarPorNombreOApellido(filtro));
        resultado.addAll(administradorRepository.buscarPorNombreOApellido(filtro));

        // Orden básico por apellido, luego nombre (case-insensitive)
        resultado.sort(Comparator
                .comparing((Usuario u) -> safeLower(u.getApellido()))
                .thenComparing(u -> safeLower(u.getNombre()))
                .thenComparing(Usuario::getId, Comparator.nullsLast(Long::compareTo)));

        return resultado;
    }

    // =========================
    //  BÚSQUEDA PAGINADA
    // =========================
    @Override
    public Page<Usuario> buscarPorNombreOApellido(String filtro, Pageable pageable) {
        // 1) Traemos listas combinadas (no paginadas) y luego aplicamos sort + slice en memoria.
        //    Esto es simple y funciona muy bien para volúmenes moderados.
        //    (Si esperas millones de filas, conviene un repositorio sobre la entidad base Usuario.)
        List<Usuario> combinado = new ArrayList<>();
        combinado.addAll(clienteRepository.buscarPorNombreOApellido(filtro));
        combinado.addAll(recepcionistaRepository.buscarPorNombreOApellido(filtro));
        combinado.addAll(administradorRepository.buscarPorNombreOApellido(filtro));

        // 2) Ordenamiento dinámico según pageable.getSort()
        Comparator<Usuario> cmp = buildComparatorFromSort(pageable.getSort());
        if (cmp != null) {
            combinado.sort(cmp);
        } else {
            // Fallback: apellido, nombre, id
            combinado.sort(Comparator
                    .comparing((Usuario u) -> safeLower(u.getApellido()))
                    .thenComparing(u -> safeLower(u.getNombre()))
                    .thenComparing(Usuario::getId, Comparator.nullsLast(Long::compareTo)));
        }

        // 3) Slice (sublista) para la página solicitada
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int start = Math.min(currentPage * pageSize, combinado.size());
        int end = Math.min(start + pageSize, combinado.size());
        List<Usuario> content = (start > end) ? Collections.emptyList() : combinado.subList(start, end);

        return new PageImpl<>(content, pageable, combinado.size());
    }

    // =========================
    //  BÚSQUEDA POR ROL
    // =========================
    @Override
    public List<Usuario> buscarPorRol(String role) {
        if (role == null) return List.of();
        String normalized = role.trim().toUpperCase(Locale.ROOT);

        List<? extends Usuario> lista;
        switch (normalized) {
            case "CLIENTE" -> lista = clienteRepository.findAll();
            case "RECEPCIONISTA" -> lista = recepcionistaRepository.findAll();
            case "ADMIN" -> lista = administradorRepository.findAll();
            default -> lista = List.of();
        }

        return new ArrayList<>(lista);
    }

    // =========================
    //  LISTAR SOLO ACTIVOS
    // =========================
    @Override
    public List<Usuario> buscarActivos() {
        List<Usuario> activos = new ArrayList<>();
        activos.addAll(clienteRepository.findActivos());
        activos.addAll(recepcionistaRepository.findActivos());
        activos.addAll(administradorRepository.findActivos());

        // Orden básico por id para consistencia
        activos.sort(Comparator.comparing(Usuario::getId, Comparator.nullsLast(Long::compareTo)));
        return activos;
    }

    // =========================
    //  BÚSQUEDA POR DEPARTAMENTO (ADMINS)
    // =========================
    @Override
    public List<Usuario> buscarPorDepartamento(String departamento) {
        if (departamento == null || departamento.isBlank()) return List.of();
        return new ArrayList<>(administradorRepository.findByDepartamento(departamento));
    }

    // =========================================================
    //  Helpers: ordenamiento dinámico y getters "seguros"
    // =========================================================

    private String safeLower(String s) {
        return (s == null) ? "" : s.toLowerCase(Locale.ROOT);
    }

    private Comparator<Usuario> buildComparatorFromSort(Sort sort) {
        if (sort == null || sort.isUnsorted()) return null;

        Map<String, Function<Usuario, Comparable<?>>> mapper = new HashMap<>();
        mapper.put("id", Usuario::getId);
        mapper.put("nombre", u -> safeLower(u.getNombre()));
        mapper.put("apellido", u -> safeLower(u.getApellido()));
        mapper.put("email", u -> safeLower(u.getEmail()));
        mapper.put("dni", u -> safeLower(u.getDni()));
        mapper.put("direccion", u -> safeLower(u.getDireccion()));
        mapper.put("telefono", u -> safeLower(u.getTelefono()));
        mapper.put("activo", u -> u.getActivo() == null ? Boolean.FALSE : u.getActivo());
        mapper.put("role", u -> u.getRole() == null ? "" : u.getRole().name());
        mapper.put("fechaRegistro", u -> u.getFechaRegistro() == null ? LocalDateTime.MIN : u.getFechaRegistro());

        Comparator<Usuario> comparator = null;

        for (Sort.Order order : sort) {
            String property = order.getProperty();
            boolean asc = order.getDirection().isAscending();

            Function<Usuario, Comparable<?>> keyExtractor = mapper.get(property);
            if (keyExtractor == null) continue;

            // ✅ Cast explícito para evitar error de inferencia de tipos
            Comparator<Usuario> local = Comparator.comparing(
                    (Usuario u) -> (Comparable) keyExtractor.apply(u),
                    Comparator.nullsLast(Comparator.naturalOrder())
            );

            if (!asc) {
                local = local.reversed();
            }

            comparator = (comparator == null) ? local : comparator.thenComparing(local);
        }

        return comparator;
    }

}
