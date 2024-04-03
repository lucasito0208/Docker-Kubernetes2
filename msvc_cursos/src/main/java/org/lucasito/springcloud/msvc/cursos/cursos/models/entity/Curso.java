package org.lucasito.springcloud.msvc.cursos.cursos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.lucasito.springcloud.msvc.cursos.cursos.models.Usuario;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios;

    //Solo voy a utilizar este atributo para poder quedar una clave foránea en el curso,
    // solo lo voy a utilizar para poder poblar los datos de los usuarios.
    @Transient
    private List<Usuario> usuarios;

    public Curso() {
        cursoUsuarios = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    public void addCursoUsuario(CursoUsuario cursoUsuario) {
        cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario) {
        cursoUsuarios.remove(cursoUsuario);
    }

    //Para poder quedar una clave foránea en el curso,


}
