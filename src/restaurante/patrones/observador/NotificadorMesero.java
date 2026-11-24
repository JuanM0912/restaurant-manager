package restaurante.patrones.observador;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;

/** PATRON OBSERVER - "Observador concreto": al mesero le interesa cuando el pedido esta listo. */
public class NotificadorMesero extends NotificadorBase {

    public NotificadorMesero() { super("MESERO"); }

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        if (nuevo == EstadoPedido.LISTO) {
            avisar("El pedido #" + pedido.getId() + " esta listo. Llevar a la mesa " + pedido.getMesa() + ".");
        }
    }
}
