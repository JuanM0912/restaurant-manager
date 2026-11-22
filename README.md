# Restaurant Manager

Sistema de gestion de pedidos para un restaurante, desarrollado en Java como parcial de
**Patrones de Diseno** (Ingenieria de Software). Funciona por **consola** y con **interfaz grafica Swing**;
ambas versiones usan exactamente las mismas clases de negocio. La informacion se guarda en archivos **CSV**
que se pueden abrir con Excel.

## Funcionalidades implementadas

| Area | Funcionalidad |
|---|---|
| Menu | Registrar productos (alimentos y bebidas), consultar, filtrar por categoria, buscar, cambiar precio y disponibilidad |
| Personalizacion | Adicionales (queso, tocineta...), tamano grande (+30 %) e indicaciones para cocina; cambian precio y descripcion |
| Pedidos | Crear pedido por mesa, agregar varios productos, cambiar cantidades, subtotal, descuento y total |
| Estados | Pendiente -> En preparacion -> Listo -> Entregado (solo se avanza al estado siguiente) |
| Avisos | Cocina, mesero y caja reciben avisos automaticos segun el estado; la pantalla de cocina se actualiza sola |
| Promociones | Porcentual, por horario (happy hour) y por dia de la semana |
| Reportes | Cierre de caja del dia: pedidos entregados, descuentos, total vendido y productos mas vendidos |

## Patrones de diseno

| Patron | Categoria | Problema que resuelve | Clases principales |
|---|---|---|---|
| Factory Method | Creacional | Crear distintos tipos de producto sin `switch` repetidos | `CreadorProducto`, `CreadorAlimento`, `CreadorBebida` |
| Decorator | Estructural | Adicionales combinables sin una clase por combinacion | `AdicionalDecorador`, `ConIngredienteExtra`, `ConTamanoGrande`, `ConIndicacion` |
| Strategy | Comportamiento | Promociones intercambiables sin `if` en `Pedido` | `EstrategiaDescuento`, `DescuentoPorcentual`, `DescuentoPorHorario`, `DescuentoPorDia` |
| Observer | Comportamiento | Avisar a cocina, mesero y caja sin acoplarlos al pedido | `ServicioPedidos` (sujeto), `ObservadorPedido`, `Notificador*`, `PanelCocina` |

La explicacion completa esta en [`docs/diseno.md`](docs/diseno.md).

## Estructura

```
src/restaurante/
  modelo/        Clases del negocio (Producto, Pedido, ItemPedido, EstadoPedido...)
  patrones/      fabrica/, decorador/, estrategia/, observador/
  servicio/      ServicioMenu, ServicioPedidos, ServicioReportes y Sistema (arma todo)
  persistencia/  Lectura y escritura de CSV
  consola/       AppConsola (main) y MenuConsola
  gui/           AppGUI (main) y ventanas Swing
test/restaurante/ Pruebas unitarias JUnit 5
datos/           productos.csv, adicionales.csv (y pedidos.csv al usar el programa)
docs/            Requisitos, diseno, UML, bitacora y registro de uso de IA
```

Regla de arquitectura: `consola` y `gui` solo usan los **servicios**; nunca leen CSV ni calculan precios.

## Requisitos para ejecutar

- JDK 21 (Eclipse Temurin)
- Visual Studio Code con la extension **Extension Pack for Java**

## Como ejecutar

**Desde VS Code (recomendado):** abrir la carpeta del proyecto, abrir
`src/restaurante/consola/AppConsola.java` o `src/restaurante/gui/AppGUI.java` y pulsar **Run** encima de `main`.

**Desde la terminal (PowerShell), ubicados en la carpeta del proyecto:**

```powershell
javac -d bin -encoding UTF-8 (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin restaurante.consola.AppConsola     # version consola
java -cp bin restaurante.gui.AppGUI             # version grafica
```

El programa debe ejecutarse desde la carpeta raiz del proyecto para encontrar la carpeta `datos/`.

## Pruebas unitarias

En VS Code: panel **Testing** (icono de matraz) -> **Enable Java Tests** -> **JUnit Jupiter**.
Luego pulsar *Run Tests*. Hay pruebas para cada patron: `CreadorProductoTest`, `DecoradorTest`,
`EstrategiaDescuentoTest`, `ObservadorPedidoTest` y `PedidoTest`.

## Datos (CSV)

Separador `;` para que Excel en espanol los abra en columnas. Para agregar un adicional nuevo basta con
agregar una fila en `datos/adicionales.csv`. Las categorias son texto: una categoria nueva se crea al registrar
un producto con ella.

## Integrantes

| Integrante | Responsabilidad |
|---|---|
| Integrante 1 — _(nombre)_ | Productos, Factory Method, Decorator y persistencia CSV |
| Integrante 2 — _(nombre)_ | Pedidos, estados, Strategy, Observer y reportes |
| Integrante 3 — _(nombre)_ | Consola, interfaz Swing, integracion y pruebas generales |

## Uso de inteligencia artificial

Se declara en [`docs/registro-ia.md`](docs/registro-ia.md).
