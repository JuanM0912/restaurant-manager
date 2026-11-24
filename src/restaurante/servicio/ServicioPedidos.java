package restaurante.servicio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.io.UncheckedIOException;

import restaurante.modelo.ElementoPedido;
import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.patrones.observador.ObservadorPedido;
import restaurante.persistencia.PedidoCSV;

/**
 * Casos de uso de los pedidos.
 *
 * PATRON OBSERVER - rol "Sujeto": guarda la lista de observadores y les avisa
 * cada vez que un pedido se registra o cambia de estado. Esta clase no sabe
 * quienes son (cocina, caja, mesero, una pantalla...): solo los recorre.
 */
public class ServicioPedidos {

    private final PedidoCSV repositorio;
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<EstrategiaDescuento> promociones;
    private final List<ObservadorPedido> observadores = new ArrayList<>();
    private int siguienteId;

    public ServicioPedidos(PedidoCSV repositorio, List<EstrategiaDescuento> promociones) {
        this.repositorio = repositorio;
        try {
            this.pedidos.addAll(repositorio.cargar());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudieron cargar los pedidos", e);
        }
        this.siguienteId = calcularSiguienteId();
    }

    // ---------- Observer: suscripcion y aviso ----------

    public void agregarObservador(ObservadorPedido observador) {
        observadores.add(observador);
    }

    public void quitarObservador(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    private void notificar(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        for (ObservadorPedido o : new ArrayList<>(observadores)) {
            o.estadoCambiado(pedido, anterior, nuevo);
        }
    }

    // ---------- Crear y registrar ----------

    /** Crea un pedido en borrador (todavia no se guarda ni se avisa a nadie). */
    public Pedido nuevoPedido(int mesa) {
        return new Pedido(siguienteId++, mesa);
    }

    /** Confirma el pedido: lo guarda y avisa a los observadores que hay un pedido nuevo. */
    public void registrarPedido(Pedido pedido) {
        if (pedido.estaVacio()) {
            throw new IllegalStateException("No se puede registrar un pedido sin productos.");
        }
        if (pedidos.contains(pedido)) {
            throw new IllegalStateException("El pedido #" + pedido.getId() + " ya estaba registrado.");
        }
        pedidos.add(pedido);
        guardar();
        notificar(pedido, null, pedido.getEstado());
    }

    // ---------- Modificar pedidos ya registrados ----------

    public void agregarItem(int idPedido, ElementoPedido elemento, int cantidad) {
        buscarPorId(idPedido).agregarItem(elemento, cantidad);
        guardar();
    }

    /** Cambia la cantidad de un item (indice empieza en 0). Cantidad 0 elimina el item. */
    public void cambiarCantidad(int idPedido, int indiceItem, int cantidad) {
        buscarPorId(idPedido).cambiarCantidad(indiceItem, cantidad);
        guardar();
    }

    public void aplicarPromocion(int idPedido, EstrategiaDescuento promocion) {
        buscarPorId(idPedido).setPromocion(promocion);
        guardar();
    }

    // ---------- Estados ----------

    /** Pasa el pedido al estado siguiente y avisa a los observadores. */
    public void avanzarEstado(int idPedido) {
        Pedido pedido = buscarPorId(idPedido);
        EstadoPedido siguiente = pedido.getEstado().siguiente();
        if (siguiente == null) {
            throw new IllegalStateException("El pedido #" + idPedido + " ya fue entregado.");
        }
        cambiarEstado(idPedido, siguiente);
    }

    public void cambiarEstado(int idPedido, EstadoPedido nuevo) {
        Pedido pedido = buscarPorId(idPedido);
        EstadoPedido anterior = pedido.getEstado();
        pedido.cambiarEstado(nuevo);      // Pedido valida que la transicion sea permitida
        guardar();
        notificar(pedido, anterior, nuevo);
    }

    // ---------- Consultas ----------

    public List<Pedido> listarPedidos() {
        return Collections.unmodifiableList(pedidos);
    }

    public List<Pedido> listarActivos() {
        List<Pedido> activos = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.estaActivo()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public Pedido buscarPorId(int id) {
        for (Pedido p : pedidos) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new IllegalArgumentException("No existe el pedido #" + id + ".");
    }

    public List<EstrategiaDescuento> getPromociones() {
        return Collections.unmodifiableList(promociones);
    }

    // ---------- Ayudas privadas ----------

    private void guardar() {
        try {
            repositorio.guardar(pedidos);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudieron guardar los pedidos", e);
        }
    }

    private int calcularSiguienteId() {
        int mayor = 0;
        for (Pedido p : pedidos) {
            mayor = Math.max(mayor, p.getId());
        }
        return mayor + 1;
    }
}
