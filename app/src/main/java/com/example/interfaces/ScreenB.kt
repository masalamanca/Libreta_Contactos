package com.example.interfaces

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Brush
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.random.Random

@Composable
fun ScreenB(navController: NavController, contactViewModel: ContactViewModel = viewModel()) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFF7F2F2), Color(0xFFD5ECD9))
    )

    var selectedLetter by remember { mutableStateOf<Char?>(null) } // Variable para la letra seleccionada

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(gradientBrush)
    ) {
        // Barra de búsqueda
        Column(
            modifier = Modifier
                .width(10.dp) // Ancho de la barra de búsqueda
                .fillMaxHeight()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ('A'..'Z').forEach { letter ->
                Text(
                    text = letter.toString(),
                    modifier = Modifier
                        .clickable { selectedLetter = letter } // Establecer la letra seleccionada
                        .padding(4.dp),
                    fontSize = 16.sp,
                    color = Color(0xFF007AFF)
                )
            }
        }

        // Contenedor principal de la lista de contactos
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp) // Espacio entre la barra y la lista
        ) {
            Text(
                text = "Lista de Contactos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0A0A0A), // Color primario
                modifier = Modifier.padding(vertical = 32.dp)
            )

            // Filtrar contactos por la letra seleccionada
            val filteredContacts = contactViewModel.contactList.filter {
                selectedLetter == null || it.nombres.startsWith(selectedLetter.toString(), ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredContacts) { contact ->
                    ContactCard(
                        contact = contact,
                        onEdit = { updatedContact ->
                            contactViewModel.updateContact(contactViewModel.contactList.indexOf(contact), updatedContact)
                        },
                        onDelete = {
                            contactViewModel.deleteContact(contactViewModel.contactList.indexOf(contact))
                        }
                    )
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF)) // Color primario
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun ContactCard(contact: Contact, onEdit: (Contact) -> Unit, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    var nombres by remember { mutableStateOf(contact.nombres) }
    var apellidos by remember { mutableStateOf(contact.apellidos) }
    var telefono by remember { mutableStateOf(contact.telefono) }
    var direccion by remember { mutableStateOf(contact.direccion) }
    var email by remember { mutableStateOf(contact.email) }
    var imagePath by remember { mutableStateOf(contact.imagePath) } // Agregar estado para imagePath

    // Update fields when entering edit mode
    if (editMode) {
        nombres = contact.nombres
        apellidos = contact.apellidos
        telefono = contact.telefono
        direccion = contact.direccion
        email = contact.email
        imagePath = contact.imagePath // Asignar la ruta de la imagen
    }

    // Color para el círculo
    val circleColor = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), // Color de fondo blanco
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = CircleShape,
                    color = circleColor
                ) {
                    Text(
                        text = contact.nombres.first().toString(),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .wrapContentSize(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (editMode) {
                    TextFieldComponent(
                        value = nombres,
                        onValueChange = { nombres = it },
                        label = "Nombres"
                    )
                    TextFieldComponent(
                        value = apellidos,
                        onValueChange = { apellidos = it },
                        label = "Apellidos"
                    )
                    TextFieldComponent(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = "Teléfono"
                    )
                    TextFieldComponent(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = "Dirección"
                    )
                    TextFieldComponent(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email"
                    )

                    Button(onClick = {
                        onEdit(Contact(nombres, apellidos, telefono, direccion, email, imagePath)) // Incluir imagePath
                        editMode = false
                    }) {
                        Text("Guardar")
                    }
                } else {
                    Text(
                        text = contact.nombres,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    if (expanded) {
                        Text(buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Apellidos: ")
                            }
                            append(contact.apellidos)
                        })
                        Text(buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Teléfono: ")
                            }
                            append(contact.telefono)
                        })
                        Text(buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Dirección: ")
                            }
                            append(contact.direccion)
                        })
                        Text(buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Email: ")
                            }
                            append(contact.email)
                        })
                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly // Distribuir espacio entre los botones
                        ) {
                            Button(
                                onClick = { editMode = true },
                                modifier = Modifier.weight(1f), // Asegura que el botón de editar ocupe el mismo espacio
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007AFF))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center, // Centra el contenido del botón
                                    modifier = Modifier.fillMaxWidth() // Llena el ancho del botón
                                ) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Editar")
                                }
                            }

                            Button(
                                onClick = { onDelete() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                modifier = Modifier.weight(1f) // Asegura que el botón de eliminar ocupe el mismo espacio
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center, // Centra el contenido del botón
                                    modifier = Modifier.fillMaxWidth() // Llena el ancho del botón
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Eliminar", color = Color.White)
                                }
                            }
                        }
                    }
                    }
                }
            }
        }
    }


@Composable
fun TextFieldComponent(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFEFEFEF),
            unfocusedContainerColor = Color(0xFFEFEFEF),
            focusedIndicatorColor = Color(0xFF007AFF),
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}