package com.example.interfaces

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun navegacion() {
    val navController = rememberNavController()
    val contactViewModel: ContactViewModel = viewModel()

    NavHost(navController = navController, startDestination = "screen_a") {
        composable("screen_a") { ScreenA(navController, contactViewModel) }
        composable("screen_b") { ScreenB(navController, contactViewModel) }
    }
}
