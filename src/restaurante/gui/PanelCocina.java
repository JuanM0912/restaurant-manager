package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.ItemPedido;
import restaurante.modelo.Pedido;
import restaurante.patrones.observador.ObservadorPedido;
import restaurante.servicio.ServicioPedidos;

/**
 * Pantalla de cocina. Es un OBSERVADOR (patron Observer): cuando un pedido se registra
 * o cambia de estado, ServicioPedidos llama a estadoCambiado() y esta pantalla se actualiza sola.
 */
public class PanelCocina extends JPanel implements ObservadorPedido, Refrescable {

    private final ServicioPedidos pedidos;
    private final JPanel tablero = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
    private final JTextArea avisos = new JTextArea(8, 40);

    public PanelCocina(ServicioPedidos pedidos) {
        this.pedidos = pedidos;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Estilo.FONDO);

        add(Estilo.etiqueta("Pedidos activos", Estilo.TITULO), BorderLayout.NORTH);

        tablero.setBackground(Estilo.FONDO);
        add(new JScrollPane(tablero), BorderLayout.CENTER);

        avisos.setEditable(false);
        avisos.setFont(Estilo.NORMAL);
        JPanel zonaAvisos = new JPanel(new BorderLayout());
        zonaAvisos.setOpaque(false);
        zonaAvisos.add(Estilo.etiqueta("Avisos recibidos (Observer)", Estilo.SUBTITULO), BorderLayout.NORTH);
        zonaAvisos.add(new JScrollPane(avisos), BorderLayout.CENTER);
        add(zonaAvisos, BorderLayout.SOUTH);

        refrescar();
    }

    /** Metodo del patron Observer: lo llama ServicioPedidos. */
    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        String hora = LocalTime.now().withNano(0).toString();
        String cambio = (anterior == null) ? "NUEVO" : anterior + " -> " + nuevo;
        avisos.append(hora + "  Pedido #" + pedido.getId() + " (mesa " + pedido.getMesa() + "): " + cambio + "\n");
        refrescar();
    }

    @Override
    public void refrescar() {
        tablero.removeAll();
        for (Pedido p : pedidos.listarActivos()) {
            tablero.add(crearTarjeta(p));
        }
        if (pedidos.listarActivos().isEmpty()) {
            tablero.add(new JLabel("No hay pedidos activos."));
        }
        tablero.revalidate();
        tablero.repaint();
    }

    private JPanel crearTarjeta(Pedido p) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        Estilo.tarjeta(tarjeta);
        tarjeta.setPreferredSize(new Dimension(250, 190));

        tarjeta.add(Estilo.etiqueta("Mesa " + p.getMesa() + "  •  Pedido #" + p.getId(), Estilo.SUBTITULO));
        JLabel estado = Estilo.etiqueta("Estado: " + p.getEstado(), Estilo.NORMAL);
        estado.setForeground(Estilo.VERDE);
        tarjeta.add(estado);
        for (ItemPedido item : p.getItems()) {
            tarjeta.add(new JLabel("<html><body style='width:200px'>" + item.getCantidad() + " x " + item.getElemento().getDescripcion() + "</html>"));
        }

        EstadoPedido siguiente = p.getEstado().siguiente();
        JButton avanzar = Estilo.botonPrincipal("Pasar a: " + siguiente);
        avanzar.addActionListener(e -> {
            try {
                pedidos.avanzarEstado(p.getId());   // avisa a todos los observadores, incluida esta pantalla
            } catch (RuntimeException ex) {
                Estilo.mostrarError(this, ex);
            }
        });
        tarjeta.add(javax.swing.Box.createVerticalGlue());
        tarjeta.add(avanzar);
        return tarjeta;
    }
}
