package com.vetclinic.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.PrimaryButton
import com.vetclinic.app.ui.components.ScreenHeader
import com.vetclinic.app.ui.components.VetTextField
import com.vetclinic.app.ui.theme.VetColors

private val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit = {},
    onSendLink: (email: String) -> Unit = {},
    onBackToLogin: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var sent by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Forgot password", onBack = onBack)

        Spacer(Modifier.height(36.dp))

        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(if (sent) VetColors.Mint else VetColors.Accent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (sent) Icons.Filled.CheckCircle else Icons.Filled.Email,
                contentDescription = null,
                tint = if (sent) VetColors.Background else Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(Modifier.height(24.dp))
        Text(
            text = if (sent) "Check your email" else "Reset your password",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (sent) {
                "We sent a reset link to\n${email.trim()}"
            } else {
                "Enter the email linked to your account and we'll send you a link to create a new password"
            },
            color = VetColors.TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VetColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            if (!sent) {
                VetTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = "Email",
                    placeholder = "name@clinic.com",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                errorMessage?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(it, color = VetColors.AccentSoft, fontSize = 13.sp)
                }

                Spacer(Modifier.height(24.dp))
                PrimaryButton(
                    text = "Send reset link",
                    onClick = {
                        if (emailRegex.matches(email.trim())) {
                            sent = true
                            onSendLink(email.trim())
                        } else {
                            errorMessage = "Please enter a valid email address."
                        }
                    }
                )
            } else {
                Text(
                    "Didn't get it? Check your spam folder or try again with a different email.",
                    color = VetColors.TextLabel,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(24.dp))
                PrimaryButton(text = "Back to sign in", onClick = onBackToLogin)
                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Use a different email",
                    color = VetColors.Mint,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { sent = false }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}