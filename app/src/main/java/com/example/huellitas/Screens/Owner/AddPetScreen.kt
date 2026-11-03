package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
data class AddPetForm(
    val name: String,
    val species: String,
    val breed: String,
    val age: String,
    val sex: String,
    val weightKg: String,
    val notes: String
)

private val petSpecies = listOf("Dog", "Cat", "Bird", "Other")
private val petSexes = listOf("Male", "Female")

@Composable
fun AddPetScreen(
    onBack: () -> Unit = {},
    onSave: (AddPetForm) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf<String?>(null) }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf<String?>(null) }
    var weight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val canSave = name.isNotBlank() && species != null && sex != null && age.isNotBlank()

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Add pet", onBack = onBack)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            PetAvatar(size = 88.dp, background = VetColors.Surface)
        }

        VetTextField(
            value = name,
            onValueChange = { name = it },
            label = "Name",
            placeholder = "Charlie"
        )

        Column {
            Text("Species", color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                petSpecies.forEach { option ->
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
                petSexes.forEach { option ->
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
            value = notes,
            onValueChange = { notes = it },
            label = "Notes (optional)",
            placeholder = "Allergies, behavior, anything the vet should know",
            singleLine = false,
            minHeight = 100.dp
        )

        if (showError && !canSave) {
            Text(
                "Please fill in name, species, sex and age.",
                color = VetColors.AccentSoft,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(4.dp))
        PrimaryButton(
            text = "Save pet",
            onClick = { if (canSave) showConfirmation = true else showError = true }
        )
    }

    if (showConfirmation && canSave) {
        AlertDialog(
            onDismissRequest = {},
            containerColor = VetColors.Surface,
            titleContentColor = Color.White,
            textContentColor = VetColors.TextLabel,
            title = { Text("Pet added", fontWeight = FontWeight.ExtraBold) },
            text = { Text("$name was added to your pets.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    onSave(AddPetForm(name.trim(), species!!, breed.trim(), age, sex!!, weight, notes.trim()))
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

@Preview(showBackground = true, widthDp = 390, heightDp = 1100)
@Composable
fun AddPetScreenPreview() {
    AddPetScreen()
}