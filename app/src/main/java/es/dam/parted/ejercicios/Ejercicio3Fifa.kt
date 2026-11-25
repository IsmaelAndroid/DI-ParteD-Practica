package es.dam.parted.ejercicios

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/*
 * ════════════════════════════════════════════════════════════════════
 *  PARTE D · COMPONENTES PROPIOS
 *  EJERCICIO 3 · FIFA · Ultimate Team                     (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de un modo de fútbol con
 *  cartas de jugadores. Escribe tu código en el bloque "✍ TU SOLUCIÓN".
 *
 *  ── D.1 · Demarcacion y ChipDemarcacion ───────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Demarcacion` con tres valores. Cada valor
 *     guarda su ETIQUETA y su COLOR:
 *
 *       Valor   Etiqueta      Color
 *       POR     "Portero"     Color(0xFFFFB300)
 *       DEF     "Defensa"     Color(0xFF1E88E5)
 *       DEL     "Delantero"   Color(0xFFE53935)
 *
 *  b) Crea el componente `ChipDemarcacion`: muestra la etiqueta con su
 *     color, dentro de una "píldora" con un fondo suave del mismo color.
 *     - Propiedad obligatoria: la demarcación.
 *     - Opcionales: el texto (por defecto, la etiqueta) y el `modifier`.
 *     - NO guarda estado propio.
 *     Uso: ChipDemarcacion(Demarcacion.DEL)
 *          ChipDemarcacion(Demarcacion.POR, texto = "PT")
 *
 *  ── D.2 · SelectorFormacion ────────────────────────────────── (0,9) ──
 *
 *  Una fila con las formaciones para que el usuario elija UNA:
 *
 *      [ 4-4-2 ]  ( 4-3-3 )  [ 3-5-2 ]  [ 5-3-2 ]
 *                    ↑ activa (botón relleno)
 *      5-3-2 está bloqueada: no se puede pulsar
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun SelectorFormacion(
 *         ______: List<String>,                  // la lista de formaciones
 *         activa: ______,                        // puede no haber ninguna
 *         onCambiar: (String) -> ______,         // avisa de la formación pulsada
 *         modifier: ______ = Modifier,
 *         bloqueadas: List<String> = ______,     // por defecto, ninguna
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. La formación activa NO se guarda dentro: se avisa con `onCambiar`.
 *     2. La activa es un `Button`; las demás, `OutlinedButton`. Las
 *        bloqueadas no se pueden pulsar.
 *     3. El `modifier` se aplica a la fila.
 *     4. Para TalkBack, cada botón se anuncia como "4-3-3, activa",
 *        "5-3-2, bloqueada" o "4-4-2, libre".
 *
 *  ── D.3 · CalculadoraMercado ───────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Mercado` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────┐  ┌─────────────┐
 *      │ Compra      │  │ Venta       │
 *      └─────────────┘  └─────────────┘
 *      [ Beneficio ] [ Comisión ] [ % Ganancia ]
 *      Resultado: 2.500 monedas         ( C )
 *
 *  1. Los dos campos solo dejan escribir lo que `esValido` acepta: si no
 *     es válido, el campo no cambia. Sale el teclado numérico.
 *  2. Cada botón calcula con `calcular` y muestra `formatear(...)`.
 *     Si no se puede calcular, se ve "No calculable".
 *  3. "C" vacía los dos campos y pone el resultado a 0.
 *  4. Con cada resultado válido avisa hacia fuera con `onResultado`.
 *     Quien use la calculadora puede no pasar este evento.
 *  5. Lo escrito y el resultado no se pierden al girar el móvil.
 *  6. Acepta `modifier`.
 *
 *  ── D.4 · TarjetaJugador ───────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌──────────────────────────────┐  ┌──────────────────────────────┐
 *    │ 91  Mbappé       [Delantero] │  │ 84  Courtois       [Portero] │
 *    │ Real Madrid                  │  │ Real Madrid                  │
 *    │ ████████████████████░░       │  │ ████████████████████████     │
 *    │ Quedan 2 partidos   (rojo)   │  │ Contrato terminado           │
 *    │                 [ Alinear ]  │  │            [ Sin contrato ]  │
 *    └──────────────────────────────┘  └──────────────────────────────┘
 *
 *  - Muestra la media, el nombre, su demarcación (chip de la D.1) y el
 *    equipo.
 *  - La barra indica qué parte del contrato ha jugado ya (partidos
 *    jugados / partidos del contrato).
 *  - Debajo: "Quedan N partidos". Si quedan 2 o menos, en el color de
 *    error del tema. Si no queda ninguno, pone "Contrato terminado".
 *  - El botón "Alinear" avisa a la pantalla. Sin contrato, el botón pone
 *    "Sin contrato" y no se puede pulsar.
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Mercado {

    // Convierte el texto de un campo en número. null si no lo es.
    fun aNumero(texto: String): Double? = texto.replace(',', '.').toDoubleOrNull()

    // true si el texto se puede dejar escrito: vacío o un número no negativo.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (aNumero(texto) ?: -1.0) >= 0.0

    // Calcula según la operación: "Beneficio", "Comisión" o "% Ganancia".
    // El mercado se queda un 5 % de cada venta. Un campo vacío cuenta como 0.
    // Devuelve null si no se puede calcular (% Ganancia con compra 0).
    fun calcular(operacion: String, compra: String, venta: String): Double? {
        val c = aNumero(compra) ?: 0.0
        val v = aNumero(venta) ?: 0.0
        return when (operacion) {
            "Beneficio" -> v * 0.95 - c
            "Comisión" -> v * 0.05
            "% Ganancia" -> if (c == 0.0) null else (v * 0.95 - c) / c * 100
            else -> null
        }
    }

    // Texto para mostrar: "No calculable" si es null, y si no, sin decimales.
    fun formatear(valor: Double?, operacion: String = ""): String = when {
        valor == null -> "No calculable"
        operacion == "% Ganancia" -> "%.1f %%".format(valor)
        else -> "%,.0f monedas".format(valor)
    }
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Demarcacion


// D.1 b) ChipDemarcacion


// D.2 a) y b) SelectorFormacion (copia la cabecera con los huecos rellenos)


// D.3 CalculadoraMercado


// D.4 TarjetaJugador (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaFifa() {
    PruebaPendiente("Ejercicio3Fifa.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu TarjetaJugador no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaFifa() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · ChipDemarcacion")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Demarcacion.entries.forEach { ChipDemarcacion(it) }
                ChipDemarcacion(Demarcacion.POR, texto = "PT")
            }
            TituloPrueba("D.2 · SelectorFormacion")
            var activa by rememberSaveable { mutableStateOf<String?>("4-3-3") }
            SelectorFormacion(
                formaciones = listOf("4-4-2", "4-3-3", "3-5-2", "5-3-2"),
                activa = activa,
                onCambiar = { activa = it },
                bloqueadas = listOf("5-3-2"),
            )
            Text("Formación activa: ${activa ?: "ninguna"}")
        },
        "D.3" to {
            TituloPrueba("D.3 · CalculadoraMercado")
            var ultimo by rememberSaveable { mutableDoubleStateOf(0.0) }
            CalculadoraMercado(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: ${"%.2f".format(ultimo)}")
        },
        "D.4" to {
            TituloPrueba("D.4 · TarjetaJugador")
            var alineados by remember { mutableStateOf(listOf<String>()) }
            val plantilla = listOf(
                Jugador("Mbappé", 91, Demarcacion.DEL, "Real Madrid", 28, 30),
                Jugador("Rüdiger", 86, Demarcacion.DEF, "Real Madrid", 10, 38),
                Jugador("Courtois", 84, Demarcacion.POR, "Real Madrid", 38, 38),
            )
            plantilla.forEach { j ->
                TarjetaJugador(j, onAlinear = { alineados = alineados + it.nombre })
            }
            Text("Alineados: ${alineados.joinToString().ifEmpty { "nadie" }}")
        },
    )
}

 */
