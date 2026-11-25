package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import restaurante.servicio.Sistema;

/** Ventana principal: encabezado, pestañas y pie con la estética vino/borgoña. */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(Sistema sistema) {
        super("Restaurant Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1150, 720));
        setLocationRelativeTo(null);

        UIManager.put("TabbedPane.background", Estilo.BLANCO_CALIDO);
        UIManager.put("TabbedPane.contentAreaColor", Estilo.BLANCO_CALIDO);
        UIManager.put("TabbedPane.selected", Estilo.BORGONA);
        UIManager.put("TabbedPane.unselectedBackground", Estilo.GRIS_SUAVE);
        UIManager.put("TabbedPane.focus", Estilo.ROSA_EMPOLVADO);
        UIManager.put("TabbedPane.borderHightlightColor", Estilo.ROSA_EMPOLVADO);
        UIManager.put("TabbedPane.darkShadow", Estilo.VINO_OSCURO);

        JPanel contenedorPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Estilo.BLANCO_CALIDO);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contenedorPrincipal.setOpaque(false);

        // Encabezado con degradado vino oscuro -> borgoña e iconos
        JPanel encabezado = Estilo.bannerVino(true);
        encabezado.setLayout(new BorderLayout());
        encabezado.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

        JPanel izquierdaHeader = new JPanel();
        izquierdaHeader.setOpaque(false);
        izquierdaHeader.setLayout(new BoxLayout(izquierdaHeader, BoxLayout.X_AXIS));

        JLabel logoChef = new JLabel(Estilo.iconoChef(30, Color.WHITE));
        izquierdaHeader.add(logoChef);
        izquierdaHeader.add(Box.createHorizontalStrut(14));

        JSeparator separador = new JSeparator(SwingConstants.VERTICAL);
        separador.setPreferredSize(new Dimension(2, 26));
        separador.setForeground(new Color(0xEA, 0xD9, 0xDF, 140));
        separador.setBackground(new Color(0xEA, 0xD9, 0xDF, 140));
        izquierdaHeader.add(separador);
        izquierdaHeader.add(Box.createHorizontalStrut(14));

        JLabel titulo = Estilo.etiqueta("Restaurant Manager", Estilo.TITULO);
        titulo.setForeground(Color.WHITE);
        izquierdaHeader.add(titulo);

        encabezado.add(izquierdaHeader, BorderLayout.WEST);

        JLabel fecha = new JLabel("Hoy: " + LocalDate.now());
        fecha.setFont(Estilo.NORMAL);
        fecha.setForeground(Estilo.ROSA_EMPOLVADO);
        fecha.setIcon(Estilo.iconoCalendario(16, Estilo.ROSA_EMPOLVADO));
        fecha.setIconTextGap(7);
        encabezado.add(fecha, BorderLayout.EAST);

        // Pestañas: todas reciben los mismos servicios del Sistema
        PanelCocina panelCocina = new PanelCocina(sistema.getServicioPedidos());
        PanelPedidos panelPedidos = new PanelPedidos(sistema.getServicioPedidos());

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(Estilo.NEGRITA);
        pestanas.setBackground(Estilo.GRIS_SUAVE);
        pestanas.setForeground(Estilo.VINO_OSCURO);
        pestanas.setOpaque(true);

        pestanas.addTab("Tomar pedido", new PanelTomarPedido(sistema.getServicioMenu(), sistema.getServicioPedidos()));
        pestanas.addTab("Cocina", panelCocina);
        pestanas.addTab("Pedidos", panelPedidos);
        pestanas.addTab("Menu", new PanelMenu(sistema.getServicioMenu()));
        pestanas.addTab("Reportes", new PanelReportes(sistema.getServicioReportes()));

        actualizarColoresPestanas(pestanas);

        // Los dos paneles se suscriben como observadores (patrón Observer)
        sistema.getServicioPedidos().agregarObservador(panelCocina);
        sistema.getServicioPedidos().agregarObservador(panelPedidos);

        // Al cambiar de pestaña, resalta la activa y refresca el panel visible
        pestanas.addChangeListener(e -> {
            actualizarColoresPestanas(pestanas);
            Component visible = pestanas.getSelectedComponent();
            if (visible instanceof Refrescable) {
                ((Refrescable) visible).refrescar();
            }
        });

        // Pie inferior con campana a la izquierda y franjas diagonales a la derecha
        JPanel pie = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                g2.setColor(Estilo.BLANCO_CALIDO);
                g2.fillRect(0, 0, w, h);

                g2.setColor(Estilo.ROSA_EMPOLVADO);
                g2.fillRect(0, 0, w, 2);

                // Franjas diagonales en esquina inferior derecha
                g2.setColor(Estilo.BORGONA);
                int[] px1 = { w - 110, w - 75, w - 45, w - 80 };
                int[] py1 = { h, h, 0, 0 };
                g2.fillPolygon(px1, py1, 4);

                g2.setColor(Estilo.ROSA_EMPOLVADO);
                int[] px2 = { w - 65, w - 35, w - 5, w - 35 };
                int[] py2 = { h, h, 0, 0 };
                g2.fillPolygon(px2, py2, 4);

                g2.dispose();
            }
        };
        pie.setPreferredSize(new Dimension(0, 44));
        pie.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        JLabel campana = new JLabel(Estilo.iconoCampana(26, Estilo.VINO_OSCURO));
        pie.add(campana, BorderLayout.WEST);

        contenedorPrincipal.add(encabezado, BorderLayout.NORTH);
        contenedorPrincipal.add(pestanas, BorderLayout.CENTER);
        contenedorPrincipal.add(pie, BorderLayout.SOUTH);
        add(contenedorPrincipal);
    }

    private void actualizarColoresPestanas(JTabbedPane pestanas) {
        int seleccionada = pestanas.getSelectedIndex();
        for (int i = 0; i < pestanas.getTabCount(); i++) {
            if (i == seleccionada) {
                pestanas.setBackgroundAt(i, Estilo.BORGONA);
                pestanas.setForegroundAt(i, Color.WHITE);
            } else {
                pestanas.setBackgroundAt(i, Estilo.GRIS_SUAVE);
                pestanas.setForegroundAt(i, Estilo.VINO_OSCURO);
            }
        }
    }
}