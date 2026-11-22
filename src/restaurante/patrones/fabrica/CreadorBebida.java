package restaurante.patrones.fabrica;

import restaurante.modelo.Bebida;
import restaurante.modelo.Producto;

/** PATRON FACTORY METHOD - rol "Creador concreto": fabrica Bebidas. */
public class CreadorBebida extends CreadorProducto {

    @Override
    protected Producto crearProducto(String codigo, String nombre, String categoria, long precio, int valorEspecifico) {
        return new Bebida(codigo, nombre, categoria, precio, valorEspecifico);
    }

    @Override
    public String getTipo() { return "BEBIDA"; }

    @Override
    public String getEtiquetaValorEspecifico() { return "Volumen (ml)"; }
}
