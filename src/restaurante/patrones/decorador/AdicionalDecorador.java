package restaurante.patrones.decorador;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;

public abstract class AdicionalDecorador implements ElementoPedido {

    protected final ElementoPedido elementoBase;
    protected final Adicional adicional;

    protected AdicionalDecorador(ElementoPedido elementoBase, Adicional adicional) {
        if (elementoBase == null) {
            throw new IllegalArgumentException("El elemento base no puede ser nulo.");
        }
        if (adicional == null) {
            throw new IllegalArgumentException("El adicional no puede ser nulo.");
        }
        this.elementoBase = elementoBase;
        this.adicional = adicional;
    }

    public ElementoPedido getElementoBase() {
        return elementoBase;
    }

    public Adicional getAdicional() {
        return adicional;
    }

    @Override
    public String getDescripcion() {
        return elementoBase.getDescripcion() + " + " + adicional.getNombre();
    }

    @Override
    public long getPrecio() {
        return elementoBase.getPrecio() + adicional.getPrecio();
    }
}