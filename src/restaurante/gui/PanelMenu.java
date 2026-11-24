package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import restaurante.modelo.Producto;
import restaurante.servicio.ServicioMenu;

/** Administracion del menu: ver productos y cambiar disponibilidad. */
public class PanelMenu extends JPanel implements Refrescable {

    private final ServicioMenu menu;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[] {"Codigo", "Tipo", "Nombre", "Categoria", "Precio", "Detalle", "Disponible"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    private final JComboBox<String> comboTipo = new JComboBox<>(new String[] {"Alimento", "Bebida"});
    private final JTextField campoNombre = new JTextField();
    private final JComboBox<String> comboCategoria = new JComboBox<>();
    private final JTextField campoPrecio = new JTextField();
    private final JLabel etiquetaValor = new JLabel("Detalle numerico:");
    private final JTextField campoValor = new JTextField();

    public PanelMenu(ServicioMenu menu) {
        this.menu = menu;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);

        JButton precio = new JButton("Cambiar precio");
        JButton disponibilidad = new JButton("Cambiar disponibilidad");
        precio.addActionListener(e -> cambiarPrecio());
        disponibilidad.addActionListener(e -> cambiarDisponibilidad());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(new JLabel("Producto seleccionado:"));
        botones.add(precio);
        botones.add(disponibilidad);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        centro.add(botones, BorderLayout.SOUTH);

        add(Estilo.etiqueta("Menu del restaurante", Estilo.TITULO), BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);
        refrescar();
    }

    private JPanel crearFormulario() {
        JPanel formulario = new JPanel(new GridLayout(0, 1, 4, 4));
        Estilo.tarjeta(formulario);
        formulario.setPreferredSize(new Dimension(300, 0));

        comboTipo.addActionListener(e -> actualizarEtiquetaValor());
        comboCategoria.setEditable(true);

        formulario.add(Estilo.etiqueta("Registrar producto", Estilo.SUBTITULO));
        formulario.add(new JLabel("Tipo:"));
        formulario.add(comboTipo);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(campoNombre);
        formulario.add(new JLabel("Categoria (elija o escriba una nueva):"));
        formulario.add(comboCategoria);
        formulario.add(new JLabel("Precio:"));
        formulario.add(campoPrecio);
        formulario.add(etiquetaValor);
        formulario.add(campoValor);
        JButton registrar = Estilo.botonPrincipal("Registrar");
        registrar.addActionListener(e -> registrar());
        formulario.add(registrar);

        actualizarEtiquetaValor();
        return formulario;
    }

    private void actualizarEtiquetaValor() {
        Object tipo = comboTipo.getSelectedItem();
        if ("Bebida".equals(tipo)) {
            etiquetaValor.setText("Capacidad (ml):");
        } else {
            etiquetaValor.setText("Peso o valor (g):");
        }
    }

    private void registrar() {
        JOptionPane.showMessageDialog(
                this,
                "El registro dinamico de productos no esta disponible en esta version del servicio.",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private String codigoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un producto en la tabla.");
            return null;
        }
        return (String) modelo.getValueAt(fila, 0);
    }

    private void cambiarPrecio() {
        if (codigoSeleccionado() == null) {
            return;
        }
        JOptionPane.showMessageDialog(
                this,
                "El cambio de precio directo no esta disponible en esta version del servicio.",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void cambiarDisponibilidad() {
        String codigo = codigoSeleccionado();
        if (codigo == null) {
            return;
        }
        Producto p = menu.buscarPorCodigo(codigo);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "No se encontro el producto seleccionado.");
            return;
        }
        menu.cambiarDisponibilidad(codigo, !p.isDisponible());
        refrescar();
    }

    private List<String> obtenerCategorias() {
        Set<String> categorias = new LinkedHashSet<>();
        for (Producto p : menu.listarProductos()) {
            if (p.getCategoria() != null && !p.getCategoria().isBlank()) {
                categorias.add(p.getCategoria());
            }
        }
        return new ArrayList<>(categorias);
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Producto p : menu.listarProductos()) {
            modelo.addRow(new Object[] {
                    p.getCodigo(), p.getTipo(), p.getNombre(), p.getCategoria(),
                    Estilo.pesos(p.getPrecio()), p.getDetalle(), p.isDisponible() ? "Si" : "No"});
        }
        Object escrita = comboCategoria.getSelectedItem();
        comboCategoria.removeAllItems();
        for (String categoria : obtenerCategorias()) {
            comboCategoria.addItem(categoria);
        }
        if (escrita != null) {
            comboCategoria.setSelectedItem(escrita);
        }
    }
}