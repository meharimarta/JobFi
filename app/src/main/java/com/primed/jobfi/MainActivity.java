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
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String token;
		FileManager fileManager = new FileManager(this, "user_data.txt");
        JSONObject userData = fileManager.getFileAsJsonObject();
        Log.d("***Main activuty", userData.toString());
        try
        {
            token =(new JSONObject(userData.getString("responseData"))).getString("token");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            token = "";
        }
        if(! token.isEmpty()) {
            replaceFragment(new HomeFragment());
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
						    replaceFragment(new HomeFragment());
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
