package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface LibroService {
    CompletableFuture<LibroResponseDTO> crearLibro(LibroRequestDTO libroDTO);
    CompletableFuture<List<LibroResponseDTO>> obtenerTodosLosLibros();
    Optional<LibroResponseDTO> obtenerLibroPorId(String id);
    CompletableFuture<LibroResponseDTO> actualizarLibro(String id, LibroRequestDTO libroDTO);
    CompletableFuture<Void> eliminarLibro(String id);
    List<LibroResponseDTO> obtenerLibrosPorEstadoLectura(EstadoLectura estadoLectura);
    List<LibroResponseDTO> obtenerLibrosPorGenero(String genero);
    List<LibroResponseDTO> obtenerLibrosPorAutor(String autor);
}
