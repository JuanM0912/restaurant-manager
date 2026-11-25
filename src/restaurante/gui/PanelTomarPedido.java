package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

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
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.servicio.ServicioMenu;
import restaurante.servicio.ServicioPedidos;

/**
 * Pantalla para tomar pedidos: filtros por categoria, tarjetas de productos
 * y, a la derecha, el pedido actual con la estetica vino/borgona.
 */
public class PanelTomarPedido extends JPanel implements Refrescable {

    private final ServicioMenu menu;
    private final ServicioPedidos pedidos;

    private Pedido pedidoActual;
    private String categoriaElegida = null;   // null = todas

    private final JPanel panelCategorias = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
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
        setLayout(new BorderLayout(14, 14));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        setBackground(Estilo.BLANCO_CALIDO);

        add(crearZonaProductos(), BorderLayout.CENTER);
        add(crearZonaPedido(), BorderLayout.EAST);
        mesa.addChangeListener(e -> pedidoActual.setMesa((Integer) mesa.getValue()));

        nuevoPedido();
        refrescar();
    }

    // ===================== Zona izquierda: productos =====================

    private JPanel crearZonaProductos() {
        JPanel zona = new JPanel(new BorderLayout(10, 10));
        zona.setOpaque(false);

        JPanel barra = new JPanel(new BorderLayout(10, 6));
        barra.setOpaque(false);
        panelCategorias.setOpaque(false);
        barra.add(panelCategorias, BorderLayout.CENTER);

        JPanel zonaBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        zonaBuscar.setOpaque(false);
        JLabel lblBuscar = Estilo.etiqueta("Buscar:", Estilo.NEGRITA);
        buscador.setFont(Estilo.NORMAL);
        buscador.setBackground(Color.WHITE);
        buscador.setForeground(Estilo.TEXTO);
        buscador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.ROSA_EMPOLVADO, 2),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        zonaBuscar.add(lblBuscar);
        zonaBuscar.add(buscador);
        barra.add(zonaBuscar, BorderLayout.EAST);

        buscador.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mostrarProductos(); }
            public void removeUpdate(DocumentEvent e) { mostrarProductos(); }
            public void changedUpdate(DocumentEvent e) { mostrarProductos(); }
        });

        cuadricula.setOpaque(false);
        JPanel contenedor = new JPanel(new BorderLayout());   // evita que las tarjetas se estiren
        contenedor.setOpaque(false);
        contenedor.add(cuadricula, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.ROSA_EMPOLVADO, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        scroll.getViewport().setBackground(Estilo.BLANCO_CALIDO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        zona.add(barra, BorderLayout.NORTH);
        zona.add(scroll, BorderLayout.CENTER);
        return zona;
    }

    private void mostrarCategorias() {
        panelCategorias.removeAll();
        panelCategorias.add(botonCategoria("Todos", null));
        for (String categoria : menu.listarCategorias()) {
            panelCategorias.add(botonCategoria(categoria, categoria));
        }
        panelCategorias.revalidate();
        panelCategorias.repaint();
    }

    private JButton botonCategoria(String texto, String categoria) {
        boolean elegida = (categoria == null) ? categoriaElegida == null : categoria.equals(categoriaElegida);
        JButton boton = elegida ? Estilo.botonAccionPequeno(texto) : Estilo.botonSecundario(texto);
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
                : menu.listarPorCategoria(categoriaElegida);
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
        nombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel info = Estilo.etiqueta(p.getCategoria() + "  •  " + p.getDetalle(), Estilo.NORMAL);
        info.setForeground(Estilo.TEXTO_SUAVE);
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel precio = Estilo.etiqueta(Estilo.pesos(p.getPrecio()), Estilo.PRECIO);
        precio.setForeground(Estilo.VINO_OSCURO);
        precio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton agregar = p.isDisponible() ? Estilo.botonAccionPequeno("Agregar") : Estilo.botonSecundario("Agotado");
        agregar.setEnabled(p.isDisponible());
        agregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (p.isDisponible()) {
            agregar.setIcon(Estilo.iconoCarrito(14, Color.WHITE));
            agregar.setIconTextGap(6);
        }
        agregar.addActionListener(e -> agregarProducto(p));

        tarjeta.add(nombre);
        tarjeta.add(Box.createVerticalStrut(3));
        tarjeta.add(info);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(precio);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(agregar);
        return tarjeta;
    }

    /** Muestra un cuadro para elegir cantidad y adicionales (Decorator en ServicioMenu). */
    private void agregarProducto(Producto producto) {
        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));

        JSpinner cantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JPanel filaCantidad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaCantidad.add(new JLabel("Cantidad:"));
        filaCantidad.add(cantidad);
        formulario.add(filaCantidad);

        formulario.add(new JLabel("Adicionales:"));
        List<JCheckBox> casillas = new ArrayList<>();
        for (Adicional a : menu.getAdicionales()) {
            JCheckBox casilla = new JCheckBox(a.toString());
            casillas.add(casilla);
            formulario.add(casilla);
        }
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

        List<Adicional> elegidos = new ArrayList<>();
        for (int i = 0; i < casillas.size(); i++) {
            if (casillas.get(i).isSelected()) {
                elegidos.add(menu.getAdicionales().get(i));
            }
        }
        try {
            ElementoPedido elemento = menu.personalizar(producto, elegidos, grande.isSelected(), indicacion.getText());
            pedidoActual.agregarItem(elemento, (Integer) cantidad.getValue());
            actualizarPedido();
        } catch (RuntimeException ex) {
            Estilo.mostrarError(this, ex);
        }
    }

    // ===================== Zona derecha: pedido actual =====================

    private JPanel crearZonaPedido() {
        JPanel zona = new JPanel(new BorderLayout());
        zona.setOpaque(true);
        zona.setBackground(Color.WHITE);
        zona.setBorder(BorderFactory.createLineBorder(Estilo.ROSA_EMPOLVADO, 2));
        zona.setPreferredSize(new Dimension(380, 0));

        // Encabezado degradado vino/borgona con icono de portapapeles
        JPanel cabecera = Estilo.bannerVino(false);
        cabecera.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel lblTitulo = new JLabel("Pedido actual");
        lblTitulo.setFont(Estilo.SECCION_BLANCA);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setIcon(Estilo.iconoPedido(18, Color.WHITE));
        lblTitulo.setIconTextGap(8);
        cabecera.add(lblTitulo);
        zona.add(cabecera, BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(8, 8));
        cuerpo.setOpaque(true);
        cuerpo.setBackground(Color.WHITE);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));

        JPanel filaMesa = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filaMesa.setOpaque(false);
        filaMesa.add(Estilo.etiqueta("Mesa:", Estilo.NEGRITA));
        mesa.setFont(Estilo.NEGRITA);
        filaMesa.add(mesa);
        cuerpo.add(filaMesa, BorderLayout.NORTH);

        listaItems.setFont(Estilo.NORMAL);
        listaItems.setForeground(Estilo.TEXTO);
        listaItems.setSelectionBackground(Estilo.ROSA_EMPOLVADO);
        listaItems.setSelectionForeground(Estilo.VINO_OSCURO);

        JScrollPane scrollLista = new JScrollPane(listaItems);
        scrollLista.setBorder(BorderFactory.createLineBorder(Estilo.ROSA_EMPOLVADO, 2));

        JPanel botonesItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        botonesItem.setOpaque(false);
        JButton menos = Estilo.botonSecundario("−");
        JButton mas = Estilo.botonSecundario("+");
        JButton quitar = Estilo.botonSecundario("Quitar");
        menos.addActionListener(e -> cambiarCantidadSeleccionada(-1));
        mas.addActionListener(e -> cambiarCantidadSeleccionada(+1));
        quitar.addActionListener(e -> quitarSeleccionado());
        botonesItem.add(Estilo.etiqueta("Item seleccionado:", Estilo.NEGRITA));
        botonesItem.add(menos);
        botonesItem.add(mas);
        botonesItem.add(quitar);

        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.setOpaque(false);
        centro.add(scrollLista, BorderLayout.CENTER);
        centro.add(botonesItem, BorderLayout.SOUTH);

        JPanel abajo = new JPanel(new GridLayout(0, 1, 5, 5));
        abajo.setOpaque(false);
        for (EstrategiaDescuento promo : pedidos.getPromociones()) {
            comboPromo.addItem(promo);
        }
        comboPromo.setFont(Estilo.NORMAL);
        comboPromo.setBackground(Estilo.GRIS_SUAVE);
        comboPromo.setForeground(Estilo.TEXTO);
        comboPromo.addActionListener(e -> {
            pedidoActual.setPromocion((EstrategiaDescuento) comboPromo.getSelectedItem());
            actualizarPedido();
        });

        lblSubtotal.setFont(Estilo.NEGRITA);
        lblSubtotal.setForeground(Estilo.TEXTO);
        lblDescuento.setFont(Estilo.NEGRITA);
        lblDescuento.setForeground(Estilo.TEXTO_SUAVE);
        lblTotal.setFont(Estilo.TITULO);
        lblTotal.setForeground(Estilo.VINO_OSCURO);

        abajo.add(Estilo.etiqueta("Promocion:", Estilo.NEGRITA));
        abajo.add(comboPromo);
        abajo.add(lblSubtotal);
        abajo.add(lblDescuento);
        abajo.add(lblTotal);

        JButton confirmar = Estilo.botonPrincipal("Confirmar pedido");
        confirmar.setIcon(Estilo.iconoCheck(15, Color.WHITE));
        confirmar.setIconTextGap(8);
        confirmar.setPreferredSize(new Dimension(0, 42));
        confirmar.addActionListener(e -> confirmarPedido());
        abajo.add(confirmar);

        cuerpo.add(centro, BorderLayout.CENTER);
        cuerpo.add(abajo, BorderLayout.SOUTH);
        zona.add(cuerpo, BorderLayout.CENTER);
        return zona;
    }

    private void cambiarCantidadSeleccionada(int cambio) {
        int indice = listaItems.getSelectedIndex();
        if (indice < 0) {
            return;
        }
        ItemPedido item = pedidoActual.getItems().get(indice);
        int nueva = item.getCantidad() + cambio;
        pedidoActual.cambiarCantidad(indice, Math.max(nueva, 0));   // 0 elimina el item
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
            pedidos.registrarPedido(pedidoActual);   // Aqui se avisa a los observadores
            JOptionPane.showMessageDialog(this, "Pedido #" + pedidoActual.getId() + " enviado a cocina.\n"
                    + "Total: " + Estilo.pesos(pedidoActual.getTotal()));
            nuevoPedido();
        } catch (RuntimeException ex) {
            Estilo.mostrarError(this, ex);
        }
    }

    /** Empieza un pedido en blanco para la mesa elegida. */
    private void nuevoPedido() {
        pedidoActual = pedidos.nuevoPedido((Integer) mesa.getValue());
        if (comboPromo.getItemCount() > 0) {
            comboPromo.setSelectedIndex(0);   // dispara el listener y deja "Sin promocion"
        }
        actualizarPedido();
    }

    private void actualizarPedido() {
        modeloItems.clear();
        for (ItemPedido item : pedidoActual.getItems()) {
            modeloItems.addElement(String.format("%d x %s  —  %s",
                    item.getCantidad(), item.getElemento().getDescripcion(), Estilo.pesos(item.getSubtotal())));
        }
        lblSubtotal.setText("Subtotal: " + Estilo.pesos(pedidoActual.getSubtotal()));
        lblDescuento.setText("Descuento: -" + Estilo.pesos(pedidoActual.getDescuento()));
        lblTotal.setText("Total: " + Estilo.pesos(pedidoActual.getTotal()));
    }

    @Override
    public void refrescar() {
        mostrarCategorias();
        mostrarProductos();
    }
}