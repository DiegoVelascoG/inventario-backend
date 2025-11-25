package com.inventario.inventario.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "historial_consumo")
public class HistorialConsumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    @Column(name = "cantidad_consumida")
    private Double cantidadConsumida;

    @Column(name = "fecha_consumo")
    private LocalDate fechaConsumo;
}