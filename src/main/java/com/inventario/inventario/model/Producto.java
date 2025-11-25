package com.inventario.inventario.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne // Muchos productos pertenecen a una categoría
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Enumerated(EnumType.STRING) // Guarda el texto "LITROS" en la BD, no números
    @Column(name = "unidad_medida")
    private UnidadMedida unidadMedida;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    // Definimos el Enum aquí mismo o en archivo aparte
    public enum UnidadMedida {
        UNIDADES, LITROS, KILOS, GRAMOS, MILILITROS
    }
}