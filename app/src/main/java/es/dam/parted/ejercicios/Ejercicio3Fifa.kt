package es.dam.parted.ejercicios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/*
 * ════════════════════════════════════════════════════════════════════
 *  PARTE D · COMPONENTES PROPIOS
 *  EJERCICIO 3 · FIFA · El director deportivo             (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de un director deportivo.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN" de este fichero.
 *  No hace falta tocar nada más.
 *
 *  ── D.1 · Posicion y AvatarPosicion ───────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Posicion` con cuatro valores. Cada valor guarda
 *     su NOMBRE, su EMOJI y su COLOR:
 *
 *       Valor       Nombre             Emoji   Color
 *       PORTERO     "Portero"          🧤      Color(0xFFFBC02D)
 *       DEFENSA     "Defensa"          🛡️      Color(0xFF1E88E5)
 *       MEDIO       "Centrocampista"   🎯      Color(0xFF43A047)
 *       DELANTERO   "Delantero"        ⚽      Color(0xFFE53935)
 *
 *  b) Crea el componente `AvatarPosicion`: un CÍRCULO con el emoji en el
 *     centro, un fondo suave de su color y un borde de 2 dp de su color.
 *
 *         ╭────╮
 *         │ ⚽ │    ← círculo de 48 dp; el emoji, a la mitad (24 sp)
 *         ╰────╯
 *
 *     - Propiedad obligatoria: la posición.
 *     - Opcionales: el `modifier` y el tamaño (`Dp`, por defecto 48.dp).
 *       El emoji mide la mitad del tamaño.
 *     - NO guarda estado propio.
 *     - TalkBack NO lee el emoji: lee el nombre ("Delantero").
 *     Uso: AvatarPosicion(Posicion.DELANTERO)
 *          AvatarPosicion(Posicion.PORTERO, tamano = 72.dp)
 *
 *  ── D.2 · ListaConvocados ──────────────────────────────────── (0,9) ──
 *
 *  Una lista de casillas para convocar VARIOS suplentes, con un máximo:
 *
 *      Elegidos: 2 de 3
 *      [x] Joselu
 *      [ ] Ceballos
 *      [x] Lunin
 *      [ ] Vallejo
 *      Si ya hay 3 elegidos, los demás se ven desactivados.
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun ListaConvocados(
 *         jugadores: ______<String>,             // todos los jugadores
 *         marcados: ______<String>,              // los convocados, sin repetidos
 *         onCambiar: (String, ______) -> ______, // jugador y si queda marcado
 *         modifier: Modifier = ______,
 *         maximo: Int = ______,                  // por defecto, todos
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. Los marcados NO se guardan dentro: se avisa con `onCambiar`.
 *     2. Arriba, el texto "Elegidos: N de M".
 *     3. Cada jugador es una fila con un `Checkbox` y su nombre. Se puede
 *        pulsar TODA la fila, no solo la casilla, y TalkBack la anuncia
 *        como casilla de verificación.
 *     4. Si ya hay `maximo` marcados, los que no están marcados se
 *        desactivan (los marcados se pueden desmarcar siempre).
 *     5. El `modifier` se aplica a la columna.
 *
 *  ── D.3 · ConfiguradorFichaje ──────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Fichaje` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────────────────────┐
 *      │ Valor (M€)                  │
 *      └─────────────────────────────┘
 *      Años de contrato: 3
 *      ━━━━━━━━━━━━●───────────────   (Slider de 1 a 5, solo enteros)
 *      Cláusula de rescisión            (  ●)
 *      Coste total: 180 M€            [ Restablecer ]
 *      (con 100 M€, 3 años y cláusula: 100 + 3·10 + 50)
 *
 *  1. El campo solo deja escribir lo que `esValido` acepta: si no es
 *     válido, el campo no cambia. Sale el teclado numérico.
 *  2. Los años se eligen con un `Slider` de ANIOS_MIN a ANIOS_MAX que solo
 *     se para en números enteros.
 *  3. La cláusula es un `Switch` con su texto al lado; se puede pulsar
 *     toda la fila.
 *  4. NO hay botón de calcular: el resultado se ve siempre al día, con
 *     `calcular` y `formatear`.
 *  5. Cada vez que el usuario cambia algo y el resultado es válido, avisa
 *     hacia fuera con `onResultado`. Quien use el configurador puede no
 *     pasar este evento.
 *  6. "Restablecer" vuelve a los valores iniciales (campo vacío, años
 *     mínimos, sin cláusula).
 *  7. Nada se pierde al girar el móvil. Acepta `modifier`.
 *
 *  ── D.4 · FilaJugador ──────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌───────────────────────────────────────┐
 *    │ (⚽) Mbappé                    (  ●)  │  ← plegada
 *    │      ▼ Ver detalles                   │
 *    └───────────────────────────────────────┘
 *    ┌───────────────────────────────────────┐
 *    │ (🧤) Courtois                  (●  )  │  ← desplegada, lesionado:
 *    │      ▲ Ocultar                        │    el Switch no se puede
 *    │ Media: 89                             │    pulsar
 *    │ Lesionado: no puede jugar    (rojo)   │
 *    └───────────────────────────────────────┘
 *
 *  - Cabecera: el avatar de su posición (D.1), el nombre y un `Switch`
 *    que dice si es titular. Quién es titular lo decide la PANTALLA: la
 *    fila solo avisa. Si está lesionado, el Switch se desactiva.
 *  - Al pulsar la cabecera se despliega o pliega el detalle. Eso solo le
 *    importa a la fila: lo guarda ella y no se pierde al girar.
 *  - Detalle: "Media: N" y debajo "En forma". Si está lesionado, en su
 *    lugar pone "Lesionado: no puede jugar" con el color de error del tema.
 *  - TalkBack: la cabecera anuncia su acción ("Ver detalles" u
 *    "Ocultar detalles") y el `Switch` se anuncia como "Titular".
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Fichaje {

    // Límites del Slider de los años.
    val ANIOS_MIN = 1
    val ANIOS_MAX = 5

    // Convierte el texto del campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un número de hasta 3 cifras.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (texto.length <= 3 && aNumero(texto) != null)

    // Coste total del fichaje en millones: el valor, el sueldo de cada año
    // (la décima parte del valor, mínimo 1) y, con cláusula, la mitad del valor.
    // Devuelve null si el valor es 0 o el campo está vacío.
    fun calcular(valor: String, anios: Int, clausula: Boolean): Int? {
        val v = aNumero(valor) ?: 0
        if (v == 0) return null
        val sueldo = maxOf(1, v / 10)
        val extra = if (clausula) v / 2 else 0
        return v + anios * sueldo + extra
    }

    // Texto para mostrar: "Escribe el valor del jugador" si es null y "N M€" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Escribe el valor del jugador" else "$valor M€"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Posicion


// D.1 b) AvatarPosicion


// D.2 a) y b) ListaConvocados (copia la cabecera con los huecos rellenos)


// D.3 ConfiguradorFichaje


// D.4 FilaJugador (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaFifa() {
    PruebaPendiente("Ejercicio3Fifa.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu FilaJugador no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaFifa() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · AvatarPosicion")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Posicion.entries.forEach { AvatarPosicion(it) }
                AvatarPosicion(Posicion.PORTERO, tamano = 72.dp)
            }
            TituloPrueba("D.2 · ListaConvocados")
            var banquillo by rememberSaveable { mutableStateOf(setOf("Joselu", "Lunin")) }
            ListaConvocados(
                jugadores = listOf("Joselu", "Ceballos", "Lunin", "Vallejo", "Arda Güler"),
                marcados = banquillo,
                onCambiar = { jugador, marcado -> banquillo = if (marcado) banquillo + jugador else banquillo - jugador },
                maximo = 3,
            )
            Text("Banquillo: ${banquillo.joinToString().ifEmpty { "vacío" }}")
        },
        "D.3" to {
            TituloPrueba("D.3 · ConfiguradorFichaje")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            ConfiguradorFichaje(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
            TituloPrueba("Sin pasar onResultado (es opcional)")
            ConfiguradorFichaje()
        },
        "D.4" to {
            TituloPrueba("D.4 · FilaJugador")
            var titulares by rememberSaveable { mutableStateOf(setOf("Mbappé")) }
            listOf(
                Jugador("Mbappé", Posicion.DELANTERO, 91),
                Jugador("Courtois", Posicion.PORTERO, 89, lesionado = true),
                Jugador("Pedri", Posicion.MEDIO, 86),
            ).forEach { j ->
                FilaJugador(
                    jugador = j,
                    titular = j.nombre in titulares,
                    onCambiarTitular = { titulares = if (it) titulares + j.nombre else titulares - j.nombre },
                )
            }
            Text("Titulares: ${titulares.joinToString().ifEmpty { "nadie" }}")
        },
    )
}

 */
