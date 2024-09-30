package com.example.interfaces

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.asImageBitmap
import java.io.IOException
import androidx.compose.runtime.remember


@Composable
fun ScreenA(navController: NavController, contactViewModel: ContactViewModel = viewModel()) {
    // Estados para los campos del formulario y la visualización de mensajes
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showSnackbar by remember { mutableStateOf(false) }
    var imagePath by remember { mutableStateOf("") }

    // Colores
    val primaryColor = Color(0xFF007AFF)
    val textColor = Color.Black
    val errorColor = Color.Red

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Cuadro para la imagen
                ImageUploadSection(imagePath) { path -> imagePath = path }

                Text(
                    text = "Crear Nuevo Contacto",
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                    modifier = Modifier.padding(vertical = 20.dp)
                )

                // Campos del formulario
                FormField("Nombres", nombres) { nombres = it }
                FormField("Apellidos", apellidos) { apellidos = it }
                FormField("Teléfono", telefono, KeyboardType.Number) {
                    // Asegurar que solo se ingresen números
                    telefono = it.filter { char -> char.isDigit() }
                }
                FormField("Dirección", direccion) {
                    // Validar alfanuméricos (esto permite caracteres alfanuméricos y espacios)
                    direccion = it.filter { char -> char.isLetterOrDigit() || char.isWhitespace() }
                }
                FormField("Email", email) {
                    email = it
                    // Validar que contenga el carácter '@' (opcional)
                    if (!email.contains("@") && email.isNotEmpty()) {
                        errorMessage = "El correo electrónico debe contener '@'."
                    } else {
                        errorMessage = ""
                    }
                }

                // Mensaje de error
                if (errorMessage.isNotEmpty()) {
                    Text(text = errorMessage, color = errorColor, fontSize = 14.sp)
                }

                // Botones de acción
                ActionButtons(navController, contactViewModel, nombres, apellidos, telefono, direccion, email, imagePath) {
                    errorMessage = it
                    showSnackbar = it.isEmpty()
                }

                // Snackbar para mostrar el mensaje de éxito
                if (showSnackbar) {
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        action = {
                            TextButton(onClick = { showSnackbar = false }) {
                                Text("Cerrar")
                            }
                        }
                    ) {
                        Text("Contacto guardado")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageUploadSection(imagePath: String, onImagePathChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Gray, shape = CircleShape)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        val imageBitmap = remember(imagePath) { loadImage(imagePath) }
        imageBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Foto del contacto", modifier = Modifier.fillMaxSize().clip(CircleShape))
        } ?: Text("Agregar foto", color = Color.White)
    }
}

@Composable
fun FormField(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, onValueChange: (String) -> Unit) {
    TextFieldComponent(
        value = value,
        onValueChange = onValueChange,
        label = label,
        primaryColor = Color(0xFF007AFF),
        textColor = Color.Black,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
    )
}

@Composable
fun ActionButtons(navController: NavController, contactViewModel: ContactViewModel, nombres: String, apellidos: String, telefono: String, direccion: String, email: String, imagePath: String, onErrorMessage: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                if (nombres.isBlank() || apellidos.isBlank() || telefono.isBlank() || direccion.isBlank() || email.isBlank() || !email.contains("@")) {
                    onErrorMessage("Verifica que todos los campos estén llenos y que el correo contenga '@'.")
                } else {
                    contactViewModel.addContact(Contact(nombres, apellidos, telefono, direccion, email, imagePath))
                    navController.navigate("screen_b")
                }
            },
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
        ) {
            Text("Guardar", color = Color.White, fontSize = 16.sp)
        }
        Button(
            onClick = { navController.navigate("screen_b") },
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
        ) {
            Text("Contactos", color = Color.White, fontSize = 14.sp)
        }
    }
}

// Función para cargar una imagen desde una ruta
private fun loadImage(path: String): Bitmap? {
    return try {
        BitmapFactory.decodeFile(path)
    } catch (e: IOException) {
        null
    }
}

@Composable
fun TextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    primaryColor: Color,
    textColor: Color,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0x1E598FDF),
            unfocusedContainerColor = Color(0xFFEFEFEF),
            focusedIndicatorColor = primaryColor,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(fontSize = 16.sp, color = textColor),
        keyboardOptions = keyboardOptions
    )
}

