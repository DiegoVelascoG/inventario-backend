package com.inventario.inventario.service;

import com.inventario.inventario.model.Producto;
import com.inventario.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    
    // Este servirá para cuando filtres en la pantalla de inicio
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // ACTUALIZAR PRODUCTO
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(productoDetalles.getNombre());
        producto.setUnidadMedida(productoDetalles.getUnidadMedida());
        producto.setStockMinimo(productoDetalles.getStockMinimo());
        
        // Si cambian la categoría
        if (productoDetalles.getCategoria() != null) {
            producto.setCategoria(productoDetalles.getCategoria());
        }

        return productoRepository.save(producto);
    }
}