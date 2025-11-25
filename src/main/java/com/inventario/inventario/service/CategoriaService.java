package com.inventario.inventario.service;

import com.inventario.inventario.model.Categoria;
import com.inventario.inventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Obtener todas para mostrarlas en el select de la App
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    // Crear una nueva (Ej: Usuario escribe "Mascotas")
    public Categoria guardarCategoria(Categoria categoria) {
        // Aquí podrías validar si ya existe con el método que creamos antes
        if(categoriaRepository.existsByNombre(categoria.getNombre())){
             return null; // O lanzar excepción
        }
        return categoriaRepository.save(categoria);
    }

    // ACTUALIZAR CATEGORÍA
    public Categoria actualizarCategoria(Long id, Categoria categoriaDetalles) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(categoriaDetalles.getNombre());
        categoria.setDescripcion(categoriaDetalles.getDescripcion());
        // Si mandan icono/color, los actualizamos
        if(categoriaDetalles.getIcono() != null) categoria.setIcono(categoriaDetalles.getIcono());
        if(categoriaDetalles.getColorHex() != null) categoria.setColorHex(categoriaDetalles.getColorHex());

        return categoriaRepository.save(categoria);
    }
}