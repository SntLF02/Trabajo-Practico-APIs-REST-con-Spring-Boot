package com.utn.productos.controller;

import com.utn.productos.dto.ProductoDTO;
import com.utn.productos.dto.ProductoResponseDTO;
import com.utn.productos.dto.ActualizarStockDTO;
import com.utn.productos.model.Categoria;
import com.utn.productos.model.Producto;
import com.utn.productos.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Obtener todos los productos", description = "Devuelve la lista completa de productos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(productoService.mapToResponseList(productoService.obtenerTodos()));
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto según su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerPorId(id)
                .map(producto -> ResponseEntity.ok(productoService.mapToResponseDTO(producto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponse(responseCode = "201", description = "Producto creado correctamente")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoDTO dto) {
        Producto producto = productoService.mapToEntity(dto);
        Producto creado = productoService.crearProducto(producto);
        return ResponseEntity.ok(productoService.mapToResponseDTO(creado));
    }

    @Operation(summary = "Actualizar un producto", description = "Modifica todos los datos de un producto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        Producto actualizado = productoService.actualizarProducto(id, productoService.mapToEntity(dto));
        return ResponseEntity.ok(productoService.mapToResponseDTO(actualizado));
    }

    @Operation(summary = "Actualizar solo el stock", description = "Modifica únicamente la cantidad disponible de un producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponseDTO> actualizarStock(@PathVariable Long id, @RequestBody ActualizarStockDTO dto) {
        Producto actualizado = productoService.actualizarStock(id, dto.getStock());
        return ResponseEntity.ok(productoService.mapToResponseDTO(actualizado));
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener productos por categoría", description = "Devuelve todos los productos que pertenecen a una categoría específica")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerPorCategoria(@PathVariable Categoria categoria) {
        List<Producto> productos = productoService.obtenerPorCategoria(categoria);
        return ResponseEntity.ok(productoService.mapToResponseList(productos));
    }
}
