package restaurante.patrones.estrategia;

import java.time.LocalDateTime;
import java.time.LocalTime;

/** PATRON STRATEGY - "Estrategia concreta": descuento solo dentro de una franja horaria. */
public class DescuentoPorHorario implements EstrategiaDescuento {

    private final String nombre;
    private final int porcentaje;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;

    public DescuentoPorHorario(String nombre, int porcentaje, LocalTime horaInicio, LocalTime horaFin) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    @Override
    public String getNombre() {
        return nombre + " " + horaInicio + "-" + horaFin + " (" + porcentaje + "%)";
    }

    @Override
    public long calcularDescuento(long subtotal, LocalDateTime momento) {
        LocalTime hora = momento.toLocalTime();
        boolean dentroDelHorario = !hora.isBefore(horaInicio) && hora.isBefore(horaFin);
        return dentroDelHorario ? subtotal * porcentaje / 100 : 0;
    }

    @Override
    public String toString() { return getNombre(); }
}
