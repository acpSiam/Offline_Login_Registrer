package bmarpc.acpsiam.offlineloginregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

public class ActivityHome extends AppCompatActivity {

    DrawerLayout drawerLayout;
    FrameLayout fragmentLayout;
    NavigationView navigationView;
    MaterialButton drawerButton;
    LottieAnimationView darkLightToggle;

    boolean isDarkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //*Finding IDs
        drawerLayout = findViewById(R.id.drawer_layout_id);
        fragmentLayout = findViewById(R.id.fragment_layout_id);
        navigationView = findViewById(R.id.navigation_view_id);
        drawerButton = findViewById(R.id.drawer_button_id);
        darkLightToggle = findViewById(R.id.dark_light_toggle_button_id);
        navigationView.setCheckedItem(R.id.dashboard_menu_id);


        drawerButton.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        selectFragment(new FragmentHome());

        boolean darkEnabled = isDarkEnabled();
        if (darkEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            darkLightToggle.setProgress(1f);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            darkLightToggle.setProgress(0f);
        }

        darkLightToggle.setOnClickListener(view -> {
            if (!isDarkEnabled) {
                darkLightToggle.setProgress(0);
                darkLightToggle.setSpeed(1);
                darkLightToggle.playAnimation();
                isDarkEnabled = true;
                saveThemeState(true);
            } else {
                darkLightToggle.setProgress(1f);
                darkLightToggle.setSpeed(-1);
                darkLightToggle.playAnimation();
                isDarkEnabled = false;
                saveThemeState(false);
            }
        });


        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.dashboard_menu_id) {
                selectFragment(new FragmentHome());
                darkLightToggle.setVisibility(View.VISIBLE);
            } else if (id == R.id.edit_profile_menu_id) {
                selectFragment(new FragmentEditProfile());
                darkLightToggle.setVisibility(View.GONE);
            } else if (id == R.id.change_pass_menu_id) {
                selectFragment(new FragmentChangePassword());
                darkLightToggle.setVisibility(View.GONE);
            } else if (id == R.id.contact_dev_menu_id) {
                selectFragment(new FragmentDev());
                darkLightToggle.setVisibility(View.GONE);
            } else if (id == R.id.log_out_menu_id) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("You're about to log out! Confirm?")
                        .setIcon(R.drawable.ic_round_logout_24)
                        .setMessage("You'll need to enter your password and id to be back to dashboard again.")
                        .setCancelable(true)
                        .setPositiveButton("Log Out", (dialogInterface, i) -> logOut())
                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }

            return true;
        });


    }


    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_SP_STR), MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
        startActivity(new Intent(ActivityHome.this, ActivityLogin.class));
    }

    public void selectFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.anime_fade_in, R.anim.anime_fade_out);
        fragmentTransaction.replace(R.id.fragment_layout_id, fragment);
        fragmentTransaction.commit();

        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        }

    }



    public void saveThemeState(boolean darkEnabled) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.THEME_TOGGLE_SP_STR), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.THEME_STATE_STR), darkEnabled);
        editor.apply();

        if (darkEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }

    public boolean isDarkEnabled() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(getString(R.string.THEME_TOGGLE_SP_STR), MODE_PRIVATE);
        isDarkEnabled = sharedPreferences.getBoolean(getString(R.string.THEME_STATE_STR), false);
        return isDarkEnabled;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            this.finishAffinity();
        }
    }






    @Override
    public void recreate() {
        finish();
        overridePendingTransition(R.anim.anime_fade_in,
                R.anim.anime_fade_out);
        startActivity(getIntent());
        overridePendingTransition(R.anim.anime_fade_in,
                R.anim.anime_fade_out);

    }

}