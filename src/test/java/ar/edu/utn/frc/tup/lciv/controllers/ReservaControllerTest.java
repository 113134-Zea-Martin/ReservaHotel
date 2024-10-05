package ar.edu.utn.frc.tup.lciv.controllers;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaRequestPostDto;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.services.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReservaRequestPostDto reservaRequestPostDto;
    private ReservaDTO reservaDTO;

    @BeforeEach
    void setUp() {
        // Inicializa el DTO de solicitud y el DTO de respuesta para las pruebas
        reservaRequestPostDto = new ReservaRequestPostDto();
        reservaRequestPostDto.setIdHotel(1L);
        reservaRequestPostDto.setIdCliente("DNI");
        reservaRequestPostDto.setTipoHabitacion("SIMPLE");
        reservaRequestPostDto.setFechaIngreso(LocalDate.now().plusDays(1));  // Fecha futura
        reservaRequestPostDto.setFechaSalida(LocalDate.now().plusDays(3));   // Fecha posterior
        reservaRequestPostDto.setMedioPago("TARJETA_CREDITO");

        reservaDTO = ReservaDTO.builder()
                .idReserva(1L)
                .idCliente("DNI")
                .idHotel(1L)
                .tipoHabitacion("SIMPLE")
                .fechaIngreso(LocalDate.now().plusDays(1))
                .fechaSalida(LocalDate.now().plusDays(3))
                .precio(new BigDecimal("5000"))
                .medioPago("TARJETA_CREDITO")
                .estadoReserva("CONFIRMADA")
                .build();
    }

    @Test
    void getReservaById() throws Exception {
        // Simula el comportamiento del servicio
        when(reservaService.getReserva(1L)).thenReturn(new Reserva());

        // Realiza una petición GET al endpoint /reserva/{id_reserva}
        mockMvc.perform(get("/reserva/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testPostReserva() throws Exception {
        // Simula el comportamiento del servicio cuando se crea una reserva
        when(reservaService.createReserva(any(ReservaRequestPostDto.class))).thenReturn(reservaDTO);

        // Realiza una petición POST al endpoint /reserva
        mockMvc.perform(post("/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaRequestPostDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reservaDTO)));
    }

    @Test
    void validarReserva() throws Exception {
        // Prueba para una reserva inválida: fechas nulas
        ReservaRequestPostDto invalidReserva = new ReservaRequestPostDto();
        invalidReserva.setIdHotel(1L);
        invalidReserva.setIdCliente("DNI");
        invalidReserva.setTipoHabitacion("SIMPLE");
        invalidReserva.setFechaIngreso(null);  // Fecha nula
        invalidReserva.setFechaSalida(LocalDate.now().plusDays(3));
        invalidReserva.setMedioPago("TARJETA_CREDITO");

        mockMvc.perform(post("/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReserva)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void esTipoHabitacionValido() {
        // Este método lo puedes probar directamente, ya que no necesita interacción HTTP
        ReservaController controller = new ReservaController();
        assertTrue(controller.esTipoHabitacionValido("SIMPLE"));
        assertFalse(controller.esTipoHabitacionValido("INVALIDO"));
    }

    @Test
    void esIdClientenValido() {
        // Similar a esTipoHabitacionValido, se prueba directamente
        ReservaController controller = new ReservaController();
        assertTrue(controller.esIdClientenValido("DNI"));
        assertFalse(controller.esIdClientenValido("INVALIDO"));
    }

    @Test
    void esMedioPagoValido() {
        // Similar a los otros métodos de validación
        ReservaController controller = new ReservaController();
        assertTrue(controller.esMedioPagoValido("TARJETA_CREDITO"));
        assertFalse(controller.esMedioPagoValido("CHEQUE"));
    }
}
