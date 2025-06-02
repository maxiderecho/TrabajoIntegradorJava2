package com.alkemy.javaNivel2.TrabajoIntegrador.Controller;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.ProductoDto;
import com.alkemy.javaNivel2.TrabajoIntegrador.Service.ProductosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    private final ProductosService productoService;

    @Autowired
    public ProductosController(ProductosService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoDto> createProducto(@RequestBody ProductoDto productoDto) {
        ProductoDto createdProducto = productoService.createProducto(productoDto);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> getProductoById(@PathVariable String id) {
        return productoService.getProductoById(id)
                .map(productoDto -> new ResponseEntity<>(productoDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> updateProducto(@PathVariable String id, @RequestBody ProductoDto productoDto) {
        return productoService.updateProducto(id, productoDto)
                .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable String id) {
        if (productoService.deleteProducto(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
