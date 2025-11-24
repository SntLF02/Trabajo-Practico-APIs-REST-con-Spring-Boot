package com.utn.productos.controller;

import com.utn.productos.dto.ProductoDTO;
import com.utn.productos.dto.ProductoResponseDTO;
import com.utn.productos.dto.ActualizarStockDTO;
import com.utn.productos.model.Categoria;
import com.utn.productos.model.Producto;
import com.utn.productos.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        List<ProductoResponseDTO> productos =
                productoService.mapToResponseList(productoService.obtenerTodos());

        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(productoService.mapToResponseDTO(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerPorCategoria(@PathVariable Categoria categoria) {

        List<ProductoResponseDTO> productos =
                productoService.mapToResponseList(productoService.obtenerPorCategoria(categoria));

        return ResponseEntity.ok(productos);
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoDTO dto) {

        // convertir DTO → entidad
        Producto producto = productoService.mapToEntity(dto);

        // guardar entidad en DB
        Producto guardado = productoService.crearProducto(producto);

        // convertir entidad guardada → ResponseDTO
        ProductoResponseDTO response = productoService.mapToResponseDTO(guardado);

        // construir la URI del recurso creado
        URI uri = URI.create("/api/productos/" + guardado.getId());

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO dto
    ) {
        // convertir DTO → entidad
        Producto productoActualizado = productoService.mapToEntity(dto);

        // actualizar en DB
        Producto actualizado = productoService.actualizarProducto(id, productoActualizado);

        // convertir entidad → DTO de respuesta
        ProductoResponseDTO response = productoService.mapToResponseDTO(actualizado);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarStockDTO dto
    ) {
        // Actualizar en base de datos
        Producto actualizado = productoService.actualizarStock(id, dto.getStock());

        // Convertir a DTO de respuesta
        ProductoResponseDTO response = productoService.mapToResponseDTO(actualizado);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build(); // 204
    }

}
