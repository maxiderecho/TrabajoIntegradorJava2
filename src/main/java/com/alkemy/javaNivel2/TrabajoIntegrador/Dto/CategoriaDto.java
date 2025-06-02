package com.alkemy.javaNivel2.TrabajoIntegrador.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDto {
    long id;
    String nombre;
    String descripcion;
}
