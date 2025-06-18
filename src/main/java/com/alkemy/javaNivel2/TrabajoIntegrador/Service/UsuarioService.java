package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.RegisterRequestDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.UsuarioResponseDTO;
import com.alkemy.javaNivel2.TrabajoIntegrador.Exception.ResourceNotFoundException;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Rol;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Usuario;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.RolRepository;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioService(UsuarioRepository userRepository, RolRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usuarioRepository = userRepository;
        this.rolRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UsuarioResponseDTO registerNewUser(RegisterRequestDTO registerRequest) {
        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Error: El nombre de usuario ya está en uso!");
        }

        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Error: El email ya está en uso!");
        }

        Usuario user = new Usuario(
                null,
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                new HashSet<>()
        );

        Set<String> strRoles = registerRequest.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Rol userRole = rolRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Rol 'ROLE_USER' no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(roleName -> {
                switch (roleName) {
                    case "admin":
                        Rol adminRole = rolRepository.findByName("ROLE_ADMIN")
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Rol 'ROLE_ADMIN' no encontrado."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Rol modRole = rolRepository.findByName("ROLE_MODERATOR")
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Rol 'ROLE_MODERATOR' no encontrado."));
                        roles.add(modRole);
                        break;
                    default:
                        Rol userRole = rolRepository.findByName("ROLE_USER")
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Rol 'ROLE_USER' no encontrado."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        Usuario savedUser = usuarioRepository.save(user);

        return modelMapper.map(savedUser, UsuarioResponseDTO.class);
    }

    public List<UsuarioResponseDTO> getAllUsers() {
        return usuarioRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UsuarioResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> getUserById(String id) {
        return usuarioRepository.findById(id)
                .map(user -> modelMapper.map(user, UsuarioResponseDTO.class));
    }
}
