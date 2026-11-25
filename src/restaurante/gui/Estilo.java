package restaurante.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/** Paleta vino/borgoña, fuentes, iconos y ayudas visuales compartidas para Restaurant Manager. */
public final class Estilo {

    // Paleta principal solicitada
    public static final Color VINO_OSCURO = new Color(0x69, 0x0D, 0x2B);
    public static final Color BORGONA = new Color(0x8B, 0x12, 0x3A);
    public static final Color BORGONA_HOVER = new Color(0x73, 0x0E, 0x30);
    public static final Color ROSA_EMPOLVADO = new Color(0xEA, 0xD9, 0xDF);
    public static final Color BLANCO_CALIDO = new Color(0xFA, 0xF8, 0xF8);
    public static final Color GRIS_SUAVE = new Color(0xE8, 0xE5, 0xE6);

    // Colores de apoyo compatibles con los paneles existentes
    public static final Color VERDE = BORGONA;
    public static final Color FONDO = BLANCO_CALIDO;
    public static final Color BORDE = ROSA_EMPOLVADO;
    public static final Color TEXTO = VINO_OSCURO;
    public static final Color TEXTO_SUAVE = new Color(0x6E, 0x5A, 0x62);

    // Tipografías
    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 22);
    public static final Font SUBTITULO = new Font("SansSerif", Font.BOLD, 16);
    public static final Font SECCION_BLANCA = new Font("SansSerif", Font.BOLD, 17);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font NEGRITA = new Font("SansSerif", Font.BOLD, 13);
    public static final Font PRECIO = new Font("SansSerif", Font.BOLD, 14);
    public static final Font MONO = new Font("Monospaced", Font.PLAIN, 13);

    private Estilo() {
    }

    /** Botón principal en borgoña (#8B123A) con texto blanco y esquinas redondeadas. */
    public static JButton botonPrincipal(String texto) {
        return crearBotonRedondeado(texto, BORGONA, BORGONA_HOVER, Color.WHITE, null, new Insets(9, 16, 9, 16));
    }

    /** Botón compacto en borgoña para tarjetas de producto (ej. "Agregar"). */
    public static JButton botonAccionPequeno(String texto) {
        return crearBotonRedondeado(texto, BORGONA, BORGONA_HOVER, Color.WHITE, null, new Insets(6, 12, 6, 12));
    }

    /** Botón secundario en gris suave (#E8E5E6) con borde rosa empolvado (#EAD9DF). */
    public static JButton botonSecundario(String texto) {
        return crearBotonRedondeado(texto, GRIS_SUAVE, ROSA_EMPOLVADO, VINO_OSCURO, ROSA_EMPOLVADO, new Insets(7, 13, 7, 13));
    }

    private static JButton crearBotonRedondeado(
            String texto,
            Color fondoNormal,
            Color fondoHover,
            Color colorTexto,
            Color colorBorde,
            Insets margen) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color actual = getModel().isRollover() || getModel().isArmed() ? fondoHover : fondoNormal;
                int w = getWidth();
                int h = getHeight();

                Shape round = new RoundRectangle2D.Float(0, 0, w - 1f, h - 1f, 12f, 12f);
                g2.setColor(actual);
                g2.fill(round);

                if (colorBorde != null) {
                    g2.setColor(colorBorde);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.draw(round);
                }

                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setForeground(colorTexto);
        boton.setFont(NEGRITA);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(margen);
        return boton;
    }

    public static JLabel etiqueta(String texto, Font fuente) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuente);
        etiqueta.setForeground(TEXTO);
        return etiqueta;
    }

    public static JLabel etiquetaSuave(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(NORMAL);
        etiqueta.setForeground(TEXTO_SUAVE);
        return etiqueta;
    }

    /** Tarjeta blanca con borde rosa empolvado (#EAD9DF) y padding cómodo. */
    public static void tarjeta(JComponent componente) {
        componente.setOpaque(true);
        componente.setBackground(Color.WHITE);
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ROSA_EMPOLVADO, 2),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
    }

    /** Panel con degradado vino oscuro -> borgoña para encabezados superiores o banners. */
    public static JPanel bannerVino(boolean conAcentosInferiores) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                GradientPaint gp = new GradientPaint(0, 0, VINO_OSCURO, w, h, BORGONA);
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);

                g2.setColor(new Color(0xAD, 0x26, 0x52, 75));
                int[] px = { (int) (w * 0.72), (int) (w * 0.84), (int) (w * 0.68), (int) (w * 0.56) };
                int[] py = { 0, 0, h, h };
                g2.fillPolygon(px, py, 4);

                if (conAcentosInferiores) {
                    g2.setColor(ROSA_EMPOLVADO);
                    g2.fillRect(0, h - 4, w, 4);
                }
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(NORMAL);
        tabla.setForeground(TEXTO);
        tabla.setBackground(Color.WHITE);
        tabla.setSelectionBackground(ROSA_EMPOLVADO);
        tabla.setSelectionForeground(VINO_OSCURO);
        tabla.setRowHeight(30);
        tabla.setShowGrid(true);
        tabla.setGridColor(ROSA_EMPOLVADO);
        tabla.setFillsViewportHeight(true);

        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setFont(NEGRITA);
            header.setBackground(GRIS_SUAVE);
            header.setForeground(VINO_OSCURO);
        }
    }

    /** Formato en pesos con punto de miles (ej. $5.000). */
    public static String pesos(long valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.of("es", "CO"));
        simbolos.setGroupingSeparator('.');
        DecimalFormat formato = new DecimalFormat("#,##0", simbolos);
        return "$" + formato.format(valor);
    }

    public static void mostrarError(Component padre, Exception e) {
        JOptionPane.showMessageDialog(padre, e.getMessage(), "No se pudo completar", JOptionPane.WARNING_MESSAGE);
    }

    // ================= ICONOS VECTORIALES (Graphics2D) =================

    /** Gorro de chef para el encabezado principal. */
    public static Icon iconoChef(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                float s = size / 24f;
                g2.scale(s, s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.fillOval(4, 3, 8, 8);
                g2.fillOval(8, 2, 8, 8);
                g2.fillOval(12, 3, 8, 8);
                g2.fillRect(6, 7, 12, 6);

                g2.fillRect(6, 14, 12, 3);
                g2.drawRoundRect(6, 17, 12, 4, 2, 2);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /** Calendario para la fecha en el encabezado. */
    public static Icon iconoCalendario(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                float s = size / 20f;
                g2.scale(s, s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawRoundRect(2, 4, 16, 14, 3, 3);
                g2.drawLine(2, 8, 18, 8);
                g2.drawLine(6, 2, 6, 6);
                g2.drawLine(14, 2, 14, 6);
                g2.fillRect(5, 11, 2, 2);
                g2.fillRect(9, 11, 2, 2);
                g2.fillRect(13, 11, 2, 2);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /** Carrito de compras para el botón "Agregar". */
    public static Icon iconoCarrito(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                float s = size / 20f;
                g2.scale(s, s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawLine(2, 4, 5, 4);
                g2.drawLine(5, 4, 7, 12);
                g2.drawLine(7, 12, 16, 12);
                g2.drawLine(5, 6, 17, 6);
                g2.drawLine(17, 6, 16, 12);
                g2.fillOval(7, 14, 3, 3);
                g2.fillOval(14, 14, 3, 3);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /** Chulo (check) para el botón "Confirmar pedido". */
    public static Icon iconoCheck(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                float s = size / 20f;
                g2.scale(s, s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawLine(4, 10, 8, 14);
                g2.drawLine(8, 14, 16, 6);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /** Portapapeles para el título "Pedido actual". */
    public static Icon iconoPedido(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                float s = size / 20f;
                g2.scale(s, s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawRoundRect(4, 4, 12, 14, 2, 2);
                g2.drawRoundRect(7, 2, 6, 3, 1, 1);
                g2.drawLine(7, 9, 13, 9);
                g2.drawLine(7, 13, 12, 13);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }

    /** Campana de servicio para la esquina inferior izquierda. */
    public static Icon iconoCampana(int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                float s = size / 24f;
                g2.scale(s, s);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawArc(4, 6, 16, 14, 0, 180);
                g2.drawLine(2, 16, 22, 16);
                g2.drawLine(12, 4, 12, 6);
                g2.drawLine(10, 4, 14, 4);
                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
}