package com.vetclinic.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vetclinic.app.ui.components.PrimaryButton
import com.vetclinic.app.ui.components.ScreenHeader
import com.vetclinic.app.ui.components.VetTextField
import com.vetclinic.app.ui.theme.VetColors

@Composable
fun RegisterOwnerScreen(
    onBack: () -> Unit = {},
    onRegister: (name: String, email: String, phone: String, password: String) -> Unit = { _, _, _, _ -> },
    onSignIn: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val passwordTransformation =
        if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VetColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ScreenHeader(title = "Create account", onBack = onBack)

        Spacer(Modifier.height(8.dp))
        Text(
            "Register as a pet owner to manage your pets and appointments",
            color = VetColors.TextSecondary,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(VetColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            VetTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full name",
                placeholder = "Sarah Johnson"
            )
            Spacer(Modifier.height(16.dp))
            VetTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "name@email.com",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(16.dp))
            VetTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                placeholder = "+57 300 123 4567",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(Modifier.height(16.dp))
            VetTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "••••••••",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = passwordTransformation,
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
            VetTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm password",
                placeholder = "••••••••",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = passwordTransformation
            )

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { acceptTerms = !acceptTerms }
            ) {
                Checkbox(
                    checked = acceptTerms,
                    onCheckedChange = { acceptTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = VetColors.Accent,
                        uncheckedColor = VetColors.Mint,
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    "I accept the terms and conditions",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            errorMessage?.let { message ->
                Spacer(Modifier.height(8.dp))
                Text(message, color = VetColors.AccentSoft, fontSize = 13.sp)
            }

            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = "Create account",
                onClick = {
                    errorMessage = when {
                        fullName.isBlank() || email.isBlank() || password.isBlank() ->
                            "Please fill in all required fields"
                        password != confirmPassword -> "Passwords do not match"
                        !acceptTerms -> "You must accept the terms to continue"
                        else -> null
                    }
                    if (errorMessage == null) {
                        onRegister(fullName.trim(), email.trim(), phone.trim(), password)
                    }
                }
            )
        }

        Spacer(Modifier.height(28.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Already have an account? ", color = VetColors.TextLabel, fontSize = 14.sp)
            Text(
                text = "Sign in",
                color = VetColors.AccentSoft,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onSignIn() }
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun RegisterOwnerScreenPreview() {
    RegisterOwnerScreen()
}