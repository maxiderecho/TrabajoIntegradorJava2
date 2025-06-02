package com.alkemy.javaNivel2.TrabajoIntegrador.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    long id;
    String nombre;
    String descripcion;
    BigDecimal precio;
    Categoria categoriaId;
}
