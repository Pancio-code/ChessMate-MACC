package com.example.chessmate.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.sendMail(context: Context,to: String, subject: String) {
    try {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "vnd.android.cursor.item/email"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No app found for handle emails!",
            Toast.LENGTH_LONG
        ).show()
    } catch (t: Throwable) {
        Toast.makeText(
            context,
            t.message,
            Toast.LENGTH_LONG
        ).show()
    }
}