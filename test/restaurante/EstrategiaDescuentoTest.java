package restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import restaurante.modelo.Alimento;
import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;
import restaurante.patrones.estrategia.DescuentoPorDia;
import restaurante.patrones.estrategia.DescuentoPorHorario;
import restaurante.patrones.estrategia.DescuentoPorcentual;
import restaurante.patrones.estrategia.SinDescuento;

/** Pruebas del patron STRATEGY. */
public class EstrategiaDescuentoTest {

    // 2026-11-24 es martes
    private final LocalDateTime martes4pm = LocalDateTime.of(2026, 11, 24, 16, 0);
    private final LocalDateTime martes8pm = LocalDateTime.of(2026, 11, 24, 20, 0);
    private final LocalDateTime miercoles4pm = LocalDateTime.of(2026, 11, 25, 16, 0);

    @Test
    void sinDescuentoDevuelveCero() {
        assertEquals(0, new SinDescuento().calcularDescuento(50000, martes4pm));
    }

    @Test
    void descuentoPorcentual() {
        assertEquals(5000, new DescuentoPorcentual("Frecuente", 10).calcularDescuento(50000, martes4pm));
    }

    @Test
    void descuentoPorHorarioSoloDentroDeLaFranja() {
        DescuentoPorHorario happyHour = new DescuentoPorHorario("Happy hour", 20, LocalTime.of(15, 0), LocalTime.of(17, 0));
        assertEquals(10000, happyHour.calcularDescuento(50000, martes4pm));
        assertEquals(0, happyHour.calcularDescuento(50000, martes8pm));
    }

    @Test
    void descuentoPorDiaSoloElDiaIndicado() {
        DescuentoPorDia martes = new DescuentoPorDia("Martes", 15, DayOfWeek.TUESDAY);
        assertEquals(7500, martes.calcularDescuento(50000, martes8pm));
        assertEquals(0, martes.calcularDescuento(50000, miercoles4pm));
    }

    @Test
    void elPedidoUsaLaEstrategiaSinConocerla() {
        Pedido pedido = new Pedido(1, 5, martes4pm, EstadoPedido.PENDIENTE);
        pedido.agregarItem(new Alimento("P1", "Bandeja", "Platos fuertes", 25000, 20), 2);   // 50000

        assertEquals(50000, pedido.getTotal());                       // por defecto: sin descuento
        pedido.setPromocion(new DescuentoPorcentual("Frecuente", 10));
        assertEquals(45000, pedido.getTotal());                       // cambia la estrategia, no el Pedido
    }
}
