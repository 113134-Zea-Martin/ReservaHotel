package ar.edu.utn.frc.tup.lciv.services;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaRequestPostDto;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import org.springframework.stereotype.Service;

@Service
public interface ReservaService {

    ReservaDTO createReserva(ReservaRequestPostDto reserva);

    Reserva getReserva(Long idReserva);

}
