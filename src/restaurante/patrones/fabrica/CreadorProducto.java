package restaurante.patrones.fabrica;

import restaurante.modelo.Producto;

/**
 * PATRON FACTORY METHOD - rol "Creador".
 *
 * Define los pasos comunes para registrar un producto (validar datos) y deja
 * a cada subclase decidir QUE clase concreta se crea, en el metodo crearProducto().
 * Para agregar un tipo nuevo (por ejemplo Postre) se crea una subclase nueva
 * sin modificar esta clase (principio Abierto/Cerrado).
 */
public abstract class CreadorProducto {

    /** El "factory method": cada creador concreto fabrica su propio tipo de producto. */
    protected abstract Producto crearProducto(String codigo, String nombre, String categoria,
                                              long precio, int valorEspecifico);

    /** Tipo que fabrica este creador: "ALIMENTO", "BEBIDA", ... */
    public abstract String getTipo();

    /** Texto que la consola o la ventana muestran para pedir el dato propio del tipo. */
    public abstract String getEtiquetaValorEspecifico();

    /** Operacion comun: valida los datos y luego usa el factory method. */
    public Producto registrar(String codigo, String nombre, String categoria, long precio, int valorEspecifico) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoria es obligatoria.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (valorEspecifico < 0) {
            throw new IllegalArgumentException(getEtiquetaValorEspecifico() + " no puede ser negativo.");
        }
        return crearProducto(codigo, nombre.trim(), categoria.trim(), precio, valorEspecifico);
    }
}
