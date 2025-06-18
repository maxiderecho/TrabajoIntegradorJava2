package com.alkemy.javaNivel2.TrabajoIntegrador.Dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private String id;
    private String username;
    private String email;
    private List<String> roles;
}
