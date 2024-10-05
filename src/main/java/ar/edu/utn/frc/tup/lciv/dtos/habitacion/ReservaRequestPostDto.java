package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class ReservaRequestPostDto {
    private Long idHotel;
    private String idCliente;
    private String tipoHabitacion;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String medioPago;
}
