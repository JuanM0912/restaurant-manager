package restaurante.patrones.decorador;

import restaurante.modelo.ElementoPedido;

/** PATRON DECORATOR - "Decorador concreto": tamano grande, cuesta 30% mas. */
public class ConTamanoGrande extends AdicionalDecorador {

    public static final int PORCENTAJE_RECARGO = 30;

    public ConTamanoGrande(ElementoPedido envuelto) {
        super(envuelto);
    }

    @Override
    public String getDescripcion() {
        return envuelto.getDescripcion() + " (grande)";
    }

    @Override
    public long getPrecio() {
        long precioBase = envuelto.getPrecio();
        return precioBase + precioBase * PORCENTAJE_RECARGO / 100;
    }
}
