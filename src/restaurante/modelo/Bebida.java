package restaurante.modelo;

/** Producto que se sirve en vaso. Su dato propio es el volumen. */
public class Bebida extends Producto {

    private final int volumenMl;

    public Bebida(String codigo, String nombre, String categoria, long precio, int volumenMl) {
        super(codigo, nombre, categoria, precio);
        this.volumenMl = volumenMl;
    }

    @Override
    public String getTipo() { return "BEBIDA"; }

    @Override
    public int getValorEspecifico() { return volumenMl; }

    @Override
    public String getDetalle() { return volumenMl + " ml"; }
}
