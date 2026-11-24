package restaurante.consola;

import restaurante.servicio.Sistema;

/** Punto de entrada de la version por consola. Ejecutar con el boton "Run" de VS Code. */
public class AppConsola {

    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        new MenuConsola(sistema).iniciar();
    }
}
