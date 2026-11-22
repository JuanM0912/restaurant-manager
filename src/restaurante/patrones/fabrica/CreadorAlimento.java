package restaurante.patrones.fabrica;

import restaurante.modelo.Alimento;
import restaurante.modelo.Producto;

/** PATRON FACTORY METHOD - rol "Creador concreto": fabrica Alimentos. */
public class CreadorAlimento extends CreadorProducto {

    @Override
    protected Producto crearProducto(String codigo, String nombre, String categoria, long precio, int valorEspecifico) {
        return new Alimento(codigo, nombre, categoria, precio, valorEspecifico);
    }

    @Override
    public String getTipo() { return "ALIMENTO"; }

    @Override
    public String getEtiquetaValorEspecifico() { return "Tiempo de preparacion (min)"; }
}
