package restaurante.persistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.ItemPedido;
import restaurante.modelo.Pedido;
import restaurante.patrones.estrategia.EstrategiaDescuento;

/**
 * Guarda y lee los pedidos en dos archivos:
 *  - pedidos.csv       -> id;mesa;fechaHora;estado;promocion;subtotal;descuento;total
 *  - pedido_items.csv  -> idPedido;descripcion;precioUnitario;cantidad;subtotal
 * Las columnas subtotal, descuento y total se guardan para poder revisar las ventas en Excel.
 */
public class PedidoCSV {

    private static final String ENC_PEDIDOS = "id;mesa;fechaHora;estado;promocion;subtotal;descuento;total";
    private static final String ENC_ITEMS = "idPedido;descripcion;precioUnitario;cantidad;subtotal";

    private final String rutaPedidos;
    private final String rutaItems;

    public PedidoCSV(String rutaPedidos, String rutaItems) {
        this.rutaPedidos = rutaPedidos;
        this.rutaItems = rutaItems;
    }

    public void guardar(List<Pedido> pedidos) {
        List<String[]> filasPedidos = new ArrayList<>();
        List<String[]> filasItems = new ArrayList<>();
        for (Pedido p : pedidos) {
            filasPedidos.add(new String[] {
                    String.valueOf(p.getId()),
                    String.valueOf(p.getMesa()),
                    p.getFechaHora().withNano(0).toString(),
                    p.getEstado().name(),
                    ArchivoCSV.limpiar(p.getPromocion().getNombre()),
                    String.valueOf(p.getSubtotal()),
                    String.valueOf(p.getDescuento()),
                    String.valueOf(p.getTotal())
            });
            for (ItemPedido item : p.getItems()) {
                filasItems.add(new String[] {
                        String.valueOf(p.getId()),
                        ArchivoCSV.limpiar(item.getElemento().getDescripcion()),
                        String.valueOf(item.getElemento().getPrecio()),
                        String.valueOf(item.getCantidad()),
                        String.valueOf(item.getSubtotal())
                });
            }
        }
        ArchivoCSV.escribir(rutaPedidos, ENC_PEDIDOS, filasPedidos);
        ArchivoCSV.escribir(rutaItems, ENC_ITEMS, filasItems);
    }

    /**
     * Lee los pedidos guardados.
     * @param promociones promociones disponibles, para volver a asociar cada pedido con la suya por nombre
     */
    public List<Pedido> cargar(List<EstrategiaDescuento> promociones) {
        // 1. Leer los items y agruparlos por id de pedido.
        Map<Integer, List<String[]>> itemsPorPedido = new HashMap<>();
        for (String[] fila : ArchivoCSV.leer(rutaItems)) {
            int idPedido = Integer.parseInt(ArchivoCSV.sinBom(fila[0]).trim());
            itemsPorPedido.computeIfAbsent(idPedido, k -> new ArrayList<>()).add(fila);
        }

        // 2. Leer los pedidos. Los items se agregan con estado PENDIENTE y al final se pone el estado real.
        List<Pedido> pedidos = new ArrayList<>();
        for (String[] fila : ArchivoCSV.leer(rutaPedidos)) {
            int id = Integer.parseInt(ArchivoCSV.sinBom(fila[0]).trim());
            int mesa = Integer.parseInt(fila[1].trim());
            LocalDateTime fecha = LocalDateTime.parse(fila[2].trim());
            EstadoPedido estadoReal = EstadoPedido.valueOf(fila[3].trim());
            String nombrePromocion = fila[4].trim();

            Pedido pedido = new Pedido(id, mesa, fecha, EstadoPedido.PENDIENTE);
            for (String[] item : itemsPorPedido.getOrDefault(id, new ArrayList<>())) {
                String descripcion = item[1];
                long precioUnitario = Long.parseLong(item[2].trim());
                int cantidad = Integer.parseInt(item[3].trim());
                pedido.agregarItem(new ElementoGuardado(descripcion, precioUnitario), cantidad);
            }
            pedido.setPromocion(buscarPromocion(promociones, nombrePromocion));

            // Avanzar paso a paso hasta el estado guardado (respeta las reglas de transicion).
            while (pedido.getEstado() != estadoReal) {
                pedido.cambiarEstado(pedido.getEstado().siguiente());
            }
            pedidos.add(pedido);
        }
        return pedidos;
    }

    private EstrategiaDescuento buscarPromocion(List<EstrategiaDescuento> promociones, String nombre) {
        for (EstrategiaDescuento e : promociones) {
            if (e.getNombre().equals(nombre)) {
                return e;
            }
        }
        return null; // Pedido.setPromocion(null) deja "Sin promocion"
    }
}
