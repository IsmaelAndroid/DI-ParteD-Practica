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
 *  EJERCICIO 1 · Mario Kart · El torneo de karts          (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de un torneo de karts.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN" de este fichero.
 *  No hace falta tocar nada más.
 *
 *  ── D.1 · Objeto y AvatarObjeto ───────────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Objeto` con cuatro valores. Cada valor guarda
 *     su NOMBRE, su EMOJI y su COLOR:
 *
 *       Valor       Nombre        Emoji   Color
 *       PLATANO     "Plátano"     🍌      Color(0xFFFBC02D)
 *       CAPARAZON   "Caparazón"   🐢      Color(0xFF43A047)
 *       ESTRELLA    "Estrella"    ⭐      Color(0xFFFF9800)
 *       RAYO        "Rayo"        ⚡      Color(0xFF1E88E5)
 *
 *  b) Crea el componente `AvatarObjeto`: un CÍRCULO con el emoji en el
 *     centro, un fondo suave de su color y un borde de 2 dp de su color.
 *
 *         ╭────╮
 *         │ ⭐ │    ← círculo de 48 dp; el emoji, a la mitad (24 sp)
 *         ╰────╯
 *
 *     - Propiedad obligatoria: el objeto.
 *     - Opcionales: el `modifier` y el tamaño (`Dp`, por defecto 48.dp).
 *       El emoji mide la mitad del tamaño.
 *     - NO guarda estado propio.
 *     - TalkBack NO lee el emoji: lee el nombre del objeto ("Estrella").
 *     Uso: AvatarObjeto(Objeto.ESTRELLA)
 *          AvatarObjeto(Objeto.RAYO, tamano = 72.dp)
 *
 *  ── D.2 · ListaCircuitos ───────────────────────────────────── (0,9) ──
 *
 *  Una lista de casillas para elegir VARIOS circuitos, con un máximo:
 *
 *      Elegidos: 2 de 3
 *      [x] Mario
 *      [ ] Koopa
 *      [x] Bowser
 *      [ ] Arcoíris
 *      Si ya hay 3 elegidos, los demás se ven desactivados.
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun ListaCircuitos(
 *         circuitos: ______<String>,             // todos los circuitos
 *         marcados: ______<String>,              // los elegidos, sin repetidos
 *         onCambiar: (String, ______) -> ______, // circuito y si queda marcado
 *         modifier: Modifier = ______,
 *         maximo: Int = ______,                  // por defecto, todos
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. Los circuitos marcados NO se guardan dentro: se avisa con `onCambiar`.
 *     2. Arriba, el texto "Elegidos: N de M".
 *     3. Cada circuito es una fila con un `Checkbox` y su nombre. Se puede
 *        pulsar TODA la fila, no solo la casilla, y TalkBack la anuncia
 *        como casilla de verificación.
 *     4. Si ya hay `maximo` marcados, los que no están marcados se
 *        desactivan (los marcados se pueden desmarcar siempre).
 *     5. El `modifier` se aplica a la columna.
 *
 *  ── D.3 · ConfiguradorKart ─────────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Velocidad` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────────────────────┐
 *      │ Monedas                     │
 *      └─────────────────────────────┘
 *      Peso del piloto: 3
 *      ━━━━━━━━━━━━●───────────────   (Slider de 1 a 5, solo enteros)
 *      Ala delta                        (  ●)
 *      Velocidad punta: 93 km/h       [ Restablecer ]
 *      (con 5 monedas: 80 + 5·3 − 3·4 + 10)
 *
 *  1. El campo solo deja escribir lo que `esValido` acepta: si no es
 *     válido, el campo no cambia. Sale el teclado numérico.
 *  2. El peso se elige con un `Slider` de PESO_MIN a PESO_MAX que solo se
 *     para en números enteros.
 *  3. El ala delta es un `Switch` con su texto al lado; se puede pulsar
 *     toda la fila.
 *  4. NO hay botón de calcular: el resultado se ve siempre al día, con
 *     `calcular` y `formatear`.
 *  5. Cada vez que el usuario cambia algo y el resultado es válido, avisa
 *     hacia fuera con `onResultado`. Quien use el configurador puede no
 *     pasar este evento.
 *  6. "Restablecer" vuelve a los valores iniciales (campo vacío, peso
 *     mínimo, sin ala delta).
 *  7. Nada se pierde al girar el móvil. Acepta `modifier`.
 *
 *  ── D.4 · FilaPiloto ───────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌───────────────────────────────────────┐
 *    │ (🐢) Yoshi                     (●  )  │  ← plegada
 *    │      ▼ Ver detalles                   │
 *    └───────────────────────────────────────┘
 *    ┌───────────────────────────────────────┐
 *    │ (⭐) Peach                     (  ●)  │  ← desplegada
 *    │      ▲ Ocultar                        │
 *    │ Carreras: 8 · Victorias: 0            │
 *    │ Aún sin victorias            (rojo)   │
 *    └───────────────────────────────────────┘
 *
 *  - Cabecera: el avatar de su objeto favorito (D.1), el nombre y un
 *    `Switch` que dice si el piloto está en el equipo. Quién está en el
 *    equipo lo decide la PANTALLA: la fila solo avisa.
 *  - Al pulsar la cabecera se despliega o pliega el detalle. Eso solo le
 *    importa a la fila: lo guarda ella y no se pierde al girar.
 *  - Detalle: "Carreras: N · Victorias: V" y debajo "Gana el P % de las
 *    carreras". Si no tiene victorias, en su lugar pone "Aún sin
 *    victorias" con el color de error del tema.
 *  - TalkBack: la cabecera anuncia su acción ("Ver detalles" u
 *    "Ocultar detalles") y el `Switch` se anuncia como "En el equipo".
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Velocidad {

    // Límites del Slider del peso.
    val PESO_MIN = 1
    val PESO_MAX = 5

    // Convierte el texto del campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un número de hasta 2 cifras.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (texto.length <= 2 && aNumero(texto) != null)

    // Velocidad punta en km/h. El campo vacío cuenta como 0 monedas.
    // Devuelve null si hay más de 10 monedas (no se pueden llevar más).
    fun calcular(monedas: String, peso: Int, alaDelta: Boolean): Int? {
        val m = aNumero(monedas) ?: 0
        if (m > 10) return null
        val ala = if (alaDelta) 10 else 0
        return 80 + m * 3 - peso * 4 + ala
    }

    // Texto para mostrar: "Máximo 10 monedas" si es null y "N km/h" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Máximo 10 monedas" else "$valor km/h"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Objeto


// D.1 b) AvatarObjeto


// D.2 a) y b) ListaCircuitos (copia la cabecera con los huecos rellenos)


// D.3 ConfiguradorKart


// D.4 FilaPiloto (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaMarioKart() {
    PruebaPendiente("Ejercicio1MarioKart.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu FilaPiloto no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaMarioKart() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · AvatarObjeto")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Objeto.entries.forEach { AvatarObjeto(it) }
                AvatarObjeto(Objeto.RAYO, tamano = 72.dp)
            }
            TituloPrueba("D.2 · ListaCircuitos")
            // El estado vive AQUÍ, en quien usa la lista.
            var copa by rememberSaveable { mutableStateOf(setOf("Mario", "Bowser")) }
            ListaCircuitos(
                circuitos = listOf("Mario", "Koopa", "Bowser", "Arcoíris", "Yoshi"),
                marcados = copa,
                onCambiar = { circuito, marcado -> copa = if (marcado) copa + circuito else copa - circuito },
                maximo = 3,
            )
            Text("Copa: ${copa.joinToString().ifEmpty { "ningún circuito" }}")
        },
        "D.3" to {
            TituloPrueba("D.3 · ConfiguradorKart")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            ConfiguradorKart(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
            TituloPrueba("Sin pasar onResultado (es opcional)")
            ConfiguradorKart()
        },
        "D.4" to {
            TituloPrueba("D.4 · FilaPiloto")
            var equipo by rememberSaveable { mutableStateOf(setOf("Peach")) }
            listOf(
                Piloto("Yoshi", Objeto.CAPARAZON, 12, 5),
                Piloto("Peach", Objeto.ESTRELLA, 8, 0),
                Piloto("Luigi", Objeto.RAYO, 10, 3),
            ).forEach { p ->
                FilaPiloto(
                    piloto = p,
                    enEquipo = p.nombre in equipo,
                    onCambiarEquipo = { equipo = if (it) equipo + p.nombre else equipo - p.nombre },
                )
            }
            Text("En el equipo: ${equipo.joinToString().ifEmpty { "nadie" }}")
        },
    )
}

 */
