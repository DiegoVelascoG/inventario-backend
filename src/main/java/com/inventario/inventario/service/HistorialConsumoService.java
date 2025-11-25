package com.inventario.inventario.service;

import com.inventario.inventario.model.HistorialConsumo;
import com.inventario.inventario.repository.HistorialConsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HistorialConsumoService {

    @Autowired
    private HistorialConsumoRepository historialRepo;

    // 1. Para tu gráfica: "¿Qué consumí este mes?"
    public List<HistorialConsumo> obtenerConsumoMensual(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        // El fin es el último día del mes (calculado automático con lengthOfMonth)
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        
        return historialRepo.findByFechaConsumoBetween(inicio, fin);
    }

    // 2. Para analizar un producto: "¿Qué tan rápido me acabo la leche?"
    // Esto te servirá para calcular la estimación de cuándo comprar más
    public List<HistorialConsumo> obtenerHistorialPorProducto(Long productoId) {
        return historialRepo.findByInventarioProductoId(productoId);
    }
    
    // 3. Obtener todo el historial (por si quieres una lista general)
    public List<HistorialConsumo> listarTodo() {
        return historialRepo.findAll();
    }
}