package com.codepath.apps.restclienttemplate;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;


public class DetailTweetActivity extends ActionBarActivity {

    private DetailTweetAdapter mAdapter;
    private ListView lvDetail;
    private Tweet mTweet;

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

        mTweet = getIntent().getParcelableExtra("tweet");
        lvDetail = (ListView) findViewById(R.id.lvDetail);
        mAdapter = new DetailTweetAdapter(this, mTweet);
        lvDetail.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}
