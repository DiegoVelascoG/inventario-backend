package com.inventario.inventario.controller;

import com.inventario.inventario.model.HistorialConsumo;
import com.inventario.inventario.service.HistorialConsumoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.inventario.inventario.dto.ReporteFinancieroDTO;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private HistorialConsumoService historialService;

    // Ejemplo: /api/reportes/mensual?mes=11&anio=2023
    @GetMapping("/mensual")
    public List<HistorialConsumo> obtenerReporteMensual(
            @RequestParam int mes, 
            @RequestParam int anio) {
        return historialService.obtenerConsumoMensual(mes, anio);
    }
    
    // Ver historial de un producto específico
    @GetMapping("/producto/{id}")
    public List<HistorialConsumo> obtenerHistorialProducto(@PathVariable Long id) {
        return historialService.obtenerHistorialPorProducto(id);
    }

    @GetMapping("/financiero")
    public ReporteFinancieroDTO obtenerResumenFinanciero(
            @RequestParam int mes, 
            @RequestParam int anio) {
        return historialService.obtenerResumenFinanciero(mes, anio);
    }
}