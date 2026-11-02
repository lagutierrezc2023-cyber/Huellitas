package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
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

// Datos de ejemplo
private data class Reminder(val text: String, val tag: String)

private val samplePets = listOf("Charlie", "Luna")
private val sampleReminders = listOf(
    Reminder("Rabies vaccine — Luna", "In 5 days"),
    Reminder("Parvovirus — Charlie", "Due soon")
)

@Composable
fun HomeScreen(
    userName: String = "Sarah Johnson",
    onNotificationsClick: () -> Unit = {},
    onPetClick: (String) -> Unit = {},
    onAddPet: () -> Unit = {},
    onBookAppointment: () -> Unit = {},
    onNavigate: (Int) -> Unit = {}
) {
    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = { VetBottomBar(items = ownerNavItems, selectedIndex = 0, onSelect = onNavigate) }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Saludo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Hi,", color = VetColors.TextSecondary, fontSize = 14.sp)
                    Text(userName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                }
                IconButton(
                    onClick = onNotificationsClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(VetColors.Surface)
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.White)
                }
                Spacer(Modifier.width(10.dp))
                InitialsAvatar(initials = "SJ")
            }

            // Mis mascotas
            SectionTitle("My pets", Modifier.padding(top = 6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                samplePets.forEach { pet ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(104.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(VetColors.Surface)
                            .clickable { onPetClick(pet) }
                    ) {
                        PetAvatar(size = 40.dp)
                        Spacer(Modifier.height(10.dp))
                        Text(pet, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
                // Botón agregar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(104.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(BorderStroke(1.5.dp, VetColors.Border), RoundedCornerShape(16.dp))
                        .clickable { onAddPet() }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.height(8.dp))
                    Text("Add", color = VetColors.TextLabel, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            // Próxima cita
            SectionTitle("Next appointment", Modifier.padding(top = 6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VetColors.Surface)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VetColors.Accent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.CalendarMonth, contentDescription = null, tint = Color.White)
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Charlie — General checkup", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Mar 15 · 10:00 AM", color = VetColors.TextSecondary, fontSize = 13.sp)
                }
            }

            PrimaryButton(text = "Book new appointment", onClick = onBookAppointment)

            // Recordatorios
            SectionTitle("Reminders", Modifier.padding(top = 6.dp))
            sampleReminders.forEach { reminder ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VetColors.Surface)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Filled.Notifications, contentDescription = null, tint = VetColors.Warning, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(reminder.text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                    StatusTag(text = reminder.tag, positive = false)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}