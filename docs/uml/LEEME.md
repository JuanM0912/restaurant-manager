# Diagramas UML - Restaurant Manager

Copiar todo este contenido en la carpeta `docs/uml/` del repositorio.

| Archivo | Diagrama |
|---|---|
| 01-casos-de-uso | Casos de uso del sistema |
| 02-clases-general | Diagrama de clases general (todos los paquetes) |
| 03-patron-factory-method | Factory Method, con roles marcados como estereotipos |
| 04-patron-decorator | Decorator, con roles marcados |
| 05-patron-strategy | Strategy, con roles marcados |
| 06-patron-observer | Observer, con roles marcados |
| 07-secuencia-registrar-pedido | Secuencia del flujo principal (Decorator + Strategy + Observer) |
| 08-secuencia-avanzar-estado | Secuencia del cambio de estado (Observer) |

- `.png`: imagen para el documento de diseno y la presentacion.
- `.svg`: misma imagen en calidad vectorial (no se pixela al ampliar).
- `.puml`: codigo fuente PlantUML, para editar si cambia el codigo.

## Como editar un diagrama

1. En VS Code instalar la extension **PlantUML** (de jebbs).
2. Abrir el `.puml` y pulsar `Alt+D` para la vista previa.
3. Para exportar: `Ctrl+Shift+P` -> **PlantUML: Export Current Diagram** -> png.

Alternativa sin instalar nada: pegar el contenido del `.puml` en https://www.plantuml.com/plantuml
(reemplazar la linea `!include estilo.iuml` por el contenido de `estilo.iuml`).

Los diagramas corresponden al codigo del proyecto entregado. Si se cambia una clase o un metodo,
hay que actualizar el `.puml` correspondiente: el enunciado exige que los diagramas coincidan con el codigo.
