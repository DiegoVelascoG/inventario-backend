package com.inventario.inventario.repository;

import com.inventario.inventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // 1. Buscar por nombre exacto
    // Útil para validaciones: Si vas a crear una categoría, revisa si ya existe.
    Optional<Categoria> findByNombre(String nombre);

    // 2. Verificar si existe (Devuelve true o false)
    // Más rápido que el anterior si solo quieres saber si el nombre está ocupado.
    boolean existsByNombre(String nombre);
}