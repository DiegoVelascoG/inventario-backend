package com.inventario.inventario.dto;
import lombok.Data;

@Data
public class ConsumoRequest {
    private Long productoId;
    private Double cantidad;
}