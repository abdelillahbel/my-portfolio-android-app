/*
 * Copyright (c) 2024. DevUnion Foundation.
 * All rights reserved.
 */

package dev.devunion.myportfolio.ui.auth.reset_password

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.devunion.myportfolio.R
import dev.devunion.myportfolio.viewmodels.auth.AuthViewModelInterface
import dev.devunion.myportfolio.viewmodels.auth.DummyAuthViewModel

@Preview
@Composable
fun ResetPasswordScreenPreview() {
    val navController = rememberNavController()
    val authViewModel: DummyAuthViewModel = viewModel()
    ResetPasswordScreen(authViewModel = authViewModel, navController = navController)
}

@Composable
fun ResetPasswordScreen(authViewModel: AuthViewModelInterface, navController: NavController) {

    val context = LocalContext.current

    val poppinsFamily = FontFamily(
        Font(R.font.poppins_light, FontWeight.Light),
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Show alert dialog if needed
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Accept")
                }
            },
            modifier = Modifier.animateContentSize()  // Add animation to dialog appearance
        )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),  // Scrollable in case of more content
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = "Enter your email bellow :",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontFamily = poppinsFamily,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = authViewModel.email,
            onValueChange = { authViewModel.email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                authViewModel.recoverPassword(
                    onSuccess = {
                        dialogMessage = "Password reset link sent, check your email"
                        showDialog = true
                    },
                    onFailure = { exception ->
                        dialogMessage = exception.message.toString()
                        showDialog = true
                    }
                )
            },
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .animateContentSize(),  // Add animation to button size change
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            shape = MaterialTheme.shapes.medium  // Use Material3 shape
        ) {
            Text("Recover Password")
        }


    }
}
