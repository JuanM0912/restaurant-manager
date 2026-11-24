package restaurante.gui;

import javax.swing.SwingUtilities;

import restaurante.servicio.Sistema;

/** Punto de entrada de la version grafica. Usa el MISMO Sistema que la consola. */
public class AppGUI {

    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        // Swing debe crear las ventanas en su propio hilo (Event Dispatch Thread).
        SwingUtilities.invokeLater(() -> new VentanaPrincipal(sistema).setVisible(true));
    }
}
