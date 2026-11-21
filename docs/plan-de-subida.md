# Plan de subida al repositorio (20 al 24 de noviembre)

El codigo completo ya existe y funciona, pero **cada integrante sube su propio modulo, en su propia rama,
el dia que corresponde**. Antes de cada commit, quien sube los archivos debe **ejecutarlos, probarlos y entender
cada clase**: en la sustentacion las preguntas son individuales.

Reglas:

1. Ramas: `integrante1`, `integrante2`, `integrante3`. Nadie trabaja directamente en `main`.
2. Cada dia: actualizar desde `main` -> pegar archivos -> ejecutar -> commit -> push -> Pull Request -> otro integrante aprueba -> merge.
3. **Orden de los merge cada dia: Integrante 1, luego 2, luego 3**, porque cada modulo usa el anterior.
4. Anotar el trabajo del dia en `docs/bitacora.md`.
5. Si algo no compila, no se sube: se pide ayuda con el mensaje de error exacto.

Paso a paso de Git: ver [`guia-git.md`](guia-git.md).

---

## Viernes 20 (noche)

| Integrante | Archivos | Mensaje de commit | Como comprobar |
|---|---|---|---|
| 1 | `.gitignore` (reemplaza el de GitHub), `.vscode/settings.json`, `lib/.gitkeep`, `datos/productos.csv`, `datos/adicionales.csv`, `src/restaurante/modelo/` → `ElementoPedido`, `Producto`, `Alimento`, `Bebida`, `Adicional` | `feat: estructura del proyecto y modelo de productos` | VS Code no marca errores rojos en `modelo` |
| 2 | `docs/requisitos.md` | `docs: alcance y requisitos del sistema` | Leerlo completo y ajustar lo que no entiendan |
| 3 | `README.md` (reemplaza el de GitHub), `docs/bitacora.md`, `docs/registro-ia.md`, `docs/plan-de-subida.md`, `docs/guia-git.md`, `docs/uml/.gitkeep` | `docs: README, bitacora, registro de IA y guias` | Se ve bien en GitHub |

## Sabado 21

| Integrante | Archivos | Mensajes de commit (uno por bloque) |
|---|---|---|
| 1 | `patrones/fabrica/` → `CreadorProducto`, `CreadorAlimento`, `CreadorBebida` | `feat: Factory Method para crear alimentos y bebidas` |
| 1 | `persistencia/` → `ArchivoCSV`, `ProductoCSV`, `AdicionalCSV` | `feat: lectura y escritura de productos en CSV` |
| 2 | `patrones/estrategia/` → `EstrategiaDescuento`, `SinDescuento`, `DescuentoPorcentual`, `DescuentoPorHorario`, `DescuentoPorDia` | `feat: Strategy para promociones` |
| 2 | `modelo/` → `EstadoPedido`, `ItemPedido`, `Pedido` | `feat: pedidos con estados y calculo de totales` |
| 3 | `test/restaurante/PedidoTest.java` (activar JUnit, ver guia) | `test: calculos y estados del pedido` |
| 3 | `docs/diseno.md` (completar la seccion 2 con palabras propias) | `docs: problemas de diseno` |

## Domingo 22

| Integrante | Archivos | Mensajes de commit |
|---|---|---|
| 1 (temprano) | `persistencia/` → `ElementoGuardado`, `PedidoCSV` | `feat: guardar y cargar pedidos en CSV` |
| 1 | `patrones/decorador/` → `AdicionalDecorador`, `ConIngredienteExtra`, `ConTamanoGrande`, `ConIndicacion` | `feat: Decorator para adicionales` |
| 1 | `servicio/ServicioMenu.java` | `feat: servicio del menu` |
| 1 | `test/` → `CreadorProductoTest`, `DecoradorTest` | `test: Factory Method y Decorator` |
| 2 | `patrones/observador/` → `ObservadorPedido`, `NotificadorBase`, `NotificadorCocina`, `NotificadorMesero`, `NotificadorCaja` | `feat: Observer para avisos de cocina, mesero y caja` |
| 2 | `servicio/` → `ServicioPedidos`, `ServicioReportes` | `feat: servicio de pedidos y cierre de caja` |
| 2 | `test/` → `EstrategiaDescuentoTest`, `ObservadorPedidoTest` | `test: Strategy y Observer` |
| 3 (en la tarde) | `servicio/Sistema.java`, `consola/` → `AppConsola`, `MenuConsola` | `feat: version por consola` |

Al final del domingo **la version por consola debe funcionar completa en los tres computadores**.

## Lunes 23

| Integrante | Archivos | Mensajes de commit |
|---|---|---|
| 3 | `gui/` → `AppGUI`, `Estilo`, `Refrescable`, `VentanaPrincipal` | `feat: ventana principal Swing` |
| 3 | `gui/PanelTomarPedido.java` | `feat: pantalla para tomar pedidos` |
| 3 | `gui/` → `PanelCocina`, `PanelPedidos` | `feat: pantallas de cocina y pedidos como observadores` |
| 3 | `gui/` → `PanelMenu`, `PanelReportes` | `feat: pantallas de menu y reportes` |
| 1 | `docs/uml/` → casos de uso y diagrama de clases general | `docs: diagramas de casos de uso y de clases` |
| 2 | `docs/uml/` → diagramas de cada patron y de secuencia | `docs: diagramas de patrones y de secuencia` |

## Martes 24 (entrega)

| Hora | Todos |
|---|---|
| Manana | Probar todo juntos (consola y ventana), corregir errores (`fix: ...`) |
| 12:00 | **Codigo congelado** |
| Tarde | Completar `docs/diseno.md` (cada uno sus patrones), conclusiones, README final con nombres, revisar que el UML coincida con el codigo, entregar |
| Noche | Ensayo de la sustentacion |
