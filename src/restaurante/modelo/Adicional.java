package restaurante.modelo;

/** Ingrediente o extra que el cliente puede agregar a un producto (queso, tocineta, hielo...). */
public class Adicional {

    private final String nombre;
    private final long precio;

    public Adicional(String nombre, long precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public long getPrecio() { return precio; }

    @Override
    public String toString() {
        return nombre + " (+$" + String.format("%,d", precio) + ")";
    }
}
