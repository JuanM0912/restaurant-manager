package restaurante.persistencia;

/**
 * Representa un elemento serializado o guardado para persistencia en archivos CSV.
 */
public class ElementoGuardado {

    private final String tipo;
    private final String identificador;
    private final String descripcion;
    private final long precio;

    public ElementoGuardado(String tipo, String identificador, String descripcion, long precio) {
        this.tipo = tipo;
        this.identificador = identificador;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public long getPrecio() {
        return precio;
    }
}