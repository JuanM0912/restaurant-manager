package restaurante.persistencia;

import java.util.ArrayList;
import java.util.List;

import restaurante.modelo.Producto;

/**
 * Guarda y lee el menu en datos/productos.csv.
 * Formato: codigo;tipo;nombre;categoria;precio;disponible;valorEspecifico
 *
 * Esta clase solo maneja texto. Convertir cada fila en un objeto Producto
 * es tarea de ServicioMenu, que usa los creadores (Factory Method).
 */
public class ProductoCSV {

    private static final String ENCABEZADO = "codigo;tipo;nombre;categoria;precio;disponible;valorEspecifico";
    private final String ruta;

    public ProductoCSV(String ruta) {
        this.ruta = ruta;
    }

    public List<String[]> leerFilas() {
        return ArchivoCSV.leer(ruta);
    }

    public void guardar(List<Producto> productos) {
        List<String[]> filas = new ArrayList<>();
        for (Producto p : productos) {
            filas.add(new String[] {
                    p.getCodigo(),
                    p.getTipo(),
                    ArchivoCSV.limpiar(p.getNombre()),
                    ArchivoCSV.limpiar(p.getCategoria()),
                    String.valueOf(p.getPrecio()),
                    p.isDisponible() ? "SI" : "NO",
                    String.valueOf(p.getValorEspecifico())
            });
        }
        ArchivoCSV.escribir(ruta, ENCABEZADO, filas);
    }
}
