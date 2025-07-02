package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Libro;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final ModelMapper modelMapper;

    private Libro mapToLibro(LibroRequestDTO dto) {
        return modelMapper.map(dto, Libro.class);
    }

    private LibroResponseDTO mapToLibroResponseDTO(Libro libro) {
        return modelMapper.map(libro, LibroResponseDTO.class);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<LibroResponseDTO> crearLibro(LibroRequestDTO libroDTO) {
        return CompletableFuture.supplyAsync(() -> {
            Libro libro = mapToLibro(libroDTO);
            Libro savedLibro = libroRepository.save(libro);
            return mapToLibroResponseDTO(savedLibro);
        });
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<LibroResponseDTO>> obtenerTodosLosLibros() {
        return CompletableFuture.supplyAsync(() ->
                libroRepository.findAll().stream()
                        .map(this::mapToLibroResponseDTO)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Optional<LibroResponseDTO> obtenerLibroPorId(String id) {
        return libroRepository.findById(id)
                .map(this::mapToLibroResponseDTO);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<LibroResponseDTO> actualizarLibro(String id, LibroRequestDTO libroDTO) {
        return CompletableFuture.supplyAsync(() ->
                libroRepository.findById(id).map(existingLibro -> {
                    Optional.ofNullable(libroDTO.getTitulo()).ifPresent(existingLibro::setTitulo);
                    Optional.ofNullable(libroDTO.getAutor()).ifPresent(existingLibro::setAutor);
                    Optional.ofNullable(libroDTO.getGenero()).ifPresent(existingLibro::setGenero);
                    Optional.ofNullable(libroDTO.getEstadoLectura()).ifPresent(existingLibro::setEstadoLectura);
                    Optional.ofNullable(libroDTO.getFechaInicioLectura()).ifPresent(existingLibro::setFechaInicioLectura);
                    Optional.ofNullable(libroDTO.getFechaFinLectura()).ifPresent(existingLibro::setFechaFinLectura);
                    Optional.ofNullable(libroDTO.getCalificacion()).ifPresent(existingLibro::setCalificacion);
                    Optional.ofNullable(libroDTO.getEditorial()).ifPresent(existingLibro::setEditorial);
                    Optional.ofNullable(libroDTO.getIsbn()).ifPresent(existingLibro::setIsbn);
                    Optional.ofNullable(libroDTO.getNotas()).ifPresent(existingLibro::setNotas);

                    Libro updatedLibro = libroRepository.save(existingLibro);
                    return mapToLibroResponseDTO(updatedLibro);
                }).orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id))
        );
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> eliminarLibro(String id) {
        return CompletableFuture.runAsync(() -> {
            if (!libroRepository.existsById(id)) {
                throw new RuntimeException("Libro no encontrado con ID: " + id);
            }
            libroRepository.deleteById(id);
        });
    }

    @Override
    public List<LibroResponseDTO> obtenerLibrosPorEstadoLectura(EstadoLectura estadoLectura) {
        return libroRepository.findByEstadoLectura(estadoLectura).stream()
                .map(this::mapToLibroResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroResponseDTO> obtenerLibrosPorGenero(String genero) {
        return libroRepository.findByGenero(genero).stream()
                .map(this::mapToLibroResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroResponseDTO> obtenerLibrosPorAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor).stream()
                .map(this::mapToLibroResponseDTO)
                .collect(Collectors.toList());
    }
}