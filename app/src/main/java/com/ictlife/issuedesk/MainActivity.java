package com.ictlife.issuedesk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ictlife.issuedesk.ui.auth.LoginActivity;
import com.ictlife.issuedesk.ui.create.issue.CreateIssueActivity;
import com.ictlife.issuedesk.ui.create.user.CreateUserActivity;
import com.ictlife.issuedesk.util.PrefManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private PrefManager prefManager;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefManager = new PrefManager(this);

        if (!prefManager.isUserLoggedIn()) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAnEntity();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_customers, R.id.nav_users)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_user_name);
        TextView navUserEmail = (TextView) headerView.findViewById(R.id.nav_user_email);
        navUsername.setText(prefManager.getUserFullName());
        navUserEmail.setText(prefManager.getUserEmail());
    }

    private void createAnEntity() {

        String currentFrag = navController.getCurrentDestination().getLabel().toString();

        Log.e(TAG, "currentFrag: " + currentFrag);

        if (currentFrag.equalsIgnoreCase("Dashboard")) {
            //create a CX issue
            Intent loginIntent = new Intent(MainActivity.this, CreateIssueActivity.class);
            startActivity(loginIntent);
        }

        if (currentFrag.equalsIgnoreCase("Users")) {
            //create a CX user
            Intent loginIntent = new Intent(MainActivity.this, CreateUserActivity.class);
            startActivity(loginIntent);
        }

        if (currentFrag.equalsIgnoreCase("Customers")) {
//            //create a CX issue
//            Intent loginIntent = new Intent(MainActivity.this, CreateIssueActivity.class);
//            startActivity(loginIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {

            prefManager.setLoggedIn(false);

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
