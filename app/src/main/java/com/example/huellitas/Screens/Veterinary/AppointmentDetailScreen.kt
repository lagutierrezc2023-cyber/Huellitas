package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
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

@Composable
fun AppointmentDetailScreen(
    petName: String = "Charlie",
    petBreed: String = "Golden Retriever",
    ownerName: String = "Sarah Johnson",
    ownerPhone: String = "+57 300 123 4567",
    date: String = "Nov 05, 2026",
    time: String = "10:00 AM",
    reason: String = "General checkup",
    status: String = "Confirmed",
    notes: String = "Owner reports mild itching and reduced appetite for the last two days.",
    onBack: () -> Unit = {},
    onViewOwner: () -> Unit = {},
    onStartConsultation: (petName: String, petBreed: String, ownerName: String) -> Unit = { _, _, _ -> },
    onCancel: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(
            title = "Appointment",
            onBack = onBack,
            trailing = { StatusTag(text = status, positive = status == "Confirmed") }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
                .padding(14.dp)
        ) {
            PetAvatar(size = 52.dp, background = VetColors.Accent, tint = Color.White)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(petName, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                Text(petBreed, color = VetColors.TextSecondary, fontSize = 13.sp)
            }
        }

        SectionLabel("Details")
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
        ) {
            AppointmentInfoRow(Icons.Filled.CalendarMonth, "Date", date)
            HorizontalDivider(color = VetColors.Divider)
            AppointmentInfoRow(Icons.Filled.AccessTime, "Time", time)
            HorizontalDivider(color = VetColors.Divider)
            AppointmentInfoRow(Icons.Filled.MedicalServices, "Reason", reason)
        }

        SectionLabel("Owner")
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
        ) {
            AppointmentInfoRow(Icons.Filled.Person, "Name", ownerName, onClick = onViewOwner)
            HorizontalDivider(color = VetColors.Divider)
            AppointmentInfoRow(Icons.Filled.Phone, "Phone", ownerPhone)
        }

        SectionLabel("Notes")
        Text(
            text = notes,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
                .padding(16.dp)
        )

        Spacer(Modifier.height(4.dp))

        PrimaryButton(
            text = "Start consultation",
            onClick = { onStartConsultation(petName, petBreed, ownerName) }
        )

        OutlinedButton(
            onClick = onCancel,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, VetColors.AccentSoft),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VetColors.AccentSoft),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Cancel appointment", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun AppointmentInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = VetColors.Mint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(label, color = VetColors.TextSecondary, fontSize = 12.sp)
            Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
        if (onClick != null) {
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun AppointmentDetailScreenPreview() {
    AppointmentDetailScreen()
}