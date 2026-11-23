package restaurante.modelo;

/**
 * Estados de un pedido, en orden. Un pedido solo puede avanzar al estado siguiente:
 * PENDIENTE -> EN_PREPARACION -> LISTO -> ENTREGADO.
 */
public enum EstadoPedido {
    PENDIENTE("Pendiente"),
    EN_PREPARACION("En preparacion"),
    LISTO("Listo"),
    ENTREGADO("Entregado");

    private final String nombreVisible;

    EstadoPedido(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    /** Devuelve el estado que sigue, o null si ya es el ultimo. */
    public EstadoPedido siguiente() {
        int posicion = this.ordinal() + 1;
        EstadoPedido[] todos = values();
        return posicion < todos.length ? todos[posicion] : null;
    }

    public boolean puedeCambiarA(EstadoPedido nuevo) {
        return nuevo != null && nuevo == siguiente();
    }

    @Override
    public String toString() { return nombreVisible; }
}
