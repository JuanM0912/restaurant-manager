# Alcance y requisitos — Restaurant Manager

Propuesta elegida: **Propuesta 3. Cafeteria o restaurante**.

## 1. Alcance

### Incluido

| Funcionalidad | Motivo |
|---|---|
| Menu con alimentos y bebidas por categorias | Base del sistema; area con variacion (tipos de producto) |
| Adicionales que cambian precio y descripcion | Area con variacion (combinaciones de extras) |
| Pedidos por mesa con varios productos y cantidades | Flujo principal del restaurante |
| Estados del pedido con avisos a cocina, mesero y caja | Area con variacion (quien recibe avisos) |
| Promociones porcentual, por horario y por dia | Area con variacion (reglas de descuento) |
| Cierre de caja y reporte de ventas | Pedido por el enunciado |
| Almacenamiento en CSV | Sencillo y se abre con Excel |
| Consola e interfaz Swing sobre el mismo nucleo | Exigido por el enunciado |

### Fuera del alcance

| Funcionalidad | Motivo |
|---|---|
| Combos armados paso a paso | Requiere otro patron (Builder) y no alcanza el tiempo (4 dias) |
| Usuarios, roles e inicio de sesion | No aporta al objetivo del parcial (patrones de diseno) |
| Promociones acumulables | Aumenta la complejidad; se aplica una promocion por pedido |
| Impuestos y propinas | No los pide el enunciado |
| Aplicacion web o base de datos | No son obligatorias ni dan puntos adicionales |

Areas con variacion o cambio frecuente: **tipos de producto, adicionales, promociones y avisos por estado** (cuatro; el minimo es tres).

## 2. Requisitos funcionales

| Codigo | Requisito |
|---|---|
| RF01 | Registrar un producto indicando tipo (alimento o bebida), nombre, categoria, precio y su dato propio (minutos de preparacion o volumen). |
| RF02 | Consultar el menu completo, por categoria y por nombre. |
| RF03 | Cambiar el precio y la disponibilidad de un producto. Un producto no disponible no se puede pedir. |
| RF04 | Personalizar un producto con adicionales, tamano grande e indicaciones; el precio y la descripcion se ajustan. |
| RF05 | Crear un pedido para una mesa y agregar varios productos con su cantidad. |
| RF06 | Modificar la cantidad o quitar productos mientras el pedido este Pendiente. |
| RF07 | Calcular subtotal, descuento y total del pedido. |
| RF08 | Aplicar una promocion al pedido: porcentual, por horario o por dia. |
| RF09 | Avanzar el estado del pedido solo al siguiente: Pendiente, En preparacion, Listo, Entregado. |
| RF10 | Avisar automaticamente a cocina (pedido nuevo), mesero (pedido listo) y caja (pedido entregado). |
| RF11 | Consultar los pedidos existentes y su detalle. |
| RF12 | Generar el cierre de caja de una fecha: pedidos entregados, descuentos y total vendido. |
| RF13 | Mostrar los productos mas vendidos. |
| RF14 | Guardar productos y pedidos en CSV y recuperarlos al iniciar. |

## 3. Requisitos no funcionales

| Codigo | Requisito |
|---|---|
| RNF01 | Desarrollado en Java 21 sin frameworks externos (solo JUnit para pruebas). |
| RNF02 | Dos interfaces (consola y Swing) que usan los mismos servicios, sin duplicar logica de negocio. |
| RNF03 | Los datos se guardan en CSV con separador `;`, legibles en Excel. |
| RNF04 | Agregar un tipo de producto, una promocion o un aviso nuevo no debe obligar a modificar las clases existentes (principio Abierto/Cerrado). |
| RNF05 | Los errores de uso (precio invalido, estado no permitido) se informan con un mensaje claro sin cerrar el programa. |
| RNF06 | Cada patron cuenta con pruebas unitarias. |
| RNF07 | Codigo organizado en paquetes con responsabilidades claras y versionado en GitHub con aportes de todos. |
