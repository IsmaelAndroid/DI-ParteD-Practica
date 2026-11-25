# DI · 2.º DAM · Práctica de la Parte D (componentes propios)

Cinco ejercicios con el **mismo formato que la Parte D del examen práctico**: programas el kit de componentes de una app en cuatro apartados.

| Apartado | Qué programas | Puntos |
|---|---|---|
| D.1 | Un `enum class` con nombre, emoji y color, y un **avatar circular** que lo muestra | 0,5 |
| D.2 | Una **lista de casillas de selección múltiple** con máximo, sin estado (completas la cabecera y escribes el cuerpo) | 0,9 |
| D.3 | Un **configurador** con campo, `Slider` y `Switch` y resultado siempre al día, que usa una clase de lógica que ya te dan | 1 |
| D.4 | Una **fila desplegable** con un `Switch` (tú decides sus parámetros) | 0,8 |

> Lo que se valora es lo mismo que en el examen: **dónde está el estado**, **cómo se propagan los eventos** y **si el componente es reutilizable** (`modifier`, parámetros opcionales, colores del tema, semántica para TalkBack).

## Ejercicios

Cada ejercicio es un fichero `.kt` en `app/src/main/java/es/dam/parted/ejercicios/`:

| Fichero | Temática | El kit de componentes |
|---|---|---|
| `Ejercicio1MarioKart.kt` | Mario Kart | Avatar de objeto, lista de circuitos, configurador del kart, fila de piloto |
| `Ejercicio2Zelda.kt` | Zelda | Avatar de elemento, lista de ingredientes, configurador de receta, fila de arma |
| `Ejercicio3Fifa.kt` | FIFA | Avatar de posición, lista de convocados, configurador de fichaje, fila de jugador |
| `Ejercicio4Valorant.kt` | Valorant | Avatar de rol, lista de armas, configurador de ronda, fila de agente |
| `Ejercicio5AnimalCrossing.kt` | Animal Crossing | Avatar de especie, lista de donaciones, configurador de reforma, fila de vecino |

En cada fichero tienes:

1. **Arriba**, el enunciado de los cuatro apartados, con dibujos de cómo tiene que quedar.
2. **En medio**, el código que te dan (la clase de lógica de la D.3). **No lo modifiques.**
3. Después, el bloque `✍ TU SOLUCIÓN`, donde escribes tus componentes.
4. **Al final**, la pantalla de prueba, comentada. Descoméntala cuando tengas los componentes.

## Cómo trabajar

1. Clona el repositorio y ábrelo en Android Studio (**File → Open**).
2. **Primero escribe los componentes en papel**, como en el examen. No te fíes del autocompletado: en el examen no lo tendrás (sí tendrás la hoja de referencia).
3. Pásalos al bloque `✍ TU SOLUCIÓN` y descomenta la pantalla de prueba del final del fichero. Si tus parámetros de la D.4 no coinciden con los de la prueba, adapta esa llamada.
4. Ejecuta la app. Hay un menú con los cinco ejercicios y, dentro de cada uno, una pestaña por apartado.
5. Comprueba también que **al girar el móvil** no se pierde nada del configurador ni de las filas desplegadas.

## Requisitos

- Android Studio con el SDK 37
- Gradle 9.6 (va incluido en el wrapper)
- `minSdk` 24
