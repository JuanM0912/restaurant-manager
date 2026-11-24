package restaurante.patrones.observador;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;

/** PATRON OBSERVER - "Observador concreto": a cocina le interesan los pedidos nuevos. */
public class NotificadorCocina extends NotificadorBase {

    public NotificadorCocina() { super("COCINA"); }

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        if (nuevo == EstadoPedido.PENDIENTE) {
            avisar("Nuevo pedido #" + pedido.getId() + " de la mesa " + pedido.getMesa()
                    + " (" + pedido.getItems().size() + " items).");
        }
    }
}
