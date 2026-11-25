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
 *  EJERCICIO 5 · Animal Crossing · La isla                (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de tu isla.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN" de este fichero.
 *  No hace falta tocar nada más.
 *
 *  ── D.1 · Especie y AvatarEspecie ─────────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Especie` con cuatro valores. Cada valor guarda
 *     su NOMBRE, su EMOJI y su COLOR:
 *
 *       Valor   Nombre    Emoji   Color
 *       GATO    "Gato"    🐱      Color(0xFFFF8A65)
 *       PERRO   "Perro"   🐶      Color(0xFF8D6E63)
 *       RANA    "Rana"    🐸      Color(0xFF66BB6A)
 *       PATO    "Pato"    🦆      Color(0xFF42A5F5)
 *
 *  b) Crea el componente `AvatarEspecie`: un CÍRCULO con el emoji en el
 *     centro, un fondo suave de su color y un borde de 2 dp de su color.
 *
 *         ╭────╮
 *         │ 🐸 │    ← círculo de 48 dp; el emoji, a la mitad (24 sp)
 *         ╰────╯
 *
 *     - Propiedad obligatoria: la especie.
 *     - Opcionales: el `modifier` y el tamaño (`Dp`, por defecto 48.dp).
 *       El emoji mide la mitad del tamaño.
 *     - NO guarda estado propio.
 *     - TalkBack NO lee el emoji: lee el nombre de la especie ("Rana").
 *     Uso: AvatarEspecie(Especie.RANA)
 *          AvatarEspecie(Especie.PATO, tamano = 72.dp)
 *
 *  ── D.2 · ListaDonaciones ──────────────────────────────────── (0,9) ──
 *
 *  Una lista de casillas para elegir VARIOS bichos que donar al museo,
 *  con un máximo:
 *
 *      Elegidos: 2 de 3
 *      [x] Mariposa
 *      [ ] Escarabajo
 *      [x] Luciérnaga
 *      [ ] Tarántula
 *      Si ya hay 3 elegidos, los demás se ven desactivados.
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun ListaDonaciones(
 *         bichos: ______<String>,                // todos los bichos
 *         marcados: ______<String>,              // los elegidos, sin repetidos
 *         onCambiar: (String, ______) -> ______, // bicho y si queda marcado
 *         modifier: Modifier = ______,
 *         maximo: Int = ______,                  // por defecto, todos
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. Los marcados NO se guardan dentro: se avisa con `onCambiar`.
 *     2. Arriba, el texto "Elegidos: N de M".
 *     3. Cada bicho es una fila con un `Checkbox` y su nombre. Se puede
 *        pulsar TODA la fila, no solo la casilla, y TalkBack la anuncia
 *        como casilla de verificación.
 *     4. Si ya hay `maximo` marcados, los que no están marcados se
 *        desactivan (los marcados se pueden desmarcar siempre).
 *     5. El `modifier` se aplica a la columna.
 *
 *  ── D.3 · ConfiguradorReforma ──────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Reforma` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────────────────────┐
 *      │ Metros²                     │
 *      └─────────────────────────────┘
 *      Plantas: 2
 *      ━━━━━━━━━━━━━●──────────────   (Slider de 1 a 3, solo enteros)
 *      Pago al contado (-10 %)          (  ●)
 *      Presupuesto: 36000 bayas       [ Restablecer ]
 *      (con 20 m²: 20 · 1000 · 2 = 40000, menos el 10 %)
 *
 *  1. El campo solo deja escribir lo que `esValido` acepta: si no es
 *     válido, el campo no cambia. Sale el teclado numérico.
 *  2. Las plantas se eligen con un `Slider` de PLANTAS_MIN a PLANTAS_MAX
 *     que solo se para en números enteros.
 *  3. El pago al contado es un `Switch` con su texto al lado; se puede
 *     pulsar toda la fila.
 *  4. NO hay botón de calcular: el resultado se ve siempre al día, con
 *     `calcular` y `formatear`.
 *  5. Cada vez que el usuario cambia algo y el resultado es válido, avisa
 *     hacia fuera con `onResultado`. Quien use el configurador puede no
 *     pasar este evento.
 *  6. "Restablecer" vuelve a los valores iniciales (campo vacío, una
 *     planta, sin pago al contado).
 *  7. Nada se pierde al girar el móvil. Acepta `modifier`.
 *
 *  ── D.4 · FilaVecino ───────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌───────────────────────────────────────┐
 *    │ (🐶) Canela                    (  ●)  │  ← plegada
 *    │      ▼ Ver detalles                   │
 *    └───────────────────────────────────────┘
 *    ┌───────────────────────────────────────┐
 *    │ (🐱) Rosa                      (●  )  │  ← desplegada
 *    │      ▲ Ocultar                        │
 *    │ Cumpleaños: 16 de marzo · Amistad: 10/100
 *    │ Piensa en mudarse            (rojo)   │
 *    └───────────────────────────────────────┘
 *
 *  - Cabecera: el avatar de su especie (D.1), el nombre y un `Switch` que
 *    dice si está invitado a la fiesta. Los invitados los decide la
 *    PANTALLA: la fila solo avisa.
 *  - Al pulsar la cabecera se despliega o pliega el detalle. Eso solo le
 *    importa a la fila: lo guarda ella y no se pierde al girar.
 *  - Detalle: "Cumpleaños: C · Amistad: N/100" y debajo "Está a gusto en
 *    la isla". Si la amistad es menor que 20, en su lugar pone "Piensa
 *    en mudarse" con el color de error del tema.
 *  - TalkBack: la cabecera anuncia su acción ("Ver detalles" u
 *    "Ocultar detalles") y el `Switch` se anuncia como "Invitado a la
 *    fiesta".
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Reforma {

    // Límites del Slider de las plantas.
    val PLANTAS_MIN = 1
    val PLANTAS_MAX = 3

    // Convierte el texto del campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un número de hasta 2 cifras.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (texto.length <= 2 && aNumero(texto) != null)

    // Presupuesto de Tom Nook en bayas: 1000 por metro cuadrado y planta.
    // Pagando al contado, un 10 % menos. El campo vacío cuenta como 0.
    // Devuelve null si los metros no están entre 1 y 50.
    fun calcular(metros: String, plantas: Int, contado: Boolean): Int? {
        val m = aNumero(metros) ?: 0
        if (m !in 1..50) return null
        val precio = m * 1000 * plantas
        return if (contado) precio * 9 / 10 else precio
    }

    // Texto para mostrar: "Entre 1 y 50 m²" si es null y "N bayas" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Entre 1 y 50 m²" else "$valor bayas"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Especie


// D.1 b) AvatarEspecie


// D.2 a) y b) ListaDonaciones (copia la cabecera con los huecos rellenos)


// D.3 ConfiguradorReforma


// D.4 FilaVecino (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaAnimalCrossing() {
    PruebaPendiente("Ejercicio5AnimalCrossing.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu FilaVecino no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaAnimalCrossing() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · AvatarEspecie")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Especie.entries.forEach { AvatarEspecie(it) }
                AvatarEspecie(Especie.PATO, tamano = 72.dp)
            }
            TituloPrueba("D.2 · ListaDonaciones")
            var museo by rememberSaveable { mutableStateOf(setOf("Mariposa", "Luciérnaga")) }
            ListaDonaciones(
                bichos = listOf("Mariposa", "Escarabajo", "Luciérnaga", "Tarántula", "Libélula"),
                marcados = museo,
                onCambiar = { bicho, marcado -> museo = if (marcado) museo + bicho else museo - bicho },
                maximo = 3,
            )
            Text("Para el museo: ${museo.joinToString().ifEmpty { "nada" }}")
        },
        "D.3" to {
            TituloPrueba("D.3 · ConfiguradorReforma")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            ConfiguradorReforma(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
            TituloPrueba("Sin pasar onResultado (es opcional)")
            ConfiguradorReforma()
        },
        "D.4" to {
            TituloPrueba("D.4 · FilaVecino")
            var invitados by rememberSaveable { mutableStateOf(setOf("Canela")) }
            listOf(
                Vecino("Canela", Especie.PERRO, "20 de diciembre", 80),
                Vecino("Rosa", Especie.GATO, "16 de marzo", 10),
                Vecino("Renato", Especie.RANA, "5 de julio", 45),
            ).forEach { v ->
                FilaVecino(
                    vecino = v,
                    invitado = v.nombre in invitados,
                    onCambiarInvitado = { invitados = if (it) invitados + v.nombre else invitados - v.nombre },
                )
            }
            Text("Invitados: ${invitados.joinToString().ifEmpty { "nadie" }}")
        },
    )
}

 */
