package com.example.chessmate.ui.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesManagerConstants {
    val IS_MINOR_KEY = booleanPreferencesKey("test")
    val AGE_KEY = intPreferencesKey("test_int")
    val NAME_KEY = stringPreferencesKey("test_string")
    val MOBILE_NUMBER = longPreferencesKey("test_long")
}