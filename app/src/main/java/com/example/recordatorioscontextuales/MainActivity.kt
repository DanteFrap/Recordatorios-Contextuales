package com.example.recordatorioscontextuales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF2457A7),
                    secondary = Color(0xFF00897B),
                    background = Color(0xFFF5F7FB),
                    surface = Color.White
                )
            ) {
                PantallaRecordatorios()
            }
        }
    }
}

@Composable
fun PantallaRecordatorios() {
    val recordatorios = ReminderData.recordatorios
    val pendientes = ReminderData.pendientes().size

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Mis recordatorios",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Organizá tus tareas del día",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResumenCard(
                    titulo = "Total",
                    cantidad = recordatorios.size.toString(),
                    modifier = Modifier.weight(1f)
                )

                ResumenCard(
                    titulo = "Pendientes",
                    cantidad = pendientes.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Lista de tareas",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = recordatorios,
                    key = { it.id }
                ) { recordatorio ->
                    RecordatorioCard(recordatorio)
                }
            }
        }
    }
}

@Composable
fun ResumenCard(
    titulo: String,
    cantidad: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = cantidad,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun RecordatorioCard(recordatorio: Recordatorio) {
    val estilo = ReminderData.estiloCategoria[recordatorio.categoria]
    val colorCategoria = estilo?.second
        ?: MaterialTheme.colorScheme.primary

    val colorPrioridad = when (recordatorio.prioridad) {
        Prioridad.Baja -> Color(0xFF388E3C)
        Prioridad.Media -> Color(0xFFF57C00)
        Prioridad.Alta -> Color(0xFFD32F2F)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recordatorio.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recordatorio.descripcion,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Etiqueta(
                    texto = estilo?.first
                        ?: recordatorio.categoria.etiqueta,
                    color = colorCategoria
                )

                Spacer(modifier = Modifier.width(8.dp))

                Etiqueta(
                    texto = recordatorio.prioridad.etiqueta,
                    color = colorPrioridad
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Estado: ${recordatorio.estado.etiqueta}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = when (recordatorio.estado) {
                    EstadoRecordatorio.Pendiente ->
                        Color(0xFFB26A00)

                    EstadoRecordatorio.EnCurso ->
                        Color(0xFF2457A7)

                    EstadoRecordatorio.Completado ->
                        Color(0xFF388E3C)
                }
            )

            recordatorio.fechaVencimiento?.let { fecha ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Vencimiento: $fecha",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            recordatorio.ubicacion?.let { ubicacion ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ubicación: ${
                        ubicacion.descripcion ?: "Sin descripción"
                    }",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            recordatorio.aviso?.let { aviso ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aviso programado: $aviso",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun Etiqueta(
    texto: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = texto,
            modifier = Modifier
                .background(Color.Transparent)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
