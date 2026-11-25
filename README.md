# DI · 2.º DAM · Práctica de la Parte D (componentes propios)

Cinco ejercicios con el **mismo formato que la Parte D del examen práctico**: programas el kit de componentes de una app en cuatro apartados.

| Apartado | Qué programas | Puntos |
|---|---|---|
| D.1 | Un `enum class` con etiqueta y color, y un **chip** que lo muestra | 0,5 |
| D.2 | Un **selector de selección única** sin estado (completas la cabecera y escribes el cuerpo) | 0,9 |
| D.3 | Una **calculadora** que usa una clase de lógica que ya te dan | 1 |
| D.4 | Una **tarjeta** con barra de progreso y botón (tú decides sus parámetros) | 0,8 |

> Lo que se valora es lo mismo que en el examen: **dónde está el estado**, **cómo se propagan los eventos** y **si el componente es reutilizable** (`modifier`, parámetros opcionales, colores del tema, semántica para TalkBack).

## Ejercicios

Cada ejercicio es un fichero `.kt` en `app/src/main/java/es/dam/parted/ejercicios/`:

| Fichero | Temática | El kit de componentes |
|---|---|---|
| `Ejercicio1MarioKart.kt` | Mario Kart | Cilindrada, selector de circuito, calculadora de puntos, tarjeta de piloto |
| `Ejercicio2Zelda.kt` | Zelda | Rareza, selector de arma, calculadora de daño, tarjeta de objeto |
| `Ejercicio3Fifa.kt` | FIFA | Demarcación, selector de formación, calculadora de mercado, tarjeta de jugador |
| `Ejercicio4Valorant.kt` | Valorant | Rol, selector de mapa, calculadora de créditos, tarjeta de agente |
| `Ejercicio5AnimalCrossing.kt` | Animal Crossing | Amistad, selector de visitante, calculadora de nabos, tarjeta de vecino |

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
5. Comprueba también que **al girar el móvil** no se pierde nada de la calculadora.

La rama `soluciones` tiene los cinco ejercicios resueltos y comentados paso a paso.

```bash
git clone <url-de-este-repo>
git switch soluciones   # para ver las soluciones
```

## Requisitos

- Android Studio con el SDK 37
- Gradle 9.6 (va incluido en el wrapper)
- `minSdk` 24
