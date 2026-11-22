package restaurante.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Lectura y escritura basica de archivos CSV.
 *
 * - Separador ";" porque Excel en espanol (Colombia) lo usa por defecto.
 * - Se escribe una marca BOM al inicio para que Excel muestre bien las tildes.
 * - Ningun texto debe contener ";" (se reemplaza por ",").
 */
public final class ArchivoCSV {

    public static final String SEPARADOR = ";";
    private static final String BOM = "﻿";

    private ArchivoCSV() { } // Clase de utilidades: no se crean objetos.

    /** Lee el archivo y devuelve sus filas SIN la fila de encabezado. Si no existe, devuelve lista vacia. */
    public static List<String[]> leer(String ruta) {
        List<String[]> filas = new ArrayList<>();
        Path archivo = Paths.get(ruta);
        if (!Files.exists(archivo)) {
            return filas;
        }
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (int i = 1; i < lineas.size(); i++) {           // i = 1 salta el encabezado
                String linea = lineas.get(i).trim();
                if (!linea.isEmpty()) {
                    filas.add(linea.split(SEPARADOR, -1));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer el archivo " + ruta + ": " + e.getMessage(), e);
        }
        return filas;
    }

    /** Escribe el archivo completo: primero el encabezado y luego las filas. */
    public static void escribir(String ruta, String encabezado, List<String[]> filas) {
        List<String> lineas = new ArrayList<>();
        lineas.add(BOM + encabezado);
        for (String[] fila : filas) {
            lineas.add(String.join(SEPARADOR, fila));
        }
        try {
            Path archivo = Paths.get(ruta);
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            Files.write(archivo, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo " + ruta + ": " + e.getMessage(), e);
        }
    }

    /** Limpia un texto para que no rompa el formato CSV. */
    public static String limpiar(String texto) {
        return texto == null ? "" : texto.replace(SEPARADOR, ",").replace("\n", " ").trim();
    }

    /** Quita la marca BOM si aparece al inicio de un valor. */
    public static String sinBom(String texto) {
        return texto.startsWith(BOM) ? texto.substring(1) : texto;
    }
}
