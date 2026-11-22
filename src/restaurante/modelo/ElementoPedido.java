package restaurante.modelo;

/**
 * Todo lo que se puede poner en un pedido: un producto solo
 * o un producto con adicionales (ver patron Decorator).
 */
public interface ElementoPedido {

    /** Texto que se muestra al cliente, por ejemplo "Hamburguesa + Queso extra". */
    String getDescripcion();

    /** Precio unitario en pesos. */
    long getPrecio();
}
