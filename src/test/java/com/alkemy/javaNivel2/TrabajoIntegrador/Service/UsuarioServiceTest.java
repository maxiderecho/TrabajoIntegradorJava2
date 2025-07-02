package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.RegisterRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.UsuarioResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Exception.ResourceNotFoundException;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Rol;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Usuario;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.RolRepository;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private RegisterRequestDTO registerRequestDTO;
    private Usuario usuario;
    private UsuarioResponseDTO usuarioResponseDTO;
    private Rol userRole;
    private Rol adminRole;
    private Rol moderatorRole;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setUsername("testuser");
        registerRequestDTO.setEmail("test@example.com");
        registerRequestDTO.setPassword("password123");

        userRole = new Rol(null, "ROLE_USER");
        adminRole = new Rol(null, "ROLE_ADMIN");
        moderatorRole = new Rol(null, "ROLE_MODERATOR");

        usuario = new Usuario("1", "testuser", "test@example.com", "encodedPassword", new HashSet<>(Collections.singletonList(userRole)));
        usuarioResponseDTO = new UsuarioResponseDTO("1", "testuser", "test@example.com", Arrays.asList("ROLE_USER"));
    }

    @Test
    @DisplayName("Debería registrar un nuevo usuario con rol por defecto (USER) exitosamente")
    void registerNewUser_shouldRegisterUserWithDefaultRoleSuccessfully() {
        // Configuración de los mocks
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(rolRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(usuarioResponseDTO);

        registerRequestDTO.setRoles(null);

        UsuarioResponseDTO result = usuarioService.registerNewUser(registerRequestDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertTrue(result.getRoles().contains("ROLE_USER"));
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, times(1)).existsByEmail("test@example.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(rolRepository, times(1)).findByName("ROLE_USER");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(modelMapper, times(1)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));
    }

    @Test
    @DisplayName("Debería registrar un nuevo usuario con rol ADMIN exitosamente")
    void registerNewUser_shouldRegisterUserWithAdminRoleSuccessfully() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(rolRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        Usuario adminUsuario = new Usuario("2", "adminuser", "admin@example.com", "encodedPassword", new HashSet<>(Collections.singletonList(adminRole)));
        UsuarioResponseDTO adminUsuarioResponseDTO = new UsuarioResponseDTO("2", "adminuser", "admin@example.com", Arrays.asList("ROLE_ADMIN"));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(adminUsuario);
        when(modelMapper.map(any(Usuario.class), eq(UsuarioResponseDTO.class))).thenReturn(adminUsuarioResponseDTO);

        registerRequestDTO.setUsername("adminuser");
        registerRequestDTO.setEmail("admin@example.com");
        registerRequestDTO.setRoles(new HashSet<>(Collections.singletonList("admin")));

        UsuarioResponseDTO result = usuarioService.registerNewUser(registerRequestDTO);

        assertNotNull(result);
        assertEquals("adminuser", result.getUsername());
        assertTrue(result.getRoles().contains("ROLE_ADMIN"));
        verify(rolRepository, times(1)).findByName("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si el nombre de usuario ya existe")
    void registerNewUser_shouldThrowExceptionIfUsernameExists() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registerNewUser(registerRequestDTO)
        );

        assertEquals("Error: El nombre de usuario ya está en uso!", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, never()).existsByEmail(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar IllegalArgumentException si el email ya existe")
    void registerNewUser_shouldThrowExceptionIfEmailExists() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.registerNewUser(registerRequestDTO)
        );

        assertEquals("Error: El email ya está en uso!", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByUsername("testuser");
        verify(usuarioRepository, times(1)).existsByEmail("test@example.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException si el rol por defecto (USER) no es encontrado")
    void registerNewUser_shouldThrowExceptionIfDefaultRoleNotFound() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(rolRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        registerRequestDTO.setRoles(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                usuarioService.registerNewUser(registerRequestDTO)
        );

        assertEquals("Error: Rol 'ROLE_USER' no encontrado.", exception.getMessage());
        verify(rolRepository, times(1)).findByName("ROLE_USER");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException si el rol ADMIN no es encontrado")
    void registerNewUser_shouldThrowExceptionIfAdminRoleNotFound() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(rolRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());

        registerRequestDTO.setRoles(new HashSet<>(Collections.singletonList("admin")));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                usuarioService.registerNewUser(registerRequestDTO)
        );

        assertEquals("Error: Rol 'ROLE_ADMIN' no encontrado.", exception.getMessage());
        verify(rolRepository, times(1)).findByName("ROLE_ADMIN");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería lanzar ResourceNotFoundException si el rol MODERATOR no es encontrado")
    void registerNewUser_shouldThrowExceptionIfModeratorRoleNotFound() {
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(rolRepository.findByName("ROLE_MODERATOR")).thenReturn(Optional.empty());

        registerRequestDTO.setRoles(new HashSet<>(Collections.singletonList("mod")));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                usuarioService.registerNewUser(registerRequestDTO)
        );

        assertEquals("Error: Rol 'ROLE_MODERATOR' no encontrado.", exception.getMessage());
        verify(rolRepository, times(1)).findByName("ROLE_MODERATOR");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debería obtener todos los usuarios exitosamente")
    void getAllUsers_shouldReturnAllUsersSuccessfully() {
        Usuario usuario2 = new Usuario("2", "anotheruser", "another@example.com", "encodedPass2", new HashSet<>(Collections.singletonList(userRole)));
        UsuarioResponseDTO usuarioResponseDTO2 = new UsuarioResponseDTO("2", "anotheruser", "another@example.com", Arrays.asList("ROLE_USER"));
        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        List<UsuarioResponseDTO> expectedResponse = Arrays.asList(usuarioResponseDTO, usuarioResponseDTO2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);
        when(modelMapper.map(usuario2, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO2);

        List<UsuarioResponseDTO> result = usuarioService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        assertEquals("anotheruser", result.get(1).getUsername());
        verify(usuarioRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Usuario.class), eq(UsuarioResponseDTO.class));
    }

    @Test
    @DisplayName("Debería retornar una lista vacía si no hay usuarios")
    void getAllUsers_shouldReturnEmptyListIfNoUsers() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioResponseDTO> result = usuarioService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(Usuario.class), eq(UsuarioResponseDTO.class));
    }

    @Test
    @DisplayName("Debería obtener un usuario por ID exitosamente")
    void getUserById_shouldReturnUserByIdSuccessfully() {
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioResponseDTO.class)).thenReturn(usuarioResponseDTO);

        Optional<UsuarioResponseDTO> result = usuarioService.getUserById("1");

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        verify(usuarioRepository, times(1)).findById("1");
        verify(modelMapper, times(1)).map(usuario, UsuarioResponseDTO.class);
    }

    @Test
    @DisplayName("Debería retornar vacío si el usuario por ID no es encontrado")
    void getUserById_shouldReturnEmptyIfNotFound() {
        when(usuarioRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        Optional<UsuarioResponseDTO> result = usuarioService.getUserById("nonExistentId");

        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findById("nonExistentId");
        verify(modelMapper, never()).map(any(Usuario.class), eq(UsuarioResponseDTO.class));
    }
}
