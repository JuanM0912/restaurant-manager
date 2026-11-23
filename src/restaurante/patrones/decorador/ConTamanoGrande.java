package restaurante.patrones.decorador;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;

public class ConTamanoGrande extends AdicionalDecorador {

    public ConTamanoGrande(ElementoPedido elementoBase, Adicional adicional) {
        super(elementoBase, adicional);
    }

    @Override
    public String getDescripcion() {
        return elementoBase.getDescripcion() + " (" + adicional.getNombre() + ")";
    }
}