package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.*
import com.vetclinic.app.ui.theme.VetColors

// Modelo temporal para la vista (luego se reemplaza por la entidad de Room)
private data class PetProfileDetail(
    val species: String,
    val breed: String,
    val age: String,
    val sex: String,
    val weight: String,
    val color: String,
    val vaccinesUpToDate: Boolean
)

private val samplePetDetails = mapOf(
    "Charlie" to PetProfileDetail("Dog", "Golden Retriever", "2 years", "Male", "28.5 kg", "Golden", false),
    "Luna" to PetProfileDetail("Cat", "Siamese", "4 years", "Female", "4.2 kg", "Cream", true),
    "Rocky" to PetProfileDetail("Dog", "French Bulldog", "1 year", "Male", "11.3 kg", "Brindle", true)
)

@Composable
fun PetProfileScreen(
    petName: String = "Charlie",
    onBack: () -> Unit = {},
    onVaccinationHistory: () -> Unit = {},
    onMedicalHistory: () -> Unit = {},
    onBookAppointment: () -> Unit = {}
) {
    val pet = samplePetDetails[petName]
        ?: PetProfileDetail("Dog", "Mixed breed", "—", "—", "—", "—", true)

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Pet profile", onBack = onBack)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VetColors.Surface)
                .padding(vertical = 24.dp)
        ) {
            PetAvatar(size = 96.dp, background = VetColors.Accent, tint = Color.White)
            Spacer(Modifier.height(14.dp))
            Text(petName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text("${pet.species} · ${pet.breed}", color = VetColors.TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
            StatusTag(
                text = if (pet.vaccinesUpToDate) "Vaccines up to date" else "Vaccine pending",
                positive = pet.vaccinesUpToDate
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PetInfoBox("Age", pet.age, Modifier.weight(1f))
            PetInfoBox("Weight", pet.weight, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PetInfoBox("Sex", pet.sex, Modifier.weight(1f))
            PetInfoBox("Color", pet.color, Modifier.weight(1f))
        }

        SectionTitle("Health records", Modifier.padding(top = 6.dp))
        PetRecordLink(
            icon = Icons.Filled.MedicalServices,
            title = "Vaccination history",
            subtitle = "Applied and upcoming vaccines",
            onClick = onVaccinationHistory
        )
        PetRecordLink(
            icon = Icons.Filled.Description,
            title = "Medical history",
            subtitle = "Consultations and treatments",
            onClick = onMedicalHistory
        )

        Spacer(Modifier.height(4.dp))
        PrimaryButton(text = "Book appointment", onClick = onBookAppointment)
    }
}

@Composable
private fun PetInfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .padding(16.dp)
    ) {
        Text(label, color = VetColors.TextSecondary, fontSize = 12.sp)
        Text(value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PetRecordLink(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(VetColors.Input),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = VetColors.Mint)
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = VetColors.TextSecondary, fontSize = 12.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PetProfileScreenPreview() {
    PetProfileScreen()
}