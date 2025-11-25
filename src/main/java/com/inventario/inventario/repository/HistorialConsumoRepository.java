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
}