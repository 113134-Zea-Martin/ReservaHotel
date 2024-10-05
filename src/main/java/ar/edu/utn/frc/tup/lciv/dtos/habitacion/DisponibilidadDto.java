package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class DisponibilidadDto {

    private String tipo_habitacion;
    private Long hotel_id;
    private LocalDate fecha_desde;
    private LocalDate fecha_hasta;
    private boolean disponible;
}
