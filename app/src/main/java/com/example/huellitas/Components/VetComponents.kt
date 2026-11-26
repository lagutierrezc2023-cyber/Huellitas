package com.vetclinic.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.theme.VetColors

/*  Encabezado con botón de volver */

@Composable
fun BackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(VetColors.Surface)
    ) {
        Icon(Icons.Filled.ChevronLeft, contentDescription = "Back", tint = Color.White)
    }
}

@Composable
fun ScreenHeader(
    title: String,
    onBack: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (onBack != null) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(14.dp))
        }
        Text(
            text = title,
            color = VetColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.weight(1f)
        )
        trailing?.invoke()
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = VetColors.TextPrimary,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = VetColors.TextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )
}

/*  Avatares */

@Composable
fun PetAvatar(
    size: Dp = 48.dp,
    background: Color = VetColors.Input,
    tint: Color = VetColors.Mint
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.Pets, contentDescription = null, tint = tint, modifier = Modifier.size(size / 2))
    }
}

@Composable
fun InitialsAvatar(initials: String, size: Dp = 44.dp, background: Color = VetColors.Accent) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(initials, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
    }
}

/*  Campos de texto  */

@Composable
fun vetTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = VetColors.Input,
    unfocusedContainerColor = VetColors.Input,
    focusedBorderColor = VetColors.Mint,
    unfocusedBorderColor = VetColors.Border,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = VetColors.Mint,
    focusedPlaceholderColor = VetColors.TextSecondary,
    unfocusedPlaceholderColor = VetColors.TextSecondary,
    focusedTrailingIconColor = VetColors.TextSecondary,
    unfocusedTrailingIconColor = VetColors.TextSecondary,
    focusedLeadingIconColor = VetColors.TextSecondary,
    unfocusedLeadingIconColor = VetColors.TextSecondary
)

@Composable
fun VetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minHeight: Dp = 52.dp,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        if (label != null) {
            Text(label, color = VetColors.TextLabel, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            singleLine = singleLine,
            shape = RoundedCornerShape(12.dp),
            colors = vetTextFieldColors(),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
        )
    }
}

/*  Botones  */

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = VetColors.Accent, contentColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

/*  Etiqueta de estado  */

@Composable
fun StatusTag(text: String, positive: Boolean) {
    Text(
        text = text,
        color = if (positive) VetColors.Background else VetColors.TagText,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (positive) VetColors.Mint else VetColors.TagBackground)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

/*  Barra de navegación inferior */

data class BottomNavItem(val label: String, val icon: ImageVector)

val ownerNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home),
    BottomNavItem("Pets", Icons.Filled.Pets),
    BottomNavItem("Appointments", Icons.Filled.CalendarMonth),
    BottomNavItem("Profile", Icons.Filled.Person)
)

val vetNavItems = listOf(
    BottomNavItem("Dashboard", Icons.Filled.Dashboard),
    BottomNavItem("Patients", Icons.Filled.Pets),
    BottomNavItem("Schedule", Icons.Filled.CalendarMonth),
    BottomNavItem("Profile", Icons.Filled.Person)
)

@Composable
fun VetBottomBar(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column(Modifier.background(VetColors.NavBar)) {
        HorizontalDivider(color = VetColors.Divider, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 10.dp, bottom = 14.dp)
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onSelect(index) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 52.dp, height = 30.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(if (selected) VetColors.Accent else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(item.icon, contentDescription = item.label, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(item.label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}