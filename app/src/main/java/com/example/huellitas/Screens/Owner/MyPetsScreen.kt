package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
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

// // Modelo temporal para la vista
data class OwnerPet(
    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val vaccinesUpToDate: Boolean
)

private val sampleOwnerPets = listOf(
    OwnerPet("Charlie", "Dog", "Golden Retriever", "2 years", false),
    OwnerPet("Luna", "Cat", "Siamese", "4 years", true),
    OwnerPet("Rocky", "Dog", "French Bulldog", "1 year", true)
)

@Composable
fun MyPetsScreen(
    pets: List<OwnerPet> = sampleOwnerPets,
    onBack: () -> Unit = {},
    onPetClick: (OwnerPet) -> Unit = {},
    onAddPet: () -> Unit = {},
    onNavigate: (Int) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val filtered = pets.filter { it.name.contains(query, ignoreCase = true) }

    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = { VetBottomBar(items = ownerNavItems, selectedIndex = 1, onSelect = onNavigate) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPet,
                shape = CircleShape,
                containerColor = VetColors.Accent,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add pet")
            }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                ScreenHeader(
                    title = "My pets",
                    onBack = onBack,
                    trailing = { Text("${pets.size} registered", color = VetColors.TextSecondary, fontSize = 13.sp) }
                )
            }
            item {
                VetTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Search by name",
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                )
            }
            items(filtered) { pet ->
                OwnerPetCard(pet = pet, onClick = { onPetClick(pet) })
            }
            if (filtered.isEmpty()) {
                item {
                    Text(
                        "No pets found",
                        color = VetColors.TextSecondary,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OwnerPetCard(pet: OwnerPet, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        PetAvatar(size = 56.dp)
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(pet.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Text("${pet.species} · ${pet.breed} · ${pet.age}", color = VetColors.TextSecondary, fontSize = 13.sp)
            StatusTag(
                text = if (pet.vaccinesUpToDate) "Vaccines up to date" else "Vaccine pending",
                positive = pet.vaccinesUpToDate
            )
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MyPetsScreenPreview() {
    MyPetsScreen()
}