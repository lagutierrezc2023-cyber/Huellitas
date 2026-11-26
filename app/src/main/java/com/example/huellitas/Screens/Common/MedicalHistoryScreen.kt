package com.vetclinic.app.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.*
import com.vetclinic.app.ui.theme.VetColors

// Modelo temporal para la vista (luego se reemplaza por la entidad de Room)
data class MedicalRecord(
    val date: String,
    val reason: String,
    val diagnosis: String,
    val treatment: String,
    val vet: String,
    val weight: String,
    val temperature: String
)

private val sampleMedicalHistories = mapOf(
    "Charlie" to listOf(
        MedicalRecord(
            "Oct 14, 2026", "Skin allergy", "Contact dermatitis",
            "Antihistamine for 7 days and medicated shampoo twice a week",
            "Dr. Martínez", "28.4 kg", "38.6 °C"
        ),
        MedicalRecord(
            "May 24, 2026", "General checkup", "Healthy, slight overweight",
            "Diet adjustment and daily walks of 30 minutes",
            "Dr. Martínez", "29.1 kg", "38.5 °C"
        ),
        MedicalRecord(
            "Jan 10, 2026", "Vaccination", "No findings",
            "Rabies and distemper booster applied",
            "Dr. Rojas", "28.0 kg", "38.4 °C"
        )
    ),
    "Luna" to listOf(
        MedicalRecord(
            "Oct 02, 2026", "Follow-up", "Recovered from upper respiratory infection",
            "No further treatment needed",
            "Dr. Rojas", "4.2 kg", "38.7 °C"
        ),
        MedicalRecord(
            "Sep 18, 2026", "Emergency", "Upper respiratory infection",
            "Antibiotics for 10 days and rest",
            "Dr. Martínez", "4.0 kg", "39.4 °C"
        )
    )
)

@Composable
fun MedicalHistoryScreen(
    petName: String = "Charlie",
    records: List<MedicalRecord> = sampleMedicalHistories[petName] ?: emptyList(),
    onBack: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Medical history", onBack = onBack)

        // Resumen de la mascota
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VetColors.Surface)
                .padding(16.dp)
        ) {
            PetAvatar(size = 52.dp, background = VetColors.Accent, tint = Color.White)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(petName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text(
                    "${records.size} consultations",
                    color = VetColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
            if (records.isNotEmpty()) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("Last visit", color = VetColors.TextSecondary, fontSize = 11.sp)
                    Text(
                        records.first().date,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (records.isEmpty()) {
            Text(
                "No consultations registered yet",
                color = VetColors.TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
        } else {
            SectionLabel("Consultations")
            records.forEachIndexed { index, record ->
                // La consulta más reciente aparece abierta
                RecordCard(record = record, initiallyExpanded = index == 0)
            }
        }
    }
}

@Composable
private fun RecordCard(record: MedicalRecord, initiallyExpanded: Boolean) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { expanded = !expanded }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VetColors.Input)
            ) {
                Icon(Icons.Filled.MedicalServices, contentDescription = null, tint = VetColors.Mint)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(record.reason, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("${record.date} · ${record.vet}", color = VetColors.TextSecondary, fontSize = 12.sp)
            }
            Icon(
                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = VetColors.TextSecondary
            )
        }

        if (expanded) {
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = VetColors.Divider)
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DetailItem("Weight", record.weight, Modifier.weight(1f))
                DetailItem("Temperature", record.temperature, Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            DetailItem("Diagnosis", record.diagnosis)
            Spacer(Modifier.height(10.dp))
            DetailItem("Treatment", record.treatment)
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, color = VetColors.TextSecondary, fontSize = 12.sp)
        Spacer(Modifier.height(2.dp))
        Text(value, color = Color.White, fontSize = 14.sp)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MedicalHistoryScreenPreview() {
    MedicalHistoryScreen()
}