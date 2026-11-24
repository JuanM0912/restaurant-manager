package restaurante.servicio;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import restaurante.modelo.Alimento;
import restaurante.modelo.Bebida;
import restaurante.patrones.estrategia.DescuentoPorDia;
import restaurante.patrones.estrategia.DescuentoPorHorario;
import restaurante.patrones.estrategia.DescuentoPorcentual;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.patrones.estrategia.SinDescuento;
import restaurante.patrones.observador.NotificadorCaja;
import restaurante.patrones.observador.NotificadorCocina;
import restaurante.patrones.observador.NotificadorMesero;
import restaurante.persistencia.PedidoCSV;

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
        List<EstrategiaDescuento> promociones = List.of(
                new SinDescuento(),
                new DescuentoPorcentual("Cliente frecuente", 10),
                new DescuentoPorHorario("Happy hour", 20, LocalTime.of(15, 0), LocalTime.of(17, 0)),
                new DescuentoPorDia("Martes de descuento", 15, DayOfWeek.TUESDAY));

        servicioMenu = new ServicioMenu();
        cargarMenuInicial(servicioMenu);

        servicioPedidos = new ServicioPedidos(
                new PedidoCSV(carpetaDatos + "pedidos.csv"),
                promociones);

        servicioPedidos.agregarObservador(new NotificadorCocina());
        servicioPedidos.agregarObservador(new NotificadorMesero());
        servicioPedidos.agregarObservador(new NotificadorCaja());

        servicioReportes = new ServicioReportes(servicioPedidos);
    }

    private void cargarMenuInicial(ServicioMenu menu) {
        menu.agregarProducto(new Alimento("A01", "Hamburguesa Clasica", "Platos fuertes", 18000, 350));
        menu.agregarProducto(new Alimento("A02", "Porcion Papas", "Entradas", 8000, 220));
        menu.agregarProducto(new Bebida("B01", "Limonada Natural", "Bebidas", 7000, 400));
        menu.agregarProducto(new Bebida("B02", "Cafe Americano", "Bebidas", 6000, 250));
    }

    public ServicioMenu getServicioMenu() { return servicioMenu; }
    public ServicioPedidos getServicioPedidos() { return servicioPedidos; }
    public ServicioReportes getServicioReportes() { return servicioReportes; }
}