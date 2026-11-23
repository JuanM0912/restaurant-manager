package restaurante;

import org.junit.jupiter.api.Test;
import restaurante.modelo.Producto;
import restaurante.servicio.ServicioMenu;

import static org.junit.jupiter.api.Assertions.*;

class CreadorProductoTest {

    @Test
    void deberiaAgregarBuscarYCambiarDisponibilidadDeProductos() {
        ServicioMenu menu = new ServicioMenu();
        Producto bebida = new Producto("B01", "Jugo Natural", 6000, true);

        menu.agregarProducto(bebida);

        assertEquals(1, menu.listarProductos().size());
        assertEquals(1, menu.listarDisponibles().size());
        assertNotNull(menu.buscarPorCodigo("B01"));
        assertEquals("Jugo Natural", menu.buscarPorCodigo("b01").getDescripcion());

        assertTrue(menu.cambiarDisponibilidad("B01", false));
        assertEquals(0, menu.listarDisponibles().size());
    }
}