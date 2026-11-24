package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import restaurante.servicio.Sistema;

/** Ventana principal: encabezado y una pestana por cada funcionalidad. */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(Sistema sistema) {
        super("Restaurant Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1150, 720));
        setLocationRelativeTo(null);

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        encabezado.setBackground(java.awt.Color.WHITE);
        encabezado.add(Estilo.etiqueta("Restaurant Manager", Estilo.TITULO), BorderLayout.WEST);
        JLabel fecha = Estilo.etiqueta("Hoy: " + LocalDate.now(), Estilo.NORMAL);
        fecha.setForeground(Estilo.TEXTO_SUAVE);
        encabezado.add(fecha, BorderLayout.EAST);

        // Pestanas: todas reciben los mismos servicios del Sistema
        PanelCocina panelCocina = new PanelCocina(sistema.getServicioPedidos());
        PanelPedidos panelPedidos = new PanelPedidos(sistema.getServicioPedidos());

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(Estilo.SUBTITULO);
        pestanas.addTab("Tomar pedido", new PanelTomarPedido(sistema.getServicioMenu(), sistema.getServicioPedidos()));
        pestanas.addTab("Cocina", panelCocina);
        pestanas.addTab("Pedidos", panelPedidos);
        pestanas.addTab("Menu", new PanelMenu(sistema.getServicioMenu()));
        pestanas.addTab("Reportes", new PanelReportes(sistema.getServicioReportes()));

        // Los dos paneles se suscriben como observadores (patron Observer)
        sistema.getServicioPedidos().agregarObservador(panelCocina);
        sistema.getServicioPedidos().agregarObservador(panelPedidos);

        // Al cambiar de pestana, el panel visible vuelve a cargar sus datos
        pestanas.addChangeListener(e -> {
            Component visible = pestanas.getSelectedComponent();
            if (visible instanceof Refrescable) {
                ((Refrescable) visible).refrescar();
            }
        });

        add(encabezado, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
    }
}
