package com.alkemy.javaNivel2.TrabajoIntegrador.Dto;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LibroResponseDTO {
    private String id;
    private String titulo;
    private String autor;
    private String genero;
    private EstadoLectura estadoLectura;
    private LocalDate fechaInicioLectura;
    private LocalDate fechaFinLectura;
    private Integer calificacion;
    private String editorial;
    private String isbn;
    private String notas;
}
