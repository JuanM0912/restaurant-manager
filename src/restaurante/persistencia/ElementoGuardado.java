package restaurante.persistencia;

import restaurante.modelo.ElementoPedido;

/**
 * Item de un pedido leido desde el CSV. Ya no es un producto con decoradores,
 * solo guarda la descripcion y el precio que tenia cuando se vendio.
 * Asi, si manana cambia el precio del menu, los pedidos viejos no cambian.
 */
public class ElementoGuardado implements ElementoPedido {

    private final String descripcion;
    private final long precio;

    public ElementoGuardado(String descripcion, long precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    @Override
    public String getDescripcion() { return descripcion; }

    @Override
    public long getPrecio() { return precio; }
}
