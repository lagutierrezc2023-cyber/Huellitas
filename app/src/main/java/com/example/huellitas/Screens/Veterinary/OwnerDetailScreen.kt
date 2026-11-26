package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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


private data class OwnerPetInfo(val name: String, val breed: String, val vaccinesUpToDate: Boolean)

private data class OwnerDetailInfo(
    val email: String,
    val phone: String,
    val address: String,
    val clientSince: String,
    val visits: Int,
    val pets: List<OwnerPetInfo>
)

private val sampleOwnerDetails = mapOf(
    "Sarah Johnson" to OwnerDetailInfo(
        "sarah.johnson@email.com", "+57 300 123 4567", "Cra 15 #93-20, Bogotá", "2024", 8,
        listOf(OwnerPetInfo("Charlie", "Golden Retriever", false), OwnerPetInfo("Luna", "Siamese", true))
    ),
    "Carlos Ruiz" to OwnerDetailInfo(
        "carlos.ruiz@email.com", "+57 310 222 3344", "Calle 80 #11-42, Bogotá", "2025", 3,
        listOf(OwnerPetInfo("Max", "Labrador", true))
    ),
    "Ana Torres" to OwnerDetailInfo(
        "ana.torres@email.com", "+57 315 987 6543", "Av. 68 #45-10, Bogotá", "2023", 11,
        listOf(OwnerPetInfo("Bella", "Persian", true))
    ),
    "Diego Mora" to OwnerDetailInfo(
        "diego.mora@email.com", "+57 320 456 7890", "Cra 7 #127-35, Bogotá", "2025", 4,
        listOf(
            OwnerPetInfo("Kiwi", "Parakeet", true),
            OwnerPetInfo("Rocky", "French Bulldog", true),
            OwnerPetInfo("Nala", "Mixed breed", false)
        )
    ),
    "María López" to OwnerDetailInfo(
        "maria.lopez@email.com", "+57 301 654 3210", "Calle 53 #21-18, Bogotá", "2024", 6,
        listOf(OwnerPetInfo("Toby", "Beagle", true), OwnerPetInfo("Milo", "Maine Coon", true))
    )
)

@Composable
fun OwnerDetailScreen(
    ownerName: String = "Sarah Johnson",
    onBack: () -> Unit = {},
    onPetClick: (petName: String, petBreed: String) -> Unit = { _, _ -> },
    onCall: () -> Unit = {}
) {
    val owner = sampleOwnerDetails[ownerName]
        ?: OwnerDetailInfo("—", "—", "—", "—", 0, emptyList())
    val initials = ownerName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Owner", onBack = onBack)

        // Tarjeta principal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VetColors.Surface)
                .padding(vertical = 24.dp)
        ) {
            InitialsAvatar(initials = initials, size = 80.dp)
            Spacer(Modifier.height(14.dp))
            Text(ownerName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
            Text("Client since ${owner.clientSince}", color = VetColors.TextSecondary, fontSize = 14.sp)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryBox("Pets", owner.pets.size.toString(), Modifier.weight(1f))
            SummaryBox("Visits", owner.visits.toString(), Modifier.weight(1f))
        }

        SectionLabel("Contact")
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
        ) {
            ContactRow(Icons.Filled.Email, "Email", owner.email)
            HorizontalDivider(color = VetColors.Divider)
            ContactRow(Icons.Filled.Phone, "Phone", owner.phone)
            HorizontalDivider(color = VetColors.Divider)
            ContactRow(Icons.Filled.LocationOn, "Address", owner.address)
        }

        SectionLabel("Pets")
        if (owner.pets.isEmpty()) {
            Text("No pets registered", color = VetColors.TextSecondary, fontSize = 14.sp)
        }
        owner.pets.forEach { pet ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VetColors.Surface)
                    .clickable { onPetClick(pet.name, pet.breed) }
                    .padding(14.dp)
            ) {
                PetAvatar(size = 44.dp)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(pet.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(pet.breed, color = VetColors.TextSecondary, fontSize = 12.sp)
                }
                StatusTag(
                    text = if (pet.vaccinesUpToDate) "Vaccines OK" else "Vaccine pending",
                    positive = pet.vaccinesUpToDate
                )
                Spacer(Modifier.width(6.dp))
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
            }
        }

        Spacer(Modifier.height(4.dp))
        PrimaryButton(text = "Call owner", onClick = onCall)
    }
}

@Composable
private fun SummaryBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .padding(16.dp)
    ) {
        Text(label, color = VetColors.TextSecondary, fontSize = 12.sp)
        Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = VetColors.Mint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(label, color = VetColors.TextSecondary, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun OwnerDetailScreenPreview() {
    OwnerDetailScreen()
}
