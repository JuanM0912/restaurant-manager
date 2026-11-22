package restaurante.persistencia;

import java.util.ArrayList;
import java.util.List;

import restaurante.modelo.Adicional;

/**
 * Lee los adicionales disponibles desde datos/adicionales.csv.
 * Formato: nombre;precio
 * Para ofrecer un adicional nuevo basta con agregar una fila en Excel.
 */
public class AdicionalCSV {

    private final String ruta;

    public AdicionalCSV(String ruta) {
        this.ruta = ruta;
    }

    public List<Adicional> cargar() {
        List<Adicional> adicionales = new ArrayList<>();
        for (String[] fila : ArchivoCSV.leer(ruta)) {
            String nombre = ArchivoCSV.sinBom(fila[0]).trim();
            long precio = Long.parseLong(fila[1].trim());
            adicionales.add(new Adicional(nombre, precio));
        }
        return adicionales;
    }
}
