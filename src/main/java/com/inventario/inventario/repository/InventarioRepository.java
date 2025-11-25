package com.inventario.inventario.repository;

import com.inventario.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventario.inventario.dto.AlertaStockDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // 1. Para ver cuánto stock total hay de un producto (sumaremos esto en el Servicio)
    // Busca todo el inventario de "Leche" que esté "DISPONIBLE"
    List<Inventario> findByProductoIdAndEstado(Long productoId, Inventario.EstadoInventario estado);

    // 2. LOGICA FIFO (Para consumir):
    // "Dame los lotes de Leche, que estén DISPONIBLES, ordenados del MÁS VIEJO al MÁS NUEVO"
    List<Inventario> findByProductoIdAndEstadoOrderByFechaCompraAsc(Long productoId, Inventario.EstadoInventario estado);
    
    // 3. Para reportes: Buscar compras hechas en un rango de fechas
    List<Inventario> findByFechaCompraBetween(java.time.LocalDate inicio, java.time.LocalDate fin);

    // CONSULTA DE ALERTAS:
    // 1. Selecciona productos.
    // 2. Suma su inventario DISPONIBLE.
    // 3. Compara si la suma es menor o igual al mínimo.
    // 4. OJO: COALESCE(SUM(...), 0) sirve para que si no hay inventario, cuente como 0.
    @Query("SELECT new com.inventario.inventario.dto.AlertaStockDTO(" +
           "p.id, p.nombre, COALESCE(SUM(i.cantidadActual), 0.0), p.stockMinimo, CAST(p.unidadMedida AS string)) " +
           "FROM Producto p " +
           "LEFT JOIN Inventario i ON p.id = i.producto.id AND i.estado = 'DISPONIBLE' " +
           "GROUP BY p.id, p.nombre, p.stockMinimo, p.unidadMedida " +
           "HAVING COALESCE(SUM(i.cantidadActual), 0.0) <= p.stockMinimo")
    List<AlertaStockDTO> obtenerProductosBajosStock();
}