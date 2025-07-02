package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.LibroResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Libro;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroServiceImplTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private LibroServiceImpl libroService;

    private LibroRequestDTO libroRequestDTO;
    private Libro libro;
    private LibroResponseDTO libroResponseDTO;

    @BeforeEach
    void setUp() {
        libroRequestDTO = new LibroRequestDTO();
        libroRequestDTO.setTitulo("El Señor de los Anillos");
        libroRequestDTO.setAutor("J.R.R. Tolkien");
        libroRequestDTO.setGenero("Fantasía");
        libroRequestDTO.setEstadoLectura(EstadoLectura.LEIDO);
        libroRequestDTO.setFechaInicioLectura(LocalDate.of(2023, 1, 1));
        libroRequestDTO.setFechaFinLectura(LocalDate.of(2023, 1, 15));
        libroRequestDTO.setCalificacion(5);
        libroRequestDTO.setEditorial("Minotauro");
        libroRequestDTO.setIsbn("978-84-450-7110-7");
        libroRequestDTO.setNotas("Una obra maestra.");

        libro = new Libro();
        libro.setId("1");
        libro.setTitulo("El Señor de los Anillos");
        libro.setAutor("J.R.R. Tolkien");
        libro.setGenero("Fantasía");
        libro.setEstadoLectura(EstadoLectura.LEIDO);
        libro.setFechaInicioLectura(LocalDate.of(2023, 1, 1));
        libro.setFechaFinLectura(LocalDate.of(2023, 1, 15));
        libro.setCalificacion(5);
        libro.setEditorial("Minotauro");
        libro.setIsbn("978-84-450-7110-7");
        libro.setNotas("Una obra maestra.");

        libroResponseDTO = new LibroResponseDTO();
        libroResponseDTO.setId("1");
        libroResponseDTO.setTitulo("El Señor de los Anillos");
        libroResponseDTO.setAutor("J.R.R. Tolkien");
        libroResponseDTO.setGenero("Fantasía");
        libroResponseDTO.setEstadoLectura(EstadoLectura.LEIDO);
        libroResponseDTO.setFechaInicioLectura(LocalDate.of(2023, 1, 1));
        libroResponseDTO.setFechaFinLectura(LocalDate.of(2023, 1, 15));
        libroResponseDTO.setCalificacion(5);
        libroResponseDTO.setEditorial("Minotauro");
        libroResponseDTO.setIsbn("978-84-450-7110-7");
        libroResponseDTO.setNotas("Una obra maestra.");
    }

    @Test
    @DisplayName("Debería crear un libro exitosamente de forma asíncrona")
    void crearLibro_shouldCreateLibroSuccessfullyAsync() throws ExecutionException, InterruptedException {
        when(modelMapper.map(libroRequestDTO, Libro.class)).thenReturn(libro);
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);
        when(modelMapper.map(libro, LibroResponseDTO.class)).thenReturn(libroResponseDTO);

        CompletableFuture<LibroResponseDTO> resultFuture = libroService.crearLibro(libroRequestDTO);

        LibroResponseDTO result = resultFuture.get();

        assertNotNull(result);
        assertEquals(libroResponseDTO.getTitulo(), result.getTitulo());
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    @DisplayName("Debería obtener todos los libros exitosamente de forma asíncrona")
    void obtenerTodosLosLibros_shouldReturnAllLibrosSuccessfullyAsync() throws ExecutionException, InterruptedException {
        List<Libro> libros = Arrays.asList(libro, new Libro("2", "1984", "George Orwell", "Distopía", EstadoLectura.PENDIENTE, null, null, null, null, null, null));
        List<LibroResponseDTO> responseDTOs = Arrays.asList(libroResponseDTO, new LibroResponseDTO("2", "1984", "George Orwell", "Distopía", EstadoLectura.PENDIENTE, null, null, null, null, null, null));

        when(libroRepository.findAll()).thenReturn(libros);
        when(modelMapper.map(libros.get(0), LibroResponseDTO.class)).thenReturn(responseDTOs.get(0));
        when(modelMapper.map(libros.get(1), LibroResponseDTO.class)).thenReturn(responseDTOs.get(1));

        CompletableFuture<List<LibroResponseDTO>> resultFuture = libroService.obtenerTodosLosLibros();
        List<LibroResponseDTO> result = resultFuture.get();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(responseDTOs.get(0).getTitulo(), result.get(0).getTitulo());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería obtener un libro por ID exitosamente")
    void obtenerLibroPorId_shouldReturnLibroByIdSuccessfully() {
        when(libroRepository.findById("1")).thenReturn(Optional.of(libro));
        when(modelMapper.map(libro, LibroResponseDTO.class)).thenReturn(libroResponseDTO);

        Optional<LibroResponseDTO> result = libroService.obtenerLibroPorId("1");

        assertTrue(result.isPresent());
        assertEquals(libroResponseDTO.getTitulo(), result.get().getTitulo());
        verify(libroRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Debería retornar vacío si el libro por ID no es encontrado")
    void obtenerLibroPorId_shouldReturnEmptyIfNotFound() {
        when(libroRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<LibroResponseDTO> result = libroService.obtenerLibroPorId("nonExistentId");

        assertFalse(result.isPresent());
        verify(libroRepository, times(1)).findById("nonExistentId");
    }

    @Test
    @DisplayName("Debería actualizar un libro exitosamente de forma asíncrona")
    void actualizarLibro_shouldUpdateLibroSuccessfullyAsync() throws ExecutionException, InterruptedException {
        LibroRequestDTO updatedLibroDTO = new LibroRequestDTO();
        updatedLibroDTO.setTitulo("El Hobbit");
        updatedLibroDTO.setEstadoLectura(EstadoLectura.LEIDO);

        Libro existingLibro = new Libro("1", "El Señor de los Anillos", "J.R.R. Tolkien", "Fantasía", EstadoLectura.PENDIENTE, null, null, null, null, null, null);
        Libro updatedLibro = new Libro("1", "El Hobbit", "J.R.R. Tolkien", "Fantasía", EstadoLectura.LEIDO, null, null, null, null, null, null);
        LibroResponseDTO updatedResponseDTO = new LibroResponseDTO("1", "El Hobbit", "J.R.R. Tolkien", "Fantasía", EstadoLectura.LEIDO, null, null, null, null, null, null);


        when(libroRepository.findById("1")).thenReturn(Optional.of(existingLibro));
        when(libroRepository.save(any(Libro.class))).thenReturn(updatedLibro);
        when(modelMapper.map(updatedLibro, LibroResponseDTO.class)).thenReturn(updatedResponseDTO);

        CompletableFuture<LibroResponseDTO> resultFuture = libroService.actualizarLibro("1", updatedLibroDTO);
        LibroResponseDTO result = resultFuture.get();

        assertNotNull(result);
        assertEquals("El Hobbit", result.getTitulo());
        assertEquals(EstadoLectura.LEIDO, result.getEstadoLectura());
        verify(libroRepository, times(1)).findById("1");
        verify(libroRepository, times(1)).save(any(Libro.class));
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException al actualizar si el libro no es encontrado")
    void actualizarLibro_shouldThrowExceptionIfNotFound() {
        when(libroRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        ExecutionException exception = assertThrows(ExecutionException.class, () ->
                libroService.actualizarLibro("nonExistentId", libroRequestDTO).get()
        );
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Libro no encontrado con ID: nonExistentId", exception.getCause().getMessage());
        verify(libroRepository, times(1)).findById("nonExistentId");
        verify(libroRepository, never()).save(any(Libro.class));
    }

    @Test
    @DisplayName("Debería eliminar un libro exitosamente de forma asíncrona")
    void eliminarLibro_shouldDeleteLibroSuccessfullyAsync() throws ExecutionException, InterruptedException {
        when(libroRepository.existsById("1")).thenReturn(true);
        doNothing().when(libroRepository).deleteById("1");

        CompletableFuture<Void> resultFuture = libroService.eliminarLibro("1");
        resultFuture.get();

        verify(libroRepository, times(1)).existsById("1");
        verify(libroRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("Debería lanzar RuntimeException al eliminar si el libro no es encontrado")
    void eliminarLibro_shouldThrowExceptionIfNotFound() {
        when(libroRepository.existsById("nonExistentId")).thenReturn(false);

        ExecutionException exception = assertThrows(ExecutionException.class, () ->
                libroService.eliminarLibro("nonExistentId").get()
        );
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Libro no encontrado con ID: nonExistentId", exception.getCause().getMessage());
        verify(libroRepository, times(1)).existsById("nonExistentId");
        verify(libroRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Debería obtener libros por estado de lectura exitosamente")
    void obtenerLibrosPorEstadoLectura_shouldReturnFilteredLibros() {
        List<Libro> librosLeidos = Arrays.asList(libro);
        List<LibroResponseDTO> responseDTOs = Arrays.asList(libroResponseDTO);

        when(libroRepository.findByEstadoLectura(EstadoLectura.LEIDO)).thenReturn(librosLeidos);
        when(modelMapper.map(libro, LibroResponseDTO.class)).thenReturn(libroResponseDTO);

        List<LibroResponseDTO> result = libroService.obtenerLibrosPorEstadoLectura(EstadoLectura.LEIDO);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(EstadoLectura.LEIDO, result.get(0).getEstadoLectura());
        verify(libroRepository, times(1)).findByEstadoLectura(EstadoLectura.LEIDO);
    }

    @Test
    @DisplayName("Debería obtener libros por género exitosamente")
    void obtenerLibrosPorGenero_shouldReturnFilteredLibros() {
        List<Libro> librosFantasia = Arrays.asList(libro);
        List<LibroResponseDTO> responseDTOs = Arrays.asList(libroResponseDTO);

        when(libroRepository.findByGenero("Fantasía")).thenReturn(librosFantasia);
        when(modelMapper.map(libro, LibroResponseDTO.class)).thenReturn(libroResponseDTO);

        List<LibroResponseDTO> result = libroService.obtenerLibrosPorGenero("Fantasía");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Fantasía", result.get(0).getGenero());
        verify(libroRepository, times(1)).findByGenero("Fantasía");
    }

    @Test
    @DisplayName("Debería obtener libros por autor exitosamente (ignorando mayúsculas/minúsculas)")
    void obtenerLibrosPorAutor_shouldReturnFilteredLibrosIgnoringCase() {
        List<Libro> librosTolkien = Arrays.asList(libro);
        List<LibroResponseDTO> responseDTOs = Arrays.asList(libroResponseDTO);

        when(libroRepository.findByAutorContainingIgnoreCase("tolkien")).thenReturn(librosTolkien);
        when(modelMapper.map(libro, LibroResponseDTO.class)).thenReturn(libroResponseDTO);

        List<LibroResponseDTO> result = libroService.obtenerLibrosPorAutor("tolkien");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("J.R.R. Tolkien", result.get(0).getAutor());
        verify(libroRepository, times(1)).findByAutorContainingIgnoreCase("tolkien");
    }
}