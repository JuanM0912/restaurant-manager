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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;
import restaurante.modelo.ItemPedido;
import restaurante.modelo.Pedido;
import restaurante.modelo.Producto;
import restaurante.patrones.decorador.ConIndicacion;
import restaurante.patrones.decorador.ConTamanoGrande;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.servicio.ServicioMenu;
import restaurante.servicio.ServicioPedidos;

/**
 * Pantalla para tomar pedidos:
 * filtros por categoria, tarjetas de productos y, a la derecha, el pedido actual.
 */
public class PanelTomarPedido extends JPanel implements Refrescable {

    private final ServicioMenu menu;
    private final ServicioPedidos pedidos;

    private Pedido pedidoActual;
    private String categoriaElegida = null; // null = todas

    private final JPanel panelCategorias = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JPanel cuadricula = new JPanel(new GridLayout(0, 3, 12, 12));
    private final JTextField buscador = new JTextField(18);

    private final JSpinner mesa = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
    private final DefaultListModel<String> modeloItems = new DefaultListModel<>();
    private final JList<String> listaItems = new JList<>(modeloItems);
    private final JComboBox<EstrategiaDescuento> comboPromo = new JComboBox<>();
    private final JLabel lblSubtotal = new JLabel();
    private final JLabel lblDescuento = new JLabel();
    private final JLabel lblTotal = new JLabel();

    public PanelTomarPedido(ServicioMenu menu, ServicioPedidos pedidos) {
        this.menu = menu;
        this.pedidos = pedidos;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Estilo.FONDO);

        add(crearZonaProductos(), BorderLayout.CENTER);
        add(crearZonaPedido(), BorderLayout.EAST);
        mesa.addChangeListener(e -> pedidoActual.setMesa((Integer) mesa.getValue()));

        nuevoPedido();
        refrescar();
    }

    // ===================== Zona izquierda: productos =====================

    private JPanel crearZonaProductos() {
        JPanel zona = new JPanel(new BorderLayout(8, 8));
        zona.setOpaque(false);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        panelCategorias.setOpaque(false);
        barra.add(panelCategorias, BorderLayout.CENTER);
        JPanel zonaBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        zonaBuscar.setOpaque(false);
        zonaBuscar.add(new JLabel("Buscar:"));
        zonaBuscar.add(buscador);
        barra.add(zonaBuscar, BorderLayout.EAST);
        buscador.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mostrarProductos(); }
            public void removeUpdate(DocumentEvent e) { mostrarProductos(); }
            public void changedUpdate(DocumentEvent e) { mostrarProductos(); }
        });

        cuadricula.setOpaque(false);
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(cuadricula, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        zona.add(barra, BorderLayout.NORTH);
        zona.add(scroll, BorderLayout.CENTER);
        return zona;
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

    private List<Producto> filtrarPorCategoria(String categoria) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : menu.listarProductos()) {
            if (categoria.equalsIgnoreCase(p.getCategoria())) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    private void mostrarCategorias() {
        panelCategorias.removeAll();
        panelCategorias.add(botonCategoria("Todos", null));
        for (String categoria : obtenerCategorias()) {
            panelCategorias.add(botonCategoria(categoria, categoria));
        }
        panelCategorias.revalidate();
        panelCategorias.repaint();
    }

    private JButton botonCategoria(String texto, String categoria) {
        boolean elegida = (categoria == null) ? categoriaElegida == null : categoria.equals(categoriaElegida);
        JButton boton = elegida ? Estilo.botonPrincipal(texto) : new JButton(texto);
        boton.addActionListener(e -> {
            categoriaElegida = categoria;
            mostrarCategorias();
            mostrarProductos();
        });
        return boton;
    }

    private void mostrarProductos() {
        List<Producto> lista = (categoriaElegida == null)
                ? menu.listarProductos()
                : filtrarPorCategoria(categoriaElegida);
        String texto = buscador.getText().trim().toLowerCase();

        cuadricula.removeAll();
        for (Producto p : lista) {
            if (texto.isEmpty() || p.getNombre().toLowerCase().contains(texto)) {
                cuadricula.add(crearTarjeta(p));
            }
        }
        cuadricula.revalidate();
        cuadricula.repaint();
    }

    private JPanel crearTarjeta(Producto p) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        Estilo.tarjeta(tarjeta);

        JLabel nombre = Estilo.etiqueta(p.getNombre(), Estilo.SUBTITULO);
        JLabel info = Estilo.etiqueta(p.getCategoria() + "  •  " + p.getDetalle(), Estilo.NORMAL);
        info.setForeground(Estilo.TEXTO_SUAVE);
        JLabel precio = Estilo.etiqueta(Estilo.pesos(p.getPrecio()), Estilo.SUBTITULO);
        precio.setForeground(Estilo.VERDE);

        JButton agregar = p.isDisponible() ? Estilo.botonPrincipal("Agregar") : new JButton("Agotado");
        agregar.setEnabled(p.isDisponible());
        agregar.addActionListener(e -> agregarProducto(p));

        tarjeta.add(nombre);
        tarjeta.add(info);
        tarjeta.add(precio);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(agregar);
        return tarjeta;
    }

    private void agregarProducto(Producto producto) {
        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JPanel filaCantidad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaCantidad.add(new JLabel("Cantidad:"));
        filaCantidad.add(cantidad);
        formulario.add(filaCantidad);

        JCheckBox grande = new JCheckBox("Tamano grande (+30%)");
        formulario.add(grande);
        JTextField indicacion = new JTextField(20);
        formulario.add(new JLabel("Indicacion para cocina (opcional):"));
        formulario.add(indicacion);

        int respuesta = JOptionPane.showConfirmDialog(this, formulario,
                "Agregar " + producto.getNombre(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (respuesta != JOptionPane.OK_OPTION) {
            return;
        }
try {
            ElementoPedido elemento = producto;
            if (grande.isSelected()) {
                long recargoGrande = Math.round(producto.getPrecio() * 0.30);
                Adicional extraGrande = new Adicional("Tamano grande", recargoGrande);
                elemento = new ConTamanoGrande(elemento, extraGrande);
            }
            String textoIndicacion = indicacion.getText().trim();
            if (!textoIndicacion.isEmpty()) {
                Adicional notaCocina = new Adicional(textoIndicacion, 0L);
                elemento = new ConIndicacion(elemento, notaCocina);
            }
            pedidoActual.agregarItem(elemento, (Integer) cantidad.getValue());
            actualizarPedido();
       } catch (RuntimeException ex) {
            String msg = (ex.getMessage() != null && !ex.getMessage().isBlank())
                    ? ex.getMessage()
                    : ex.toString();
            JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearZonaPedido() {
        JPanel zona = new JPanel(new BorderLayout(8, 8));
        Estilo.tarjeta(zona);
        zona.setPreferredSize(new Dimension(380, 0));

        JPanel arriba = new JPanel();
        arriba.setOpaque(false);
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        arriba.add(Estilo.etiqueta("Pedido actual", Estilo.TITULO));
        JPanel filaMesa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaMesa.setOpaque(false);
        filaMesa.add(new JLabel("Mesa:"));
        filaMesa.add(mesa);
        arriba.add(filaMesa);

        JPanel botonesItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botonesItem.setOpaque(false);
        JButton menos = new JButton("−");
        JButton mas = new JButton("+");
        JButton quitar = new JButton("Quitar");
        menos.addActionListener(e -> cambiarCantidadSeleccionada(-1));
        mas.addActionListener(e -> cambiarCantidadSeleccionada(+1));
        quitar.addActionListener(e -> quitarSeleccionado());
        botonesItem.add(new JLabel("Item seleccionado:"));
        botonesItem.add(menos);
        botonesItem.add(mas);
        botonesItem.add(quitar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);
        centro.add(new JScrollPane(listaItems), BorderLayout.CENTER);
        centro.add(botonesItem, BorderLayout.SOUTH);

        JPanel abajo = new JPanel(new GridLayout(0, 1, 4, 4));
        abajo.setOpaque(false);
        for (EstrategiaDescuento promo : pedidos.getPromociones()) {
            comboPromo.addItem(promo);
        }
        comboPromo.addActionListener(e -> {
            pedidoActual.setPromocion((EstrategiaDescuento) comboPromo.getSelectedItem());
            actualizarPedido();
        });
        abajo.add(new JLabel("Promocion:"));
        abajo.add(comboPromo);
        abajo.add(lblSubtotal);
        abajo.add(lblDescuento);
        lblTotal.setFont(Estilo.TITULO);
        abajo.add(lblTotal);
        JButton confirmar = Estilo.botonPrincipal("Confirmar pedido");
        confirmar.setPreferredSize(new Dimension(0, 42));
        confirmar.addActionListener(e -> confirmarPedido());
        abajo.add(confirmar);

        zona.add(arriba, BorderLayout.NORTH);
        zona.add(centro, BorderLayout.CENTER);
        zona.add(abajo, BorderLayout.SOUTH);
        return zona;
    }

    private void cambiarCantidadSeleccionada(int cambio) {
        int indice = listaItems.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        ItemPedido item = pedidoActual.getItems().get(indice);
        int nueva = item.getCantidad() + cambio;
        pedidoActual.cambiarCantidad(indice, Math.max(nueva, 0));
        actualizarPedido();
        if (indice < modeloItems.size()) {
            listaItems.setSelectedIndex(indice);
        }
    }

    private void quitarSeleccionado() {
        int indice = listaItems.getSelectedIndex();
        if (indice >= 0) {
            pedidoActual.quitarItem(indice);
            actualizarPedido();
        }
    }

    private void confirmarPedido() {
        try {
            pedidos.registrarPedido(pedidoActual);
            JOptionPane.showMessageDialog(this, "Pedido #" + pedidoActual.getId() + " enviado a cocina.\n"
                    + "Total: " + Estilo.pesos(pedidoActual.getTotal()));
            nuevoPedido();
        } catch (RuntimeException ex) {
            Estilo.mostrarError(this, ex);
        }
    }

    private void nuevoPedido() {
        pedidoActual = pedidos.nuevoPedido((Integer) mesa.getValue());
        if (comboPromo.getItemCount() > 0) {
            comboPromo.setSelectedIndex(0);
        }
        actualizarPedido();
    }

    private void actualizarPedido() {
        modeloItems.clear();
        for (ItemPedido item : pedidoActual.getItems()) {
            modeloItems.addElement(item.toString());
        }
        lblSubtotal.setText("Subtotal: " + Estilo.pesos(pedidoActual.getSubtotal()));
        lblDescuento.setText("Descuento: -" + Estilo.pesos(pedidoActual.getDescuento()));
        lblTotal.setText("Total: " + Estilo.pesos(pedidoActual.getTotal()));
    }

    @Override
    public void refrescar() {
        mostrarCategorias();
        mostrarProductos();
        actualizarPedido();
    }
}