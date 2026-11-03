package com.vetclinic.app.ui.screens.owner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

// Modelo temporal para la vista
private data class BookingDay(val weekday: String, val number: String, val full: String)

private val bookingReasons = listOf(
    "General checkup", "Vaccination", "Emergency", "Grooming", "Follow-up"
)

private val bookingTimes = listOf(
    "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
    "02:00 PM", "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM"
)

// Horarios ya ocupados (solo para mostrar el estado deshabilitado)
private val takenTimes = setOf("09:30 AM", "11:00 AM", "03:00 PM")

private fun nextBookingDays(count: Int = 7): List<BookingDay> {
    val weekdayFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
    val numberFormat = SimpleDateFormat("dd", Locale.ENGLISH)
    val fullFormat = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    return List(count) { index ->
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_YEAR, index + 1)
        val date = calendar.time
        BookingDay(weekdayFormat.format(date), numberFormat.format(date), fullFormat.format(date))
    }
}

@Composable
fun BookAppointmentScreen(
    pets: List<String> = listOf("Charlie", "Luna"),
    onBack: () -> Unit = {},
    onConfirm: (pet: String, reason: String, date: String, time: String) -> Unit = { _, _, _, _ -> }
) {
    val days = remember { nextBookingDays() }

    var selectedPet by remember { mutableStateOf(pets.firstOrNull()) }
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var notes by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val canBook = selectedPet != null && selectedReason != null && selectedDay != null && selectedTime != null

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Book appointment", onBack = onBack)

        // 1. Mascota
        SectionLabel("1. Pet")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            pets.forEach { pet ->
                val selected = pet == selectedPet
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VetColors.Surface)
                        .border(
                            BorderStroke(1.5.dp, if (selected) VetColors.Accent else Color.Transparent),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedPet = pet }
                        .padding(12.dp)
                ) {
                    PetAvatar(
                        size = 36.dp,
                        background = if (selected) VetColors.Accent else VetColors.Input,
                        tint = if (selected) Color.White else VetColors.Mint
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(pet, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 2. Motivo
        SectionLabel("2. Reason")
        bookingReasons.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { reason ->
                    SelectableChip(
                        text = reason,
                        selected = reason == selectedReason,
                        onClick = { selectedReason = reason },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }

        // 3. Fecha
        SectionLabel("3. Date")
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            days.forEachIndexed { index, day ->
                val selected = selectedDay == index
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
                }
            }
        }

        // 4. Hora
        SectionLabel("4. Time")
        bookingTimes.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { time ->
                    SelectableChip(
                        text = time,
                        selected = time == selectedTime,
                        enabled = time !in takenTimes,
                        onClick = { selectedTime = time },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Notas
        VetTextField(
            value = notes,
            onValueChange = { notes = it },
            label = "Notes (optional)",
            placeholder = "Tell the vet anything relevant",
            singleLine = false,
            minHeight = 100.dp
        )

        if (showError && !canBook) {
            Text(
                "Please choose a pet, reason, date and time.",
                color = VetColors.AccentSoft,
                fontSize = 13.sp
            )
        }

        PrimaryButton(
            text = "Confirm appointment",
            onClick = {
                if (canBook) showConfirmation = true else showError = true
            }
        )
    }

    if (showConfirmation && canBook) {
        val day = days[selectedDay!!]
        AlertDialog(
            onDismissRequest = {},
            containerColor = VetColors.Surface,
            titleContentColor = Color.White,
            textContentColor = VetColors.TextLabel,
            title = { Text("Appointment requested", fontWeight = FontWeight.ExtraBold) },
            text = {
                Text("${selectedPet} · ${selectedReason}\n${day.full} at ${selectedTime}")
            },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmation = false
                    onConfirm(selectedPet!!, selectedReason!!, day.full, selectedTime!!)
                }) {
                    Text("Done", color = VetColors.Mint, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun SelectableChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .alpha(if (enabled) 1f else 0.4f)
            .height(44.dp)
            .clip(shape)
            .background(if (selected) VetColors.Accent else VetColors.Surface)
            .border(
                BorderStroke(1.dp, if (selected) VetColors.Accent else VetColors.Border),
                shape
            )
            .then(if (enabled) Modifier.clickable { onClick() } else Modifier)
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
fun BookAppointmentScreenPreview() {
    BookAppointmentScreen()
}
