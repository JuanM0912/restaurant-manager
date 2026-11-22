# Guia de Git y GitHub para el equipo (desde VS Code)

Requisito previo: haber hecho los pasos 1 a 7 (cuenta, instalar Git, configurar nombre y correo,
VS Code, crear repositorio, invitar y clonar).

## Conceptos en una frase

- **Repositorio:** la carpeta del proyecto con todo su historial.
- **Commit:** una "foto" guardada de los cambios, con un mensaje que explica que se hizo.
- **Rama (branch):** una copia de trabajo separada; cada integrante tiene la suya.
- **Push:** subir tus commits a GitHub.
- **Pull:** bajar a tu computador lo que otros subieron.
- **Pull Request (PR):** pedir que los cambios de tu rama entren a `main`, para que otro los revise.
- **Merge:** unir una rama con otra.

## Paso 8. Crear tu rama (solo la primera vez)

1. En la barra azul de abajo a la izquierda haz clic donde dice `main`.
2. Elige **+ Crear nueva rama...** y escribe tu nombre de rama: `integrante1`, `integrante2` o `integrante3`.
3. Pulsa Enter. Ahora la barra dice tu rama.
4. Haz clic en el icono de nube **Publicar rama** (Publish Branch).

Comandos equivalentes (Terminal -> Nuevo terminal):
```
git checkout -b integrante1
git push -u origin integrante1
```

## Rutina diaria (pasos 9 a 14)

### A. Antes de empezar: traer lo ultimo de `main`

1. Clic en el nombre de la rama (abajo a la izquierda) -> elige `main`.
2. Icono de Control de codigo fuente (ramas, barra izquierda) -> boton **Sincronizar cambios** (o `...` -> Extraer/Pull).
3. Vuelve a tu rama: clic abajo a la izquierda -> `integranteX`.
4. `Ctrl+Shift+P` -> escribe **Git: Merge Branch** -> elige `main`.

Comandos equivalentes:
```
git checkout main
git pull
git checkout integrante1
git merge main
```

### B. Paso 9. Agregar archivos

Crea los archivos en la carpeta exacta que indica el plan (clic derecho sobre la carpeta -> **Nuevo archivo**)
y pega el codigo. Guarda con `Ctrl+S`. Ejecuta y comprueba que funciona.

### C. Paso 10. Hacer el commit

1. Abre Control de codigo fuente (`Ctrl+Shift+G`).
2. En **Cambios**, pasa el raton sobre cada archivo tuyo y pulsa **+** (Stage). No agregues `bin/` ni pedidos de prueba.
3. Escribe el mensaje arriba, por ejemplo `feat: Strategy para promociones`.
4. Pulsa **✓ Confirmar** (Commit).

```
git add src/restaurante/patrones/estrategia
git commit -m "feat: Strategy para promociones"
```

### D. Paso 11. Subir (push)

Pulsa **Sincronizar cambios** (o el icono de nube). Si VS Code pide iniciar sesion en GitHub, acepta en el navegador.
```
git push
```

### E. Paso 12. Crear el Pull Request

1. En github.com, entra al repositorio. Aparece un aviso amarillo **Compare & pull request**: pulsalo.
   (Si no aparece: pestana **Pull requests** -> **New pull request** -> base: `main`, compare: tu rama.)
2. Titulo: lo que hiciste. Descripcion: archivos y como probarlo.
3. Pulsa **Create pull request**.

### F. Paso 13. Revisar y aprobar (lo hace OTRO integrante)

1. Pestana **Pull requests** -> abrir el PR del companero.
2. Pestana **Files changed**: leer el codigo.
3. **Review changes** -> **Approve** -> **Submit review**.

### G. Paso 14. Integrar a `main`

En el PR aprobado pulsar **Merge pull request** -> **Confirm merge**. Luego todos repiten el paso A.

## Paso 15. Resolver un conflicto sencillo

Un conflicto ocurre cuando dos personas cambiaron las mismas lineas. VS Code lo muestra asi:

```
<<<<<<< HEAD (Cambio actual)
lo que tu tienes
=======
lo que viene de main
>>>>>>> main (Cambio entrante)
```

1. Encima del bloque aparecen botones: **Aceptar cambio actual**, **Aceptar cambio entrante**, **Aceptar ambos**.
2. Elige el correcto (si dudas, pregunta al companero dueno del archivo).
3. Guarda, haz **+** (Stage) del archivo y **✓ Confirmar**. Luego **Sincronizar**.

Para evitar conflictos: cada uno modifica solo los archivos de su modulo, y siempre se hace el paso A antes de empezar.

## Errores frecuentes

| Mensaje | Que significa | Solucion |
|---|---|---|
| `Author identity unknown` | Falta configurar nombre y correo | Paso 3: `git config --global user.name` y `user.email` |
| `rejected ... fetch first` | Hay cambios en GitHub que no tienes | Haz *Pull* (Sincronizar) y luego *Push* |
| `Permission denied` / 403 | No aceptaste la invitacion o no iniciaste sesion | Aceptar invitacion en github.com/notifications; iniciar sesion desde VS Code |
| `datos/productos.csv` aparece modificado | Registraste productos mientras probabas | Si no querias cambiarlo: clic derecho -> **Descartar cambios** |
