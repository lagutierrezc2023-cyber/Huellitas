package com.vetclinic.app.ui.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.InitialsAvatar
import com.vetclinic.app.ui.components.ScreenHeader
import com.vetclinic.app.ui.components.SectionLabel
import com.vetclinic.app.ui.theme.VetColors

@Composable
fun SettingsScreen(
    userName: String = "Sarah Johnson",
    userEmail: String = "sarah.johnson@email.com",
    onBack: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var vaccineReminders by remember { mutableStateOf(true) }
    var appointmentReminders by remember { mutableStateOf(true) }
    var treatmentAlerts by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Settings", onBack = onBack)

        // Tarjeta de perfil
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
                .clickable { onProfileClick() }
                .padding(16.dp)
        ) {
            InitialsAvatar(initials = "SJ", size = 48.dp)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(userName, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(userEmail, color = VetColors.TextSecondary, fontSize = 12.sp)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
        }

        Spacer(Modifier.height(4.dp))
        SectionLabel("Notifications")
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
        ) {
            ToggleRow("Vaccine reminders", "Notice before they expire", vaccineReminders) { vaccineReminders = it }
            HorizontalDivider(color = VetColors.Divider)
            ToggleRow("Appointment reminders", "One day before your appointment", appointmentReminders) { appointmentReminders = it }
            HorizontalDivider(color = VetColors.Divider)
            ToggleRow("Treatment alerts", "Pending doses and check-ups", treatmentAlerts) { treatmentAlerts = it }
        }

        Spacer(Modifier.height(4.dp))
        SectionLabel("Account")
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VetColors.Surface)
        ) {
            NavRow(Icons.Filled.Lock, "Change password", onClick = onChangePassword)
            HorizontalDivider(color = VetColors.Divider)
            NavRow(Icons.Filled.Language, "Language", value = "English", onClick = onLanguageClick)
        }

        Spacer(Modifier.weight(1f))

        OutlinedButton(
            onClick = onLogout,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, VetColors.AccentSoft),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = VetColors.AccentSoft),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
            Spacer(Modifier.width(10.dp))
            Text("Log out", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ToggleRow(title: String, subtitle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = VetColors.TextSecondary, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = VetColors.Accent,
                checkedBorderColor = VetColors.Accent,
                uncheckedThumbColor = VetColors.Mint,
                uncheckedTrackColor = VetColors.Background,
                uncheckedBorderColor = VetColors.Border
            )
        )
    }
}

@Composable
private fun NavRow(icon: ImageVector, title: String, value: String? = null, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = VetColors.TextLabel, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, color = VetColors.TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}