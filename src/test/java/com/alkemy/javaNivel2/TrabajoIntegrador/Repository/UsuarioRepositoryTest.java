package com.alkemy.javaNivel2.TrabajoIntegrador.Repository;

import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Rol;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Usuario;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
class UsuarioRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario user1;
    private Usuario user2;
    private Rol userRole;
    private Rol adminRole;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        userRole = new Rol("1", "ROLE_USER");
        adminRole = new Rol("1","ROLE_ADMIN");

        Set<Rol> rolesUser = new HashSet<>();
        rolesUser.add(userRole);

        Set<Rol> rolesAdmin = new HashSet<>();
        rolesAdmin.add(userRole);
        rolesAdmin.add(adminRole);

        user1 = new Usuario(null, "testuser", "test@example.com", "password123", rolesUser);
        user2 = new Usuario(null, "adminuser", "admin@example.com", "adminpass", rolesAdmin);

        usuarioRepository.saveAll(List.of(user1, user2));
    }

    @Test
    @DisplayName("Debería guardar un usuario")
    void shouldSaveUser() {
        Set<Rol> roles = new HashSet<>();
        roles.add(new Rol("1", "ROLE_USER"));
        Usuario newUser = new Usuario(null, "newuser", "new@example.com", "newpass", roles);
        Usuario savedUser = usuarioRepository.save(newUser);

        assertNotNull(savedUser.getId());
        assertEquals("newuser", savedUser.getUsername());
    }

    @Test
    @DisplayName("Debería encontrar un usuario por username")
    void shouldFindByUsername() {
        Optional<Usuario> foundUser = usuarioRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("No debería encontrar un usuario por username si no existe")
    void shouldNotFindByUsernameIfNotFound() {
        Optional<Usuario> foundUser = usuarioRepository.findByUsername("nonexistent");
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Debería encontrar un usuario por email")
    void shouldFindByEmail() {
        Optional<Usuario> foundUser = usuarioRepository.findByEmail("admin@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("adminuser", foundUser.get().getUsername());
    }

    @Test
    @DisplayName("No debería encontrar un usuario por email si no existe")
    void shouldNotFindByEmailIfNotFound() {
        Optional<Usuario> foundUser = usuarioRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Debería retornar true si el username existe")
    void shouldReturnTrueIfUsernameExists() {
        Boolean exists = usuarioRepository.existsByUsername("testuser");
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debería retornar false si el username no existe")
    void shouldReturnFalseIfUsernameDoesNotExist() {
        Boolean exists = usuarioRepository.existsByUsername("nonexistent");
        assertFalse(exists);
    }

    @Test
    @DisplayName("Debería retornar true si el email existe")
    void shouldReturnTrueIfEmailExists() {
        Boolean exists = usuarioRepository.existsByEmail("test@example.com");
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debería retornar false si el email no existe")
    void shouldReturnFalseIfEmailDoesNotExist() {
        Boolean exists = usuarioRepository.existsByEmail("nonexistent@example.com");
        assertFalse(exists);
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void shouldFindAllUsers() {
        List<Usuario> users = usuarioRepository.findAll();
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Debería eliminar un usuario por ID")
    void shouldDeleteUserById() {
        Usuario savedUser = usuarioRepository.findByUsername("testuser").get();
        usuarioRepository.deleteById(savedUser.getId());

        Optional<Usuario> deletedUser = usuarioRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
        assertEquals(1, usuarioRepository.findAll().size());
    }
}
