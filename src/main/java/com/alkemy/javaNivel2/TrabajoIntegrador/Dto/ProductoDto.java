package com.alkemy.javaNivel2.TrabajoIntegrador.Dto;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDto {
    long id;
    String nombre;
    String descripcion;
    BigDecimal precio;
    Categoria categoriaId;
}
