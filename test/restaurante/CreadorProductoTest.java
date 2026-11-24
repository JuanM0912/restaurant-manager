package restaurante;

import org.junit.jupiter.api.Test;

import restaurante.modelo.Bebida;
import restaurante.modelo.Producto;
import restaurante.servicio.ServicioMenu;

import static org.junit.jupiter.api.Assertions.*;

class CreadorProductoTest {

    @Test
    void deberiaAgregarBuscarYCambiarDisponibilidadDeProductos() {
        ServicioMenu menu = new ServicioMenu();
        Producto bebida = new Bebida("B01", "Jugo Natural", "Bebidas", 6000, 350);

        menu.agregarProducto(bebida);

        assertEquals(1, menu.listarProductos().size());
        assertEquals(1, menu.listarDisponibles().size());
        assertNotNull(menu.buscarPorCodigo("B01"));
        assertEquals("Jugo Natural", menu.buscarPorCodigo("b01").getDescripcion());

        assertTrue(menu.cambiarDisponibilidad("B01", false));
        assertEquals(0, menu.listarDisponibles().size());
    }
}