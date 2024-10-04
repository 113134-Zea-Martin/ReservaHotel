package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservaController {


    @GetMapping("/reserva/{id_reserva}")
    ReservaDTO getReserva(@PathVariable("id_reserva") Long idReserva) {
        return ReservaDTO.builder().idReserva(idReserva).build();
    }


    @PostMapping("/reserva")
    ReservaDTO getReserva(@RequestBody ReservaDTO reservaDTO) {
        return reservaDTO;
    }
}
