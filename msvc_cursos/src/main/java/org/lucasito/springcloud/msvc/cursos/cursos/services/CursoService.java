package org.lucasito.springcloud.msvc.cursos.cursos.services;

import org.lucasito.springcloud.msvc.cursos.cursos.models.Usuario;
import org.lucasito.springcloud.msvc.cursos.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> listar();

    Optional<Curso> obtenerPorId(Long id);

    Curso guardar(Curso curso);

    void eliminar(Long id);

    //Asigna un usuario existente a un curso en particular.
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);

    //Asigna un nuevo usuario a un curso en particular, todavía no está registrado en la bd.
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);

    //Elimina un usuario de un curso en particular.
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}
