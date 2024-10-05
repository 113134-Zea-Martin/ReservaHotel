package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaRequestPostDto;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
public class ReservaController {

    @Autowired
    private ReservaService reservaService;


    @GetMapping("/reserva/{id_reserva}")
    Reserva getReserva(@PathVariable("id_reserva") Long idReserva) {
        return reservaService.getReserva(idReserva);
    }


    @PostMapping("/reserva")
    public ResponseEntity<ReservaDTO> getReserva(@RequestBody ReservaRequestPostDto reservaRequestPostDto) {
        validarReserva(reservaRequestPostDto);
        ReservaDTO reservaDTO = reservaService.createReserva(reservaRequestPostDto);
        return ResponseEntity.ok(reservaDTO);
    }

    public void validarReserva(ReservaRequestPostDto reserva) {
        // Validar que las fechas no sean nulas
        if (reserva.getFechaIngreso() == null || reserva.getFechaSalida() == null) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "Fecha de ingreso nulo");
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las fechas de ingreso y salida son obligatorias");
        }

        // Validar que la fecha de ingreso no sea anterior a la fecha actual
        if (reserva.getFechaIngreso().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de ingreso no puede ser en el pasado");
        }

        // Validar que la fecha de salida sea posterior a la fecha de ingreso
        if (!reserva.getFechaSalida().isAfter(reserva.getFechaIngreso())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha de salida debe ser posterior a la fecha de ingreso");
        }

        // Validar el tipo de habitación (puedes añadir más validaciones aquí según las reglas de negocio)
        if (!esTipoHabitacionValido(reserva.getTipoHabitacion())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de habitación no es válido");
        }

        // Validar el tipo de habitación (puedes añadir más validaciones aquí según las reglas de negocio)
        if (!esIdClientenValido(reserva.getIdCliente())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de cliente no es válido");
        }

        // Validar el tipo de habitación (puedes añadir más validaciones aquí según las reglas de negocio)
        if (!esMedioPagoValido(reserva.getMedioPago())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de pago no es válido");
        }
    }

    // Simular una validación de tipo de habitación (puedes mejorarla con lógica de negocio)
    public boolean esTipoHabitacionValido(String tipoHabitacion) {
        // Aquí puedes agregar lógica para verificar si el tipo de habitación es válido.
        // Por ejemplo, una lista de tipos válidos: "Sencilla", "Doble", "Suite"
        return tipoHabitacion != null && (tipoHabitacion.equals("SIMPLE") || tipoHabitacion.equals("DOBLE") || tipoHabitacion.equals("TRIPLE"));
    }

    // Simular una validación de tipo de habitación (puedes mejorarla con lógica de negocio)
    public boolean esIdClientenValido(String idCliente) {
        // Aquí puedes agregar lógica para verificar si el tipo de habitación es válido.
        // Por ejemplo, una lista de tipos válidos: "Sencilla", "Doble", "Suite"
        return idCliente != null && (idCliente.equals("PASAPORTE") || idCliente.equals("DNI") || idCliente.equals("CUIT"));
    }

    // Simular una validación de tipo de habitación (puedes mejorarla con lógica de negocio)
    public boolean esMedioPagoValido(String medioPago) {
        // Aquí puedes agregar lógica para verificar si el tipo de habitación es válido.
        // Por ejemplo, una lista de tipos válidos: "Sencilla", "Doble", "Suite"
        return medioPago != null && (medioPago.equals("EFECTIVO") || medioPago.equals("TARJETA_DEBITO") || medioPago.equals("TARJETA_CREDITO"));
    }
}