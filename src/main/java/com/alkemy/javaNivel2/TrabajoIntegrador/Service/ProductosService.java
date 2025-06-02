package com.alkemy.javaNivel2.TrabajoIntegrador.Service;

import com.alkemy.javaNivel2.TrabajoIntegrador.Dto.ProductoDto;
import com.alkemy.javaNivel2.TrabajoIntegrador.Model.Producto;
import com.alkemy.javaNivel2.TrabajoIntegrador.Repository.ProductoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductosService {

    private final ProductoRepository productoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductosService(ProductoRepository productoRepository, ModelMapper modelMapper) {
        this.productoRepository = productoRepository;
        this.modelMapper = modelMapper;
    }

    public ProductoDto createProducto(ProductoDto productoDto) {
        Producto producto = modelMapper.map(productoDto, Producto.class);
        Producto savedProducto = productoRepository.save(producto);
        return modelMapper.map(savedProducto, ProductoDto.class);
    }

    public List<ProductoDto> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(producto -> modelMapper.map(producto, ProductoDto.class))
                .collect(Collectors.toList());
    }

    public Optional<ProductoDto> getProductoById(String id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(p -> modelMapper.map(p, ProductoDto.class));
    }

    public Optional<ProductoDto> updateProducto(String id, ProductoDto productoDto) {
        return productoRepository.findById(id).map(existingProducto -> {
            existingProducto.setNombre(productoDto.getNombre());
            existingProducto.setDescripcion(productoDto.getDescripcion());
            existingProducto.setPrecio(productoDto.getPrecio());
            existingProducto.setCategoriaId(productoDto.getCategoriaId()); // Assuming CategoriaId is directly manageable

            Producto updatedProducto = productoRepository.save(existingProducto);
            return modelMapper.map(updatedProducto, ProductoDto.class);
        });
    }

    public boolean deleteProducto(String id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
