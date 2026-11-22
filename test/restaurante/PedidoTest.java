package restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import restaurante.modelo.Alimento;
import restaurante.modelo.Bebida;
import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;

/** Pruebas de calculos y reglas de estado del Pedido. */
public class PedidoTest {

    private Pedido crearPedido() {
        Pedido p = new Pedido(1, 2);
        p.agregarItem(new Alimento("P1", "Bandeja", "Platos fuertes", 30000, 20), 1);
        p.agregarItem(new Bebida("P2", "Limonada", "Bebidas", 8000, 400), 2);
        return p;
    }

    @Test
    void calculaSubtotalYTotal() {
        Pedido p = crearPedido();
        assertEquals(46000, p.getSubtotal());
        assertEquals(46000, p.getTotal());
    }

    @Test
    void cambiarCantidadYQuitarItem() {
        Pedido p = crearPedido();
        p.cambiarCantidad(1, 3);            // 3 limonadas
        assertEquals(54000, p.getSubtotal());
        p.cambiarCantidad(1, 0);            // 0 elimina el item
        assertEquals(1, p.getItems().size());
    }

    @Test
    void soloAvanzaAlEstadoSiguiente() {
        Pedido p = crearPedido();
        assertThrows(IllegalStateException.class, () -> p.cambiarEstado(EstadoPedido.LISTO));
        p.cambiarEstado(EstadoPedido.EN_PREPARACION);
        assertEquals(EstadoPedido.EN_PREPARACION, p.getEstado());
    }

    @Test
    void noSePuedeModificarUnPedidoQueYaSeEstaPreparando() {
        Pedido p = crearPedido();
        p.cambiarEstado(EstadoPedido.EN_PREPARACION);
        assertThrows(IllegalStateException.class, () -> p.cambiarCantidad(0, 5));
    }
}
