package restaurante.patrones.decorador;

import restaurante.modelo.ElementoPedido;

/**
 * PATRON DECORATOR - rol "Decorador".
 *
 * Envuelve un ElementoPedido (un producto u otro decorador) y le agrega algo.
 * Asi una hamburguesa puede llevar queso, tocineta y "sin cebolla" sin crear
 * una clase por cada combinacion.
 */
public abstract class AdicionalDecorador implements ElementoPedido {

    protected final ElementoPedido envuelto;

    protected AdicionalDecorador(ElementoPedido envuelto) {
        if (envuelto == null) {
            throw new IllegalArgumentException("El decorador necesita un elemento para envolver.");
        }
        this.envuelto = envuelto;
    }

    // Por defecto delega todo al elemento envuelto. Cada decorador concreto cambia lo que necesita.
    @Override
    public String getDescripcion() { return envuelto.getDescripcion(); }

    @Override
    public long getPrecio() { return envuelto.getPrecio(); }
}
