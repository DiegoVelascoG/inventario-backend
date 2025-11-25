package com.inventario.inventario.repository;

import com.inventario.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar productos por su categoría (Ej: Dame todos los de 'Limpieza')
    List<Producto> findByCategoriaId(Long categoriaId);
}