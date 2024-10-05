package ar.edu.utn.frc.tup.lciv.services.Impl;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.DisponibilidadDto;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.PrecioDto;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaRequestPostDto;
import ar.edu.utn.frc.tup.lciv.entities.ReservaEntity;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.respositories.ReservaRepository;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;


@Service
public class ReservaServiceImpl implements ReservaService {

    //private final String URL = "http://localhost:8080/habitacion/";
    private final String URL = "http://java-api-hotel:8080/habitacion/";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ReservaRepository reservaRepository;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public ReservaDTO createReserva(ReservaRequestPostDto reservaRequestPostDto) {
        ReservaDTO reservaDTO = new ReservaDTO();
        ReservaDTO reservaDTO1 = new ReservaDTO();

        DisponibilidadDto disponibilidadDto = restTemplate.getForEntity(URL +
                                "disponibilidad?hotel_id=" + reservaRequestPostDto.getIdHotel() +
                                "&tipo_habitacion=" + reservaRequestPostDto.getTipoHabitacion() +
                                "&fecha_desde=" + reservaRequestPostDto.getFechaIngreso() +
                                "&fecha_hasta=" + reservaRequestPostDto.getFechaSalida()
                        , DisponibilidadDto.class)
                .getBody();

        if (disponibilidadDto.isDisponible()) {

            PrecioDto precioDto = restTemplate.getForEntity(URL + "precio?hotel_id=" + disponibilidadDto.getHotel_id()
                            + "&tipo_habitacion=" + disponibilidadDto.getTipo_habitacion()
                    , PrecioDto.class).getBody();

            BigDecimal precio = precioDto.getPrecio_lista().multiply(determinarTemporada(disponibilidadDto.getFecha_desde(), disponibilidadDto.getFecha_hasta()));

            if (reservaRequestPostDto.getMedioPago() == "EFECTIVO") {
                precio = precio.multiply(new BigDecimal(0.75));
            } else if (reservaRequestPostDto.getMedioPago() == "TARJETA_DEBITO") {
                precio = precio.multiply(new BigDecimal(0.90));
            }

            if (reservaRequestPostDto.getIdCliente() == "CUIT") {

                if (determinarTemporada(disponibilidadDto.getFecha_desde(), disponibilidadDto.getFecha_hasta()) == new BigDecimal(0.9)) {
                    precio = precio.multiply(new BigDecimal(0.85));
                } else {
                    precio = precio.multiply(new BigDecimal(0.9));
                }

            } else {
                if (determinarTemporada(disponibilidadDto.getFecha_desde(), disponibilidadDto.getFecha_hasta()) == new BigDecimal(0.9)) {
                    precio = precio.multiply(new BigDecimal(0.9));
                }
            }

            reservaDTO.setIdHotel(reservaRequestPostDto.getIdHotel());
            reservaDTO.setIdCliente(reservaRequestPostDto.getIdCliente());
            reservaDTO.setTipoHabitacion(reservaRequestPostDto.getTipoHabitacion());
            reservaDTO.setFechaIngreso(reservaRequestPostDto.getFechaIngreso());
            reservaDTO.setFechaSalida(reservaRequestPostDto.getFechaSalida());
            reservaDTO.setPrecio(precio);

            Reserva reserva = new Reserva();
            reserva.setIdCliente(reservaDTO.getIdCliente());
            reserva.setIdHotel(reservaDTO.getIdHotel());
            reserva.setTipoHabitacion(reservaDTO.getTipoHabitacion());
            reserva.setFechaIngreso(reservaDTO.getFechaIngreso());
            reserva.setFechaSalida(reservaDTO.getFechaSalida());
            reserva.setEstadoReserva("EXITOSA");
            reserva.setPrecio(precio);
            reserva.setMedioPago(reservaRequestPostDto.getMedioPago());

            ReservaEntity re = modelMapper.map(reserva, ReservaEntity.class);
            reservaRepository.save(re);
            reservaDTO1 = modelMapper.map(re, ReservaDTO.class);

        }

        return reservaDTO1;
    }


    public BigDecimal determinarTemporada(LocalDate fechaDesde, LocalDate fechaHasta) {
        // Variables para verificar las temporadas
        boolean esTemporadaAlta = false;
        boolean esTemporadaMedia = false;
        boolean esTemporadaBaja = false;

        // Inicializar un calendario
        Calendar cal = Calendar.getInstance();

        // Iterar sobre las fechas desde 'fechaDesde' hasta 'fechaHasta'
        LocalDate fechaActual = fechaDesde;
        while (!fechaActual.isAfter(fechaHasta)) {
            int mes = fechaActual.getMonthValue(); // Obtener el mes de LocalDate (1-12)

            if (esMesTemporadaAlta(mes)) {
                esTemporadaAlta = true;
            } else if (esMesTemporadaMedia(mes)) {
                esTemporadaMedia = true;
            } else if (esMesTemporadaBaja(mes)) {
                esTemporadaBaja = true;
            }

            // Avanzar la fecha en un día
            fechaActual = fechaActual.plusDays(1);
        }

        // Determinar la temporada de mayor impacto
        if (esTemporadaAlta) {
            return new BigDecimal(1.3);
        } else if (esTemporadaMedia) {
            return new BigDecimal(1);
        } else if (esTemporadaBaja) {
            return new BigDecimal(0.9);
        }

        return new BigDecimal(0);
    }

    public boolean esMesTemporadaAlta(int mes) {
        // Temporada alta: enero (1), febrero (2), julio (7), agosto (8)
        return mes == 1 || mes == 2 || mes == 7 || mes == 8;
    }

    public boolean esMesTemporadaMedia(int mes) {
        // Temporada media: junio (6), septiembre (9), diciembre (12)
        return mes == 6 || mes == 9 || mes == 12;
    }

    public boolean esMesTemporadaBaja(int mes) {
        // Temporada baja: marzo (3), abril (4), octubre (10), noviembre (11)
        return mes == 3 || mes == 4 || mes == 10 || mes == 11;
    }


    @Override
    public Reserva getReserva(Long idReserva) {
        Optional<ReservaEntity> reservaEntity = reservaRepository.findById(idReserva);
        return modelMapper.map(reservaEntity, Reserva.class);
    }
}
