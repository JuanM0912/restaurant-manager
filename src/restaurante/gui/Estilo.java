package restaurante.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/** Colores, fuentes y ayudas visuales compartidas por todos los paneles (evita repetir codigo). */
public final class Estilo {

    public static final Color VERDE = new Color(32, 178, 140);
    public static final Color FONDO = new Color(246, 248, 250);
    public static final Color BORDE = new Color(225, 228, 232);
    public static final Color TEXTO_SUAVE = new Color(110, 115, 120);

    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font SUBTITULO = new Font("SansSerif", Font.BOLD, 15);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 13);

    private Estilo() { }

    public static JButton botonPrincipal(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(VERDE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        return boton;
    }

    public static JLabel etiqueta(String texto, Font fuente) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuente);
        return etiqueta;
    }

    public static void tarjeta(JComponent componente) {
        componente.setBackground(Color.WHITE);
        componente.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    }

    public static String pesos(long valor) {
        return String.format("$%,d", valor);
    }

    /** Muestra el mensaje de un error de negocio sin cerrar la aplicacion. */
    public static void mostrarError(java.awt.Component padre, Exception e) {
        JOptionPane.showMessageDialog(padre, e.getMessage(), "No se pudo completar", JOptionPane.WARNING_MESSAGE);
    }
}
