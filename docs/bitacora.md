# Bitacora diaria del equipo

Cada integrante agrega una fila por cada dia de trabajo. Debe coincidir con sus commits en GitHub.

| Fecha | Integrante | Que hizo | Commits | Problemas y como se resolvieron |
|---|---|---|---|---|
| 2026-11-20 | Integrante 1 | Creo el repositorio, invito al equipo, subio la estructura y el modelo de productos | `feat: estructura del proyecto y modelo de productos` | |
| 2026-11-20 | Integrante 2 | Redacto alcance y requisitos | `docs: alcance y requisitos del sistema` | |
| 2026-11-20 | Integrante 3 | README inicial, bitacora, registro de IA y guias del equipo | `docs: README, bitacora, registro de IA y guias` | |
| 2026-11-21 | Integrante 1 | Implemento el patron creacional Factory Method para la creacion de alimentos y bebidas | `feat: Factory Method para crear alimentos y bebidas` | Sin inconvenientes en la logica de fabricas |
| 2026-11-22 | Integrante 1 | Desarrollo la capa de persistencia en CSV (`ArchivoCSV`, `ProductoCSV`, `AdicionalCSV`), sincronizo la rama `integrante1` con `main` y abrio el Pull Request | `feat: lectura y escritura de productos en CSV`, `merge: integrar historial base de origin/main en integrante1` | Historias de commits no relacionadas al conectar el repositorio local con `origin/main` y conflicto en `.gitignore`; se resolvio guardando documentos temporales con `git stash`, integrando con `--allow-unrelated-histories` y conservando el `.gitignore` base |