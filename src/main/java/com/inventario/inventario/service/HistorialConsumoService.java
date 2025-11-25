package com.inventario.inventario.service;

import com.inventario.inventario.model.HistorialConsumo;
import com.inventario.inventario.repository.HistorialConsumoRepository;
import com.inventario.inventario.dto.ReporteFinancieroDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
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

    public ReporteFinancieroDTO obtenerResumenFinanciero(int mes, int anio) {
        // 1. Calcular Gasto del Mes Actual
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());
        BigDecimal gastoMes = historialRepo.calcularGastoEnRango(inicioMes, finMes);

        // 2. Calcular Promedio Mensual
        BigDecimal gastoTotal = historialRepo.calcularGastoTotalHistorico();
        LocalDate primerConsumo = historialRepo.obtenerPrimeraFechaConsumo();
        
        BigDecimal promedio = BigDecimal.ZERO;
        
        if (primerConsumo != null) {
            // Calcular cuántos meses han pasado desde el primer uso hasta hoy
            long mesesActivo = java.time.temporal.ChronoUnit.MONTHS.between(
                primerConsumo.withDayOfMonth(1), 
                LocalDate.now().withDayOfMonth(1)
            ) + 1; // +1 para contar el mes actual aunque no haya terminado
            
            if (mesesActivo > 0) {
                // División con 2 decimales y redondeo HALF_UP
                promedio = gastoTotal.divide(BigDecimal.valueOf(mesesActivo), 2, java.math.RoundingMode.HALF_UP);
            } else {
                promedio = gastoTotal; // Si es el primer mes, el promedio es el total
            }
        }

        return new ReporteFinancieroDTO(gastoMes, promedio);
    }
}