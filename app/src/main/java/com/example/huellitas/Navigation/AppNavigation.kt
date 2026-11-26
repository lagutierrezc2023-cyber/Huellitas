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
import com.vetclinic.app.ui.screens.auth.RegisterOwnerScreen
import com.vetclinic.app.ui.screens.auth.SplashScreen
import com.vetclinic.app.ui.screens.common.NotificationsScreen
import com.vetclinic.app.ui.screens.common.ProfileScreen
import com.vetclinic.app.ui.screens.common.SettingsScreen
import com.vetclinic.app.ui.screens.owner.BookAppointmentScreen
import com.vetclinic.app.ui.screens.owner.HomeScreen
import com.vetclinic.app.ui.screens.owner.MyPetsScreen
import com.vetclinic.app.ui.screens.owner.PetProfileScreen
import com.vetclinic.app.ui.screens.owner.VaccinationHistoryScreen
import com.vetclinic.app.ui.screens.veterinary.AppointmentDetailScreen
import com.vetclinic.app.ui.screens.veterinary.DashboardScreen
import com.vetclinic.app.ui.screens.veterinary.NewConsultationScreen
import com.vetclinic.app.ui.screens.veterinary.OwnerDetailScreen
import com.vetclinic.app.ui.screens.veterinary.OwnerListScreen
import com.vetclinic.app.ui.screens.veterinary.PetListScreen
import com.vetclinic.app.ui.screens.veterinary.VaccinationRecordScreen
import com.vetclinic.app.ui.theme.VetColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Routes {
    // Auth
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val FORGOT_PASSWORD = "forgot_password"
    const val REGISTER_OWNER = "register_owner"
    const val SETTINGS = "settings"
    const val NOTIFICATIONS = "notifications"
    const val MEDICAL_HISTORY = "medical_history"
    const val PROFILE = "profile?isVet={isVet}"
    const val HOME = "home"
    const val MY_PETS = "my_pets"
    const val BOOK_APPOINTMENT = "book_appointment"
    const val VACCINATION_HISTORY = "vaccination_history?petName={petName}"
    const val ADD_PET = "add_pet"
    const val MY_APPOINTMENTS = "my_appointments"
    const val PET_PROFILE = "pet_profile/{petName}"
    const val PET_LIST = "pet_list"
    const val NEW_CONSULTATION = "new_consultation/{petName}/{petBreed}/{ownerName}"
    const val DASHBOARD = "dashboard"
    const val OWNER_DETAIL = "owner_detail?ownerName={ownerName}"
    const val APPOINTMENT_SCHEDULE = "appointment_schedule"
    const val PET_FORM = "pet_form"
    const val APPOINTMENT_DETAIL = "appointment_detail"
    const val OWNER_LIST = "owner_list"
    const val VACCINATION_RECORD = "vaccination_record"

    fun petProfile(petName: String) = "pet_profile/${Uri.encode(petName)}"
    fun newConsultation(petName: String, petBreed: String, ownerName: String) =
        "new_consultation/${Uri.encode(petName)}/${Uri.encode(petBreed)}/${Uri.encode(ownerName)}"
    fun profile(isVet: Boolean) = "profile?isVet=$isVet"
    fun vaccinationHistory(petName: String? = null) =
        if (petName == null) "vaccination_history" else "vaccination_history?petName=${Uri.encode(petName)}"
    fun ownerDetail(ownerName: String? = null) =
        if (ownerName == null) "owner_detail" else "owner_detail?ownerName=${Uri.encode(ownerName)}"
}

private const val OWNER_START = Routes.HOME
private const val VET_START = Routes.DASHBOARD


@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLogin = { email, _ ->

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
            RegisterOwnerScreen(
                onBack = { navController.popBackStack() },
                onRegister = { _, _, _, _ ->

                    navController.navigate(OWNER_START) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onSignIn = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onProfileClick = { navController.navigate(Routes.profile(isVet = false)) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.PROFILE,
            arguments = listOf(navArgument("isVet") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { entry ->
            val isVet = entry.arguments?.getBoolean("isVet") ?: false
            ProfileScreen(
                isVet = isVet,
                onBack = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                onNavigate = { index ->
                    if (isVet) navController.navigateTab(vetTabRoute(index), VET_START)
                    else navController.navigateTab(ownerTabRoute(index), OWNER_START)
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MEDICAL_HISTORY) {
            PlaceholderScreen("Medical history", "the team") { navController.popBackStack() }
        }

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
            PetProfileScreen(
                petName = entry.arguments?.getString("petName").orEmpty(),
                onBack = { navController.popBackStack() },
                onVaccinationHistory = {
                    navController.navigate(Routes.vaccinationHistory(entry.arguments?.getString("petName")))
                },
                onMedicalHistory = { navController.navigate(Routes.MEDICAL_HISTORY) },
                onBookAppointment = { navController.navigate(Routes.BOOK_APPOINTMENT) }
            )
        }

        composable(
            route = Routes.VACCINATION_HISTORY,
            arguments = listOf(navArgument("petName") {
                type = NavType.StringType
                defaultValue = "Charlie"
            })
        ) { entry ->
            VaccinationHistoryScreen(
                petName = entry.arguments?.getString("petName") ?: "Charlie",
                onBack = { navController.popBackStack() },
                onBookAppointment = { navController.navigate(Routes.BOOK_APPOINTMENT) }
            )
        }

        composable(Routes.ADD_PET) {
            PlaceholderScreen("Add pet", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.BOOK_APPOINTMENT) {
            BookAppointmentScreen(
                onBack = { navController.popBackStack() },
                onConfirm = { _, _, _, _ -> navController.popBackStack() }
            )
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
            DashboardScreen(
                onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) },
                onAppointmentClick = { navController.navigate(Routes.APPOINTMENT_DETAIL) },
                onRegisterPet = { navController.navigate(Routes.PET_FORM) },
                onOwners = { navController.navigate(Routes.OWNER_LIST) },
                onVaccinationRecord = { navController.navigate(Routes.VACCINATION_RECORD) },
                onNavigate = { index -> navController.navigateTab(vetTabRoute(index), VET_START) }
            )
        }

        composable(Routes.APPOINTMENT_SCHEDULE) {
            PlaceholderScreen("Appointment schedule", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.PET_FORM) {
            PlaceholderScreen("New pet", "Jennifer") { navController.popBackStack() }
        }

        composable(Routes.APPOINTMENT_DETAIL) {
            AppointmentDetailScreen(
                onBack = { navController.popBackStack() },
                onViewOwner = { navController.navigate(Routes.ownerDetail()) },
                onStartConsultation = { petName, petBreed, ownerName ->
                    navController.navigate(Routes.newConsultation(petName, petBreed, ownerName))
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(Routes.OWNER_LIST) {
            OwnerListScreen(
                onBack = { navController.popBackStack() },
                onOwnerClick = { owner -> navController.navigate(Routes.ownerDetail(owner.name)) }
            )
        }

        composable(
            route = Routes.OWNER_DETAIL,
            arguments = listOf(navArgument("ownerName") {
                type = NavType.StringType
                defaultValue = "Sarah Johnson"
            })
        ) { entry ->
            val ownerName = entry.arguments?.getString("ownerName") ?: "Sarah Johnson"
            OwnerDetailScreen(
                ownerName = ownerName,
                onBack = { navController.popBackStack() },
                onPetClick = { petName, petBreed ->
                    navController.navigate(Routes.newConsultation(petName, petBreed, ownerName))
                }
            )
        }

        composable(Routes.VACCINATION_RECORD) {
            VaccinationRecordScreen(
                onBack = { navController.popBackStack() },
                onSave = { _ -> navController.popBackStack() }
            )
        }
    }
}


private fun ownerTabRoute(index: Int) = when (index) {
    0 -> Routes.HOME
    1 -> Routes.MY_PETS
    2 -> Routes.MY_APPOINTMENTS
    else -> Routes.profile(isVet = false)
}

private fun vetTabRoute(index: Int) = when (index) {
    0 -> Routes.DASHBOARD
    1 -> Routes.PET_LIST
    2 -> Routes.APPOINTMENT_SCHEDULE
    else -> Routes.profile(isVet = true)
}

private fun NavHostController.navigateTab(route: String, startRoute: String) {
    if (currentDestination?.route == route) return
    navigate(route) {
        popUpTo(startRoute) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/*  Pantalla temporal */

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