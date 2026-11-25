package restaurante.servicio;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import restaurante.patrones.estrategia.DescuentoPorDia;
import restaurante.patrones.estrategia.DescuentoPorHorario;
import restaurante.patrones.estrategia.DescuentoPorcentual;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.patrones.estrategia.SinDescuento;
import restaurante.patrones.fabrica.CreadorAlimento;
import restaurante.patrones.fabrica.CreadorBebida;
import restaurante.patrones.fabrica.CreadorProducto;
import restaurante.patrones.observador.NotificadorCaja;
import restaurante.patrones.observador.NotificadorCocina;
import restaurante.patrones.observador.NotificadorMesero;
import restaurante.persistencia.AdicionalCSV;
import restaurante.persistencia.PedidoCSV;
import restaurante.persistencia.ProductoCSV;

/**
 * Arma todo el sistema en un solo lugar. La consola y la ventana hacen
 * "new Sistema()" y reciben los MISMOS servicios: asi no se duplica logica.
 *
 * Aqui se registran los tipos de producto, las promociones y los observadores.
 * Para el cambio de requisito en vivo casi siempre basta con agregar una linea aqui
 * y una clase nueva.
 */
public class Sistema {

    private static final String CARPETA_DATOS = "datos/";

    private final ServicioMenu servicioMenu;
    private final ServicioPedidos servicioPedidos;
    private final ServicioReportes servicioReportes;

    public Sistema() {
        this(CARPETA_DATOS);
    }

    /** Permite usar otra carpeta de datos (util para pruebas). */
    public Sistema(String carpetaDatos) {
        // Tipos de producto (Factory Method)
        List<CreadorProducto> creadores = List.of(
                new CreadorAlimento(),
                new CreadorBebida());

        // Promociones disponibles (Strategy)
        List<EstrategiaDescuento> promociones = List.of(
                new SinDescuento(),
                new DescuentoPorcentual("Cliente frecuente", 10),
                new DescuentoPorHorario("Happy hour", 20, LocalTime.of(15, 0), LocalTime.of(17, 0)),
                new DescuentoPorDia("Martes de descuento", 15, DayOfWeek.TUESDAY));

        servicioMenu = new ServicioMenu(
                new ProductoCSV(carpetaDatos + "productos.csv"),
                new AdicionalCSV(carpetaDatos + "adicionales.csv"),
                creadores);

        servicioPedidos = new ServicioPedidos(
                new PedidoCSV(carpetaDatos + "pedidos.csv", carpetaDatos + "pedido_items.csv"),
                promociones);

        // Observadores (Observer)
        servicioPedidos.agregarObservador(new NotificadorCocina());
        servicioPedidos.agregarObservador(new NotificadorMesero());
        servicioPedidos.agregarObservador(new NotificadorCaja());

        servicioReportes = new ServicioReportes(servicioPedidos);
    }

    public ServicioMenu getServicioMenu() { return servicioMenu; }
    public ServicioPedidos getServicioPedidos() { return servicioPedidos; }
    public ServicioReportes getServicioReportes() { return servicioReportes; }
}
