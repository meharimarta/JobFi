package com.primed.jobfi;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private final List<User> user = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        user.add(0, new User(this));

        String token;

        token = user.get(0).getToken();

        if (token != null && !token.isEmpty()) {
            replaceFragment(new HomeFragment(user.get(0)), "HomeFragment");
        } else {
            replaceFragment(new SetupFragment(), null);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        FloatingActionButton fab = findViewById(R.id.fab);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            // Check if HomeFragment is already in backstack
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            boolean fragmentPopped = fragmentManager.popBackStackImmediate("HomeFragment", 0);

                            if (!fragmentPopped) {
                                // If not found in backstack, add a new instance
                                replaceFragment(new HomeFragment(user.get(0)), "HomeFragment");
                            }
                            break;
                        case R.id.nav_about:
                            // Handle About fragment
                            break;
                        case R.id.nav_blog:
                            // Handle Blog fragment
                            break;
                        case R.id.nav_setup:
                            replaceFragment(new SetupFragment(), null);
                            break;
                    }
                    drawerLayout.closeDrawer(navigationView); // Close the drawer after handling item click
                    return true; // Indicate that the item click is handled
                }
            });

        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(navigationView)) {
                        drawerLayout.closeDrawer(navigationView);
                    } else {
                        drawerLayout.openDrawer(navigationView);
                    }
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = user.get(0).getToken();

        // Check if fragment is already loaded before replacing it
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            if (token != null && !token.isEmpty()) {
                replaceFragment(new HomeFragment(user.get(0)), "HomeFragment");
            } else {
                replaceFragment(new SetupFragment(), null);
            }
        }
    }

    private void replaceFragment(Fragment fragment, String tag) {
        // Get FragmentManager instance
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin a transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Set custom animations if desired
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_right,  // Enter animation for the new fragment
            R.anim.slide_out_left,  // Exit animation for the current fragment
            R.anim.slide_in_left,   // Enter animation when popping back to the current fragment
            R.anim.slide_out_right  // Exit animation when popping back to the previous fragment
        );

        // Replace the current fragment with the new fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);

        // Optionally, add the transaction to the backstack with a tag (like "HomeFragment")
        if (tag != null) {
            fragmentTransaction.addToBackStack(tag);
        } else {
            fragmentTransaction.addToBackStack(null);
        }

        // Commit the transaction
        fragmentTransaction.commit();
    }
}
