package com.vetclinic.app.navigation

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vetclinic.app.ui.components.ScreenHeader
import com.vetclinic.app.ui.screens.auth.LoginScreen
import com.vetclinic.app.ui.screens.common.SettingsScreen
import com.vetclinic.app.ui.screens.owner.HomeScreen
import com.vetclinic.app.ui.screens.owner.MyPetsScreen
import com.vetclinic.app.ui.screens.veterinary.NewConsultationScreen
import com.vetclinic.app.ui.screens.veterinary.PetListScreen
import com.vetclinic.app.ui.theme.VetColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*  Rutas de las 24 pantallas  */

object Routes {
    // Auth
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val REGISTER_OWNER = "register_owner"

    // Common
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val MEDICAL_HISTORY = "medical_history"
    const val PROFILE = "profile"

    // Owner
    const val HOME = "home"
    const val MY_PETS = "my_pets"
    const val BOOK_APPOINTMENT = "book_appointment"
    const val VACCINATION_HISTORY = "vaccination_history"
    const val ADD_PET = "add_pet"
    const val MY_APPOINTMENTS = "my_appointments"
    const val PET_PROFILE = "pet_profile/{petName}"

    // Veterinary
    const val PET_LIST = "pet_list"
    const val NEW_CONSULTATION = "new_consultation/{petName}/{petBreed}/{ownerName}"
    const val DASHBOARD = "dashboard"
    const val OWNER_DETAIL = "owner_detail"
    const val APPOINTMENT_SCHEDULE = "appointment_schedule"
    const val PET_FORM = "pet_form"
    const val APPOINTMENT_DETAIL = "appointment_detail"
    const val OWNER_LIST = "owner_list"
    const val VACCINATION_RECORD = "vaccination_record"

    //Funciones para armar rutas que llevan datos
    fun petProfile(petName: String) = "pet_profile/${Uri.encode(petName)}"
    fun newConsultation(petName: String, petBreed: String, ownerName: String) =
        "new_consultation/${Uri.encode(petName)}/${Uri.encode(petBreed)}/${Uri.encode(ownerName)}"
}

private const val OWNER_START = Routes.HOME
private const val VET_START = Routes.PET_LIST

/*  Grafo de navegación */

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        /* ===== AUTH ===== */

        composable(Routes.LOGIN) {
            LoginScreen(
                onLogin = { email, _ ->
                    // DEMO: si el correo termina en @clinic.com entra como veterinario,
                    // si no, entra como dueño. Luego se reemplaza por validación real (Room / Firebase).
                    val destination =
                        if (email.trim().endsWith("@clinic.com", ignoreCase = true)) VET_START else OWNER_START
                    navController.navigate(destination) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
                onRegister = { navController.navigate(Routes.REGISTER_OWNER) }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            PlaceholderScreen("Forgot password", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.REGISTER_OWNER) {
            PlaceholderScreen("Register", "Ashley") { navController.popBackStack() }
        }

        /* ===== COMMON ===== */

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NOTIFICATIONS) {
            PlaceholderScreen("Notifications", "Gabi") { navController.popBackStack() }
        }

        composable(Routes.PROFILE) {
            PlaceholderScreen("Profile", "Ashley") { navController.popBackStack() }
        }

        /* ===== OWNER ===== */

        composable(Routes.HOME) {
            HomeScreen(
                onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) },
                onPetClick = { petName -> navController.navigate(Routes.petProfile(petName)) },
                onAddPet = { navController.navigate(Routes.ADD_PET) },
                onBookAppointment = { navController.navigate(Routes.BOOK_APPOINTMENT) },
                onNavigate = { index -> navController.navigateTab(ownerTabRoute(index), OWNER_START) }
            )
        }

        composable(Routes.MY_PETS) {
            MyPetsScreen(
                onBack = { navController.popBackStack() },
                onPetClick = { pet -> navController.navigate(Routes.petProfile(pet.name)) },
                onAddPet = { navController.navigate(Routes.ADD_PET) },
                onNavigate = { index -> navController.navigateTab(ownerTabRoute(index), OWNER_START) }
            )
        }

        composable(
            route = Routes.PET_PROFILE,
            arguments = listOf(navArgument("petName") { type = NavType.StringType })
        ) { entry ->
            val petName = entry.arguments?.getString("petName").orEmpty()
            PlaceholderScreen("Pet profile — $petName", "Ashley") { navController.popBackStack() }
        }

        composable(Routes.ADD_PET) {
            PlaceholderScreen("Add pet", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.BOOK_APPOINTMENT) {
            PlaceholderScreen("Book appointment", "Gabi") { navController.popBackStack() }
        }

        composable(Routes.MY_APPOINTMENTS) {
            PlaceholderScreen("My appointments", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.PET_LIST) {
            PetListScreen(
                onPatientClick = { patient ->
                    navController.navigate(
                        Routes.newConsultation(patient.name, patient.breed, patient.owner)
                    )
                },
                onAddPet = { navController.navigate(Routes.PET_FORM) },
                onNavigate = { index -> navController.navigateTab(vetTabRoute(index), VET_START) }
            )
        }

        composable(
            route = Routes.NEW_CONSULTATION,
            arguments = listOf(
                navArgument("petName") { type = NavType.StringType },
                navArgument("petBreed") { type = NavType.StringType },
                navArgument("ownerName") { type = NavType.StringType }
            )
        ) { entry ->
            val args = entry.arguments
            NewConsultationScreen(
                petName = args?.getString("petName").orEmpty(),
                petBreed = args?.getString("petBreed").orEmpty(),
                ownerName = args?.getString("ownerName").orEmpty(),
                // Fecha de hoy en formato "Nov 02"
                date = SimpleDateFormat("MMM dd", Locale.ENGLISH).format(Date()),
                onBack = { navController.popBackStack() },
                onSave = { _ ->
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.DASHBOARD) {
            PlaceholderScreen("Dashboard", "Gabi") { navController.popBackStack() }
        }

        composable(Routes.APPOINTMENT_SCHEDULE) {
            PlaceholderScreen("Appointment schedule", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.PET_FORM) {
            PlaceholderScreen("New pet", "Jennifer") { navController.popBackStack() }
        }
    }
}

    /*  Pestañas de la barra inferior  */

    private fun ownerTabRoute(index: Int) = when (index) {
        0 -> Routes.HOME
        1 -> Routes.MY_PETS
        2 -> Routes.MY_APPOINTMENTS
        else -> Routes.SETTINGS // cambiar a Routes.PROFILE cuando Ashley tenga su pantalla
    }


    private fun vetTabRoute(index: Int) = when (index) {
        0 -> Routes.DASHBOARD
        1 -> Routes.PET_LIST
        2 -> Routes.APPOINTMENT_SCHEDULE
        else -> Routes.SETTINGS // cambiar a Routes.PROFILE cuando Ashley tenga su pantalla
    }

    // Cambia de pestaña sin ir acumulando pantallas en el historial
    private fun NavHostController.navigateTab(route: String, startRoute: String) {
        if (currentDestination?.route == route) return
        navigate(route) {
            popUpTo(startRoute) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    /*  Pantalla temporal para las vistas de los compañeros  */

    @Composable
    private fun PlaceholderScreen(title: String, assignedTo: String, onBack: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(VetColors.Background)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            ScreenHeader(title = title, onBack = onBack)
            Spacer(Modifier.weight(1f))
            Text(
                text = "Coming soon — assigned to $assignedTo",
                color = VetColors.TextSecondary,
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.weight(1f))
        }
    }