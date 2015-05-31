package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.NetworkListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.FollowersFragment;
import com.codepath.apps.restclienttemplate.fragments.ProgressFragment;
import com.codepath.apps.restclienttemplate.models.User;

public class FollowersActivity extends AppCompatActivity {

    private FollowersFragment followersFragment;
    private ProgressFragment progressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        setupActionBar();
        if (savedInstanceState == null) {
            startFragment(getIntent());
        }
    }

    private void startFragment(Intent i) {
        User user = getIntent().getParcelableExtra("user");
        boolean forFollowers = getIntent().getBooleanExtra("forFollowers", false);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        followersFragment = FollowersFragment.newInstance(user, forFollowers);
        ft.add(R.id.flContainer, followersFragment, "Followers");
        progressFragment = new ProgressFragment();
        ft.add(R.id.flContainer, progressFragment, "Progress");
        followersFragment.setListener(new NetworkListener() {
            @Override
            public void finishedInitialLoad(boolean success) {
                ft.hide(progressFragment);
            }
        });
        ft.commit();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_followers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
