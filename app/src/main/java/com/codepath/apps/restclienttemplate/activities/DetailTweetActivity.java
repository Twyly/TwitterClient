package com.codepath.apps.restclienttemplate.activities;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.restclienttemplate.adapters.DetailTweetAdapter;
import com.codepath.apps.restclienttemplate.fragments.ComposeTweetDialog;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DetailTweetActivity extends ActionBarActivity implements ComposeTweetDialog.ComposteTweetDialogListener {

    private DetailTweetAdapter mAdapter;
    private ListView lvDetail;
    private Tweet mTweet;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViews();
    }

    private void setupViews() {

        // Add twitter bird
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_bird);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setElevation(0);

        mTweet = getIntent().getParcelableExtra("tweet");
        currentUser = getIntent().getParcelableExtra("user");
        lvDetail = (ListView) findViewById(R.id.lvDetail);
        mAdapter = new DetailTweetAdapter(this, mTweet);
        lvDetail.setAdapter(mAdapter);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showReplyComposeDialog(Tweet tweet) {
        if (currentUser != null) {
            FragmentManager fm = getSupportFragmentManager();
            ComposeTweetDialog composeDialog = ComposeTweetDialog.newInstance(currentUser, tweet);
            composeDialog.show(fm, "fragment_compose_tweet");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishComposeTweet(Tweet tweet) {
        // Do nothing here as replies should not show up in your own feed
    }
}
