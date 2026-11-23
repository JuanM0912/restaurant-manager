package restaurante.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.patrones.estrategia.SinDescuento;

/**
 * Pedido de una mesa. Sabe calcular su subtotal y su total, pero NO sabe
 * como se calcula cada promocion: eso lo delega a una EstrategiaDescuento (patron Strategy).
 */
public class Pedido {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final int id;
    private int mesa;
    private final LocalDateTime fechaHora;
    private EstadoPedido estado;
    private final List<ItemPedido> items = new ArrayList<>();
    private EstrategiaDescuento promocion = new SinDescuento();

    /** Pedido nuevo: queda PENDIENTE con la fecha y hora actual. */
    public Pedido(int id, int mesa) {
        this(id, mesa, LocalDateTime.now(), EstadoPedido.PENDIENTE);
    }

    /** Pedido con todos sus datos (se usa al cargar desde el CSV y en las pruebas). */
    public Pedido(int id, int mesa, LocalDateTime fechaHora, EstadoPedido estado) {
        this.id = id;
        validarMesa(mesa);
        this.mesa = mesa;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    // ---------- Items ----------

    public void agregarItem(ElementoPedido elemento, int cantidad) {
        verificarQueSePuedeModificar();
        items.add(new ItemPedido(elemento, cantidad));
    }

    /** Cambia la cantidad de un item. Si la cantidad es 0, el item se elimina. */
    public void cambiarCantidad(int indiceItem, int nuevaCantidad) {
        verificarQueSePuedeModificar();
        verificarIndice(indiceItem);
        if (nuevaCantidad == 0) {
            items.remove(indiceItem);
        } else {
            items.get(indiceItem).setCantidad(nuevaCantidad);
        }
    }

    public void quitarItem(int indiceItem) {
        verificarQueSePuedeModificar();
        verificarIndice(indiceItem);
        items.remove(indiceItem);
    }

    /** Lista de solo lectura: para cambiar items hay que usar los metodos de Pedido. */
    public List<ItemPedido> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean estaVacio() { return items.isEmpty(); }

    // ---------- Calculos ----------

    public long getSubtotal() {
        long suma = 0;
        for (ItemPedido item : items) {
            suma += item.getSubtotal();
        }
        return suma;
    }

    /** El descuento lo calcula la estrategia elegida, usando la hora en que se hizo el pedido. */
    public long getDescuento() {
        return promocion.calcularDescuento(getSubtotal(), fechaHora);
    }

    public long getTotal() {
        return getSubtotal() - getDescuento();
    }

    // ---------- Estado ----------

    /** Cambia el estado solo si la transicion esta permitida. */
    public void cambiarEstado(EstadoPedido nuevo) {
        if (!estado.puedeCambiarA(nuevo)) {
            throw new IllegalStateException("No se puede pasar de " + estado + " a " + nuevo + ".");
        }
        this.estado = nuevo;
    }

    public boolean estaActivo() { return estado != EstadoPedido.ENTREGADO; }

    /** Permite cambiar la mesa mientras el pedido siga pendiente. */
    public void setMesa(int mesa) {
        verificarQueSePuedeModificar();
        validarMesa(mesa);
        this.mesa = mesa;
    }

    // ---------- Promocion ----------

    public void setPromocion(EstrategiaDescuento promocion) {
        this.promocion = (promocion == null) ? new SinDescuento() : promocion;
    }

    public EstrategiaDescuento getPromocion() { return promocion; }

    // ---------- Getters ----------

    public int getId() { return id; }
    public int getMesa() { return mesa; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public EstadoPedido getEstado() { return estado; }

    public String getFechaHoraTexto() { return fechaHora.format(FORMATO_HORA); }

    // ---------- Ayudas privadas ----------

    private void verificarQueSePuedeModificar() {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden modificar pedidos en estado Pendiente.");
        }
    }

    private void validarMesa(int mesa) {
        if (mesa <= 0) {
            throw new IllegalArgumentException("El numero de mesa debe ser mayor que cero.");
        }
    }

    private void verificarIndice(int indice) {
        if (indice < 0 || indice >= items.size()) {
            throw new IllegalArgumentException("No existe el item numero " + (indice + 1) + ".");
        }
    }

    /** Resumen de varias lineas para mostrar en consola o en un cuadro de texto. */
    public String getResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido #%d - Mesa %d - %s - %s%n", id, mesa, getFechaHoraTexto(), estado));
        for (int i = 0; i < items.size(); i++) {
            sb.append(String.format("  %d. %s%n", i + 1, items.get(i)));
        }
        sb.append(String.format("  Subtotal: $%,d%n", getSubtotal()));
        if (getDescuento() > 0) {
            sb.append(String.format("  Descuento (%s): -$%,d%n", promocion.getNombre(), getDescuento()));
        }
        sb.append(String.format("  TOTAL: $%,d", getTotal()));
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("#%d | Mesa %d | %s | %d items | $%,d", id, mesa, estado, items.size(), getTotal());
    }
}
