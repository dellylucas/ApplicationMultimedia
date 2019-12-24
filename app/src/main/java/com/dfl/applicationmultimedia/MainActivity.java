package com.dfl.applicationmultimedia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISOS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int leer_exte = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int escribir_exte = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int perm_audio = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (leer_exte == PackageManager.PERMISSION_DENIED || escribir_exte == PackageManager.PERMISSION_DENIED || perm_audio == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, PERMISOS, REQUEST_CODE);


        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new AnimationFragment())
                .commit();

        updateToolbar("", "");
    }

    public void updateToolbar(String title, String subTitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            toolbar.setSubtitle(subTitle);
        }
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int idItem = item.getItemId();
        navigationView.setCheckedItem(item);
        Fragment fragment = null;
        switch (idItem) {
            case R.id.nav_animacion:
                fragment = new AnimationFragment();
                break;
            case R.id.nav_graficos:
                fragment = new GraficFragment();
                break;
            case R.id.nav_imagen:
                fragment = new ImageFragment();
                break;
            case R.id.nav_audio:
                fragment = new AudioFragment();
                break;
            case R.id.nav_video:
                fragment = new VideoFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
