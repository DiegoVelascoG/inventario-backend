package com.inventario.inventario.service;

import com.inventario.inventario.dto.ReporteFinancieroDTO; // Asegúrate que coincida tu paquete
import com.inventario.inventario.model.HistorialConsumo;
import com.inventario.inventario.repository.HistorialConsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal; // <--- ¡ESTA ERA LA QUE FALTABA!
import java.time.LocalDate;
import java.util.List;

@Service
public class HistorialConsumoService {

    @Autowired
    private HistorialConsumoRepository historialRepo;

    // 1. Para tu gráfica: "¿Qué consumí este mes?"
    public List<HistorialConsumo> obtenerConsumoMensual(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        
        return historialRepo.findByFechaConsumoBetween(inicio, fin);
    }

    // 2. Para analizar un producto
    public List<HistorialConsumo> obtenerHistorialPorProducto(Long productoId) {
        return historialRepo.findByInventarioProductoId(productoId);
    }
    
    // 3. Obtener todo
    public List<HistorialConsumo> listarTodo() {
        return historialRepo.findAll();
    }

    // 4. NUEVO: Resumen Financiero (El que daba error)
    public ReporteFinancieroDTO obtenerResumenFinanciero(int mes, int anio) {
        // A. Calcular Gasto del Mes Actual
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());
        
        // Usamos BigDecimal para dinero
        BigDecimal gastoMes = historialRepo.calcularGastoEnRango(inicioMes, finMes);

        // B. Calcular Promedio Mensual
        BigDecimal gastoTotal = historialRepo.calcularGastoTotalHistorico();
        LocalDate primerConsumo = historialRepo.obtenerPrimeraFechaConsumo();
        
        BigDecimal promedio = BigDecimal.ZERO;
        
        if (primerConsumo != null) {
            long mesesActivo = java.time.temporal.ChronoUnit.MONTHS.between(
                primerConsumo.withDayOfMonth(1), 
                LocalDate.now().withDayOfMonth(1)
            ) + 1; 
            
            if (mesesActivo > 0) {
                // División segura con redondeo
                promedio = gastoTotal.divide(BigDecimal.valueOf(mesesActivo), 2, java.math.RoundingMode.HALF_UP);
            } else {
                promedio = gastoTotal;
            }
        }

        return new ReporteFinancieroDTO(gastoMes, promedio);
    }
}