package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.FollowersFragment;
import com.codepath.apps.restclienttemplate.models.User;

public class FollowersActivity extends AppCompatActivity {

    private FollowersFragment followersFragment;

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
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        followersFragment = FollowersFragment.newInstance(user, forFollowers);
        ft.replace(R.id.flContainer, followersFragment, "Followers");
        ft.commit();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);

        boolean forFollowers = getIntent().getBooleanExtra("forFollowers", false);
        if (forFollowers) {
            getSupportActionBar().setTitle(R.string.followers_label);
        } else {
            getSupportActionBar().setTitle(R.string.following_label);
        }
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
