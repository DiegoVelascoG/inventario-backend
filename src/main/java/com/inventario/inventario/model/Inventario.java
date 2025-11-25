package com.inventario.inventario.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "cantidad_inicial")
    private Double cantidadInicial;

    @Column(name = "cantidad_actual")
    private Double cantidadActual;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario; // BigDecimal para dinero siempre

    @Column(name = "precio_total")
    private BigDecimal precioTotal;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    @Enumerated(EnumType.STRING)
    private EstadoInventario estado;

    public enum EstadoInventario {
        DISPONIBLE, AGOTADO
    }
}