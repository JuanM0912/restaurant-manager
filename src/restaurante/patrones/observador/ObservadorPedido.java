package restaurante.patrones.observador;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;

/**
 * PATRON OBSERVER - rol "Observador".
 *
 * Cualquier clase que quiera enterarse de los cambios de un pedido
 * (cocina, caja, mesero, una pantalla) implementa esta interfaz y se
 * registra en ServicioPedidos, que es el "Sujeto".
 */
public interface ObservadorPedido {

    /**
     * @param pedido   pedido que cambio
     * @param anterior estado anterior; es null cuando el pedido es nuevo
     * @param nuevo    estado actual
     */
    void estadoCambiado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo);
}
