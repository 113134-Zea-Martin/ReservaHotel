package ar.edu.utn.frc.tup.lciv.dtos.habitacion;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({
        "id_reserva",
        "id_cliente",
        "id_hotel",
        "tipo_habitacion",
        "fecha_ingreso",
        "fecha_salida",
        "estado_reserva",
        "precio",
        "medio_pago"
})
public class ReservaDTO {

    @JsonProperty("id_reserva")
    private Long idReserva;

    @JsonProperty("id_cliente")
    private String idCliente;

    @JsonProperty("id_hotel")
    private Long idHotel;

    @JsonProperty("tipo_habitacion")
    private String tipoHabitacion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("fecha_ingreso")
    private LocalDate fechaIngreso;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("fecha_salida")
    private LocalDate fechaSalida;

    @JsonProperty("estado_reserva")
    private String estadoReserva;

    @JsonProperty("medio_pago")
    private String medioPago;

    private BigDecimal precio;
}
