package com.inventario.inventario.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReporteFinancieroDTO {
    private BigDecimal gastoEsteMes;
    private BigDecimal promedioMensual;
}