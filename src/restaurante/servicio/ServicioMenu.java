package restaurante.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import restaurante.modelo.Producto;

public class ServicioMenu {

    private final List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        productos.add(producto);
    }

    public List<Producto> listarProductos() {
        return Collections.unmodifiableList(productos);
    }

    public List<Producto> listarDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.isDisponible()) {
                disponibles.add(producto);
            }
        }
        return disponibles;
    }

    public Producto buscarPorCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Producto producto : productos) {
            if (codigo.equalsIgnoreCase(producto.getCodigo())) {
                return producto;
            }
        }
        return null;
    }

    public boolean cambiarDisponibilidad(String codigo, boolean disponible) {
        Producto producto = buscarPorCodigo(codigo);
        if (producto == null) {
            return false;
        }
        producto.setDisponible(disponible);
        return true;
    }
}