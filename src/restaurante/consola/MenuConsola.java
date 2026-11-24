package restaurante.consola;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import restaurante.modelo.ElementoPedido;
import restaurante.modelo.Pedido;
import restaurante.modelo.Producto;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.servicio.ServicioMenu;
import restaurante.servicio.ServicioPedidos;
import restaurante.servicio.ServicioReportes;
import restaurante.servicio.Sistema;

/**
 * Interfaz por consola. Solo pide datos, llama a los servicios y muestra resultados.
 */
public class MenuConsola {

    private final ServicioMenu menu;
    private final ServicioPedidos pedidos;
    private final ServicioReportes reportes;
    private final Scanner teclado = new Scanner(System.in);

    public MenuConsola(Sistema sistema) {
        this.menu = sistema.getServicioMenu();
        this.pedidos = sistema.getServicioPedidos();
        this.reportes = sistema.getServicioReportes();
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerEntero("Elija una opcion: ");
            try {
                ejecutar(opcion);
            } catch (RuntimeException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
        System.out.println("Hasta pronto.");
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("========== RESTAURANT MANAGER ==========");
        System.out.println(" MENU                    PEDIDOS");
        System.out.println(" 1. Ver menu             6. Crear pedido");
        System.out.println(" 2. Ver por categoria    7. Ver pedidos");
        System.out.println(" 3. Registrar producto   8. Modificar cantidad");
        System.out.println(" 4. Cambiar precio       9. Avanzar estado");
        System.out.println(" 5. Cambiar disponib.   10. Cierre de caja");
        System.out.println(" 0. Salir");
    }

    private void ejecutar(int opcion) {
        switch (opcion) {
            case 1 -> verMenu();
            case 2 -> verPorCategoria();
            case 3 -> registrarProducto();
            case 4 -> cambiarPrecio();
            case 5 -> cambiarDisponibilidad();
            case 6 -> crearPedido();
            case 7 -> verPedidos();
            case 8 -> modificarCantidad();
            case 9 -> avanzarEstado();
            case 10 -> System.out.println(reportes.cierreDeCaja(LocalDate.now()));
            case 0 -> { }
            default -> System.out.println("Opcion no valida.");
        }
    }

    // ================= MENU =================

    private void verMenu() {
        imprimirProductos(menu.listarProductos());
    }

    private List<String> obtenerCategorias() {
        Set<String> categorias = new LinkedHashSet<>();
        for (Producto p : menu.listarProductos()) {
            if (p.getCategoria() != null && !p.getCategoria().isBlank()) {
                categorias.add(p.getCategoria());
            }
        }
        return new ArrayList<>(categorias);
    }

    private List<Producto> filtrarPorCategoria(String categoria) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : menu.listarProductos()) {
            if (categoria.equalsIgnoreCase(p.getCategoria())) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    private void verPorCategoria() {
        List<String> categorias = obtenerCategorias();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias registradas.");
            return;
        }
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i));
        }
        int elegida = leerEntero("Numero de categoria: ") - 1;
        if (elegida >= 0 && elegida < categorias.size()) {
            imprimirProductos(filtrarPorCategoria(categorias.get(elegida)));
        } else {
            System.out.println("Categoria no valida.");
        }
    }

    private void registrarProducto() {
        System.out.println("El registro dinamico de productos no esta disponible en esta version del servicio.");
        System.out.println("Productos actuales en el menu:");
        verMenu();
    }

    private void cambiarPrecio() {
        System.out.println("El cambio de precio directo no esta disponible en esta version del servicio.");
    }

    private void cambiarDisponibilidad() {
        String codigo = leerTexto("Codigo del producto: ");
        boolean disponible = leerTexto("Disponible? (s/n): ").equalsIgnoreCase("s");
        boolean actualizada = menu.cambiarDisponibilidad(codigo, disponible);
        if (actualizada) {
            System.out.println("Disponibilidad actualizada.");
        } else {
            System.out.println("No se encontro un producto con ese codigo.");
        }
    }

    // ================= PEDIDOS =================

    private void crearPedido() {
        Pedido pedido = pedidos.nuevoPedido(leerEntero("Numero de mesa: "));
        boolean seguir = true;
        while (seguir) {
            verMenu();
            String codigo = leerTexto("Codigo del producto: ");
            Producto producto = menu.buscarPorCodigo(codigo);
            if (producto == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }
            int cantidad = leerEntero("Cantidad: ");
            ElementoPedido elemento = personalizar(producto);
            pedido.agregarItem(elemento, cantidad);
            System.out.println(pedido.getResumen());
            seguir = leerTexto("Agregar otro producto? (s/n): ").equalsIgnoreCase("s");
        }
        elegirPromocion(pedido);
        pedidos.registrarPedido(pedido);
        System.out.println("Pedido registrado:");
        System.out.println(pedido.getResumen());
    }

    private ElementoPedido personalizar(Producto producto) {
        return producto;
    }

    private void elegirPromocion(Pedido pedido) {
        List<EstrategiaDescuento> promos = pedidos.getPromociones();
        for (int i = 0; i < promos.size(); i++) {
            System.out.println((i + 1) + ". " + promos.get(i).getNombre());
        }
        int p = leerEntero("Promocion: ") - 1;
        if (p >= 0 && p < promos.size()) {
            pedido.setPromocion(promos.get(p));
        }
    }

    private void verPedidos() {
        List<Pedido> lista = pedidos.listarPedidos();
        if (lista.isEmpty()) {
            System.out.println("No hay pedidos.");
        }
        for (Pedido p : lista) {
            System.out.println(p.getResumen());
            System.out.println();
        }
    }

    private void modificarCantidad() {
        Pedido pedido = pedidos.buscarPorId(leerEntero("Numero de pedido: "));
        if (pedido == null) {
            System.out.println("Pedido no encontrado.");
            return;
        }
        System.out.println(pedido.getResumen());
        int item = leerEntero("Numero de item: ") - 1;
        int cantidad = leerEntero("Nueva cantidad (0 para quitar): ");
        pedidos.cambiarCantidad(pedido.getId(), item, cantidad);
        System.out.println(pedido.getResumen());
    }

    private void avanzarEstado() {
        for (Pedido p : pedidos.listarActivos()) {
            System.out.println(p);
        }
        int id = leerEntero("Numero de pedido: ");
        pedidos.avanzarEstado(id);
        Pedido actualizado = pedidos.buscarPorId(id);
        if (actualizado != null) {
            System.out.println("Estado actual: " + actualizado.getEstado());
        }
    }

    // ================= AYUDAS =================

    private void imprimirProductos(List<Producto> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay productos.");
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escriba un numero entero, sin puntos ni signos.");
            }
        }
    }
}