package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.VetBottomBar
import com.vetclinic.app.ui.components.VetTextField
import com.vetclinic.app.ui.components.vetNavItems
import com.vetclinic.app.ui.theme.VetColors

// Modelo temporal para la vista (luego se reemplaza por la entidad de Room)
data class Patient(
    val name: String,
    val breed: String,
    val owner: String,
    val lastVisit: String,
    val category: String // "Dogs", "Cats", "Others"
)

private val samplePatients = listOf(
    Patient("Charlie", "Golden Retriever", "Sarah Johnson", "May 24", "Dogs"),
    Patient("Luna", "Siamese", "Sarah Johnson", "Oct 02", "Cats"),
    Patient("Max", "Labrador", "Carlos Ruiz", "Sep 18", "Dogs"),
    Patient("Bella", "Persian", "Ana Torres", "Oct 30", "Cats"),
    Patient("Kiwi", "Parakeet", "Diego Mora", "Aug 11", "Others")
)

private val filters = listOf("All", "Dogs", "Cats", "Others")

@Composable
fun PetListScreen(
    patients: List<Patient> = samplePatients,
    onPatientClick: (Patient) -> Unit = {},
    onAddPet: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onNavigate: (Int) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    val filtered = patients.filter { p ->
        (selectedFilter == "All" || p.category == selectedFilter) &&
                (p.name.contains(query, ignoreCase = true) || p.owner.contains(query, ignoreCase = true))
    }

    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = { VetBottomBar(items = vetNavItems, selectedIndex = 1, onSelect = onNavigate) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPet,
                shape = CircleShape,
                containerColor = VetColors.Accent,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Register pet")
            }
        }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Encabezado
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Patients", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                        Text("${patients.size} registered pets", color = VetColors.TextSecondary, fontSize = 13.sp)
                    }
                    IconButton(
                        onClick = onFilterClick,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(VetColors.Surface)
                    ) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter", tint = Color.White)
                    }
                }
            }
            // Buscador
            item {
                VetTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = "Search patient or owner",
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
                )
            }
            // Filtros
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { f ->
                        val selected = f == selectedFilter
                        Surface(
                            onClick = { selectedFilter = f },
                            shape = RoundedCornerShape(18.dp),
                            color = if (selected) VetColors.Accent else Color.Transparent,
                            border = BorderStroke(1.dp, if (selected) VetColors.Accent else VetColors.Border)
                        ) {
                            Text(
                                f,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                            )
                        }
                    }
                }
            }
            // Lista
            items(filtered) { patient ->
                PatientCard(patient = patient, onClick = { onPatientClick(patient) })
            }
            if (filtered.isEmpty()) {
                item {
                    Text(
                        "No matching patients",
                        color = VetColors.TextSecondary,
                        modifier = Modifier.fillMaxWidth().padding(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PatientCard(patient: Patient, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(VetColors.Input),
            contentAlignment = Alignment.Center
        ) {
            Text(patient.name.take(1), color = VetColors.Mint, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(patient.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(
                    " · ${patient.breed}",
                    color = VetColors.TextSecondary,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text("Owner: ${patient.owner}", color = VetColors.TextSecondary, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Last visit", color = VetColors.TextSecondary, fontSize = 11.sp)
            Text(patient.lastVisit, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PetListScreenPreview() {
    PetListScreen()
}