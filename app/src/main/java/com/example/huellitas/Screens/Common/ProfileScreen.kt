package com.vetclinic.app.ui.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
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

// Modelo temporal para las filas de información
private data class ProfileInfo(val icon: ImageVector, val label: String, val value: String)

@Composable
fun ProfileScreen(
    isVet: Boolean = false,
    userName: String = if (isVet) "Laura Gómez" else "Sarah Johnson",
    userEmail: String = if (isVet) "laura.gomez@clinic.com" else "sarah.johnson@email.com",
    onBack: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onNavigate: (Int) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val initials = userName
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    val info = if (isVet) {
        listOf(
            ProfileInfo(Icons.Filled.Phone, "Phone", "+57 300 555 0142"),
            ProfileInfo(Icons.Filled.MedicalServices, "Specialty", "General practice"),
            ProfileInfo(Icons.Filled.Badge, "License", "VET-20458")
        )
    } else {
        listOf(
            ProfileInfo(Icons.Filled.Phone, "Phone", "+57 300 123 4567"),
            ProfileInfo(Icons.Filled.LocationOn, "Address", "Calle 100 #15-20, Bogotá"),
            ProfileInfo(Icons.Filled.Pets, "Pets", "3 registered")
        )
    }

    Scaffold(
        containerColor = VetColors.Background,
        bottomBar = {
            VetBottomBar(
                items = if (isVet) vetNavItems else ownerNavItems,
                selectedIndex = 3,
                onSelect = onNavigate
            )
        }
    ) { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            ScreenHeader(title = "Profile", onBack = onBack)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                InitialsAvatar(initials = initials, size = 88.dp)
                Spacer(Modifier.height(14.dp))
                Text(userName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
                Text(userEmail, color = VetColors.TextSecondary, fontSize = 14.sp)
                Spacer(Modifier.height(10.dp))
                StatusTag(text = if (isVet) "Veterinarian" else "Pet owner", positive = true)
            }

            SectionLabel("Personal information")
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VetColors.Surface)
            ) {
                info.forEachIndexed { index, item ->
                    ProfileInfoRow(item)
                    if (index < info.lastIndex) HorizontalDivider(color = VetColors.Divider)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VetColors.Surface)
                    .clickable { onSettingsClick() }
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = null, tint = VetColors.TextLabel, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(12.dp))
                Text("Settings", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
            }

            Spacer(Modifier.height(8.dp))

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
}

@Composable
private fun ProfileInfoRow(item: ProfileInfo) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(item.icon, contentDescription = null, tint = VetColors.Mint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.label, color = VetColors.TextSecondary, fontSize = 12.sp)
            Text(item.value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}