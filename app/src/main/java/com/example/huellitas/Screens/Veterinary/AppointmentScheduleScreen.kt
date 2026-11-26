package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Modelos temporales para la vista (luego se reemplazan por entidades de Room)
data class ScheduleItem(
    val time: String,
    val pet: String,
    val owner: String,
    val reason: String,
    val confirmed: Boolean
)

private data class ScheduleDay(val weekday: String, val number: String, val full: String)

// Citas de ejemplo por día
private val sampleSchedule: Map<Int, List<ScheduleItem>> = mapOf(
    0 to listOf(
        ScheduleItem("09:00 AM", "Charlie", "Sarah Johnson", "General checkup", true),
        ScheduleItem("10:30 AM", "Luna", "Sarah Johnson", "Vaccination", true),
        ScheduleItem("11:30 AM", "Max", "Carlos Ruiz", "Skin allergy", false),
        ScheduleItem("02:00 PM", "Bella", "Ana Torres", "Follow-up", true)
    ),
    1 to listOf(
        ScheduleItem("09:30 AM", "Kiwi", "Diego Mora", "General checkup", true),
        ScheduleItem("03:00 PM", "Rocky", "Sarah Johnson", "Grooming", false)
    ),
    3 to listOf(
        ScheduleItem("10:00 AM", "Max", "Carlos Ruiz", "Follow-up", true),
        ScheduleItem("11:00 AM", "Bella", "Ana Torres", "Vaccination", true),
        ScheduleItem("04:00 PM", "Charlie", "Sarah Johnson", "Emergency", false)
    ),
    4 to listOf(
        ScheduleItem("09:00 AM", "Luna", "Sarah Johnson", "General checkup", true)
    )
)

private fun nextScheduleDays(count: Int = 7): List<ScheduleDay> {
    val weekdayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
    val numberFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    val fullFormat = SimpleDateFormat("EEEE, MMM dd", Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    return List(count) { index ->
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_YEAR, index)
        val date = calendar.time
        ScheduleDay(weekdayFormat.format(date), numberFormat.format(date), fullFormat.format(date))
    }
}

@Composable
fun AppointmentScheduleScreen(
    schedule: Map<Int, List<ScheduleItem>> = sampleSchedule,
    onAppointmentClick: (ScheduleItem) -> Unit = {},
    onNavigate: (Int) -> Unit = {}
) {
    val days = remember { nextScheduleDays() }
    var selectedDay by remember { mutableIntStateOf(0) }
    val dayItems = schedule[selectedDay].orEmpty()

    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = { VetBottomBar(items = vetNavItems, selectedIndex = 2, onSelect = onNavigate) }
    ) { padding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column {
                    Text("Schedule", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                    Text(days[selectedDay].full, color = VetColors.TextSecondary, fontSize = 13.sp)
                }
            }

            // Selector de día
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    days.forEachIndexed { index, day ->
                        val selected = index == selectedDay
                        val hasVisits = !schedule[index].isNullOrEmpty()
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(62.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (selected) VetColors.Accent else VetColors.Surface)
                                .clickable { selectedDay = index }
                                .padding(vertical = 12.dp)
                        ) {
                            Text(
                                day.weekday,
                                color = if (selected) Color.White else VetColors.TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(day.number, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                            Spacer(Modifier.height(6.dp))
                            // Puntico: indica que ese día tiene citas
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        when {
                                            !hasVisits -> Color.Transparent
                                            selected -> Color.White
                                            else -> VetColors.Mint
                                        }
                                    )
                            )
                        }
                    }
                }
            }

            item {
                SectionLabel("${dayItems.size} ${if (dayItems.size == 1) "visit" else "visits"}")
            }

            items(dayItems) { visit ->
                ScheduleCard(item = visit, onClick = { onAppointmentClick(visit) })
            }

            if (dayItems.isEmpty()) {
                item {
                    Text(
                        "No appointments for this day",
                        color = VetColors.TextSecondary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleCard(item: ScheduleItem, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.width(70.dp)) {
            Text(item.time.substringBefore(" "), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Text(item.time.substringAfter(" "), color = VetColors.TextSecondary, fontSize = 11.sp)
        }
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(40.dp)
                .background(if (item.confirmed) VetColors.Mint else VetColors.Warning)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(item.pet, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text("${item.reason} · ${item.owner}", color = VetColors.TextSecondary, fontSize = 12.sp, maxLines = 1)
        }
        Spacer(Modifier.width(8.dp))
        StatusTag(text = if (item.confirmed) "Confirmed" else "Pending", positive = item.confirmed)
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun AppointmentScheduleScreenPreview() {
    AppointmentScheduleScreen()
}