package com.alkemy.javaNivel2.TrabajoIntegrador.Repository;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.EstadoLectura;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class LibroRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private LibroRepository libroRepository;

    @BeforeEach
    void setUp() {
        libroRepository.deleteAll();

        Libro libro1 = new Libro(null, "El Señor de los Anillos", "J.R.R. Tolkien", "Fantasía", EstadoLectura.LEIDO, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 15), 5, "Minotauro", "978-84-450-7110-7", "Una obra maestra.");
        Libro libro2 = new Libro(null, "1984", "George Orwell", "Distopía", EstadoLectura.PENDIENTE, null, null, null, "Debolsillo", "978-84-9759-291-0", "Un clásico distópico.");
        Libro libro3 = new Libro(null, "Cien años de soledad", "Gabriel García Márquez", "Realismo Mágico", EstadoLectura.LEIDO, LocalDate.of(2022, 5, 1), LocalDate.of(2022, 6, 1), 4, "Sudamericana", "978-84-376-0494-7", "Gran narrativa.");
        Libro libro4 = new Libro(null, "Fahrenheit 451", "Ray Bradbury", "Ciencia Ficción", EstadoLectura.LEYENDO, LocalDate.of(2024, 3, 1), null, null, "Minotauro", "978-84-450-0255-2", "Interesante.");

        libroRepository.saveAll(List.of(libro1, libro2, libro3, libro4));
    }

    @Test
    @DisplayName("Debería guardar un libro")
    void shouldSaveLibro() {
        Libro nuevoLibro = new Libro(null, "Dune", "Frank Herbert", "Ciencia Ficción", EstadoLectura.PENDIENTE, null, null, null, "Debolsillo", "978-84-9759-688-8", "Clásico de ciencia ficción.");
        Libro savedLibro = libroRepository.save(nuevoLibro);

        assertNotNull(savedLibro.getId());
        assertEquals("Dune", savedLibro.getTitulo());
    }

    @Test
    @DisplayName("Debería encontrar todos los libros")
    void shouldFindAllLibros() {
        List<Libro> libros = libroRepository.findAll();
        assertNotNull(libros);
        assertEquals(4, libros.size());
    }

    @Test
    @DisplayName("Debería encontrar un libro por ID")
    void shouldFindLibroById() {
        List<Libro> libros = libroRepository.findAll();
        String id = libros.get(0).getId();
        Optional<Libro> foundLibro = libroRepository.findById(id);

        assertTrue(foundLibro.isPresent());
        assertEquals(libros.get(0).getTitulo(), foundLibro.get().getTitulo());
    }

    @Test
    @DisplayName("Debería encontrar libros por estado de lectura")
    void shouldFindByEstadoLectura() {
        List<Libro> leidos = libroRepository.findByEstadoLectura(EstadoLectura.LEIDO);
        assertNotNull(leidos);
        assertEquals(2, leidos.size());
        assertTrue(leidos.stream().allMatch(l -> l.getEstadoLectura() == EstadoLectura.LEIDO));
    }

    @Test
    @DisplayName("Debería encontrar libros por género")
    void shouldFindByGenero() {
        List<Libro> fantasia = libroRepository.findByGenero("Fantasía");
        assertNotNull(fantasia);
        assertEquals(1, fantasia.size());
        assertEquals("El Señor de los Anillos", fantasia.get(0).getTitulo());
    }

    @Test
    @DisplayName("Debería encontrar libros por autor ignorando mayúsculas/minúsculas")
    void shouldFindByAutorContainingIgnoreCase() {
        List<Libro> tolkienBooks = libroRepository.findByAutorContainingIgnoreCase("tolkien");
        assertNotNull(tolkienBooks);
        assertEquals(1, tolkienBooks.size());
        assertEquals("J.R.R. Tolkien", tolkienBooks.get(0).getAutor());

        List<Libro> orwellBooks = libroRepository.findByAutorContainingIgnoreCase("ORWELL");
        assertNotNull(orwellBooks);
        assertEquals(1, orwellBooks.size());
        assertEquals("George Orwell", orwellBooks.get(0).getAutor());
    }

    @Test
    @DisplayName("Debería eliminar un libro por ID")
    void shouldDeleteLibroById() {
        List<Libro> libros = libroRepository.findAll();
        String idToDelete = libros.get(0).getId();
        libroRepository.deleteById(idToDelete);

        Optional<Libro> deletedLibro = libroRepository.findById(idToDelete);
        assertFalse(deletedLibro.isPresent());
        assertEquals(3, libroRepository.findAll().size());
    }
}
