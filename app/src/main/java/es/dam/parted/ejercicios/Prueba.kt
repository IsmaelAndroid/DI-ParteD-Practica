package es.dam.parted.ejercicios

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/*
 * ════════════════════════════════════════════════════════════════════
 *  PANTALLA DE PRUEBA (no es parte del ejercicio, no hace falta tocarla)
 * ════════════════════════════════════════════════════════════════════
 *
 *  Cada ejercicio tiene una pantalla de prueba con tres pestañas:
 *    - "D.1 · D.2": el chip y el selector.
 *    - "D.3": la calculadora.
 *    - "D.4": las tarjetas.
 *
 *  Así puedes ver cada componente que programas por separado.
 * ════════════════════════════════════════════════════════════════════
 */

@Composable
fun PruebaPorApartados(vararg apartados: Pair<String, @Composable () -> Unit>) {
    // La pestaña elegida se guarda con rememberSaveable: no se pierde al girar.
    var actual by rememberSaveable { mutableIntStateOf(0) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            apartados.forEachIndexed { i, (titulo, _) ->
                if (i == actual) {
                    Button(onClick = { actual = i }) { Text(titulo) }
                } else {
                    OutlinedButton(onClick = { actual = i }) { Text(titulo) }
                }
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            apartados[actual].second()
        }
    }
}

/** Título pequeño para separar los bloques dentro de una pestaña. */
@Composable
fun TituloPrueba(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
}

/** Lo que se ve mientras el ejercicio está sin hacer. */
@Composable
fun PruebaPendiente(nombreFichero: String) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Ejercicio sin hacer", style = MaterialTheme.typography.titleLarge)
        Text("1. Programa los componentes en $nombreFichero.")
        Text("2. Al final del fichero, descomenta el bloque de la pantalla de prueba.")
        Text("3. Vuelve a ejecutar la app y comprueba cada apartado.")
    }
}
