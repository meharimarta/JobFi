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
import android.util.Log;
import com.primed.jobfi.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity
{

    private DrawerLayout drawerLayout;
    private final List<User> user = new ArrayList<>();

    private String TAG = "#### MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        user.add(0, new User(this));

        String token;

        token = user.get(0).getToken();

        if (token != null && !token.isEmpty())
        {
            replaceFragment(new HomeFragment(user.get(0)));
        }
        else
        {
            replaceFragment(new AuthFragment());
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        FloatingActionButton fab = findViewById(R.id.fab);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    switch (item.getItemId())
                    {
                        case R.id.nav_home:

                            // If not found in backstack, add a new instance
                            replaceFragment(new HomeFragment(user.get(0)));
                            break;
                        case R.id.nav_about:
                            // Handle About fragment
                            break;
                        case R.id.nav_blog:
                            // Handle Blog fragment
                            break;
                        case R.id.nav_setup:
                            replaceFragment(new SetupFragment());
                            break;
                    }
                    drawerLayout.closeDrawer(navigationView); // Close the drawer after handling item click
                    return true; // Indicate that the item click is handled
                }
            });

        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (drawerLayout.isDrawerOpen(navigationView))
                    {
                        drawerLayout.closeDrawer(navigationView);
                    }
                    else
                    {
                        drawerLayout.openDrawer(navigationView);
                    }
                }
            });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        );

        String tag = fragment.getClass().getSimpleName();  // Use fragment class name as tag
        Fragment fragmentFromBackstack = fragmentManager.findFragmentByTag(tag);

        if (fragmentFromBackstack != null) {
            // If the fragment is found but not on top, pop backstack until this fragment is reached
            fragmentManager.popBackStack(tag, 0);
            
        } else {
            // Fragment not found, add a new instance
            fragmentTransaction.replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag);
            fragmentTransaction.commit();
        }
    }
}

