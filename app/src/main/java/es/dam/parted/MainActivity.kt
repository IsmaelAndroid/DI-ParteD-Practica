package es.dam.parted

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.dam.parted.ejercicios.PruebaAnimalCrossing
import es.dam.parted.ejercicios.PruebaFifa
import es.dam.parted.ejercicios.PruebaMarioKart
import es.dam.parted.ejercicios.PruebaValorant
import es.dam.parted.ejercicios.PruebaZelda

/*
 * Menú para probar cada ejercicio.
 * 1. Programa los componentes en el fichero del ejercicio (paquete `ejercicios`).
 * 2. Descomenta la pantalla de prueba que hay al final de ese fichero.
 * 3. Ejecuta la app, entra en el ejercicio y comprueba cada apartado.
 */

private val ejercicios: List<Pair<String, @Composable () -> Unit>> = listOf(
    "1 · Mario Kart · El torneo de karts" to { PruebaMarioKart() },
    "2 · Zelda · El inventario de Link" to { PruebaZelda() },
    "3 · FIFA · Ultimate Team" to { PruebaFifa() },
    "4 · Valorant · La selección de agentes" to { PruebaValorant() },
    "5 · Animal Crossing · La isla" to { PruebaAnimalCrossing() },
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppPracticas()
            }
        }
    }
}

@Composable
fun AppPracticas() {
    var actual by rememberSaveable { mutableIntStateOf(-1) }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.safeDrawingPadding()) {
            if (actual == -1) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Parte D · Componentes propios", style = MaterialTheme.typography.headlineSmall)
                    Text("Programa el kit de componentes de cada ejercicio y pruébalo aquí.")
                    ejercicios.forEachIndexed { i, (titulo, _) ->
                        FilledTonalButton(onClick = { actual = i }, Modifier.fillMaxWidth()) { Text(titulo) }
                    }
                }
            } else {
                BackHandler { actual = -1 }
                TextButton(onClick = { actual = -1 }) { Text("← Volver") }
                ejercicios[actual].second()
            }
        }
    }
}
