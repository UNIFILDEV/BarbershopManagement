package com.barbershop.erp.repository;

import com.barbershop.erp.model.Usuario;
import com.barbershop.erp.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    boolean existsByEmail(String email);
}