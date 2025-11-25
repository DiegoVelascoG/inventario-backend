package com.inventario.inventario.repository;

import com.inventario.inventario.model.HistorialConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistorialConsumoRepository extends JpaRepository<HistorialConsumo, Long> {

    // Para reportes: "Dame todo lo que consumí en Octubre"
    List<HistorialConsumo> findByFechaConsumoBetween(LocalDate inicio, LocalDate fin);
    
    // Para ver el historial de un producto específico (Ej: ¿Cuándo me acabé las leches?)
    // Navegamos: Historial -> Inventario -> Producto -> ID
    List<HistorialConsumo> findByInventarioProductoId(Long productoId);

    // Sumar (Cantidad Consumida * Precio Unitario del Inventario) en un rango de fechas
    @Query("SELECT COALESCE(SUM(h.cantidadConsumida * i.precioUnitario), 0) " +
           "FROM HistorialConsumo h " +
           "JOIN h.inventario i " +
           "WHERE h.fechaConsumo BETWEEN :inicio AND :fin")
    BigDecimal calcularGastoEnRango(LocalDate inicio, LocalDate fin);

    // Sumar Gasto TOTAL Histórico (Desde el principio de los tiempos)
    @Query("SELECT COALESCE(SUM(h.cantidadConsumida * i.precioUnitario), 0) " +
           "FROM HistorialConsumo h JOIN h.inventario i")
    BigDecimal calcularGastoTotalHistorico();
    
    // Obtener la fecha del primer consumo (Para saber cuántos meses llevamos)
    @Query("SELECT MIN(h.fechaConsumo) FROM HistorialConsumo h")
    LocalDate obtenerPrimeraFechaConsumo();
}