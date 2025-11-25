package com.inventario.inventario.repository;

import com.inventario.inventario.model.HistorialConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // <--- FALTABA ESTE
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // <--- FALTABA ESTE
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistorialConsumoRepository extends JpaRepository<HistorialConsumo, Long> {

    // Para reportes: "Dame todo lo que consumí en Octubre"
    List<HistorialConsumo> findByFechaConsumoBetween(LocalDate inicio, LocalDate fin);
    
    // Para ver el historial de un producto específico
    List<HistorialConsumo> findByInventarioProductoId(Long productoId);

    // --- MÉTODOS FINANCIEROS ---

    // 1. Calcular gasto en un rango de fechas
    // (Cantidad * Precio Unitario)
    @Query("SELECT COALESCE(SUM(h.cantidadConsumida * i.precioUnitario), 0) " +
           "FROM HistorialConsumo h " +
           "JOIN h.inventario i " +
           "WHERE h.fechaConsumo BETWEEN :inicio AND :fin")
    BigDecimal calcularGastoEnRango(LocalDate inicio, LocalDate fin);

    // 2. Calcular Gasto TOTAL Histórico
    @Query("SELECT COALESCE(SUM(h.cantidadConsumida * i.precioUnitario), 0) " +
           "FROM HistorialConsumo h JOIN h.inventario i")
    BigDecimal calcularGastoTotalHistorico();
    
    // 3. Obtener la fecha del primer consumo
    @Query("SELECT MIN(h.fechaConsumo) FROM HistorialConsumo h")
    LocalDate obtenerPrimeraFechaConsumo();
}