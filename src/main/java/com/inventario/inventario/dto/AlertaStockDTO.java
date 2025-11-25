package com.inventario.inventario.dto;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class AlertaStockDTO {
    private Long productoId;
    private String nombre;
    private Double stockActual;
    private Integer stockMinimo;
    private String unidadMedida;
}