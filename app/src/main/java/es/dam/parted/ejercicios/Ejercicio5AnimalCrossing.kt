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
 *  EJERCICIO 5 · Animal Crossing · La isla                (3,2 puntos)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Vas a crear el kit de componentes de la app de una isla con vecinos.
 *  Escribe tu código en el bloque "✍ TU SOLUCIÓN".
 *
 *  ── D.1 · Amistad y ChipAmistad ───────────────────────────── (0,5) ──
 *
 *  a) Crea un `enum class Amistad` con tres valores. Cada valor guarda
 *     su ETIQUETA y su COLOR:
 *
 *       Valor   Etiqueta        Color
 *       BAJA    "Conocido"      Color(0xFF8D6E63)
 *       MEDIA   "Amigo"         Color(0xFF43A047)
 *       ALTA    "Mejor amigo"   Color(0xFFEC407A)
 *
 *  b) Crea el componente `ChipAmistad`: muestra la etiqueta con su color,
 *     dentro de una "píldora" con un fondo suave del mismo color.
 *     - Propiedad obligatoria: la amistad.
 *     - Opcionales: el texto (por defecto, la etiqueta) y el `modifier`.
 *     - NO guarda estado propio.
 *     Uso: ChipAmistad(Amistad.ALTA)
 *          ChipAmistad(Amistad.BAJA, texto = "Recién llegado")
 *
 *     Extra: añade al enum una función `desde(puntos: Int): Amistad`
 *     dentro de un `companion object` que devuelva BAJA por debajo de
 *     40 puntos, MEDIA de 40 a 79 y ALTA de 80 en adelante.
 *
 *  ── D.2 · SelectorVisitante ────────────────────────────────── (0,9) ──
 *
 *  Una fila con los visitantes de la isla para que el usuario invite a
 *  UNO:
 *
 *      [ Tendo ]  [ Guindo ]  ( Sócrates )  [ Ladino ]
 *                                ↑ invitado (botón relleno)
 *      Ladino no está hoy: no se puede pulsar
 *
 *  a) Completa los huecos (______) de la cabecera:
 *
 *     @Composable
 *     fun SelectorVisitante(
 *         visitantes: ______,                     // la lista de visitantes
 *         invitado: ______,                       // puede no haber ninguno
 *         onInvitar: ______,                      // avisa del visitante pulsado
 *         modifier: ______ = ______,
 *         ausentes: ______ = ______,              // por defecto, ninguno
 *     ) {
 *
 *  b) Escribe el contenido del componente:
 *     1. El invitado NO se guarda dentro: se avisa con `onInvitar`.
 *     2. El invitado es un `Button`; los demás, `OutlinedButton`. Los
 *        ausentes no se pueden pulsar.
 *     3. El `modifier` se aplica a la fila.
 *     4. Para TalkBack, cada botón se anuncia como "Sócrates, invitado",
 *        "Ladino, ausente" o "Tendo, disponible".
 *
 *  ── D.3 · CalculadoraNabos ─────────────────────────────────── (1,0) ──
 *
 *  La lógica YA ESTÁ HECHA en la clase `MercadoNabos` (más abajo). Tú
 *  solo montas la interfaz y llamas a sus funciones. NO modifiques la
 *  clase.
 *
 *      ┌─────────────┐  ┌─────────────┐
 *      │ Nabos       │  │ Precio      │
 *      └─────────────┘  └─────────────┘
 *      [ Comprar ] [ Vender ] [ Ganancia ]
 *      Bayas: 12000                     ( C )
 *      (precio de compra de la semana: 100 bayas)
 *
 *  1. Los dos campos solo dejan escribir lo que `esValido` acepta: si no
 *     es válido, el campo no cambia. Sale el teclado numérico.
 *  2. Cada botón calcula con `calcular` y muestra `formatear(...)`.
 *  3. "C" vacía los dos campos y pone el resultado a 0.
 *  4. Con cada resultado válido avisa hacia fuera con `onResultado`.
 *     Quien use la calculadora puede no pasar este evento.
 *  5. Lo escrito y el resultado no se pierden al girar el móvil.
 *  6. Acepta `modifier` y un parámetro OPCIONAL `precioSemana` (Int,
 *     por defecto 100), que se pasa a `calcular`.
 *
 *  ── D.4 · TarjetaVecino ────────────────────────────────────── (0,8) ──
 *
 *  Tú decides sus parámetros.
 *
 *    ┌──────────────────────────────┐  ┌──────────────────────────────┐
 *    │ Canela            [Conocido] │  │ Gaspar         [Mejor amigo] │
 *    │ Perro · vive en la isla      │  │ Gato · vive en la isla       │
 *    │ ███░░░░░░░░░░░░░░░░░░        │  │ ████████████████████████     │
 *    │ Piensa en mudarse   (rojo)   │  │ ¡Te ha dado su foto!         │
 *    │                 [ Regalar ]  │  │         [ Foto conseguida ]  │
 *    └──────────────────────────────┘  └──────────────────────────────┘
 *
 *  - Muestra el nombre del vecino, su especie y su nivel de amistad con
 *    el chip de la D.1. El nivel NO se pasa: se calcula con los puntos
 *    (usa `Amistad.desde` si lo has hecho).
 *  - La barra indica los puntos de amistad (de 0 a 100).
 *  - Debajo: "Amistad: N / 100". Si tiene menos de 20 puntos, en lugar
 *    de eso pone "Piensa en mudarse" en el color de error del tema. Con
 *    100 puntos pone "¡Te ha dado su foto!".
 *  - El botón "Regalar" avisa a la pantalla. Con 100 puntos pone "Foto
 *    conseguida" y no se puede pulsar.
 * ════════════════════════════════════════════════════════════════════
 */

// ─── Código que te dan (NO lo modifiques) ────────────────────────────

class MercadoNabos {

    // Convierte el texto de un campo en número entero. null si no lo es.
    fun aNumero(texto: String): Int? = texto.toIntOrNull()

    // true si el texto se puede dejar escrito: vacío o un entero de 0 a 9999.
    fun esValido(texto: String): Boolean = texto.isEmpty() || (aNumero(texto) ?: -1) in 0..9999

    // Según la operación: "Comprar", "Vender" o "Ganancia".
    // - Comprar: lo que cuesta comprar los nabos a `precioSemana`. Los nabos
    //   se venden en manojos de 10: null si la cantidad no es múltiplo de 10.
    // - Vender: lo que te dan por los nabos al precio de hoy.
    // - Ganancia: vender hoy menos lo que costaron (puede ser negativa).
    // Un campo vacío cuenta como 0.
    fun calcular(operacion: String, nabos: String, precio: String, precioSemana: Int): Int? {
        val n = aNumero(nabos) ?: 0
        val p = aNumero(precio) ?: 0
        return when (operacion) {
            "Comprar" -> if (n % 10 != 0) null else n * precioSemana
            "Vender" -> n * p
            "Ganancia" -> n * p - n * precioSemana
            else -> null
        }
    }

    // Texto para mostrar: el aviso de los manojos si es null y, si no, el número.
    fun formatear(valor: Int?): String = if (valor == null) "Solo en manojos de 10" else "$valor"
}

// ─── ✍ TU SOLUCIÓN ───────────────────────────────────────────────────

// D.1 a) enum class Amistad


// D.1 b) ChipAmistad


// D.2 a) y b) SelectorVisitante (copia la cabecera con los huecos rellenos)


// D.3 CalculadoraNabos


// D.4 TarjetaVecino (y la data class, si decides usar una)



// ─── Pantalla de prueba (la abre el menú de la app) ──────────────────

@Composable
fun PruebaAnimalCrossing() {
    PruebaPendiente("Ejercicio5AnimalCrossing.kt")
}

/*
 * Cuando tengas los componentes, BORRA la función de arriba y
 * DESCOMENTA la de abajo. Si los parámetros de tu TarjetaVecino no son los
 * mismos, adapta las llamadas de la pestaña "D.4".

@Composable
fun PruebaAnimalCrossing() {
    PruebaPorApartados(
        "D.1 · D.2" to {
            TituloPrueba("D.1 · ChipAmistad")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Amistad.entries.forEach { ChipAmistad(it) }
            }
            ChipAmistad(Amistad.BAJA, texto = "Recién llegado")
            Text("Amistad.desde(55) = ${Amistad.desde(55)}")
            TituloPrueba("D.2 · SelectorVisitante")
            var invitado by rememberSaveable { mutableStateOf<String?>("Sócrates") }
            SelectorVisitante(
                visitantes = listOf("Tendo", "Guindo", "Sócrates", "Ladino"),
                invitado = invitado,
                onInvitar = { invitado = it },
                ausentes = listOf("Ladino"),
            )
            Text("Invitado hoy: ${invitado ?: "nadie"}")
        },
        "D.3" to {
            TituloPrueba("D.3 · CalculadoraNabos")
            var ultimo by rememberSaveable { mutableIntStateOf(0) }
            CalculadoraNabos(onResultado = { ultimo = it })
            Text("La pantalla ha recibido con onResultado: $ultimo")
            TituloPrueba("Con precioSemana = 90")
            CalculadoraNabos(precioSemana = 90)
        },
        "D.4" to {
            TituloPrueba("D.4 · TarjetaVecino")
            var puntos by rememberSaveable { mutableIntStateOf(10) }
            TarjetaVecino("Canela", "Perro", puntos, onRegalar = { puntos = (puntos + 15).coerceAtMost(100) })
            TarjetaVecino("Rosa", "Gato", 55, onRegalar = {})
            TarjetaVecino("Gaspar", "Gato", 100, onRegalar = {})
        },
    )
}

 */
