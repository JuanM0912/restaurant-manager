package restaurante.patrones.estrategia;

import java.time.LocalDateTime;

/** PATRON STRATEGY - "Estrategia concreta": descuenta un porcentaje siempre. */
public class DescuentoPorcentual implements EstrategiaDescuento {

    private final String nombre;
    private final int porcentaje;

    public DescuentoPorcentual(String nombre, int porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
        }
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    @Override
    public String getNombre() { return nombre + " (" + porcentaje + "%)"; }

    @Override
    public long calcularDescuento(long subtotal, LocalDateTime momento) {
        return subtotal * porcentaje / 100;
    }

    @Override
    public String toString() { return getNombre(); }
}
