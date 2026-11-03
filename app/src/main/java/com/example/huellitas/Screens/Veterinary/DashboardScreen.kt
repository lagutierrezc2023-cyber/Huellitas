package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private data class DashboardStat(val label: String, val value: String, val icon: ImageVector)

private data class DashboardAppointment(
    val time: String,
    val pet: String,
    val owner: String,
    val reason: String,
    val confirmed: Boolean
)

private val sampleStats = listOf(
    DashboardStat("Today's visits", "5", Icons.Filled.CalendarMonth),
    DashboardStat("Patients", "128", Icons.Filled.Pets),
    DashboardStat("Pending vaccines", "12", Icons.Filled.Vaccines),
    DashboardStat("Owners", "87", Icons.Filled.Groups)
)

private val sampleTodayAppointments = listOf(
    DashboardAppointment("09:00 AM", "Charlie", "Sarah Johnson", "General checkup", true),
    DashboardAppointment("10:30 AM", "Luna", "Sarah Johnson", "Vaccination", true),
    DashboardAppointment("11:30 AM", "Max", "Carlos Ruiz", "Skin allergy", false),
    DashboardAppointment("02:00 PM", "Bella", "Ana Torres", "Follow-up", true)
)

@Composable
fun DashboardScreen(
    vetName: String = "Dr. Laura Martínez",
    onNotificationsClick: () -> Unit = {},
    onAppointmentClick: () -> Unit = {},
    onRegisterPet: () -> Unit = {},
    onOwners: () -> Unit = {},
    onVaccinationRecord: () -> Unit = {},
    onNavigate: (Int) -> Unit = {}
) {
    val today = remember { SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH).format(Date()) }
    val initials = vetName
        .removePrefix("Dr. ")
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = { VetBottomBar(items = vetNavItems, selectedIndex = 0, onSelect = onNavigate) }
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
                    Text(today, color = VetColors.TextSecondary, fontSize = 14.sp)
                    Text(vetName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
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
                InitialsAvatar(initials = initials)
            }

            // Estadísticas
            sampleStats.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { stat -> StatCard(stat, Modifier.weight(1f)) }
                }
            }

            // Accesos rápidos
            SectionTitle("Quick actions", Modifier.padding(top = 6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                QuickAction("New pet", Icons.Filled.Add, onRegisterPet, Modifier.weight(1f))
                QuickAction("Owners", Icons.Filled.Groups, onOwners, Modifier.weight(1f))
                QuickAction("Vaccines", Icons.Filled.Vaccines, onVaccinationRecord, Modifier.weight(1f))
            }

            // Agenda del día
            SectionTitle("Today's schedule", Modifier.padding(top = 6.dp))
            sampleTodayAppointments.forEach { appointment ->
                ScheduleRow(appointment, onClick = onAppointmentClick)
            }
        }
    }
}

@Composable
private fun StatCard(stat: DashboardStat, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .padding(16.dp)
    ) {
        Icon(stat.icon, contentDescription = null, tint = VetColors.Mint, modifier = Modifier.size(22.dp))
        Text(stat.value, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Text(stat.label, color = VetColors.TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun QuickAction(label: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(VetColors.Accent)
        ) {
            Icon(icon, contentDescription = null, tint = Color.White)
        }
        Spacer(Modifier.height(8.dp))
        Text(label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ScheduleRow(item: DashboardAppointment, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(Modifier.width(74.dp)) {
            Text(item.time, color = VetColors.Mint, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("${item.pet} · ${item.reason}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(item.owner, color = VetColors.TextSecondary, fontSize = 12.sp)
        }
        Spacer(Modifier.width(8.dp))
        StatusTag(text = if (item.confirmed) "Confirmed" else "Pending", positive = item.confirmed)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}
