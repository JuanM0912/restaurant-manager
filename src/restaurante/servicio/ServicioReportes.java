package restaurante.servicio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.ItemPedido;
import restaurante.modelo.Pedido;

/** Reportes de ventas y cierre de caja. Solo consulta; no modifica pedidos. */
public class ServicioReportes {

    private final ServicioPedidos servicioPedidos;

    public ServicioReportes(ServicioPedidos servicioPedidos) {
        this.servicioPedidos = servicioPedidos;
    }

    /** Pedidos entregados (es decir, cobrados) en una fecha. */
    public List<Pedido> pedidosEntregados(LocalDate fecha) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : servicioPedidos.listarPedidos()) {
            if (p.getEstado() == EstadoPedido.ENTREGADO && p.getFechaHora().toLocalDate().equals(fecha)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public long totalVendido(LocalDate fecha) {
        long total = 0;
        for (Pedido p : pedidosEntregados(fecha)) {
            total += p.getTotal();
        }
        return total;
    }

    public long totalDescuentos(LocalDate fecha) {
        long total = 0;
        for (Pedido p : pedidosEntregados(fecha)) {
            total += p.getDescuento();
        }
        return total;
    }

    /** Productos mas vendidos en todos los pedidos entregados, de mayor a menor. */
    public List<String> masVendidos(int cuantos) {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (Pedido p : servicioPedidos.listarPedidos()) {
            if (p.getEstado() == EstadoPedido.ENTREGADO) {
                for (ItemPedido item : p.getItems()) {
                    conteo.merge(item.getElemento().getDescripcion(), item.getCantidad(), Integer::sum);
                }
            }
        }
        List<Map.Entry<String, Integer>> ordenados = new ArrayList<>(conteo.entrySet());
        ordenados.sort((a, b) -> b.getValue() - a.getValue());

        List<String> resultado = new ArrayList<>();
        for (int i = 0; i < Math.min(cuantos, ordenados.size()); i++) {
            resultado.add(ordenados.get(i).getValue() + " x " + ordenados.get(i).getKey());
        }
        return resultado;
    }

    /** Texto del cierre de caja del dia, listo para mostrar en consola o en la ventana. */
    public String cierreDeCaja(LocalDate fecha) {
        List<Pedido> entregados = pedidosEntregados(fecha);
        int pendientes = servicioPedidos.listarActivos().size();

        StringBuilder sb = new StringBuilder();
        sb.append("===== CIERRE DE CAJA ").append(fecha).append(" =====\n");
        for (Pedido p : entregados) {
            sb.append(String.format("Pedido #%-4d Mesa %-3d %s  $%,d%n",
                    p.getId(), p.getMesa(), p.getFechaHora().toLocalTime().withNano(0), p.getTotal()));
        }
        sb.append("--------------------------------------\n");
        sb.append(String.format("Pedidos entregados: %d%n", entregados.size()));
        sb.append(String.format("Descuentos aplicados: $%,d%n", totalDescuentos(fecha)));
        sb.append(String.format("TOTAL VENDIDO: $%,d%n", totalVendido(fecha)));
        sb.append(String.format("Pedidos aun activos (sin cobrar): %d%n", pendientes));
        sb.append("\nProductos mas vendidos (historico):\n");
        List<String> top = masVendidos(5);
        if (top.isEmpty()) {
            sb.append("  (sin ventas)\n");
        }
        for (String linea : top) {
            sb.append("  ").append(linea).append('\n');
        }
        return sb.toString();
    }
}
