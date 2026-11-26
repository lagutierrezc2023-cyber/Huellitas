package com.vetclinic.app.ui.screens.veterinary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import com.vetclinic.app.ui.components.InitialsAvatar
import com.vetclinic.app.ui.components.ScreenHeader
import com.vetclinic.app.ui.components.VetTextField
import com.vetclinic.app.ui.theme.VetColors

// Modelo temporal para la vista (luego se reemplaza por la entidad de Room)
data class OwnerSummary(
    val name: String,
    val email: String,
    val phone: String,
    val petsCount: Int
)

private val sampleOwners = listOf(
    OwnerSummary("Sarah Johnson", "sarah.johnson@email.com", "+57 300 123 4567", 2),
    OwnerSummary("Carlos Ruiz", "carlos.ruiz@email.com", "+57 310 222 3344", 1),
    OwnerSummary("Ana Torres", "ana.torres@email.com", "+57 315 987 6543", 1),
    OwnerSummary("Diego Mora", "diego.mora@email.com", "+57 320 456 7890", 3),
    OwnerSummary("María López", "maria.lopez@email.com", "+57 301 654 3210", 2)
)

@Composable
fun OwnerListScreen(
    owners: List<OwnerSummary> = sampleOwners,
    onBack: () -> Unit = {},
    onOwnerClick: (OwnerSummary) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    val filtered = owners.filter {
        it.name.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
    ) {
        item {
            ScreenHeader(
                title = "Owners",
                onBack = onBack,
                trailing = { Text("${owners.size} registered", color = VetColors.TextSecondary, fontSize = 13.sp) }
            )
        }
        item {
            VetTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = "Search by name or email",
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) }
            )
        }
        items(filtered) { owner ->
            OwnerCard(owner = owner, onClick = { onOwnerClick(owner) })
        }
        if (filtered.isEmpty()) {
            item {
                Text(
                    "No owners found",
                    color = VetColors.TextSecondary,
                    modifier = Modifier.fillMaxWidth().padding(24.dp)
                )
            }
        }
    }
}

@Composable
private fun OwnerCard(owner: OwnerSummary, onClick: () -> Unit) {
    val initials = owner.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VetColors.Surface)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        InitialsAvatar(initials = initials, size = 46.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(owner.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                owner.email,
                color = VetColors.TextSecondary,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                if (owner.petsCount == 1) "1 pet" else "${owner.petsCount} pets",
                color = VetColors.Mint,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = VetColors.TextSecondary)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun OwnerListScreenPreview() {
    OwnerListScreen()
}