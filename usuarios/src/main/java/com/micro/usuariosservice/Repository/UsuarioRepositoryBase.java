package com.micro.usuariosservice.Repository;

import com.micro.usuariosservice.Model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UsuarioRepositoryBase <T extends Usuario> extends JpaRepository<T, Long> {
    Optional<T> findByEmail(String email);
    Optional<T> findByDni(String dni);

    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    List<T> findActivos();

    // 🔹 Buscar usuarios por nombre o apellido (útil para búsquedas en el frontend)
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<T> buscarPorNombreOApellido(String filtro);
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
            "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    Page<T> buscarPorNombreOApellido(String filtro, Pageable pageable);

}
