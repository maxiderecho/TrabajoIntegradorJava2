package com.alkemy.javaNivel2.TrabajoIntegrador.Controller;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import com.alkemy.javaNivel2.TrabajoIntegrador.Service.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @PostMapping
    public ResponseEntity<LibroResponseDTO> crearLibro(@Valid @RequestBody LibroRequestDTO libroDTO) {
        LibroResponseDTO nuevoLibro = libroService.crearLibro(libroDTO);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> obtenerTodosLosLibros() {
        List<LibroResponseDTO> libros = libroService.obtenerTodosLosLibros();
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibroPorId(@PathVariable String id) {
        return libroService.obtenerLibroPorId(id)
                .map(libro -> new ResponseEntity<>(libro, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable String id, @Valid @RequestBody LibroRequestDTO libroDTO) {
        try {
            LibroResponseDTO libroActualizado = libroService.actualizarLibro(id, libroDTO);
            return new ResponseEntity<>(libroActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable String id) {
        try {
            libroService.eliminarLibro(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/estado/{estadoLectura}")
    public ResponseEntity<List<LibroResponseDTO>> obtenerLibrosPorEstado(@PathVariable EstadoLectura estadoLectura) {
        List<LibroResponseDTO> libros = libroService.obtenerLibrosPorEstadoLectura(estadoLectura);
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LibroResponseDTO>> obtenerLibrosPorGenero(@PathVariable String genero) {
        List<LibroResponseDTO> libros = libroService.obtenerLibrosPorGenero(genero);
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LibroResponseDTO>> obtenerLibrosPorAutor(@PathVariable String autor) {
        List<LibroResponseDTO> libros = libroService.obtenerLibrosPorAutor(autor);
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }
}
