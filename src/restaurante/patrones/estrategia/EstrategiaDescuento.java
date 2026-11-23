package restaurante.patrones.estrategia;

import java.time.LocalDateTime;

/**
 * PATRON STRATEGY - rol "Estrategia".
 *
 * Cada promocion es una clase que implementa esta interfaz. El Pedido solo
 * llama a calcularDescuento() y no necesita saber que promocion es.
 * Para crear una promocion nueva se agrega una clase, sin tocar Pedido.
 */
public interface EstrategiaDescuento {

    /** Nombre que se muestra al usuario, por ejemplo "Happy hour 15:00-17:00 (20%)". */
    String getNombre();

    /**
     * @param subtotal valor del pedido antes del descuento
     * @param momento  fecha y hora en que se hizo el pedido
     * @return cuantos pesos se descuentan (0 si la promocion no aplica)
     */
    long calcularDescuento(long subtotal, LocalDateTime momento);
}
