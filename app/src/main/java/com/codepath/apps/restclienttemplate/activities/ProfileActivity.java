package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.ProgressFragment;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;

public class ProfileActivity extends AppCompatActivity {

    private UserTimelineFragment fragmentUser;
    private ProgressFragment fragmentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User user = getIntent().getExtras().getParcelable("user");
        if (savedInstanceState == null) {
            fragmentUser = UserTimelineFragment.newInstance(user.getScreenName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUser);
//            fragmentProgress = new ProgressFragment();
//            ft.hide(fragmentUser);
//            ft.add(R.id.flContainer, fragmentProgress, "Progress");
//            fragmentUser.setNetworkListener(new NetworkListener() {
//                @Override
//                public void finishedInitialLoad(boolean success) {
//                    Log.d("DEBUG", "Switch Fragments");
//                    ft.hide(fragmentProgress);
//                    ft.show(fragmentUser);
//                }
//            });
            ft.commit();
        }

        setupActionBar();

    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        Class<?> parentClass = (Class) getIntent().getSerializableExtra("ParentClass");
        Intent i = new Intent(this, parentClass);
        return i;
    }

    @Override
    public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateSupportNavigateUpTaskStack(builder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
