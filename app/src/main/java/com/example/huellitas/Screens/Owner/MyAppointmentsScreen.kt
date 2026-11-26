package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
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

// Modelo prueba para la vista
data class OwnerAppointment(
    val id: Int,
    val pet: String,
    val reason: String,
    val date: String,
    val time: String,
    val vet: String,
    val status: String // "Confirmed", "Pending", "Completed", "Cancelled"
)

private val sampleOwnerAppointments = listOf(
    OwnerAppointment(1, "Charlie", "General checkup", "Nov 05, 2026", "10:00 AM", "Dr. Martínez", "Confirmed"),
    OwnerAppointment(2, "Luna", "Vaccination", "Nov 12, 2026", "03:30 PM", "Dr. Rojas", "Pending"),
    OwnerAppointment(3, "Charlie", "Skin allergy", "Oct 14, 2026", "09:00 AM", "Dr. Martínez", "Completed"),
    OwnerAppointment(4, "Luna", "Follow-up", "Oct 02, 2026", "11:30 AM", "Dr. Rojas", "Completed"),
    OwnerAppointment(5, "Rocky", "Grooming", "Sep 20, 2026", "02:00 PM", "Dr. Rojas", "Cancelled")
)

private val tabs = listOf("Upcoming", "Past")

private fun OwnerAppointment.isUpcoming() = status == "Confirmed" || status == "Pending"

@Composable
fun MyAppointmentsScreen(
    initialAppointments: List<OwnerAppointment> = sampleOwnerAppointments,
    onBookAppointment: () -> Unit = {},
    onCancelAppointment: (OwnerAppointment) -> Unit = {},
    onNavigate: (Int) -> Unit = {}
) {
    val appointments = remember {
        mutableStateListOf<OwnerAppointment>().apply { addAll(initialAppointments) }
    }
    var selectedTab by remember { mutableIntStateOf(0) }
    var appointmentToCancel by remember { mutableStateOf<OwnerAppointment?>(null) }

    val shown = appointments.filter { if (selectedTab == 0) it.isUpcoming() else !it.isUpcoming() }
    val upcomingCount = appointments.count { it.isUpcoming() }

    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = { VetBottomBar(items = ownerNavItems, selectedIndex = 2, onSelect = onNavigate) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onBookAppointment,
                shape = CircleShape,
                containerColor = VetColors.Accent,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Book appointment")
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
                    title = "My appointments",
                    trailing = {
                        Text("$upcomingCount upcoming", color = VetColors.TextSecondary, fontSize = 13.sp)
                    }
                )
            }

            // Pestañas
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tabs.forEachIndexed { index, label ->
                        val selected = index == selectedTab
                        Surface(
                            onClick = { selectedTab = index },
                            shape = RoundedCornerShape(18.dp),
                            color = if (selected) VetColors.Accent else Color.Transparent,
                            border = BorderStroke(1.dp, if (selected) VetColors.Accent else VetColors.Border)
                        ) {
                            Text(
                                label,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp)
                            )
                        }
                    }
                }
            }

            items(shown, key = { it.id }) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onCancel = { appointmentToCancel = appointment }
                )
            }

            if (shown.isEmpty()) {
                item {
                    Text(
                        if (selectedTab == 0) "No upcoming appointments" else "No past appointments",
                        color = VetColors.TextSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }
        }
    }

    appointmentToCancel?.let { target ->
        AlertDialog(
            onDismissRequest = { appointmentToCancel = null },
            containerColor = VetColors.Surface,
            titleContentColor = Color.White,
            textContentColor = VetColors.TextLabel,
            title = { Text("Cancel appointment?", fontWeight = FontWeight.ExtraBold) },
            text = { Text("${target.pet} · ${target.reason}\n${target.date} at ${target.time}") },
            confirmButton = {
                TextButton(onClick = {
                    val index = appointments.indexOfFirst { it.id == target.id }
                    if (index >= 0) appointments[index] = target.copy(status = "Cancelled")
                    onCancelAppointment(target)
                    appointmentToCancel = null
                }) {
                    Text("Yes, cancel", color = VetColors.AccentSoft, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { appointmentToCancel = null }) {
                    Text("Keep it", color = VetColors.Mint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun AppointmentCard(appointment: OwnerAppointment, onCancel: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VetColors.Surface)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PetAvatar(size = 48.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(appointment.pet, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                Text(appointment.reason, color = VetColors.TextSecondary, fontSize = 13.sp)
            }
            StatusTag(text = appointment.status, positive = appointment.status in listOf("Confirmed", "Completed"))
        }

        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = VetColors.Divider)
        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = VetColors.Mint, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("${appointment.date} · ${appointment.time}", color = Color.White, fontSize = 13.sp)
        }
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.MedicalServices, contentDescription = null, tint = VetColors.Mint, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(appointment.vet, color = VetColors.TextLabel, fontSize = 13.sp)
        }

        if (appointment.isUpcoming()) {
            Spacer(Modifier.height(12.dp))
            TextButton(
                onClick = onCancel,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Cancel appointment", color = VetColors.AccentSoft, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MyAppointmentsScreenPreview() {
    MyAppointmentsScreen()
}