package com.example.control;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.example.control.utils.DeviceIdManager;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forzar el modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        // Obtener el ID único del dispositivo
        String deviceId = DeviceIdManager.getDeviceId(this);
        Log.d("MainActivity", "ID del dispositivo: " + deviceId);

        // Configuración del Toolbar
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        // Configuración del NavigationView (identificador @id/barra)
        NavigationView navigationView = findViewById(R.id.barra);
        drawerLayout = findViewById(R.id.Main);  // Inicializamos drawerLayout

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.op1) {
                    ActividadFisicaFragment a = new ActividadFisicaFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, a).commit();
                } else if (id == R.id.op2) {
                    HigienicoFragment h = new HigienicoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, h).commit();
                } else if (id == R.id.op3) {
                    HobbieFragment ho = new HobbieFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, ho).commit();
                } else if (id == R.id.op4) {
                    DietaFragment d = new DietaFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, d).commit();
                } else if (id == R.id.op5) {
                    HistorialFragment hi = new HistorialFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, hi).commit();
                } else if (id == R.id.op6) {
                    DesafioFragment de = new DesafioFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, de).commit();
                }

                // Cerrar el menú lateral automáticamente
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;  // Cambiado a true para reflejar que la opción fue seleccionada
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }
}
