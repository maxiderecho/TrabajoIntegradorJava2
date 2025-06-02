package com.alkemy.javaNivel2.TrabajoIntegrador.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @Id
    long id;
    Producto producto;
    int cantidad;
}
