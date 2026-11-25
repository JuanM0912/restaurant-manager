package restaurante.patrones.decorador;

import restaurante.modelo.ElementoPedido;

/** PATRON DECORATOR - "Decorador concreto": nota para cocina ("sin cebolla"). No cambia el precio. */
public class ConIndicacion extends AdicionalDecorador {

    private final String indicacion;

    public ConIndicacion(ElementoPedido envuelto, String indicacion) {
        super(envuelto);
        this.indicacion = indicacion;
    }

    @Override
    public String getDescripcion() {
        return envuelto.getDescripcion() + " [" + indicacion + "]";
    }
    // getPrecio() no se sobrescribe: se hereda y devuelve el mismo precio del envuelto.
}
