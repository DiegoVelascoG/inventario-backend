package com.inventario.inventario.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CompraRequest {
    private Long productoId;
    private Double cantidad;
    private BigDecimal precioUnitario;
    private LocalDate fechaCompra;
}