package ar.edu.utn.frc.tup.lciv.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class Reserva {
    private Long idReserva;
    private String idCliente;
    private Long idHotel;
    private String tipoHabitacion;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
    private String estadoReserva;
    private BigDecimal precio;
    private String medioPago;
}