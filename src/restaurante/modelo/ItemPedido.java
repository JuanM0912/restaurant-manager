package restaurante.modelo;

/** Una linea del pedido: que se pidio y cuantas unidades. */
public class ItemPedido {

    private final ElementoPedido elemento;
    private int cantidad;

    public ItemPedido(ElementoPedido elemento, int cantidad) {
        this.elemento = elemento;
        setCantidad(cantidad);
    }

    public ElementoPedido getElemento() { return elemento; }
    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.cantidad = cantidad;
    }

    public long getSubtotal() {
        return elemento.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return String.format("%d x %s  ($%,d c/u)  = $%,d",
                cantidad, elemento.getDescripcion(), elemento.getPrecio(), getSubtotal());
    }
}
