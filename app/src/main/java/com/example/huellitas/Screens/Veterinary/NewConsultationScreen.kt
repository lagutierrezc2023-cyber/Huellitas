package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.*
import com.vetclinic.app.ui.theme.VetColors

// Datos que se envían al guardar (luego se mapean a la entidad de Room)
data class ConsultationForm(
    val reason: String,
    val weightKg: String,
    val temperatureC: String,
    val diagnosis: String,
    val treatment: String,
    val scheduleFollowUp: Boolean
)

private val consultationReasons = listOf(
    "General checkup",
    "Vaccination",
    "Deworming",
    "Post-surgery check",
    "Emergency"
)

@Composable
fun NewConsultationScreen(
    petName: String = "Charlie",
    petBreed: String = "Golden Retriever",
    ownerName: String = "Sarah Johnson",
    date: String = "Nov 02",
    onBack: () -> Unit = {},
    onSave: (ConsultationForm) -> Unit = {}
) {
    var reason by remember { mutableStateOf(consultationReasons.first()) }
    var reasonMenuOpen by remember { mutableStateOf(false) }
    var weight by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var treatment by remember { mutableStateOf("") }
    var followUp by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "New consultation", onBack = onBack)

        // Tarjeta del paciente
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
                .padding(14.dp)
        ) {
            PetAvatar(size = 48.dp, background = VetColors.Accent, tint = Color.White)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(petName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                Text("$petBreed · Owner: $ownerName", color = VetColors.TextSecondary, fontSize = 12.sp)
            }
            Text(date, color = VetColors.TextSecondary, fontSize = 12.sp)
        }

        // Motivo (desplegable)
        Column {
            Text("Reason for visit", color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VetColors.Input)
                        .clickable { reasonMenuOpen = true }
                        .padding(horizontal = 16.dp)
                ) {
                    Text(reason, color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Choose reason", tint = VetColors.TextSecondary)
                }
                DropdownMenu(
                    expanded = reasonMenuOpen,
                    onDismissRequest = { reasonMenuOpen = false },
                    modifier = Modifier.background(VetColors.Surface)
                ) {
                    consultationReasons.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White) },
                            onClick = {
                                reason = option
                                reasonMenuOpen = false
                            }
                        )
                    }
                }
            }
        }

        // Peso y temperatura
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VetTextField(
                value = weight,
                onValueChange = { weight = it },
                label = "Weight (kg)",
                placeholder = "28.5",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
            VetTextField(
                value = temperature,
                onValueChange = { temperature = it },
                label = "Temperature (°C)",
                placeholder = "38.6",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
        }

        VetTextField(
            value = diagnosis,
            onValueChange = { diagnosis = it },
            label = "Diagnosis",
            placeholder = "Describe the diagnosis",
            singleLine = false,
            minHeight = 96.dp
        )

        VetTextField(
            value = treatment,
            onValueChange = { treatment = it },
            label = "Treatment / medications",
            placeholder = "e.g. Amoxicillin 250 mg every 12 h for 7 days",
            singleLine = false,
            minHeight = 96.dp
        )

        // Control de seguimiento
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { followUp = !followUp }
        ) {
            Checkbox(
                checked = followUp,
                onCheckedChange = { followUp = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = VetColors.Accent,
                    uncheckedColor = VetColors.Mint,
                    checkmarkColor = Color.White
                )
            )
            Text("Schedule follow-up check", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(8.dp))

        PrimaryButton(
            text = "Save consultation",
            onClick = {
                onSave(ConsultationForm(reason, weight, temperature, diagnosis, treatment, followUp))
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun NewConsultationScreenPreview() {
    NewConsultationScreen()
}