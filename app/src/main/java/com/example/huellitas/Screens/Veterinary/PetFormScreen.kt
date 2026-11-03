package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

// Datos que se envían al guardar 
data class PetRegistrationForm(
    val owner: String,
    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val sex: String,
    val weightKg: String,
    val microchip: String,
    val notes: String
)

// Dueños de ejemplo
private val sampleOwnerNames = listOf("Sarah Johnson", "Carlos Ruiz", "Ana Torres", "Diego Mora")
private val formSpecies = listOf("Dog", "Cat", "Bird", "Other")
private val formSexes = listOf("Male", "Female")

@Composable
fun PetFormScreen(
    owners: List<String> = sampleOwnerNames,
    onBack: () -> Unit = {},
    onSave: (PetRegistrationForm) -> Unit = {}
) {
    var owner by remember { mutableStateOf<String?>(null) }
    var ownerMenuOpen by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf<String?>(null) }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf<String?>(null) }
    var weight by remember { mutableStateOf("") }
    var microchip by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val canSave = owner != null && name.isNotBlank() && species != null && sex != null && age.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "New pet", onBack = onBack)

        // Dueño
        Column {
            Text("Owner", color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VetColors.Input)
                        .clickable { ownerMenuOpen = true }
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        owner ?: "Select owner",
                        color = if (owner == null) VetColors.TextSecondary else Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Choose owner",
                        tint = VetColors.TextSecondary
                    )
                }
                DropdownMenu(
                    expanded = ownerMenuOpen,
                    onDismissRequest = { ownerMenuOpen = false },
                    modifier = Modifier.background(VetColors.Surface)
                ) {
                    owners.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White) },
                            onClick = {
                                owner = option
                                ownerMenuOpen = false
                            }
                        )
                    }
                }
            }
        }

        VetTextField(
            value = name,
            onValueChange = { name = it },
            label = "Pet name",
            placeholder = "Charlie"
        )

        Column {
            Text("Species", color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                formSpecies.forEach { option ->
                    OptionChip(
                        text = option,
                        selected = option == species,
                        onClick = { species = option },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        VetTextField(
            value = breed,
            onValueChange = { breed = it },
            label = "Breed",
            placeholder = "Golden Retriever"
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            VetTextField(
                value = age,
                onValueChange = { input -> age = input.filter { it.isDigit() }.take(2) },
                label = "Age (years)",
                placeholder = "2",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            VetTextField(
                value = weight,
                onValueChange = { input -> weight = input.filter { it.isDigit() || it == '.' }.take(5) },
                label = "Weight (kg)",
                placeholder = "12.5",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )
        }

        Column {
            Text("Sex", color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                formSexes.forEach { option ->
                    OptionChip(
                        text = option,
                        selected = option == sex,
                        onClick = { sex = option },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        VetTextField(
            value = microchip,
            onValueChange = { microchip = it },
            label = "Microchip (optional)",
            placeholder = "e.g. 985112345678901"
        )

        VetTextField(
            value = notes,
            onValueChange = { notes = it },
            label = "Medical notes (optional)",
            placeholder = "Allergies, conditions, previous surgeries...",
            singleLine = false,
            minHeight = 96.dp
        )

        if (showError && !canSave) {
            Text(
                "Please select the owner and fill in name, species, sex and age.",
                color = VetColors.AccentSoft,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(4.dp))
        PrimaryButton(
            text = "Register pet",
            onClick = { if (canSave) showConfirmation = true else showError = true }
        )
    }

    if (showConfirmation && canSave) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = VetColors.Surface,
            titleContentColor = Color.White,
            textContentColor = VetColors.TextLabel,
            title = { Text("Pet registered", fontWeight = FontWeight.ExtraBold) },
            text = { Text("$name was registered under ${owner}.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    onSave(
                        PetRegistrationForm(
                            owner!!, name.trim(), species!!, breed.trim(),
                            age, sex!!, weight, microchip.trim(), notes.trim()
                        )
                    )
                }) {
                    Text("Done", color = VetColors.Mint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun OptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .background(if (selected) VetColors.Accent else VetColors.Surface)
            .border(
                BorderStroke(1.dp, if (selected) VetColors.Accent else VetColors.Border),
                shape
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text,
            color = if (selected) Color.White else VetColors.TextLabel,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 1200)
@Composable
fun PetFormScreenPreview() {
    PetFormScreen()
}