package com.example.control;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.example.control.utils.DeviceIdManager;  // Importamos la clase

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtener el ID único del dispositivo
        String deviceId = DeviceIdManager.getDeviceId(this);
        Log.d("MainActivity", "ID del dispositivo: " + deviceId);

        // Configuración del Toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        // Configuración del NavigationView (identificador @id/barra)
        NavigationView navigationView = findViewById(R.id.barra);
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
                return false;
            }
        });

        DrawerLayout dl = findViewById(R.id.Main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dl,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        dl.addDrawerListener(toggle);
        toggle.syncState();

        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                } else {
                    dl.openDrawer((int) Gravity.START);
                }
            }
        });
    }
}
