package restaurante.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import restaurante.servicio.ServicioReportes;

/** Cierre de caja y reporte de ventas. Usa el mismo ServicioReportes que la consola. */
public class PanelReportes extends JPanel implements Refrescable {

    private final ServicioReportes reportes;
    private final JTextField campoFecha = new JTextField(LocalDate.now().toString(), 10);
    private final JTextArea resultado = new JTextArea();

    public PanelReportes(ServicioReportes reportes) {
        this.reportes = reportes;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.add(Estilo.etiqueta("Cierre de caja", Estilo.TITULO));
        barra.add(new JLabel("   Fecha (AAAA-MM-DD):"));
        barra.add(campoFecha);
        JButton generar = Estilo.botonPrincipal("Generar");
        generar.addActionListener(e -> refrescar());
        barra.add(generar);

        resultado.setEditable(false);
        resultado.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 14));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(resultado), BorderLayout.CENTER);
        refrescar();
    }

    @Override
    public void refrescar() {
        try {
            LocalDate fecha = LocalDate.parse(campoFecha.getText().trim());
            resultado.setText(reportes.cierreDeCaja(fecha));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Escriba la fecha asi: 2026-11-24");
        }
    }
}
