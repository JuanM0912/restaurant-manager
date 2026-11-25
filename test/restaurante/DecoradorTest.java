package restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import restaurante.modelo.Adicional;
import restaurante.modelo.Alimento;
import restaurante.modelo.ElementoPedido;
import restaurante.patrones.decorador.ConIndicacion;
import restaurante.patrones.decorador.ConIngredienteExtra;
import restaurante.patrones.decorador.ConTamanoGrande;

/** Pruebas del patron DECORATOR. */
public class DecoradorTest {

    private final Alimento hamburguesa = new Alimento("P1", "Hamburguesa", "Platos fuertes", 20000, 15);

    @Test
    void ingredienteExtraSumaPrecioYDescripcion() {
        ElementoPedido conQueso = new ConIngredienteExtra(hamburguesa, new Adicional("Queso", 3000));

        assertEquals(23000, conQueso.getPrecio());
        assertEquals("Hamburguesa + Queso", conQueso.getDescripcion());
    }

    @Test
    void sePuedenApilarVariosDecoradores() {
        ElementoPedido e = hamburguesa;
        e = new ConIngredienteExtra(e, new Adicional("Queso", 3000));
        e = new ConIngredienteExtra(e, new Adicional("Tocineta", 4000));
        e = new ConTamanoGrande(e);                 // (20000 + 3000 + 4000) * 1.3 = 35100
        e = new ConIndicacion(e, "sin cebolla");    // no cambia el precio

        assertEquals(35100, e.getPrecio());
        assertEquals("Hamburguesa + Queso + Tocineta (grande) [sin cebolla]", e.getDescripcion());
    }

    @Test
    void elProductoOriginalNoCambia() {
        new ConIngredienteExtra(hamburguesa, new Adicional("Queso", 3000));
        assertEquals(20000, hamburguesa.getPrecio());
    }
}
