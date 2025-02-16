package com.example.testauth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        AppBarConfiguration appBarConfiguration ;
//        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeContentFragment).build();

        // Find the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_home);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Set up BottomNavigationView with NavController

            BottomNavigationView bottomNav = findViewById(R.id.buttom_nav);
            NavigationUI.setupWithNavController(bottomNav, navController);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {

//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.nav_host_home);
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//            return navController.navigateUp() || super.onSupportNavigateUp();
//        }
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.nav_host_home);
//
//        if (navHostFragment != null) {
//            NavController navController = navHostFragment.getNavController();
//
//            if (navController.getCurrentDestination() != null &&
//                    navController.getCurrentDestination().getId() != R.id.homeFragment) {
//                // Navigate back to the home fragment
//                navController.navigate(R.id.action_mealDetails_to_homeContentFragment);
//            }
//        }

//    }
}

//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_home);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//}