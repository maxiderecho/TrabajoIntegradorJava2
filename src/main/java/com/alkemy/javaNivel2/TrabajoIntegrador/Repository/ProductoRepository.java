package com.alkemy.javaNivel2.TrabajoIntegrador.Repository;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
    Optional<Producto> findById(String id);
}
