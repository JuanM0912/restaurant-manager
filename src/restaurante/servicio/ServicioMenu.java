package restaurante.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;
import restaurante.modelo.Producto;
import restaurante.patrones.decorador.ConIndicacion;
import restaurante.patrones.decorador.ConIngredienteExtra;
import restaurante.patrones.decorador.ConTamanoGrande;
import restaurante.patrones.fabrica.CreadorProducto;
import restaurante.persistencia.AdicionalCSV;
import restaurante.persistencia.ArchivoCSV;
import restaurante.persistencia.ProductoCSV;

/**
 * Casos de uso del menu: registrar, consultar, cambiar precio/disponibilidad
 * y personalizar productos con adicionales.
 * La consola y la ventana usan ESTA clase; nunca leen el CSV directamente.
 */
public class ServicioMenu {

    private final ProductoCSV repositorio;
    private final Map<String, CreadorProducto> creadores = new LinkedHashMap<>();
    private final List<Producto> productos = new ArrayList<>();
    private final List<Adicional> adicionales = new ArrayList<>();

    public ServicioMenu(ProductoCSV repositorio, AdicionalCSV repositorioAdicionales, List<CreadorProducto> creadores) {
        this.repositorio = repositorio;
        for (CreadorProducto creador : creadores) {
            this.creadores.put(creador.getTipo(), creador);
        }
        this.adicionales.addAll(repositorioAdicionales.cargar());
        cargarProductos();
    }

    // ---------- Consultas ----------

    public List<Producto> listarProductos() {
        return Collections.unmodifiableList(productos);
    }

    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Producto> listarPorTipo(String tipo) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Producto> buscarPorNombre(String texto) {
        List<Producto> resultado = new ArrayList<>();
        String buscado = texto.toLowerCase();
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    /** Categorias existentes, sin repetir y en orden alfabetico. */
    public List<String> listarCategorias() {
        TreeSet<String> categorias = new TreeSet<>();
        for (Producto p : productos) {
            categorias.add(p.getCategoria());
        }
        return new ArrayList<>(categorias);
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        throw new IllegalArgumentException("No existe un producto con codigo " + codigo + ".");
    }

    /** Tipos de producto que se pueden crear (uno por cada creador registrado). */
    public List<CreadorProducto> getCreadores() {
        return new ArrayList<>(creadores.values());
    }

    public List<Adicional> getAdicionales() {
        return Collections.unmodifiableList(adicionales);
    }

    // ---------- Cambios ----------

    /** Registra un producto nuevo usando el creador (Factory Method) del tipo indicado. */
    public Producto registrarProducto(String tipo, String nombre, String categoria, long precio, int valorEspecifico) {
        CreadorProducto creador = buscarCreador(tipo);
        Producto nuevo = creador.registrar(generarCodigo(), nombre, categoria, precio, valorEspecifico);
        productos.add(nuevo);
        repositorio.guardar(productos);
        return nuevo;
    }

    public void cambiarPrecio(String codigo, long nuevoPrecio) {
        buscarPorCodigo(codigo).setPrecio(nuevoPrecio);
        repositorio.guardar(productos);
    }

    public void cambiarDisponibilidad(String codigo, boolean disponible) {
        buscarPorCodigo(codigo).setDisponible(disponible);
        repositorio.guardar(productos);
    }

    // ---------- Personalizacion (patron Decorator) ----------

    /**
     * Envuelve el producto con los decoradores elegidos.
     * Ejemplo: Hamburguesa -> + Queso -> + Tocineta -> (grande) -> [sin cebolla]
     */
    public ElementoPedido personalizar(Producto producto, List<Adicional> extras, boolean grande, String indicacion) {
        if (!producto.isDisponible()) {
            throw new IllegalStateException(producto.getNombre() + " no esta disponible.");
        }
        ElementoPedido resultado = producto;
        for (Adicional extra : extras) {
            resultado = new ConIngredienteExtra(resultado, extra);
        }
        if (grande) {
            resultado = new ConTamanoGrande(resultado);
        }
        if (indicacion != null && !indicacion.isBlank()) {
            resultado = new ConIndicacion(resultado, indicacion.trim());
        }
        return resultado;
    }

    // ---------- Ayudas privadas ----------

    private CreadorProducto buscarCreador(String tipo) {
        CreadorProducto creador = creadores.get(tipo.toUpperCase());
        if (creador == null) {
            throw new IllegalArgumentException("Tipo de producto desconocido: " + tipo);
        }
        return creador;
    }

    /** Lee el CSV y convierte cada fila en un Producto usando el creador de su tipo. */
    private void cargarProductos() {
        for (String[] fila : repositorio.leerFilas()) {
            String codigo = ArchivoCSV.sinBom(fila[0]).trim();
            String tipo = fila[1].trim();
            String nombre = fila[2].trim();
            String categoria = fila[3].trim();
            long precio = Long.parseLong(fila[4].trim());
            boolean disponible = fila[5].trim().equalsIgnoreCase("SI");
            int valor = Integer.parseInt(fila[6].trim());

            Producto p = buscarCreador(tipo).registrar(codigo, nombre, categoria, precio, valor);
            p.setDisponible(disponible);
            productos.add(p);
        }
    }

    /** Genera el siguiente codigo: P001, P002, ... */
    private String generarCodigo() {
        int mayor = 0;
        for (Producto p : productos) {
            try {
                mayor = Math.max(mayor, Integer.parseInt(p.getCodigo().substring(1)));
            } catch (NumberFormatException e) {
                // Codigo con otro formato: se ignora.
            }
        }
        return String.format("P%03d", mayor + 1);
    }
}
