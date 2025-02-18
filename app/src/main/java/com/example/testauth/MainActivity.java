package com.example.testauth;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;


import com.example.testauth.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

    }

    private static final String TAG = "MainActivity";
    @Override
    protected void onResume() {
        super.onResume();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.bottom_nav_bar_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

//    @Override
//    public void onBackPressed() {
//        // Get the current fragment
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
//
//        // If the current fragment is not SplashFragment, allow back press
//        if (!(currentFragment instanceof SplashFragment)) {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//
//        // Check if we are NOT in the HomeFragment
//        if (navController.getCurrentDestination() != null &&
//                navController.getCurrentDestination().getId() != R.id.RHomeFragment) {
//
//            // Navigate to HomeFragment instead of closing the app
//            navController.navigate(R.id.RHomeFragment);
//        } else {
//            // If already on Home, exit the app
//            finish();
//        }
//    }

}





