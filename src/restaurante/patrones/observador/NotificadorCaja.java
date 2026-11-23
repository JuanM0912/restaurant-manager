package restaurante.patrones.observador;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;

/** PATRON OBSERVER - "Observador concreto": a caja le interesa cuando el pedido se entrega (para cobrar). */
public class NotificadorCaja extends NotificadorBase {

    public NotificadorCaja() { super("CAJA"); }

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        if (nuevo == EstadoPedido.ENTREGADO) {
            avisar(String.format("Cobrar pedido #%d de la mesa %d: $%,d",
                    pedido.getId(), pedido.getMesa(), pedido.getTotal()));
        }
    }
}
