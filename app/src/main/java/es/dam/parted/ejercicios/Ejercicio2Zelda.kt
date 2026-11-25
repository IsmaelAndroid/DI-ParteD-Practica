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
 *  EJERCICIO 2 · Zelda · El inventario de Link            (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes del inventario de un juego de
 *  aventuras. Escribe tu código en el bloque "✍ TU SOLUCIÓN".
 *
 *  ── D.1 · Rareza y ChipRareza ─────────────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Rareza` con tres valores. Cada valor guarda su
 *     ETIQUETA y su COLOR:
 *
 *       Valor        Etiqueta      Color
 *       COMUN        "Común"       Color(0xFF757575)
 *       RARA         "Rara"        Color(0xFF2196F3)
 *       LEGENDARIA   "Legendaria"  Color(0xFF9C27B0)
 *
 *  b) Crea el componente `ChipRareza`: muestra la etiqueta con su color,
 *     dentro de una "píldora" con un fondo suave del mismo color.
 *     - Propiedad obligatoria: la rareza.
 *     - Opcionales: el texto (por defecto, la etiqueta) y el `modifier`.
 *     - NO guarda estado propio.
 *     Uso: ChipRareza(Rareza.LEGENDARIA)
 *          ChipRareza(Rareza.RARA, texto = "Botín")
 *
 *  ── D.2 · SelectorArma ─────────────────────────────────────── (0,9) ──
 *
 *  Una fila con las armas de Link para que el usuario equipe UNA:
 *
 *      ( Espada )  [ Arco ]  [ Escudo ]  [ Bumerán ]
 *         ↑ equipada (botón relleno)
 *      Escudo está roto: no se puede pulsar
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun SelectorArma(
 *         armas: List<______>,                // la lista de armas
 *         equipada: ______?,                  // puede no haber ninguna
 *         onEquipar: ______,                  // avisa del arma pulsada
 *         ______: Modifier = Modifier,
 *         rotas: ______ = emptyList(),        // armas rotas; por defecto, ninguna
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. El arma equipada NO se guarda dentro: se avisa con `onEquipar`.
 *     2. La equipada es un `Button`; las demás, `OutlinedButton`. Las
 *        rotas no se pueden pulsar.
 *     3. El `modifier` se aplica a la fila.
 *     4. Para TalkBack, cada botón se anuncia como "Espada, equipada",
 *        "Escudo, rota" o "Arco, disponible".
 *
 *  ── D.3 · CalculadoraDanio ─────────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `Combate` (más abajo). Tú solo
 *  montas la interfaz y llamas a sus funciones. NO modifiques la clase.
 *
 *      ┌─────────────┐  ┌─────────────┐
 *      │ Ataque      │  │ Defensa     │
 *      └─────────────┘  └─────────────┘
 *      [ Espada ] [ Flecha ] [ Bomba ] [ Magia ]
 *      Daño: 18 ❤                      ( C )
 *
 *  1. Los dos campos solo dejan escribir lo que `esValido` acepta: si no
 *     es válido, el campo no cambia. Sale el teclado numérico.
 *  2. Cada botón calcula con `calcular` y muestra `formatear(...)`.
 *     Si el golpe no hace daño, se ve "Sin daño".
 *  3. "C" vacía los dos campos y pone el daño a 0.
 *  4. Con cada daño válido avisa hacia fuera con `onDanio`. Quien use
 *     la calculadora puede no pasar este evento.
 *  5. Lo escrito y el resultado no se pierden al girar el móvil.
 *  6. Acepta `modifier`.
 *
 *  ── D.4 · TarjetaObjeto ────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌──────────────────────────────┐  ┌──────────────────────────────┐
 *    │ Espada Maestra  [Legendaria] │  │ Palo de madera      [Común]  │
 *    │ Repele el mal                │  │ Mejor que nada               │
 *    │ ████░░░░░░░░░░░░░░░░         │  │ ░░░░░░░░░░░░░░░░░░░░░░░      │
 *    │ Quedan 3 usos     (rojo)     │  │ Roto                         │
 *    │                    [ Usar ]  │  │                    [ Roto ]  │
 *    └──────────────────────────────┘  └──────────────────────────────┘
 *
 *  - Muestra el nombre del objeto, su rareza (chip de la D.1) y su
 *    descripción.
 *  - La barra indica la durabilidad que le QUEDA (usos que quedan / usos
 *    máximos).
 *  - Debajo: "Quedan N usos". Si quedan 3 o menos, en el color de error
 *    del tema. Si no queda ninguno, pone "Roto".
 *  - El botón "Usar" avisa a la pantalla. Si está roto, el botón pone
 *    "Roto" y no se puede pulsar.
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class Combate {

    // Convierte el texto de un campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un número de 0 a 999.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (aNumero(texto) ?: -1) in 0..999

    // Daño del golpe según el arma: "Espada", "Flecha", "Bomba" o "Magia".
    // Un campo vacío cuenta como 0. La Magia ignora la defensa.
    // Devuelve null si el golpe no hace daño (0 o menos).
    fun calcular(arma: String, ataque: String, defensa: String): Int? {
        val a = aNumero(ataque) ?: 0
        val d = aNumero(defensa) ?: 0
        val danio = when (arma) {
            "Espada" -> a - d
            "Flecha" -> (a - d) * 3 / 2
            "Bomba" -> a * 2 - d
            "Magia" -> a
            else -> 0
        }
        return if (danio <= 0) null else danio
    }

    // Texto para mostrar: "Sin daño" si es null y "N ❤" si no.
    fun formatear(valor: Int?): String = if (valor == null) "Sin daño" else "$valor ❤"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Rareza


// D.1 b) ChipRareza


// D.2 a) y b) SelectorArma (copia la cabecera con los huecos rellenos)


// D.3 CalculadoraDanio


// D.4 TarjetaObjeto (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaZelda() {
    PruebaPendiente("Ejercicio2Zelda.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu TarjetaObjeto no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaZelda() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · ChipRareza")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Rareza.entries.forEach { ChipRareza(it) }
            }
            ChipRareza(Rareza.RARA, texto = "Botín")
            TituloPrueba("D.2 · SelectorArma")
            var equipada by rememberSaveable { mutableStateOf<String?>("Espada") }
            SelectorArma(
                armas = listOf("Espada", "Arco", "Escudo", "Bumerán"),
                equipada = equipada,
                onEquipar = { equipada = it },
                rotas = listOf("Escudo"),
            )
            Text("Link lleva: ${equipada ?: "nada"}")
        },
        "D.3" to {
            TituloPrueba("D.3 · CalculadoraDanio")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            CalculadoraDanio(onDanio = { ultimo = it })
            Text("La pantalla ha recibido con onDanio: $ultimo")
        },
        "D.4" to {
            TituloPrueba("D.4 · TarjetaObjeto")
            var usos by rememberSaveable { mutableIntStateOf(5) }
            TarjetaObjeto(
                nombre = "Espada Maestra",
                rareza = Rareza.LEGENDARIA,
                descripcion = "Repele el mal",
                usos = usos,
                usosMax = 20,
                onUsar = { usos-- },
            )
            TarjetaObjeto("Arco real", Rareza.RARA, "Dispara tres flechas", 12, 15, onUsar = {})
            TarjetaObjeto("Palo de madera", Rareza.COMUN, "Mejor que nada", 0, 8, onUsar = {})
        },
    )
}

 */
