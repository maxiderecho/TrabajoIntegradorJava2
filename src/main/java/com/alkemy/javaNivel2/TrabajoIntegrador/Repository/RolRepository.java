package com.alkemy.javaNivel2.TrabajoIntegrador.Repository;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends MongoRepository<Rol, String> {
    Optional<Rol> findByName(String name);
}
