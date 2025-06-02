package com.alkemy.javaNivel2.TrabajoIntegrador.Dto;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {
    long id;
    Producto producto;
    int cantidad;
}
