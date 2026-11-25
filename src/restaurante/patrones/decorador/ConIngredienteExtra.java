package restaurante.patrones.decorador;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;

/** PATRON DECORATOR - "Decorador concreto": suma un ingrediente y su precio. */
public class ConIngredienteExtra extends AdicionalDecorador {

    private final Adicional adicional;

    public ConIngredienteExtra(ElementoPedido envuelto, Adicional adicional) {
        super(envuelto);
        this.adicional = adicional;
    }

    @Override
    public String getDescripcion() {
        return envuelto.getDescripcion() + " + " + adicional.getNombre();
    }

    @Override
    public long getPrecio() {
        return envuelto.getPrecio() + adicional.getPrecio();
    }
}
