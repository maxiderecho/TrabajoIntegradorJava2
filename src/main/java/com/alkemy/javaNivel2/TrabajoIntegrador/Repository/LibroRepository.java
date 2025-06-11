package com.alkemy.javaNivel2.TrabajoIntegrador.Repository;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Libro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends MongoRepository<Libro, String> {

    List<Libro> findByEstadoLectura(EstadoLectura estadoLectura);
    List<Libro> findByGenero(String genero);
    List<Libro> findByAutorContainingIgnoreCase(String autor);
}
