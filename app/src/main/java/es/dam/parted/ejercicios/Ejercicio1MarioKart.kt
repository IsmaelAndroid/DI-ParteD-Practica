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
 *  EJERCICIO 1 · Mario Kart · El torneo de karts          (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de un torneo de karts.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN" de este fichero.
 *  No hace falta tocar nada más.
 *
 *  ── D.1 · Cilindrada y ChipCilindrada ─────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Cilindrada` con tres valores. Cada valor guarda
 *     su ETIQUETA y su COLOR:
 *
 *       Valor   Etiqueta   Color
 *       C50     "50cc"     Color(0xFF4CAF50)
 *       C100    "100cc"    Color(0xFFFF9800)
 *       C150    "150cc"    Color(0xFFF44336)
 *
 *  b) Crea el componente `ChipCilindrada`: muestra la etiqueta con su
 *     color, dentro de una "píldora" con un fondo suave del mismo color.
 *     - Propiedad obligatoria: la cilindrada.
 *     - Opcionales: el texto (por defecto, la etiqueta) y el `modifier`.
 *     - NO guarda estado propio.
 *     Uso: ChipCilindrada(Cilindrada.C150)
 *          ChipCilindrada(Cilindrada.C50, texto = "Novato")
 *
 *  ── D.2 · SelectorCircuito ─────────────────────────────────── (0,9) ──
 *
 *  Una fila con los circuitos de la copa para que el usuario elija UNO:
 *
 *      [ Mario ]  [ Koopa ]  ( Bowser )  [ Arcoíris ]
 *                               ↑ elegido (botón relleno)
 *      Arcoíris está bloqueado: no se puede pulsar
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun SelectorCircuito(
 *         circuitos: ______<String>,          // la lista de circuitos
 *         elegido: String______,              // puede no haber ninguno
 *         onElegir: (______) -> ______,       // avisa del circuito pulsado
 *         modifier: Modifier = ______,
 *         bloqueados: List<String> = ______,  // por defecto, ninguno
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. El circuito elegido NO se guarda dentro: se avisa con `onElegir`.
 *     2. El elegido es un `Button`; los demás, `OutlinedButton`. Los
 *        bloqueados no se pueden pulsar.
 *     3. El `modifier` se aplica a la fila.
 *     4. Para TalkBack, cada botón se anuncia como "Bowser, elegido",
 *        "Arcoíris, bloqueado" o "Mario, disponible".
 *
 *  ── D.3 · CalculadoraPuntos ────────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Puntos` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────┐  ┌─────────────┐
 *      │ Posición    │  │ Monedas     │
 *      └─────────────┘  └─────────────┘
 *      [ Normal ] [ Doble ] [ Espejo ]
 *      Puntos: 20 pts               ( C )
 *
 *  1. Los dos campos solo dejan escribir lo que `esValido` acepta: si no
 *     es válido, el campo no cambia. Sale el teclado numérico.
 *  2. Cada botón calcula con `calcular` y muestra `formatear(...)`.
 *  3. "C" vacía los dos campos y pone el resultado a 0.
 *  4. Con cada resultado válido avisa hacia fuera con `onResultado`.
 *     Quien use la calculadora puede no pasar este evento.
 *  5. Lo escrito y el resultado no se pierden al girar el móvil.
 *  6. Acepta `modifier`.
 *
 *  ── D.4 · TarjetaPiloto ────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌──────────────────────────────┐  ┌──────────────────────────────┐
 *    │ Yoshi               [150cc]  │  │ Toad                 [50cc]  │
 *    │ Kart: Estándar               │  │ Kart: Champiñón              │
 *    │ ████████████████░░░░░        │  │ ████████████████████████     │
 *    │ Queda 1 vuelta     (rojo)    │  │ Carrera terminada            │
 *    │              [ Usar objeto ] │  │               [ Terminada ]  │
 *    └──────────────────────────────┘  └──────────────────────────────┘
 *
 *  - Muestra el nombre del piloto, su cilindrada (chip de la D.1) y el kart.
 *  - La barra indica qué parte de las vueltas lleva hechas.
 *  - Debajo: "Quedan N vueltas" ("Queda 1 vuelta" si es una). Si queda 1,
 *    el texto sale en el color de error del tema. Si no queda ninguna,
 *    pone "Carrera terminada".
 *  - El botón "Usar objeto" avisa a la pantalla. Con la carrera terminada
 *    pone "Terminada" y no se puede pulsar.
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Puntos {

    // Puntos por puesto: el 1.º gana 15, el 2.º 12... el 12.º gana 1.
    private val tabla = listOf(15, 12, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)

    // Convierte el texto de un campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un número de hasta 3 cifras.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (texto.length <= 3 && aNumero(texto) != null)

    // Puntos de la carrera según el modo: "Normal", "Doble" o "Espejo".
    // Un campo vacío cuenta como 0. Devuelve null si la posición no está entre 1 y 12.
    fun calcular(modo: String, posicion: String, monedas: String): Int? {
        val p = aNumero(posicion) ?: 0
        val m = aNumero(monedas) ?: 0
        if (p !in 1..12) return null
        val base = tabla[p - 1] + m / 2
        return when (modo) {
            "Normal" -> base
            "Doble" -> base * 2
            "Espejo" -> base + 5
            else -> null
        }
    }

    // Texto para mostrar: "Posición no válida" si es null y "N pts" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Posición no válida" else "$valor pts"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Cilindrada


// D.1 b) ChipCilindrada


// D.2 a) y b) SelectorCircuito (copia la cabecera con los huecos rellenos)


// D.3 CalculadoraPuntos


// D.4 TarjetaPiloto (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaMarioKart() {
    PruebaPendiente("Ejercicio1MarioKart.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu TarjetaPiloto no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaMarioKart() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · ChipCilindrada")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Cilindrada.entries.forEach { ChipCilindrada(it) }
                ChipCilindrada(Cilindrada.C50, texto = "Novato")
            }
            TituloPrueba("D.2 · SelectorCircuito")
            // El estado vive AQUÍ, en quien usa el selector.
            var elegido by rememberSaveable { mutableStateOf<String?>("Bowser") }
            SelectorCircuito(
                circuitos = listOf("Mario", "Koopa", "Bowser", "Arcoíris"),
                elegido = elegido,
                onElegir = { elegido = it },
                bloqueados = listOf("Arcoíris"),
            )
            Text("Circuito elegido: ${elegido ?: "ninguno"}")
        },
        "D.3" to {
            TituloPrueba("D.3 · CalculadoraPuntos")
            var total by rememberSaveable { mutableIntStateOf(0) }
            CalculadoraPuntos(onResultado = { total = it })
            Text("La pantalla ha recibido con onResultado: $total")
            TituloPrueba("Sin pasar onResultado (es opcional)")
            CalculadoraPuntos()
        },
        "D.4" to {
            TituloPrueba("D.4 · TarjetaPiloto")
            var yoshi by remember { mutableStateOf(Piloto("Yoshi", Cilindrada.C150, "Estándar", 2)) }
            var usos by rememberSaveable { mutableIntStateOf(0) }
            TarjetaPiloto(yoshi, onUsarObjeto = { usos++ })
            Text("Objetos usados: $usos")
            Button(onClick = { yoshi = yoshi.copy(vueltasHechas = (yoshi.vueltasHechas + 1) % 4) }) {
                Text("Yoshi completa una vuelta")
            }
            TarjetaPiloto(Piloto("Mario", Cilindrada.C100, "Turbo", 0), onUsarObjeto = {})
            TarjetaPiloto(Piloto("Toad", Cilindrada.C50, "Champiñón", 3), onUsarObjeto = {})
        },
    )
}

 */
