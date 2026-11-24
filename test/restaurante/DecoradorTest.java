package restaurante;

import org.junit.jupiter.api.Test;
import restaurante.modelo.Adicional;
import restaurante.modelo.Alimento;
import restaurante.modelo.ElementoPedido;
import restaurante.patrones.decorador.ConIndicacion;
import restaurante.patrones.decorador.ConIngredienteExtra;
import restaurante.patrones.decorador.ConTamanoGrande;

import static org.junit.jupiter.api.Assertions.*;

class DecoradorTest {

    @Test
    void deberiaAplicarDecoradoresYAcumularPrecioYDescripcion() {
        ElementoPedido base = new Alimento("P01", "Hamburguesa Clasica", "Platos fuertes", 15000, 15);
        Adicional quesoExtra = new Adicional("Queso", 3000);
        Adicional grande = new Adicional("Grande", 4000);
        Adicional sinCebolla = new Adicional("Sin cebolla", 0);

        ElementoPedido pedidoDecorado = new ConIndicacion(
                new ConTamanoGrande(
                        new ConIngredienteExtra(base, quesoExtra),
                        grande
                ),
                sinCebolla
        );

        assertEquals(22000L, pedidoDecorado.getPrecio());
        assertTrue(pedidoDecorado.getDescripcion().contains("Hamburguesa Clasica"));
        assertTrue(pedidoDecorado.getDescripcion().contains("Queso extra"));
        assertTrue(pedidoDecorado.getDescripcion().contains("Grande"));
        assertTrue(pedidoDecorado.getDescripcion().contains("Sin cebolla"));
    }
}