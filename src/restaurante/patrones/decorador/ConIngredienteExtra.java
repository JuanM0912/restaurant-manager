package restaurante.patrones.decorador;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;

public class ConIngredienteExtra extends AdicionalDecorador {

    public ConIngredienteExtra(ElementoPedido elementoBase, Adicional adicional) {
        super(elementoBase, adicional);
    }

    @Override
    public String getDescripcion() {
        return elementoBase.getDescripcion() + " + " + adicional.getNombre() + " extra";
    }
}