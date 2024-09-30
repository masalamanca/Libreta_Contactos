package com.example.interfaces

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Contact(
    val nombres: String,
    val apellidos: String,
    val telefono: String,
    val direccion: String,
    val email: String,
    val imagePath: String // Agregar el campo imagePath
)

class ContactViewModel : ViewModel() {
    val contactList = mutableStateListOf<Contact>()

    fun addContact(contact: Contact) {
        contactList.add(contact)
    }

    fun updateContact(index: Int, updatedContact: Contact) {
        // Verifica si el índice está en el rango válido
        if (index in contactList.indices) {
            contactList[index] = updatedContact
        }
    }

    fun deleteContact(index: Int) {
        // Verifica si el índice está en el rango válido
        if (index in contactList.indices) {
            contactList.removeAt(index)
        }
    }
}




