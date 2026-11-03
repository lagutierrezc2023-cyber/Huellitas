package com.vetclinic.app.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Vaccines
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
import com.vetclinic.app.ui.components.ScreenHeader
import com.vetclinic.app.ui.components.SectionLabel
import com.vetclinic.app.ui.theme.VetColors

// Modelo temporal para la vista (luego se reemplaza por la entidad de Room)
private enum class NotificationKind { APPOINTMENT, VACCINE, REMINDER, GENERAL }

private data class NotificationItem(
    val id: Int,
    val kind: NotificationKind,
    val title: String,
    val message: String,
    val time: String,
    val isToday: Boolean,
    val read: Boolean = false
)

private val sampleNotifications = listOf(
    NotificationItem(1, NotificationKind.APPOINTMENT, "Appointment confirmed", "Charlie — General checkup on Mar 15 at 10:00 AM.", "10 min ago", true),
    NotificationItem(2, NotificationKind.VACCINE, "Vaccine due soon", "Luna's rabies vaccine is due in 5 days.", "2 h ago", true),
    NotificationItem(3, NotificationKind.REMINDER, "Appointment reminder", "Tomorrow at 10:00 AM you have a visit for Charlie.", "5 h ago", true, read = true),
    NotificationItem(4, NotificationKind.VACCINE, "Parvovirus booster pending", "Charlie needs his parvovirus booster.", "Yesterday", false),
    NotificationItem(5, NotificationKind.GENERAL, "Welcome to Huellitas", "Add your pets to start tracking their health.", "2 days ago", false, read = true),
    NotificationItem(6, NotificationKind.APPOINTMENT, "Appointment completed", "Luna's checkup was registered by Dr. Martínez.", "3 days ago", false, read = true)
)

@Composable
fun NotificationsScreen(
    onBack: () -> Unit = {}
) {
    val notifications = remember { mutableStateListOf<NotificationItem>().apply { addAll(sampleNotifications) } }
    val unread = notifications.count { !it.read }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
    ) {
        item {
            ScreenHeader(
                title = "Notifications",
                onBack = onBack,
                trailing = {
                    if (unread > 0) {
                        TextButton(onClick = {
                            for (i in notifications.indices) notifications[i] = notifications[i].copy(read = true)
                        }) {
                            Text("Mark all read", color = VetColors.Mint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            )
        }

        item {
            Text(
                text = if (unread == 0) "You're all caught up" else "$unread unread",
                color = VetColors.TextSecondary,
                fontSize = 13.sp
            )
        }

        if (notifications.isEmpty()) {
            item {
                Text(
                    "No notifications yet",
                    color = VetColors.TextSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }
        }

        listOf(true, false).forEach { today ->
            val group = notifications.filter { it.isToday == today }
            if (group.isNotEmpty()) {
                item(key = "label_$today") {
                    Box(Modifier.padding(top = 6.dp)) { SectionLabel(if (today) "Today" else "Earlier") }
                }
                items(group, key = { it.id }) { notification ->
                    NotificationCard(notification) {
                        val index = notifications.indexOfFirst { it.id == notification.id }
                        if (index >= 0) notifications[index] = notifications[index].copy(read = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(item: NotificationItem, onClick: () -> Unit) {
    val (icon, iconBg, iconTint) = when (item.kind) {
        NotificationKind.APPOINTMENT -> Triple(Icons.Filled.CalendarMonth, VetColors.Accent, Color.White)
        NotificationKind.VACCINE -> Triple(Icons.Filled.Vaccines, VetColors.Input, VetColors.Mint)
        NotificationKind.REMINDER -> Triple(Icons.Filled.Notifications, VetColors.Input, VetColors.Warning)
        NotificationKind.GENERAL -> Triple(Icons.Filled.Info, VetColors.Input, VetColors.TextLabel)
    }
    val shape = RoundedCornerShape(16.dp)

    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (item.read) VetColors.Background else VetColors.Surface)
            .then(if (item.read) Modifier.border(1.dp, VetColors.Divider, shape) else Modifier)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        NotificationIcon(icon, iconBg, iconTint)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    item.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = if (item.read) FontWeight.SemiBold else FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (!item.read) {
                    Box(
                        Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(VetColors.AccentSoft)
                    )
                }
            }
            Text(item.message, color = VetColors.TextSecondary, fontSize = 13.sp, lineHeight = 18.sp)
            Text(item.time, color = VetColors.TextLabel, fontSize = 11.sp)
        }
    }
}

@Composable
private fun NotificationIcon(icon: ImageVector, background: Color, tint: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
