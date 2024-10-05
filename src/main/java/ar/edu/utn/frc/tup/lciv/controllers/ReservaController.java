package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaRequestPostDto;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReservaController {

    @Autowired
    private ReservaService reservaService;


    @GetMapping("/reserva/{id_reserva}")
    Reserva getReserva(@PathVariable("id_reserva") Long idReserva) {
        return reservaService.getReserva(idReserva);
    }


    @PostMapping("/reserva")
    ReservaDTO getReserva(@RequestBody ReservaRequestPostDto reservaRequestPostDto) {
        return reservaService.createReserva(reservaRequestPostDto);
    }
}