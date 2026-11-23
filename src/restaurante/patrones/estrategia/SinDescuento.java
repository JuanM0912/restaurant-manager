package restaurante.patrones.estrategia;

import java.time.LocalDateTime;

/** PATRON STRATEGY - "Estrategia concreta": no descuenta nada (valor por defecto). */
public class SinDescuento implements EstrategiaDescuento {

    @Override
    public String getNombre() { return "Sin promocion"; }

    @Override
    public long calcularDescuento(long subtotal, LocalDateTime momento) { return 0; }

    @Override
    public String toString() { return getNombre(); }
}
