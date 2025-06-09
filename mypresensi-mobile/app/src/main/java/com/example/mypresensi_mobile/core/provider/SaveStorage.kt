package com.example.mypresensi_mobile.core.provider

import android.content.Context
import android.content.SharedPreferences

class SaveStorage {
    companion object {
        private const val PREFS_NAME = "MyPresensiPrefs"
        const val KEY_MASUK_TIME = "masukTime"
        const val KEY_LAST_SUBMISSION_DATE= "isSubmissionDate"
        const val KEY_KELUAR_TIME = "keluarTime"
        const val KEY_IS_MASUK_CLICKED = "isMasukClicked"  // New key for isMasukClicked
    }

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveData(context: Context, key: String, value: String) {
        val editor = getPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(context: Context, key: String): String? {
        return getPreferences(context).getString(key, null)
    }

    fun saveBooleanData(context: Context, key: String, value: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanData(context: Context, key: String): Boolean {
        return getPreferences(context).getBoolean(key, false)
    }

    fun clearData(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}