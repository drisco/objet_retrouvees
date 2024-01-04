package com.soutra.soutramoi;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharePreference {

    private static final String PREF_NAME = "etudiants"; // Nom de votre fichier SharedPreferences

    // Méthode pour écrire une chaîne de caractères dans les SharedPreferences
    public static void writeString(Context context, String key, String value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // Méthode pour lire une chaîne de caractères depuis les SharedPreferences
    public static String readString(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, defaultValue);
    }
    // Méthode pour effacer toutes les données des SharedPreferences
    public static void clearAllData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Efface toutes les données
        editor.apply();
    }
}

