package org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.services;

import org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.models.entity.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();

    Optional<Usuario> obtenerPorId(Long id);

    Usuario guardar(Usuario usuario);

    void eliminar(Long id);

    List<Usuario> obtenerPorCurso(Iterable<Long> ids);

    Optional<Usuario> obtenerPorEmail(String email);

}
