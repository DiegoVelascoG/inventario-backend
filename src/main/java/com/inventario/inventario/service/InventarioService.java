package com.inventario.inventario.service;

import com.inventario.inventario.model.HistorialConsumo;
import com.inventario.inventario.model.Inventario;
import com.inventario.inventario.model.Producto;
import com.inventario.inventario.repository.HistorialConsumoRepository;
import com.inventario.inventario.repository.InventarioRepository;
import com.inventario.inventario.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private HistorialConsumoRepository historialConsumoRepository;

    // ==========================================
    // 1. REGISTRAR UNA COMPRA (ENTRADA)
    // ==========================================
    @Transactional // Si algo falla, no guarda nada (Seguridad de datos)
    public Inventario registrarCompra(Long productoId, Double cantidad, BigDecimal precioUnitario, LocalDate fechaCompra) {
        
        // 1. Buscamos el producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        // 2. Preparamos el nuevo lote
        Inventario nuevoLote = new Inventario();
        nuevoLote.setProducto(producto);
        nuevoLote.setCantidadInicial(cantidad);
        nuevoLote.setCantidadActual(cantidad); // Al inicio, actual = inicial
        nuevoLote.setPrecioUnitario(precioUnitario);
        
        // Calculamos el total: Precio * Cantidad (Usando BigDecimal para dinero)
        BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        nuevoLote.setPrecioTotal(total);

        nuevoLote.setFechaCompra(fechaCompra);
        nuevoLote.setEstado(Inventario.EstadoInventario.DISPONIBLE);

        // 3. Guardamos en BD
        return inventarioRepository.save(nuevoLote);
    }

    // ==========================================
    // 2. CONSUMIR PRODUCTO (SALIDA FIFO) - LA MAGIA 🪄
    // ==========================================
    @Transactional
    public void consumirProducto(Long productoId, Double cantidadAConsumir) {
        
        // PASO 1: Obtener todos los lotes disponibles ordenados por fecha (EL MÁS VIEJO PRIMERO)
        List<Inventario> lotes = inventarioRepository.findByProductoIdAndEstadoOrderByFechaCompraAsc(
                productoId, Inventario.EstadoInventario.DISPONIBLE);

        // Validar si hay stock total suficiente
        double stockTotal = lotes.stream().mapToDouble(Inventario::getCantidadActual).sum();
        if (stockTotal < cantidadAConsumir) {
            throw new RuntimeException("No hay suficiente stock. Tienes: " + stockTotal + ", intentas consumir: " + cantidadAConsumir);
        }

        double cantidadRestantePorConsumir = cantidadAConsumir;

        // PASO 2: Recorrer los lotes y descontar (Bucle FIFO)
        for (Inventario lote : lotes) {
            
            // Si ya terminamos de consumir lo necesario, rompemos el bucle
            if (cantidadRestantePorConsumir <= 0) break;

            // ¿Cuánto tomamos de este lote?
            // Tomamos lo que necesitemos, O lo que tenga el lote (lo que sea menor)
            double cantidadATomarDeEsteLote = Math.min(lote.getCantidadActual(), cantidadRestantePorConsumir);

            // A. Actualizar el lote
            lote.setCantidadActual(lote.getCantidadActual() - cantidadATomarDeEsteLote);
            
            // B. Registrar en el Historial (Para tus gráficas)
            registrarHistorial(lote, cantidadATomarDeEsteLote);

            // C. Si el lote se quedó en 0, lo cerramos
            if (lote.getCantidadActual() == 0) {
                lote.setEstado(Inventario.EstadoInventario.AGOTADO);
                lote.setFechaTermino(LocalDate.now()); // ¡Aquí guardamos cuándo se acabó!
            }

            // D. Guardar cambios del lote
            inventarioRepository.save(lote);

            // E. Reducir lo que nos falta por consumir
            cantidadRestantePorConsumir -= cantidadATomarDeEsteLote;
        }
    }

    // Método auxiliar privado para guardar el historial
    private void registrarHistorial(Inventario inventario, Double cantidad) {
        HistorialConsumo historial = new HistorialConsumo();
        historial.setInventario(inventario);
        historial.setCantidadConsumida(cantidad);
        historial.setFechaConsumo(LocalDate.now());
        historialConsumoRepository.save(historial);
    }
}