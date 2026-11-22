package restaurante.modelo;

/** Producto que se cocina. Su dato propio es el tiempo de preparacion. */
public class Alimento extends Producto {

    private final int minutosPreparacion;

    public Alimento(String codigo, String nombre, String categoria, long precio, int minutosPreparacion) {
        super(codigo, nombre, categoria, precio);
        this.minutosPreparacion = minutosPreparacion;
    }

    @Override
    public String getTipo() { return "ALIMENTO"; }

    @Override
    public int getValorEspecifico() { return minutosPreparacion; }

    @Override
    public String getDetalle() { return minutosPreparacion + " min"; }
}
