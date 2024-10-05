package ar.edu.utn.frc.tup.lciv.services.Impl;

import ar.edu.utn.frc.tup.lciv.dtos.habitacion.DisponibilidadDto;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.PrecioDto;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaDTO;
import ar.edu.utn.frc.tup.lciv.dtos.habitacion.ReservaRequestPostDto;
import ar.edu.utn.frc.tup.lciv.entities.ReservaEntity;
import ar.edu.utn.frc.tup.lciv.models.Reserva;
import ar.edu.utn.frc.tup.lciv.respositories.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservaServiceImplTest {

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ModelMapper modelMapper;

    private ReservaRequestPostDto reservaRequestPostDto;
    private DisponibilidadDto disponibilidadDto;
    private PrecioDto precioDto;
    private ReservaEntity reservaEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar un ejemplo de ReservaRequestPostDto
        reservaRequestPostDto = new ReservaRequestPostDto();
        reservaRequestPostDto.setIdHotel(1L);
        reservaRequestPostDto.setIdCliente("12345678");
        reservaRequestPostDto.setTipoHabitacion("DOBLE");
        reservaRequestPostDto.setFechaIngreso(LocalDate.of(2024, 10, 10));
        reservaRequestPostDto.setFechaSalida(LocalDate.of(2024, 10, 15));
        reservaRequestPostDto.setMedioPago("EFECTIVO");

        // Configurar un ejemplo de DisponibilidadDto
        disponibilidadDto = new DisponibilidadDto();
        disponibilidadDto.setTipo_habitacion("DOBLE");
        disponibilidadDto.setHotel_id(1L);
        disponibilidadDto.setFecha_desde(LocalDate.of(2024, 10, 10));
        disponibilidadDto.setFecha_hasta(LocalDate.of(2024, 10, 15));
        disponibilidadDto.setDisponible(true);

        // Configurar un ejemplo de PrecioDto
        precioDto = new PrecioDto();
        precioDto.setIdHotel(1L);
        precioDto.setTipoHabitacion("DOBLE");
        precioDto.setPrecio_lista(new BigDecimal("200"));

        // Configurar un ejemplo de ReservaEntity
        reservaEntity = new ReservaEntity();
        reservaEntity.setIdReserva(1L);
        reservaEntity.setIdCliente("12345678");
        reservaEntity.setIdHotel(1L);
        reservaEntity.setTipoHabitacion("DOBLE");
        reservaEntity.setFechaIngreso(LocalDate.of(2024, 10, 10));
        reservaEntity.setFechaSalida(LocalDate.of(2024, 10, 15));
        reservaEntity.setEstadoReserva("EXITOSA");
        reservaEntity.setPrecio(new BigDecimal("150"));
        reservaEntity.setMedioPago("EFECTIVO");
    }


    @Test
    void createReserva() {
        // Preparar los mocks
        when(restTemplate.getForEntity(any(String.class), eq(DisponibilidadDto.class))).thenReturn(ResponseEntity.ok(disponibilidadDto));
        when(restTemplate.getForEntity(any(String.class), eq(PrecioDto.class))).thenReturn(ResponseEntity.ok(precioDto));
        when(modelMapper.map(any(Reserva.class), eq(ReservaEntity.class))).thenReturn(reservaEntity);
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reservaEntity);
        when(modelMapper.map(any(ReservaEntity.class), eq(ReservaDTO.class))).thenReturn(new ReservaDTO());

        // Llamar al método
        ReservaDTO resultado = reservaService.createReserva(reservaRequestPostDto);

        // Verificar resultados
        assertNotNull(resultado);
        assertEquals("DOBLE", resultado.getTipoHabitacion());
        assertEquals("EXITOSA", resultado.getEstadoReserva());
        verify(reservaRepository, times(1)).save(any(ReservaEntity.class));
    }



    @Test
    void determinarTemporada() {
        // Test para temporada alta
        assertEquals(new BigDecimal(1.3), reservaService.determinarTemporada(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 28)));

        // Test para temporada media
        assertEquals(new BigDecimal(1), reservaService.determinarTemporada(LocalDate.of(2024, 6, 1), LocalDate.of(2024, 6, 30)));

        // Test para temporada baja
        assertEquals(new BigDecimal(0.9), reservaService.determinarTemporada(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 4, 30)));

        // Test sin temporada
        assertEquals(new BigDecimal(0), reservaService.determinarTemporada(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 15)));
    }

    @Test
    void esMesTemporadaAlta() {
        assertTrue(reservaService.esMesTemporadaAlta(1)); // Enero
        assertTrue(reservaService.esMesTemporadaAlta(2)); // Febrero
        assertTrue(reservaService.esMesTemporadaAlta(7)); // Julio
        assertTrue(reservaService.esMesTemporadaAlta(8)); // Agosto
        assertFalse(reservaService.esMesTemporadaAlta(3)); // Marzo
    }

    @Test
    void esMesTemporadaMedia() {
        assertTrue(reservaService.esMesTemporadaMedia(6)); // Junio
        assertTrue(reservaService.esMesTemporadaMedia(9)); // Septiembre
        assertTrue(reservaService.esMesTemporadaMedia(12)); // Diciembre
        assertFalse(reservaService.esMesTemporadaMedia(1)); // Enero
    }

    @Test
    void esMesTemporadaBaja() {
        assertTrue(reservaService.esMesTemporadaBaja(3)); // Marzo
        assertTrue(reservaService.esMesTemporadaBaja(4)); // Abril
        assertTrue(reservaService.esMesTemporadaBaja(10)); // Octubre
        assertTrue(reservaService.esMesTemporadaBaja(11)); // Noviembre
        assertFalse(reservaService.esMesTemporadaBaja(1)); // Enero
    }

    /*
    @Test
    void getReserva() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaEntity));
        Reserva reserva = new Reserva();
        reserva.setIdReserva(1L);
        when(modelMapper.map(reservaEntity, Reserva.class)).thenReturn(reserva);

        Reserva resultado = reservaService.getReserva(1L);

        assertNotNull(resultado);
        assertEquals(reservaEntity.getIdReserva(), resultado.getIdReserva());
    }

     */
}
