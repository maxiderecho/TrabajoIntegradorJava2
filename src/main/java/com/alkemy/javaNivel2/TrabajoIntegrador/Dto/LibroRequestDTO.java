package com.alkemy.javaNivel2.TrabajoIntegrador.Dto;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

import java.time.LocalDate;

@Data
public class LibroRequestDTO {
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    private String autor;

    private String genero;

    @NotNull(message = "El estado de lectura no puede ser nulo")
    private EstadoLectura estadoLectura;

    private LocalDate fechaInicioLectura;
    private LocalDate fechaFinLectura;

    @Min(value = 1, message = "La calificación debe ser al menos 1")
    @Max(value = 5, message = "La calificación no puede ser mayor a 5")
    private Integer calificacion;

    private String editorial;
    private String isbn;
    private String notas;
}
