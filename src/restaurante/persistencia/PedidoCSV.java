package restaurante.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;

public class PedidoCSV {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String SEPARADOR = ";";
    private static final String ENCABEZADO = "id;mesa;fechaHora;estado;subtotal;descuento;total;items";

    private final Path archivo;

    public PedidoCSV(Path archivo) {
        this.archivo = archivo;
    }

    public PedidoCSV(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
    }

    public void guardar(List<Pedido> pedidos) throws IOException {
        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write(ENCABEZADO);
            writer.newLine();
            for (Pedido pedido : pedidos) {
                writer.write(formatearPedido(pedido));
                writer.newLine();
            }
        }
    }

    public List<Pedido> cargar() throws IOException {
        List<Pedido> pedidos = new ArrayList<>();
        if (!Files.exists(archivo)) {
            return pedidos;
        }
        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    if (linea.startsWith("id;")) {
                        continue;
                    }
                }
                if (linea.trim().isEmpty()) {
                    continue;
                }
                pedidos.add(parsearPedido(linea));
            }
        }
        return pedidos;
    }

    private String formatearPedido(Pedido pedido) {
        StringBuilder itemsTexto = new StringBuilder();
        for (int i = 0; i < pedido.getItems().size(); i++) {
            if (i > 0) {
                itemsTexto.append(" | ");
            }
            itemsTexto.append(pedido.getItems().get(i).toString());
        }
        return String.join(SEPARADOR,
                String.valueOf(pedido.getId()),
                String.valueOf(pedido.getMesa()),
                pedido.getFechaHora().format(FORMATO_FECHA),
                pedido.getEstado().name(),
                String.valueOf(pedido.getSubtotal()),
                String.valueOf(pedido.getDescuento()),
                String.valueOf(pedido.getTotal()),
                itemsTexto.toString().replace(SEPARADOR, ",")
        );
    }

    private Pedido parsearPedido(String linea) {
        String[] partes = linea.split(SEPARADOR, -1);
        if (partes.length < 4) {
            throw new IllegalArgumentException("Linea de pedido CSV invalida: " + linea);
        }
        int id = Integer.parseInt(partes[0].trim());
        int mesa = Integer.parseInt(partes[1].trim());
        LocalDateTime fechaHora = LocalDateTime.parse(partes[2].trim(), FORMATO_FECHA);
        EstadoPedido estado = EstadoPedido.valueOf(partes[3].trim());
        return new Pedido(id, mesa, fechaHora, estado);
    }
}