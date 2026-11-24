package restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import restaurante.modelo.Alimento;
import restaurante.modelo.Pedido;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.patrones.estrategia.SinDescuento;
import restaurante.patrones.observador.NotificadorCaja;
import restaurante.patrones.observador.NotificadorCocina;
import restaurante.patrones.observador.NotificadorMesero;
import restaurante.persistencia.PedidoCSV;
import restaurante.servicio.ServicioPedidos;

/** Pruebas del patron OBSERVER. Usa una carpeta temporal para no tocar los datos reales. */
public class ObservadorPedidoTest {

    private ServicioPedidos servicio;
    private NotificadorCocina cocina;
    private NotificadorMesero mesero;
    private NotificadorCaja caja;

    @BeforeEach
    void preparar() throws IOException {
        Path carpeta = Files.createTempDirectory("pruebas-pedidos");
        List<EstrategiaDescuento> promociones = new ArrayList<>();
        promociones.add(new SinDescuento());
        servicio = new ServicioPedidos(
                new PedidoCSV(carpeta.resolve("pedidos.csv")),
                promociones);

        cocina = new NotificadorCocina();
        mesero = new NotificadorMesero();
        caja = new NotificadorCaja();
        servicio.agregarObservador(cocina);
        servicio.agregarObservador(mesero);
        servicio.agregarObservador(caja);
    }

    private Pedido registrarPedidoDePrueba() {
        Pedido p = servicio.nuevoPedido(3);
        p.agregarItem(new Alimento("P1", "Sopa", "Entradas", 10000, 10), 1);
        servicio.registrarPedido(p);
        return p;
    }

    @Test
    void cocinaSeEnteraDeLosPedidosNuevos() {
        registrarPedidoDePrueba();
        assertEquals(1, cocina.getMensajes().size());
        assertTrue(cocina.getMensajes().get(0).contains("Nuevo pedido"));
        assertEquals(0, mesero.getMensajes().size());
    }

    @Test
    void cadaAreaRecibeSoloLoQueLeInteresa() {
        Pedido p = registrarPedidoDePrueba();
        servicio.avanzarEstado(p.getId());   // En preparacion
        servicio.avanzarEstado(p.getId());   // Listo -> mesero
        servicio.avanzarEstado(p.getId());   // Entregado -> caja

        assertEquals(1, cocina.getMensajes().size());
        assertEquals(1, mesero.getMensajes().size());
        assertEquals(1, caja.getMensajes().size());
        assertTrue(caja.getMensajes().get(0).contains("Cobrar"));
    }

    @Test
    void unObservadorRetiradoYaNoRecibeAvisos() {
        servicio.quitarObservador(cocina);
        registrarPedidoDePrueba();
        assertEquals(0, cocina.getMensajes().size());
    }
}
