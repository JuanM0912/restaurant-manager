package restaurante.modelo;

/**
 * Producto del menu. Es abstracta porque nunca se crea un "producto" generico:
 * siempre es un Alimento, una Bebida u otro tipo concreto.
 */
public abstract class Producto implements ElementoPedido {

    private final String codigo;
    private final String nombre;
    private final String categoria;   // Ej: "Platos fuertes", "Jugos". Es texto: se pueden crear categorias nuevas sin tocar codigo.
    private long precio;
    private boolean disponible;

    protected Producto(String codigo, String nombre, String categoria, long precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.disponible = true;
    }

    /** Tipo del producto: "ALIMENTO", "BEBIDA", ... */
    public abstract String getTipo();

    /** Dato propio de cada tipo (minutos de preparacion, mililitros, ...). */
    public abstract int getValorEspecifico();

    /** Texto corto que explica el dato propio, por ejemplo "20 min". */
    public abstract String getDetalle();

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }

    @Override
    public long getPrecio() { return precio; }

    @Override
    public String getDescripcion() { return nombre; }

    public void setPrecio(long precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        this.precio = precio;
    }

    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return String.format("%s | %-25s | %-15s | $%,d | %s%s",
                codigo, nombre, categoria, precio, getDetalle(), disponible ? "" : " | AGOTADO");
    }
}
