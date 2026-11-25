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
 *  EJERCICIO 4 · Valorant · La economía del equipo        (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de un equipo de Valorant.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN" de este fichero.
 *  No hace falta tocar nada más.
 *
 *  ── D.1 · Rol y AvatarRol ─────────────────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Rol` con cuatro valores. Cada valor guarda su
 *     NOMBRE, su EMOJI y su COLOR:
 *
 *       Valor         Nombre          Emoji   Color
 *       DUELISTA      "Duelista"      ⚔️      Color(0xFFE53935)
 *       CENTINELA     "Centinela"     🛡️      Color(0xFF00897B)
 *       CONTROLADOR   "Controlador"   🌫️      Color(0xFF7E57C2)
 *       INICIADOR     "Iniciador"     🎯      Color(0xFFFB8C00)
 *
 *  b) Crea el componente `AvatarRol`: un CÍRCULO con el emoji en el
 *     centro, un fondo suave de su color y un borde de 2 dp de su color.
 *
 *         ╭────╮
 *         │ ⚔️ │    ← círculo de 48 dp; el emoji, a la mitad (24 sp)
 *         ╰────╯
 *
 *     - Propiedad obligatoria: el rol.
 *     - Opcionales: el `modifier` y el tamaño (`Dp`, por defecto 48.dp).
 *       El emoji mide la mitad del tamaño.
 *     - NO guarda estado propio.
 *     - TalkBack NO lee el emoji: lee el nombre del rol ("Duelista").
 *     Uso: AvatarRol(Rol.DUELISTA)
 *          AvatarRol(Rol.CONTROLADOR, tamano = 72.dp)
 *
 *  ── D.2 · ListaArmas ───────────────────────────────────────── (0,9) ──
 *
 *  Una lista de casillas para elegir VARIAS armas que comprar, con un
 *  máximo:
 *
 *      Elegidos: 1 de 2
 *      [x] Vandal
 *      [ ] Phantom
 *      [ ] Operator
 *      [ ] Sheriff
 *      Si ya hay 2 elegidas, las demás se ven desactivadas.
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun ListaArmas(
 *         armas: ______<String>,                 // todas las armas
 *         marcadas: ______<String>,              // las elegidas, sin repetidas
 *         onCambiar: (String, ______) -> ______, // arma y si queda marcada
 *         modifier: Modifier = ______,
 *         maximo: Int = ______,                  // por defecto, todas
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. Las marcadas NO se guardan dentro: se avisa con `onCambiar`.
 *     2. Arriba, el texto "Elegidos: N de M".
 *     3. Cada arma es una fila con un `Checkbox` y su nombre. Se puede
 *        pulsar TODA la fila, no solo la casilla, y TalkBack la anuncia
 *        como casilla de verificación.
 *     4. Si ya hay `maximo` marcadas, las que no están marcadas se
 *        desactivan (las marcadas se pueden desmarcar siempre).
 *     5. El `modifier` se aplica a la columna.
 *
 *  ── D.3 · ConfiguradorRonda ────────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Economia` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────────────────────┐
 *      │ Créditos                    │
 *      └─────────────────────────────┘
 *      Rondas perdidas seguidas: 1
 *      ━━━━━━━━━━━━━●──────────────   (Slider de 0 a 2, solo enteros)
 *      Spike plantada                   (  ●)
 *      Próxima ronda: 3700 ¢          [ Restablecer ]
 *      (con 1000 créditos: 1000 + 1900 + 500 + 300)
 *
 *  1. El campo solo deja escribir lo que `esValido` acepta: si no es
 *     válido, el campo no cambia. Sale el teclado numérico.
 *  2. Las rondas perdidas se eligen con un `Slider` de RACHA_MIN a
 *     RACHA_MAX que solo se para en números enteros.
 *  3. La spike es un `Switch` con su texto al lado; se puede pulsar toda
 *     la fila.
 *  4. NO hay botón de calcular: el resultado se ve siempre al día, con
 *     `calcular` y `formatear`.
 *  5. Cada vez que el usuario cambia algo y el resultado es válido, avisa
 *     hacia fuera con `onResultado`. Quien use el configurador puede no
 *     pasar este evento.
 *  6. "Restablecer" vuelve a los valores iniciales (campo vacío, racha
 *     mínima, sin spike).
 *  7. Nada se pierde al girar el móvil. Acepta `modifier`.
 *
 *  ── D.4 · FilaAgente ───────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌───────────────────────────────────────┐
 *    │ (⚔️) Jett                      (  ●)  │  ← plegada
 *    │      ▼ Ver detalles                   │
 *    └───────────────────────────────────────┘
 *    ┌───────────────────────────────────────┐
 *    │ (🛡️) Sage                      (●  )  │  ← desplegada
 *    │      ▲ Ocultar                        │
 *    │ Bajas: 9 · Muertes: 15                │
 *    │ K/D: 0.60 (negativo)         (rojo)   │
 *    └───────────────────────────────────────┘
 *
 *  - Cabecera: el avatar de su rol (D.1), el nombre y un `Switch` que
 *    dice si es uno de tus favoritos. Los favoritos los decide la
 *    PANTALLA: la fila solo avisa.
 *  - Al pulsar la cabecera se despliega o pliega el detalle. Eso solo le
 *    importa a la fila: lo guarda ella y no se pierde al girar.
 *  - Detalle: "Bajas: B · Muertes: M" y debajo "K/D: x.xx" con dos
 *    decimales (bajas / muertes; si no tiene muertes, K/D = bajas).
 *    Si el K/D es menor que 1, el texto es "K/D: x.xx (negativo)" con
 *    el color de error del tema.
 *  - TalkBack: la cabecera anuncia su acción ("Ver detalles" u
 *    "Ocultar detalles") y el `Switch` se anuncia como "Favorito".
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Economia {

    // Límites del Slider de la racha de derrotas.
    val RACHA_MIN = 0
    val RACHA_MAX = 2

    // Convierte el texto del campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un número de hasta 4 cifras.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (texto.length <= 4 && aNumero(texto) != null)

    // Créditos al empezar la ronda siguiente tras PERDER esta: lo que tienes,
    // 1900 de base, 500 por cada derrota seguida y 300 si plantaste la spike.
    // Nunca pasa de 9000. El campo vacío cuenta como 0.
    // Devuelve null si escribes más de 9000 créditos.
    fun calcular(creditos: String, racha: Int, spike: Boolean): Int? {
        val c = aNumero(creditos) ?: 0
        if (c > 9000) return null
        val bonus = if (spike) 300 else 0
        return minOf(9000, c + 1900 + 500 * racha + bonus)
    }

    // Texto para mostrar: "Máximo 9000 créditos" si es null y "N ¢" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Máximo 9000 créditos" else "$valor ¢"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Rol


// D.1 b) AvatarRol


// D.2 a) y b) ListaArmas (copia la cabecera con los huecos rellenos)


// D.3 ConfiguradorRonda


// D.4 FilaAgente (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaValorant() {
    PruebaPendiente("Ejercicio4Valorant.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu FilaAgente no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaValorant() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · AvatarRol")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Rol.entries.forEach { AvatarRol(it) }
                AvatarRol(Rol.CONTROLADOR, tamano = 72.dp)
            }
            TituloPrueba("D.2 · ListaArmas")
            var compra by rememberSaveable { mutableStateOf(setOf("Vandal")) }
            ListaArmas(
                armas = listOf("Vandal", "Phantom", "Operator", "Sheriff", "Spectre"),
                marcadas = compra,
                onCambiar = { arma, marcada -> compra = if (marcada) compra + arma else compra - arma },
                maximo = 2,
            )
            Text("Compra: ${compra.joinToString().ifEmpty { "nada" }}")
        },
        "D.3" to {
            TituloPrueba("D.3 · ConfiguradorRonda")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            ConfiguradorRonda(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
            TituloPrueba("Sin pasar onResultado (es opcional)")
            ConfiguradorRonda()
        },
        "D.4" to {
            TituloPrueba("D.4 · FilaAgente")
            var favoritos by rememberSaveable { mutableStateOf(setOf("Jett")) }
            listOf(
                Agente("Jett", Rol.DUELISTA, 21, 14),
                Agente("Sage", Rol.CENTINELA, 9, 15),
                Agente("Omen", Rol.CONTROLADOR, 12, 12),
            ).forEach { a ->
                FilaAgente(
                    agente = a,
                    favorito = a.nombre in favoritos,
                    onCambiarFavorito = { favoritos = if (it) favoritos + a.nombre else favoritos - a.nombre },
                )
            }
            Text("Favoritos: ${favoritos.joinToString().ifEmpty { "ninguno" }}")
        },
    )
}

 */
