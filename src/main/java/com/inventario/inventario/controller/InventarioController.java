package com.inventario.inventario.controller;

import com.inventario.inventario.dto.AlertaStockDTO;
import com.inventario.inventario.dto.CompraRequest;
import com.inventario.inventario.dto.ConsumoRequest;
import com.inventario.inventario.model.Inventario;
import com.inventario.inventario.repository.InventarioRepository;
import com.inventario.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    private final InventarioRepository inventarioRepository;

    @Autowired
    private InventarioService inventarioService;

    InventarioController(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    // 1. REGISTRAR COMPRA (Entrada)
    // Recibe JSON: { "productoId": 1, "cantidad": 5, "precioUnitario": 20.0, "fechaCompra": "2023-11-23" }
    @PostMapping("/comprar")
    public ResponseEntity<Inventario> registrarCompra(@RequestBody CompraRequest request) {
        Inventario nuevoLote = inventarioService.registrarCompra(
                request.getProductoId(),
                request.getCantidad(),
                request.getPrecioUnitario(),
                request.getFechaCompra()
        );
        return ResponseEntity.ok(nuevoLote);
    }

    // 2. CONSUMIR PRODUCTO (Salida FIFO)
    // Recibe JSON: { "productoId": 1, "cantidad": 1 }
    @PostMapping("/consumir")
    public ResponseEntity<String> consumirProducto(@RequestBody ConsumoRequest request) {
        try {
            inventarioService.consumirProducto(request.getProductoId(), request.getCantidad());
            return ResponseEntity.ok("Producto consumido con éxito (Stock actualizado)");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Envía el error si no hay stock
        }
    }
    
    @GetMapping("/alertas")
    public List<AlertaStockDTO> obtenerAlertas() {
        return inventarioRepository.obtenerProductosBajosStock();
    }
}