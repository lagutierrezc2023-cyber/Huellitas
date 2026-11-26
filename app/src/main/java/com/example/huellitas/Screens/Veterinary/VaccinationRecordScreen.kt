package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
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

// Datos que se envían al guardar (luego se mapean a la entidad de Room)
data class VaccinationForm(
    val vaccine: String,
    val dateApplied: String,
    val nextDose: String,
    val lotNumber: String,
    val notes: String
)

private data class VaccineRecordEntry(val vaccine: String, val date: String, val valid: Boolean)

private val vaccineOptions = listOf(
    "Rabies",
    "Parvovirus",
    "Distemper",
    "Bordetella",
    "Leptospirosis",
    "Feline leukemia"
)

private val sampleVaccineHistory = listOf(
    VaccineRecordEntry("Rabies", "Jan 10, 2026", true),
    VaccineRecordEntry("Distemper", "Jan 10, 2026", true),
    VaccineRecordEntry("Parvovirus", "Mar 02, 2025", false)
)

@Composable
fun VaccinationRecordScreen(
    petName: String = "Charlie",
    petBreed: String = "Golden Retriever",
    ownerName: String = "Sarah Johnson",
    onBack: () -> Unit = {},
    onSave: (VaccinationForm) -> Unit = {}
) {
    var vaccine by remember { mutableStateOf(vaccineOptions.first()) }
    var vaccineMenuOpen by remember { mutableStateOf(false) }
    var dateApplied by remember { mutableStateOf("") }
    var nextDose by remember { mutableStateOf("") }
    var lotNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Vaccination record", onBack = onBack)

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
        }

        Column {
            Text("Vaccine", color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VetColors.Input)
                        .clickable { vaccineMenuOpen = true }
                        .padding(horizontal = 16.dp)
                ) {
                    Text(vaccine, color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Choose vaccine", tint = VetColors.TextSecondary)
                }
                DropdownMenu(
                    expanded = vaccineMenuOpen,
                    onDismissRequest = { vaccineMenuOpen = false },
                    modifier = Modifier.background(VetColors.Surface)
                ) {
                    vaccineOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White) },
                            onClick = {
                                vaccine = option
                                vaccineMenuOpen = false
                            }
                        )
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VetTextField(
                value = dateApplied,
                onValueChange = { dateApplied = it },
                label = "Date applied",
                placeholder = "Nov 02, 2026",
                trailingIcon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
            VetTextField(
                value = nextDose,
                onValueChange = { nextDose = it },
                label = "Next dose",
                placeholder = "Nov 02, 2027",
                trailingIcon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null) },
                modifier = Modifier.weight(1f)
            )
        }

        VetTextField(
            value = lotNumber,
            onValueChange = { lotNumber = it },
            label = "Lot number",
            placeholder = "e.g. LT-20458"
        )

        VetTextField(
            value = notes,
            onValueChange = { notes = it },
            label = "Notes",
            placeholder = "Reactions, observations...",
            singleLine = false,
            minHeight = 96.dp
        )

        Spacer(Modifier.height(4.dp))

        PrimaryButton(
            text = "Save vaccination",
            onClick = { onSave(VaccinationForm(vaccine, dateApplied, nextDose, lotNumber, notes)) }
        )

        SectionTitle("Previous vaccines", Modifier.padding(top = 10.dp))
        sampleVaccineHistory.forEach { entry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(VetColors.Surface)
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text(entry.vaccine, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text(entry.date, color = VetColors.TextSecondary, fontSize = 12.sp)
                }
                StatusTag(text = if (entry.valid) "Valid" else "Overdue", positive = entry.valid)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun VaccinationRecordScreenPreview() {
    VaccinationRecordScreen()
}