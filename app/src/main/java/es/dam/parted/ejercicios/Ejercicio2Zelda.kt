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
 *  EJERCICIO 2 · Zelda · La cocina de Link                (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de cocina y armas de Link.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN" de este fichero.
 *  No hace falta tocar nada más.
 *
 *  ── D.1 · Elemento y AvatarElemento ───────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Elemento` con cuatro valores. Cada valor guarda
 *     su NOMBRE, su EMOJI y su COLOR:
 *
 *       Valor       Nombre        Emoji   Color
 *       FUEGO       "Fuego"       🔥      Color(0xFFE53935)
 *       HIELO       "Hielo"       ❄️      Color(0xFF29B6F6)
 *       ELECTRICO   "Eléctrico"   ⚡      Color(0xFFFFB300)
 *       NORMAL      "Normal"      🗡️      Color(0xFF757575)
 *
 *  b) Crea el componente `AvatarElemento`: un CÍRCULO con el emoji en el
 *     centro, un fondo suave de su color y un borde de 2 dp de su color.
 *
 *         ╭────╮
 *         │ 🔥 │    ← círculo de 48 dp; el emoji, a la mitad (24 sp)
 *         ╰────╯
 *
 *     - Propiedad obligatoria: el elemento.
 *     - Opcionales: el `modifier` y el tamaño (`Dp`, por defecto 48.dp).
 *       El emoji mide la mitad del tamaño.
 *     - NO guarda estado propio.
 *     - TalkBack NO lee el emoji: lee el nombre del elemento ("Fuego").
 *     Uso: AvatarElemento(Elemento.FUEGO)
 *          AvatarElemento(Elemento.HIELO, tamano = 72.dp)
 *
 *  ── D.2 · ListaIngredientes ────────────────────────────────── (0,9) ──
 *
 *  Una lista de casillas para echar VARIOS ingredientes a la olla, con
 *  un máximo:
 *
 *      Elegidos: 2 de 3
 *      [x] Manzana
 *      [ ] Seta vigorosa
 *      [x] Miel
 *      [ ] Trigo
 *      Si ya hay 3 elegidos, los demás se ven desactivados.
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun ListaIngredientes(
 *         ingredientes: ______<String>,           // todos los ingredientes
 *         marcados: ______<String>,               // los elegidos, sin repetidos
 *         onCambiar: (String, ______) -> ______,  // ingrediente y si queda marcado
 *         modifier: Modifier = ______,
 *         maximo: Int = ______,                   // por defecto, todos
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. Los ingredientes marcados NO se guardan dentro: se avisa con
 *        `onCambiar`.
 *     2. Arriba, el texto "Elegidos: N de M".
 *     3. Cada ingrediente es una fila con un `Checkbox` y su nombre. Se
 *        puede pulsar TODA la fila, no solo la casilla, y TalkBack la
 *        anuncia como casilla de verificación.
 *     4. Si ya hay `maximo` marcados, los que no están marcados se
 *        desactivan (los marcados se pueden desmarcar siempre).
 *     5. El `modifier` se aplica a la columna.
 *
 *  ── D.3 · ConfiguradorReceta ───────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Cocina` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────────────────────┐
 *      │ Ingredientes                │
 *      └─────────────────────────────┘
 *      Intensidad del fuego: 2
 *      ━━━━━━━━━━━━━●──────────────   (Slider de 1 a 3, solo enteros)
 *      Añadir un hada                   (  ●)
 *      Recupera: 9 ❤                  [ Restablecer ]
 *      (con 3 ingredientes: 3·2 + 3 del hada)
 *
 *  1. El campo solo deja escribir lo que `esValido` acepta: si no es
 *     válido, el campo no cambia. Sale el teclado numérico.
 *  2. La intensidad se elige con un `Slider` de FUEGO_MIN a FUEGO_MAX que
 *     solo se para en números enteros.
 *  3. El hada es un `Switch` con su texto al lado; se puede pulsar toda
 *     la fila.
 *  4. NO hay botón de calcular: el resultado se ve siempre al día, con
 *     `calcular` y `formatear`.
 *  5. Cada vez que el usuario cambia algo y el resultado es válido, avisa
 *     hacia fuera con `onResultado`. Quien use el configurador puede no
 *     pasar este evento.
 *  6. "Restablecer" vuelve a los valores iniciales (campo vacío, fuego
 *     mínimo, sin hada).
 *  7. Nada se pierde al girar el móvil. Acepta `modifier`.
 *
 *  ── D.4 · FilaArma ─────────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌───────────────────────────────────────┐
 *    │ (🗡️) Espada maestra            (  ●)  │  ← plegada
 *    │      ▼ Ver detalles                   │
 *    └───────────────────────────────────────┘
 *    ┌───────────────────────────────────────┐
 *    │ (🔥) Vara de fuego             (●  )  │  ← desplegada
 *    │      ▲ Ocultar                        │
 *    │ Ataque: 10 · Usos: 2                  │
 *    │ ¡A punto de romperse!        (rojo)   │
 *    └───────────────────────────────────────┘
 *
 *  - Cabecera: el avatar de su elemento (D.1), el nombre y un `Switch`
 *    que dice si Link la lleva equipada. Qué armas van equipadas lo
 *    decide la PANTALLA: la fila solo avisa.
 *  - Al pulsar la cabecera se despliega o pliega el detalle. Eso solo le
 *    importa a la fila: lo guarda ella y no se pierde al girar.
 *  - Detalle: "Ataque: A · Usos: U" y debajo "Aguanta U golpes más". Si
 *    le quedan 3 usos o menos, en su lugar pone "¡A punto de romperse!"
 *    con el color de error del tema.
 *  - TalkBack: la cabecera anuncia su acción ("Ver detalles" u
 *    "Ocultar detalles") y el `Switch` se anuncia como "Equipada".
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Cocina {

    // Límites del Slider del fuego.
    val FUEGO_MIN = 1
    val FUEGO_MAX = 3

    // Convierte el texto del campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o una sola cifra.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (texto.length == 1 && aNumero(texto) != null)

    // Corazones que recupera el plato. El campo vacío cuenta como 0.
    // Devuelve null si los ingredientes no están entre 1 y 5.
    fun calcular(ingredientes: String, fuego: Int, conHada: Boolean): Int? {
        val i = aNumero(ingredientes) ?: 0
        if (i !in 1..5) return null
        val hada = if (conHada) 3 else 0
        return i * fuego + hada
    }

    // Texto para mostrar: "Pon entre 1 y 5 ingredientes" si es null y "N ❤" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Pon entre 1 y 5 ingredientes" else "$valor ❤"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Elemento


// D.1 b) AvatarElemento


// D.2 a) y b) ListaIngredientes (copia la cabecera con los huecos rellenos)


// D.3 ConfiguradorReceta


// D.4 FilaArma (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaZelda() {
    PruebaPendiente("Ejercicio2Zelda.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu FilaArma no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaZelda() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · AvatarElemento")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Elemento.entries.forEach { AvatarElemento(it) }
                AvatarElemento(Elemento.HIELO, tamano = 72.dp)
            }
            TituloPrueba("D.2 · ListaIngredientes")
            var olla by rememberSaveable { mutableStateOf(setOf("Manzana", "Miel")) }
            ListaIngredientes(
                ingredientes = listOf("Manzana", "Seta vigorosa", "Carne cruda", "Miel", "Trigo"),
                marcados = olla,
                onCambiar = { ingrediente, marcado -> olla = if (marcado) olla + ingrediente else olla - ingrediente },
                maximo = 3,
            )
            Text("En la olla: ${olla.joinToString().ifEmpty { "nada" }}")
        },
        "D.3" to {
            TituloPrueba("D.3 · ConfiguradorReceta")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            ConfiguradorReceta(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
            TituloPrueba("Sin pasar onResultado (es opcional)")
            ConfiguradorReceta()
        },
        "D.4" to {
            TituloPrueba("D.4 · FilaArma")
            var equipadas by rememberSaveable { mutableStateOf(setOf("Espada maestra")) }
            listOf(
                Arma("Espada maestra", Elemento.NORMAL, 30, 40),
                Arma("Vara de fuego", Elemento.FUEGO, 10, 2),
                Arma("Lanza eléctrica", Elemento.ELECTRICO, 18, 12),
            ).forEach { a ->
                FilaArma(
                    arma = a,
                    equipada = a.nombre in equipadas,
                    onCambiarEquipada = { equipadas = if (it) equipadas + a.nombre else equipadas - a.nombre },
                )
            }
            Text("Equipadas: ${equipadas.joinToString().ifEmpty { "ninguna" }}")
        },
    )
}

 */
