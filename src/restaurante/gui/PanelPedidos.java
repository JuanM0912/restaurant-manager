package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import restaurante.modelo.EstadoPedido;
import restaurante.modelo.Pedido;
import restaurante.patrones.observador.ObservadorPedido;
import restaurante.servicio.ServicioPedidos;

/** Consulta de todos los pedidos. Tambien es observador para mantenerse al dia. */
public class PanelPedidos extends JPanel implements ObservadorPedido, Refrescable {

    private final ServicioPedidos pedidos;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] {"#", "Mesa", "Fecha y hora", "Estado", "Promocion", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final JTextArea detalle = new JTextArea();

    public PanelPedidos(ServicioPedidos pedidos) {
        this.pedidos = pedidos;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        tabla.getSelectionModel().addListSelectionListener(e -> mostrarDetalle());
        detalle.setEditable(false);
        detalle.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 13));

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabla), new JScrollPane(detalle));
        division.setResizeWeight(0.6);

        JButton avanzar = Estilo.botonPrincipal("Avanzar estado del pedido seleccionado");
        avanzar.addActionListener(e -> avanzarSeleccionado());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(avanzar);

        add(Estilo.etiqueta("Todos los pedidos", Estilo.TITULO), BorderLayout.NORTH);
        add(division, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        refrescar();
    }

    @Override
    public void estadoCambiado(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo) {
        refrescar();
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Pedido p : pedidos.listarPedidos()) {
            modelo.addRow(new Object[] {
                    p.getId(), p.getMesa(), p.getFechaHoraTexto(), p.getEstado(),
                    p.getPromocion().getNombre(), Estilo.pesos(p.getTotal())});
        }
        detalle.setText("");
    }

    private Integer idSeleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 ? null : (Integer) modelo.getValueAt(fila, 0);
    }

    private void mostrarDetalle() {
        Integer id = idSeleccionado();
        if (id != null) {
            detalle.setText(pedidos.buscarPorId(id).getResumen());
        }
    }

    private void avanzarSeleccionado() {
        Integer id = idSeleccionado();
        if (id == null) {
            return;
        }
        try {
            pedidos.avanzarEstado(id);
        } catch (RuntimeException ex) {
            Estilo.mostrarError(this, ex);
        }
    }
}
