package com.michael.template.core.common

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michael.template.feature.contacts.domain.model.ContactUiModel
import java.io.IOException

fun displayToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun Dp.toPx(): Int = LocalDensity.current.run {
    roundToPx()
}

// Height Extension
@Composable
fun Int.height() = Spacer(modifier = Modifier.height(this.dp))

// Weight Extension
@Composable
fun Int.width() = Spacer(modifier = Modifier.width(this.dp))

// Rounded corner All Extension
@Composable
fun Int.radius() = RoundedCornerShape(this.dp)

fun Context.assetsToBitmap(fileName: String): Bitmap? {
    return try {
        with(assets.open(fileName)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        null
    }
}

fun Context.dialPhoneNumber(phoneNumber: String) {
    // Check if the cleaned phone number is a valid phone number using a regular expression
    if (isValidPhoneNumber(phoneNumber)) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    } else {
        // Handle the case where the phone number is not valid
        // You may want to show a toast, log an error, or take other appropriate action
        Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
    }
}

// Function to validate if the string is a valid phone number
fun isValidPhoneNumber(phoneNumber: String): Boolean {
    // Define your phone number validation logic here using a regular expression
    // For simplicity, let's assume a valid phone number contains only digits and optional '+' at the beginning
    val phoneRegex = """^\+?\d+$""".toRegex()
    return phoneRegex.matches(phoneNumber)
}

fun Context.shareContact(contact: ContactUiModel) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, "Name: ${contact.name}\nPhone: ${contact.phones.first()}")
    }

    startActivity(Intent.createChooser(shareIntent, "Share ${contact.name}'s Contact"))
}
