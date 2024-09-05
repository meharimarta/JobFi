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
        
        if(token != null &&  token != "") {
            replaceFragment(new HomeFragment(user.get(0)));
        } else {
            replaceFragment(new SetupFragment());
        }
        
		drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        FloatingActionButton fab = findViewById(R.id.fab);

		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.nav_home:
						    replaceFragment(new HomeFragment(user.get(0)));
							break;
						case R.id.nav_about:
							// Handle item two click
							break;
						case R.id.nav_blog:
							//replaceFragment(new FragmentWebView());
							break;
                         case R.id.nav_setup:
                             replaceFragment(new SetupFragment());
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
    protected void onResume()
    {
        //create new user instance on resume of app
        super.onResume();
        user.add(0, new User(this));

        String token;

        token = user.get(0).getToken();

        if(token != null &&  token != "") {
            replaceFragment(new HomeFragment(user.get(0)));
        } else {
            replaceFragment(new SetupFragment());
        }
    }
	
    
	private void replaceFragment(Fragment fragment) {
		// Assume you have a FragmentManager instance
		FragmentManager fragmentManager = getSupportFragmentManager();

		// Begin a transaction
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		// Replace the current fragment with a new fragment
		// Replace YourFragment with your actual fragment class
		fragmentTransaction.replace(R.id.fragment_container, fragment);

        // Optionally, add the transaction to the back stack
		fragmentTransaction.addToBackStack(null);

        // Commit the transaction
		fragmentTransaction.commit();
	}
}
