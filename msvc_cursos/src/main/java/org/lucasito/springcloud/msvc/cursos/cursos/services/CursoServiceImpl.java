package org.lucasito.springcloud.msvc.cursos.cursos.services;

import org.lucasito.springcloud.msvc.cursos.cursos.clients.UsuarioClientRest;
import org.lucasito.springcloud.msvc.cursos.cursos.models.Usuario;
import org.lucasito.springcloud.msvc.cursos.cursos.models.entity.Curso;
import org.lucasito.springcloud.msvc.cursos.cursos.models.entity.CursoUsuario;
import org.lucasito.springcloud.msvc.cursos.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest clientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {

        //Busco al curso existente a la base de datos
        Optional<Curso> cursoDb = repository.findById(cursoId);

        if(cursoDb.isPresent()) {
            Usuario usuarioMsvc = clientRest.detalle(usuario.getId());
            Curso curso = cursoDb.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {

        Optional<Curso> cursoDb = repository.findById(cursoId);

        if(cursoDb.isPresent()) {
            //
            Usuario usuarioNuevoMsvc = clientRest.crear(usuario);
            //Obtenemos el curso de la base de datos
            Curso curso = cursoDb.get();
            //Creamos la relación entre curso y usuario
            CursoUsuario cursoUsuario = new CursoUsuario();
            //Creamos el ide del usuario
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }


        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {

        Optional<Curso> cursoDb = repository.findById(cursoId);

        if(cursoDb.isPresent()) {
            Usuario usuarioMsvc = clientRest.detalle(usuario.getId());

            Curso curso = cursoDb.get();
            CursoUsuario cursoUsuario = new CursoUsuario();

            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
