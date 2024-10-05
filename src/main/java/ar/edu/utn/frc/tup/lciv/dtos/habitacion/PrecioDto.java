package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrecioDto {
    private Long idHotel;
    private String tipoHabitacion;
    private BigDecimal precio_lista;
}