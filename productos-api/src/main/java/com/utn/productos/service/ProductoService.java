package com.utn.productos.service;

import com.utn.productos.exception.ProductoNotFoundException;
import com.utn.productos.model.Producto;
import com.utn.productos.model.Categoria;
import com.utn.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import com.utn.productos.dto.ProductoDTO;
import com.utn.productos.dto.ProductoResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<Producto> obtenerPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        existente.setNombre(productoActualizado.getNombre());
        existente.setDescripcion(productoActualizado.getDescripcion());
        existente.setPrecio(productoActualizado.getPrecio());
        existente.setStock(productoActualizado.getStock());
        existente.setCategoria(productoActualizado.getCategoria());

        return productoRepository.save(existente);
    }

    public Producto actualizarStock(Long id, Integer nuevoStock) {
        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        existente.setStock(nuevoStock);
        return productoRepository.save(existente);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto mapToEntity(ProductoDTO dto) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .categoria(dto.getCategoria())
                .build();
    }

    public ProductoResponseDTO mapToResponseDTO(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .categoria(producto.getCategoria())
                .build();
    }

    public List<ProductoResponseDTO> mapToResponseList(List<Producto> productos) {
        return productos.stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

}