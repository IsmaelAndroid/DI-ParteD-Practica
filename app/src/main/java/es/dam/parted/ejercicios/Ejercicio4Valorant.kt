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
 *  EJERCICIO 4 · Valorant · La selección de agentes       (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de un shooter táctico.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN".
 *
 *  ── D.1 · Rol y ChipRol ───────────────────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Rol` con tres valores. Cada valor guarda su
 *     ETIQUETA y su COLOR:
 *
 *       Valor         Etiqueta        Color
 *       DUELISTA      "Duelista"      Color(0xFFFF4655)
 *       CENTINELA     "Centinela"     Color(0xFF00897B)
 *       CONTROLADOR   "Controlador"   Color(0xFF7E57C2)
 *
 *  b) Crea el componente `ChipRol`: muestra la etiqueta con su color,
 *     dentro de una "píldora" con un fondo suave del mismo color.
 *     - Propiedad obligatoria: el rol.
 *     - Opcionales: el texto (por defecto, la etiqueta) y el `modifier`.
 *     - NO guarda estado propio.
 *     Uso: ChipRol(Rol.DUELISTA)
 *          ChipRol(Rol.CENTINELA, texto = "Ancla")
 *
 *  ── D.2 · SelectorMapa ─────────────────────────────────────── (0,9) ──
 *
 *  Una fila con los mapas para que el usuario vote UNO:
 *
 *      [ Ascent ]  ( Bind )  [ Haven ]  [ Split ]
 *                     ↑ votado (botón relleno)
 *      Split está en mantenimiento: no se puede pulsar
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun SelectorMapa(
 *         mapas: List<String>,
 *         votado: ______,                         // puede no haber ninguno
 *         ______: (String) -> Unit,               // evento: avisa del mapa pulsado
 *         modifier: Modifier = ______,
 *         enMantenimiento: ______<String> = ______,  // por defecto, ninguno
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. El mapa votado NO se guarda dentro: se avisa con el evento.
 *     2. El votado es un `Button`; los demás, `OutlinedButton`. Los que
 *        están en mantenimiento no se pueden pulsar.
 *     3. El `modifier` se aplica a la fila.
 *     4. Para TalkBack, cada botón se anuncia como "Bind, votado",
 *        "Split, en mantenimiento" o "Ascent, disponible".
 *
 *  ── D.3 · CalculadoraCreditos ──────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Economia` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────┐  ┌─────────────┐
 *      │ Créditos    │  │ Precio      │
 *      └─────────────┘  └─────────────┘
 *      [ Comprar ] [ Equipo x5 ] [ Ahorrar ]
 *      Resultado: Te sobran 1100 ¢      ( C )
 *
 *  1. Los dos campos solo dejan escribir lo que `esValido` acepta: si no
 *     es válido, el campo no cambia. Sale el teclado numérico.
 *  2. Cada botón calcula con `calcular` y muestra `formatear(...)`.
 *  3. "C" vacía los dos campos y deja el resultado vacío ("—").
 *  4. Con cada resultado válido avisa hacia fuera con `onResultado`.
 *     Quien use la calculadora puede no pasar este evento.
 *  5. Lo escrito y el resultado no se pierden al girar el móvil.
 *  6. Acepta `modifier`.
 *
 *  ── D.4 · TarjetaAgente ────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros. ¡OJO! Aquí el botón funciona AL REVÉS que
 *  en el examen: solo se puede pulsar cuando la barra está llena.
 *
 *    ┌──────────────────────────────┐  ┌──────────────────────────────┐
 *    │ Jett              [Duelista] │  │ Sage             [Centinela] │
 *    │ Definitiva: Tormenta de filos│  │ Definitiva: Resurrección     │
 *    │ ██████████████░░░░░░░        │  │ ████████████████████████     │
 *    │ Faltan 3 puntos              │  │ ¡Definitiva lista!  (color)  │
 *    │               [ Cargando… ]  │  │          [ Usar definitiva ] │
 *    └──────────────────────────────┘  └──────────────────────────────┘
 *
 *  - Muestra el nombre del agente, su rol (chip de la D.1) y el nombre
 *    de su definitiva.
 *  - La barra indica cuántos puntos de definitiva tiene cargados
 *    (puntos / puntos necesarios).
 *  - Debajo: "Faltan N puntos" ("Falta 1 punto" si es uno). Si ya tiene
 *    todos, pone "¡Definitiva lista!" en el color primario del tema.
 *  - El botón "Usar definitiva" avisa a la pantalla y SOLO se puede
 *    pulsar con la definitiva lista. Mientras carga pone "Cargando…".
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Economia {

    // Lo que se gana, como mínimo, en una ronda.
    private val creditosPorRonda = 1900

    // Convierte el texto de un campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un entero de 0 a 9000.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (aNumero(texto) ?: -1) in 0..9000

    // Según la operación: "Comprar", "Equipo x5" o "Ahorrar".
    // - Comprar: créditos que te sobran tras comprar el arma. null si no te llega.
    // - Equipo x5: créditos que sobran si compras el arma a los 5 del equipo
    //   (todos con tus mismos créditos). null si no llega.
    // - Ahorrar: rondas que tienes que ahorrar para poder comprar el arma (0 si ya te llega).
    // Un campo vacío cuenta como 0.
    fun calcular(operacion: String, creditos: String, precio: String): Int? {
        val c = aNumero(creditos) ?: 0
        val p = aNumero(precio) ?: 0
        return when (operacion) {
            "Comprar" -> (c - p).takeIf { it >= 0 }
            "Equipo x5" -> (c * 5 - p * 5).takeIf { it >= 0 }
            "Ahorrar" -> if (c >= p) 0 else (p - c + creditosPorRonda - 1) / creditosPorRonda
            else -> null
        }
    }

    // Texto para mostrar según la operación.
    fun formatear(valor: Int?, operacion: String): String = when {
        valor == null -> "No te llega"
        operacion == "Ahorrar" -> if (valor == 0) "Ya te llega" else "Ahorra $valor rondas"
        else -> "Te sobran $valor ¢"
    }
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Rol


// D.1 b) ChipRol


// D.2 a) y b) SelectorMapa (copia la cabecera con los huecos rellenos)


// D.3 CalculadoraCreditos


// D.4 TarjetaAgente (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaValorant() {
    PruebaPendiente("Ejercicio4Valorant.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu TarjetaAgente no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaValorant() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · ChipRol")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Rol.entries.forEach { ChipRol(it) }
            }
            ChipRol(Rol.CENTINELA, texto = "Ancla")
            TituloPrueba("D.2 · SelectorMapa")
            var votado by rememberSaveable { mutableStateOf<String?>("Bind") }
            SelectorMapa(
                mapas = listOf("Ascent", "Bind", "Haven", "Split"),
                votado = votado,
                onVotar = { votado = it },
                enMantenimiento = listOf("Split"),
            )
            Text("Mapa votado: ${votado ?: "ninguno"}")
        },
        "D.3" to {
            TituloPrueba("D.3 · CalculadoraCreditos")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            CalculadoraCreditos(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
        },
        "D.4" to {
            TituloPrueba("D.4 · TarjetaAgente")
            var jett by remember { mutableStateOf(Agente("Jett", Rol.DUELISTA, "Tormenta de filos", 4, 7)) }
            TarjetaAgente(jett, onUsarDefinitiva = { jett = jett.copy(puntos = 0) })
            Button(onClick = { jett = jett.copy(puntos = (jett.puntos + 1).coerceAtMost(7)) }) {
                Text("Jett gana un punto")
            }
            TarjetaAgente(Agente("Omen", Rol.CONTROLADOR, "Desde las sombras", 6, 7), onUsarDefinitiva = {})
            TarjetaAgente(Agente("Sage", Rol.CENTINELA, "Resurrección", 8, 8), onUsarDefinitiva = {})
        },
    )
}

 */
