package com.primed.jobfi;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragment_container, new SetupFragment());
		ft.addToBackStack(null);
		ft.commit();
		
		
		drawerLayout = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.navigation_view);
        FloatingActionButton fab = findViewById(R.id.fab);

		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.nav_home:
						    replaceFragment(new HomeFragment());
							break;
						case R.id.nav_about:
							// Handle item two click
							break;
						case R.id.nav_blog:
							//replaceFragment(new FragmentWebView());
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
