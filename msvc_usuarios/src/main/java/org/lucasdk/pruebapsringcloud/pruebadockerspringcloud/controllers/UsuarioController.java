package org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.controllers;


import jakarta.validation.Valid;
import org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.models.entity.usuarios.Usuario;
import org.lucasdk.pruebapsringcloud.pruebadockerspringcloud.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.obtenerPorId(id);

        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok().body(usuarioOptional.get());
        }

        return ResponseEntity.notFound().build();
    }

    /*
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario guardar(@RequestBody Usuario usuario){
        return service.guardar(usuario);
    }
    */

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> devolverUsuariosPorCurso(@RequestParam("id_curso") List<Long> ids) {
        return ResponseEntity.ok(service.obtenerPorCurso(ids));
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario, BindingResult result){

        if(service.obtenerPorEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(Collections
                            .singletonMap("error", "Ya existe un usuario con ese email"));
        }

        if(result.hasErrors()) {
            return validar(result);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service
                        .guardar(usuario));
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {


        if(result.hasErrors()) {
            return validar(result);
        }

        Optional<Usuario> usuarioOptional = service.obtenerPorId(id);
        if(usuarioOptional.isPresent()){

            Usuario usuarioDb = usuarioOptional.get();
            if(!usuario.getEmail().isEmpty() && service.obtenerPorEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity
                        .badRequest()
                        .body(Collections
                                .singletonMap("error", "Ya existe un usuario con ese email"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.obtenerPorId(id);
        if(usuarioOptional.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errores);
    }

    
}
