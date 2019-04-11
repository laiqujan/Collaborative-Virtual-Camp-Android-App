package com.example.mahnoor.CollaborativeVirtualCamp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("My Teams");
        MyTeams myTeams = new MyTeams();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frag_fram, myTeams).addToBackStack("My Teams").commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(getApplicationContext());
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_team) {
            // Handle the my teams action
            setTitle("My Teams");
            MyTeams myTeams = new MyTeams();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frag_fram, myTeams).addToBackStack("My Teams").commit();
        }
        else if (id == R.id.joined_teams)
        {
            // handle joined teams here
            setTitle("My Joined Teams");
            JoinedTeam joinedTeam = new JoinedTeam();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frag_fram,joinedTeam).addToBackStack("Joined Teams").commit();

        } else if (id == R.id.my_account) {
            setTitle("My Account");
            MyAccountFragment myAccountFragment = new MyAccountFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frag_fram,myAccountFragment).addToBackStack("My Account").commit();
        } else if (id == R.id.logout) {
            // Handle logout here
            UserDetails.Username = null;
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
        }
        else if(id == R.id.create_team){
            setTitle("Create New Team");
            CreateTeam createTeam = new CreateTeam();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frag_fram,createTeam).addToBackStack("Create New Team").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        // Handle the my teams action
        setTitle("My Teams");
        MyTeams myTeams = new MyTeams();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frag_fram, myTeams).addToBackStack("My Teams").commit();
    }
}
