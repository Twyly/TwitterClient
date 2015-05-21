package com.codepath.apps.restclienttemplate;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;


public class DetailTweetActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tweet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViews();
    }

    private void setupViews() {
        Tweet tweet = getIntent().getParcelableExtra("tweet");
        ImageView ivImage = (ImageView) findViewById(R.id.ivImage);
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvScreename = (TextView) findViewById(R.id.tvScreenname);
        TextView tvTweet = (TextView) findViewById(R.id.tvTweet);

        tvUsername.setText(tweet.getUser().getName());
        tvScreename.setText(tweet.getUser().getScreenName());
        tvTweet.setText(tweet.getBody());

        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivImage);

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
