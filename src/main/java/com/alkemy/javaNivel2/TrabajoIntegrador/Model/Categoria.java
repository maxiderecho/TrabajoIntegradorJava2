package com.alkemy.javaNivel2.TrabajoIntegrador.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    @Id
    long id;
    String nombre;
    String descripcion;
}
