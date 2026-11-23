package restaurante.patrones.estrategia;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

/** PATRON STRATEGY - "Estrategia concreta": descuento solo un dia de la semana. */
public class DescuentoPorDia implements EstrategiaDescuento {

    private final String nombre;
    private final int porcentaje;
    private final DayOfWeek dia;

    public DescuentoPorDia(String nombre, int porcentaje, DayOfWeek dia) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.dia = dia;
    }

    @Override
    public String getNombre() {
        String nombreDia = dia.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-CO"));
        return nombre + " - " + nombreDia + " (" + porcentaje + "%)";
    }

    @Override
    public long calcularDescuento(long subtotal, LocalDateTime momento) {
        return momento.getDayOfWeek() == dia ? subtotal * porcentaje / 100 : 0;
    }

    @Override
    public String toString() { return getNombre(); }
}
