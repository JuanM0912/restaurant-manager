package restaurante.patrones.observador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Codigo comun de los notificadores: guardar los mensajes recibidos y mostrarlos.
 * Guardar los mensajes permite revisarlos en las pruebas unitarias.
 */
public abstract class NotificadorBase implements ObservadorPedido {

    private final String area;
    private final List<String> mensajes = new ArrayList<>();

    protected NotificadorBase(String area) {
        this.area = area;
    }

    protected void avisar(String mensaje) {
        String texto = "[" + area + "] " + mensaje;
        mensajes.add(texto);
        System.out.println(texto);
    }

    public List<String> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }
}
