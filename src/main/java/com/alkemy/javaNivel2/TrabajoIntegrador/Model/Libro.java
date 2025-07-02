package com.alkemy.javaNivel2.TrabajoIntegrador.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "libros")
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
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
