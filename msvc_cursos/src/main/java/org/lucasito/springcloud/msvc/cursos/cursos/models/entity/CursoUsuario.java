package org.lucasito.springcloud.msvc.cursos.cursos.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="cursos_usuarios")
@Getter
@Setter
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "usuario_id", unique = true)
    private Long usuarioId;



    @Override
    public boolean equals(Object obj) {
        //Si el objeto que se pasa por argumento es igual al objeto actual
        if(this == obj) {
            return true;
        }

        //Si el objeto que se pasa por argumento no es una instancia de CursoUsuario
        if(!(obj instanceof CursoUsuario)) {
            return false;
        }

        //Castear el objeto obj a CursoUsuario
        CursoUsuario cursoUsuario = (CursoUsuario) obj;

        return this.usuarioId != null && this.usuarioId.equals(cursoUsuario.usuarioId);

    }


}
