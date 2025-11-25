package com.inventario.inventario.model;

import jakarta.persistence.*;
import lombok.Data;

@Data // Genera getters, setters, toString, etc.
@Entity
@Table(name = "categorias") // Vincula esta clase con la tabla 'categorias'
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Vincula con la columna 'nombre'
    // nullable = false significa que Java no dejará guardar si esto va vacío (igual que tu NOT NULL en SQL)
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    // Vincula con la columna 'descripcion'
    @Column(name = "descripcion", length = 120)
    private String descripcion;

    // Vincula con 'icono'. Si no mandas nada, SQL pondrá 'default-icon', 
    // pero aquí mapeamos el campo para poder leerlo y escribirlo.
    @Column(name = "icono", length = 50)
    private String icono;

    // OJO AQUÍ: 
    // En Java usamos 'colorHex' (camelCase)
    // En la BD se llama 'color_hex' (snake_case)
    // La anotación @Column hace el puente entre los dos nombres.
    @Column(name = "color_hex", length = 7)
    private String colorHex;
}