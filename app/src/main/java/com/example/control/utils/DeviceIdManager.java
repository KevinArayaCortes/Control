package com.example.control.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.UUID;

public class DeviceIdManager {
    private static final String PREFS_NAME = "ControlAppPrefs";  // Nombre de las preferencias
    private static final String DEVICE_ID_KEY = "device_id";      // Clave del ID del dispositivo

    /**
     * Obtiene el ID único del dispositivo. Si no existe, genera uno nuevo.
     *
     * @param context El contexto de la aplicación.
     * @return El ID único del dispositivo.
     */
    public static String getDeviceId(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String deviceId = sharedPreferences.getString(DEVICE_ID_KEY, null);

        // Si no hay un ID guardado, generamos uno nuevo
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString();
            // Guardamos el nuevo ID en SharedPreferences
            sharedPreferences.edit().putString(DEVICE_ID_KEY, deviceId).apply();
        }
        return deviceId;
    }
}
