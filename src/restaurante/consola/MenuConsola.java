package restaurante.consola;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import restaurante.modelo.Adicional;
import restaurante.modelo.ElementoPedido;
import restaurante.modelo.Pedido;
import restaurante.modelo.Producto;
import restaurante.patrones.estrategia.EstrategiaDescuento;
import restaurante.patrones.fabrica.CreadorProducto;
import restaurante.servicio.ServicioMenu;
import restaurante.servicio.ServicioPedidos;
import restaurante.servicio.ServicioReportes;
import restaurante.servicio.Sistema;

/**
 * Interfaz por consola. Solo pide datos, llama a los servicios y muestra resultados.
 * No calcula precios ni lee archivos: eso lo hacen los servicios.
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
                // Cualquier error de negocio (precio invalido, estado no permitido...) se muestra sin cerrar el programa.
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

    private void verPorCategoria() {
        List<String> categorias = menu.listarCategorias();
        for (int i = 0; i < categorias.size(); i++) {
            System.out.println((i + 1) + ". " + categorias.get(i));
        }
        int elegida = leerEntero("Numero de categoria: ") - 1;
        if (elegida >= 0 && elegida < categorias.size()) {
            imprimirProductos(menu.listarPorCategoria(categorias.get(elegida)));
        } else {
            System.out.println("Categoria no valida.");
        }
    }

    private void registrarProducto() {
        List<CreadorProducto> tipos = menu.getCreadores();
        for (int i = 0; i < tipos.size(); i++) {
            System.out.println((i + 1) + ". " + tipos.get(i).getTipo());
        }
        int t = leerEntero("Tipo de producto: ") - 1;
        if (t < 0 || t >= tipos.size()) {
            System.out.println("Tipo no valido.");
            return;
        }
        CreadorProducto creador = tipos.get(t);
        String nombre = leerTexto("Nombre: ");
        String categoria = leerTexto("Categoria (existentes: " + menu.listarCategorias() + "): ");
        long precio = leerEntero("Precio: ");
        int valor = leerEntero(creador.getEtiquetaValorEspecifico() + ": ");
        Producto nuevo = menu.registrarProducto(creador.getTipo(), nombre, categoria, precio, valor);
        System.out.println("Producto registrado: " + nuevo);
    }

    private void cambiarPrecio() {
        String codigo = leerTexto("Codigo del producto: ");
        long precio = leerEntero("Nuevo precio: ");
        menu.cambiarPrecio(codigo, precio);
        System.out.println("Precio actualizado.");
    }

    private void cambiarDisponibilidad() {
        String codigo = leerTexto("Codigo del producto: ");
        boolean disponible = leerTexto("Disponible? (s/n): ").equalsIgnoreCase("s");
        menu.cambiarDisponibilidad(codigo, disponible);
        System.out.println("Disponibilidad actualizada.");
    }

    // ================= PEDIDOS =================

    private void crearPedido() {
        Pedido pedido = pedidos.nuevoPedido(leerEntero("Numero de mesa: "));
        boolean seguir = true;
        while (seguir) {
            verMenu();
            Producto producto = menu.buscarPorCodigo(leerTexto("Codigo del producto: "));
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

    /** Pregunta por adicionales, tamano e indicaciones (patron Decorator en ServicioMenu). */
    private ElementoPedido personalizar(Producto producto) {
        List<Adicional> elegidos = new ArrayList<>();
        if (leerTexto("Agregar adicionales? (s/n): ").equalsIgnoreCase("s")) {
            List<Adicional> disponibles = menu.getAdicionales();
            for (int i = 0; i < disponibles.size(); i++) {
                System.out.println((i + 1) + ". " + disponibles.get(i));
            }
            String respuesta = leerTexto("Numeros separados por coma (ej: 1,3): ");
            for (String parte : respuesta.split(",")) {
                try {
                    int i = Integer.parseInt(parte.trim()) - 1;
                    if (i >= 0 && i < disponibles.size()) {
                        elegidos.add(disponibles.get(i));
                    }
                } catch (NumberFormatException e) {
                    // Se ignora lo que no sea numero.
                }
            }
        }
        boolean grande = leerTexto("Tamano grande (+30%)? (s/n): ").equalsIgnoreCase("s");
        String indicacion = leerTexto("Indicacion para cocina (Enter si no hay): ");
        return menu.personalizar(producto, elegidos, grande, indicacion);
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
        pedidos.avanzarEstado(id);   // Los notificadores (Observer) imprimen sus avisos
        System.out.println("Estado actual: " + pedidos.buscarPorId(id).getEstado());
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
