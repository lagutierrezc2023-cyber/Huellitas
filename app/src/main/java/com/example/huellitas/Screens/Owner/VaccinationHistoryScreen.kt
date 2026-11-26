package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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


private data class VaccineHistoryEntry(
    val name: String,
    val appliedOn: String,
    val nextDue: String,
    val applied: Boolean,
    val vet: String
)

private val sampleVaccineHistories = mapOf(
    "Charlie" to listOf(
        VaccineHistoryEntry("Parvovirus", "Mar 02, 2025", "Mar 02, 2026", false, "Dr. Martínez"),
        VaccineHistoryEntry("Rabies", "Jan 10, 2026", "Jan 10, 2027", true, "Dr. Martínez"),
        VaccineHistoryEntry("Distemper", "Jan 10, 2026", "Jan 10, 2027", true, "Dr. Martínez"),
        VaccineHistoryEntry("Bordetella", "Aug 21, 2025", "Aug 21, 2026", true, "Dr. Rojas")
    ),
    "Luna" to listOf(
        VaccineHistoryEntry("Rabies", "Nov 08, 2025", "Nov 08, 2026", true, "Dr. Rojas"),
        VaccineHistoryEntry("FVRCP", "Nov 08, 2025", "Nov 08, 2026", true, "Dr. Rojas"),
        VaccineHistoryEntry("Feline leukemia", "Feb 14, 2026", "Feb 14, 2027", true, "Dr. Martínez")
    )
)

@Composable
fun VaccinationHistoryScreen(
    petName: String = "Charlie",
    onBack: () -> Unit = {},
    onBookAppointment: () -> Unit = {}
) {
    val vaccines = sampleVaccineHistories[petName] ?: emptyList()
    val pending = vaccines.filter { !it.applied }
    val upToDate = vaccines.filter { it.applied }
    val progress = if (vaccines.isEmpty()) 0f else upToDate.size.toFloat() / vaccines.size

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Vaccination history", onBack = onBack)

        // Resumen de la mascota
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VetColors.Surface)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PetAvatar(size = 52.dp, background = VetColors.Accent, tint = Color.White)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(petName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text(
                        "${upToDate.size} of ${vaccines.size} vaccines up to date",
                        color = VetColors.TextSecondary,
                        fontSize = 13.sp
                    )
                }
                StatusTag(
                    text = if (pending.isEmpty()) "Up to date" else "Action needed",
                    positive = pending.isEmpty()
                )
            }
            Spacer(Modifier.height(14.dp))
            LinearProgressIndicator(
                progress = { progress },
                color = VetColors.Mint,
                trackColor = VetColors.Input,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
        }

        if (vaccines.isEmpty()) {
            Text(
                "No vaccines registered yet",
                color = VetColors.TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            )
        }

        if (pending.isNotEmpty()) {
            SectionLabel("Pending")
            pending.forEach { VaccineCard(it) }
        }

        if (upToDate.isNotEmpty()) {
            SectionLabel("Applied")
            upToDate.forEach { VaccineCard(it) }
        }

        Spacer(Modifier.height(4.dp))
        PrimaryButton(text = "Book appointment", onClick = onBookAppointment)
    }
}

@Composable
private fun VaccineCard(entry: VaccineHistoryEntry) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .padding(14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(VetColors.Input)
        ) {
            Icon(
                Icons.Filled.Vaccines,
                contentDescription = null,
                tint = if (entry.applied) VetColors.Mint else VetColors.Warning
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(entry.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("Applied: ${entry.appliedOn} · ${entry.vet}", color = VetColors.TextSecondary, fontSize = 12.sp)
            Text("Next dose: ${entry.nextDue}", color = VetColors.TextLabel, fontSize = 12.sp)
        }
        Spacer(Modifier.width(8.dp))
        StatusTag(
            text = if (entry.applied) "Valid" else "Due soon",
            positive = entry.applied
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun VaccinationHistoryScreenPreview() {
    VaccinationHistoryScreen()
}
