package restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import restaurante.modelo.Alimento;
import restaurante.modelo.Bebida;
import restaurante.modelo.Producto;
import restaurante.patrones.fabrica.CreadorAlimento;
import restaurante.patrones.fabrica.CreadorBebida;
import restaurante.patrones.fabrica.CreadorProducto;

/** Pruebas del patron FACTORY METHOD. */
public class CreadorProductoTest {

    @Test
    void creadorAlimentoFabricaUnAlimento() {
        CreadorProducto creador = new CreadorAlimento();
        Producto p = creador.registrar("P100", "Arepa", "Entradas", 6000, 8);

        assertInstanceOf(Alimento.class, p);
        assertEquals("ALIMENTO", p.getTipo());
        assertEquals("8 min", p.getDetalle());
    }

    @Test
    void creadorBebidaFabricaUnaBebida() {
        CreadorProducto creador = new CreadorBebida();
        Producto p = creador.registrar("P101", "Avena", "Bebidas", 5000, 300);

        assertInstanceOf(Bebida.class, p);
        assertEquals("300 ml", p.getDetalle());
    }

    @Test
    void noPermitePrecioCeroNiNombreVacio() {
        CreadorProducto creador = new CreadorAlimento();
        assertThrows(IllegalArgumentException.class, () -> creador.registrar("P1", "Arepa", "Entradas", 0, 5));
        assertThrows(IllegalArgumentException.class, () -> creador.registrar("P1", "  ", "Entradas", 5000, 5));
    }
}
