package org.lucasito.springcloud.msvc.cursos.cursos.controller;


import feign.FeignException;
import jakarta.validation.Valid;
import org.lucasito.springcloud.msvc.cursos.cursos.models.Usuario;
import org.lucasito.springcloud.msvc.cursos.cursos.models.entity.Curso;
import org.lucasito.springcloud.msvc.cursos.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){

        Optional<Curso> curso = service.obtenerPorId(id);

        if(curso.isPresent()) {
            return ResponseEntity.ok(curso.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result){

        if(result.hasErrors()) {
            return validar(result);
        }

        Curso cursoDb = service.guardar(curso);

        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {

        if(result.hasErrors()) {
            return validar(result);
        }

        Optional<Curso> cursoDb = service.obtenerPorId(id);

        if(cursoDb.isPresent()){
            Curso cursoNuevo = (Curso) cursoDb.get();
            cursoNuevo.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoNuevo));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Curso no encontrado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Optional<Curso> cursoDb = service.obtenerPorId(id);

        if(cursoDb.isPresent()){
            service.eliminar(cursoDb.get().getId());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {

        Optional<Usuario> usuarioDb;

        try{
            usuarioDb = service.asignarUsuario(usuario, cursoId);
        } catch(FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje", "No existe el usuario " +
                                    "por el id o error de comunicación: "+e.getMessage()));
        }

        if(usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDb.get());
        }

        return ResponseEntity.notFound().build();


    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {

        Optional<Usuario> usuarioDb;

        try{
            usuarioDb = service.crearUsuario(usuario, cursoId);
        } catch(FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje", "No existe el usuario " +
                                    "por el id o error de comunicación: "+e.getMessage()));
        }

        if(usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioDb.get());
        }

        return ResponseEntity.notFound().build();


    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioDb;

        try{
            usuarioDb = service.eliminarUsuario(usuario, cursoId);
        }catch(FeignException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Collections
                            .singletonMap("mensaje", "El usuario no existe: " +e.getMessage()));
        }

        if(usuarioDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioDb.get());
        }

        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();

        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "El campo" + error.getField() + " " + error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errores);
    }





}
