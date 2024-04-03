package org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.repositories;

import org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.models.entity.usuarios.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

}
