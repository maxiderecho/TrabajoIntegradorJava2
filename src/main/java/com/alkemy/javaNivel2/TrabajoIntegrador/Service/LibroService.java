package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;

import java.util.List;
import java.util.Optional;

public interface LibroService {
    LibroResponseDTO crearLibro(LibroRequestDTO libroDTO);
    List<LibroResponseDTO> obtenerTodosLosLibros();
    Optional<LibroResponseDTO> obtenerLibroPorId(String id);
    LibroResponseDTO actualizarLibro(String id, LibroRequestDTO libroDTO);
    void eliminarLibro(String id);
    List<LibroResponseDTO> obtenerLibrosPorEstadoLectura(EstadoLectura estadoLectura);
    List<LibroResponseDTO> obtenerLibrosPorGenero(String genero);
    List<LibroResponseDTO> obtenerLibrosPorAutor(String autor);
}
