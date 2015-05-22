package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * Created by teddywyly on 5/21/15.
 */
public class DetailTweetView extends RelativeLayout {

    private ImageView ivImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvTweet;
    private TextView tvTimestamp;

//    public DetailTweetView(Context context, AttributeSet atts, int defStyle) {
//        super(context, atts, defStyle);
//        if (!isInEditMode()) {
//            LayoutInflater.from(context).inflate(R.layout.item_detail_tweet_children, this, true);
//            setupChildren();
//        }
//    }


    public DetailTweetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        if (!isInEditMode()) {
//            LayoutInflater.from(context).inflate(R.layout.item_detail_tweet_children, this, true);
//            setupChildren();
//        }
    }

    public static DetailTweetView inflate(ViewGroup parent) {
        DetailTweetView view = (DetailTweetView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tweet_detail, parent, false);
        return view;
    }

    public void setTweet(Tweet tweet) {
//        ivImage.setImageResource(android.R.color.transparent);
//        tvUsername.setText(tweet.getUser().getName());
//        tvScreenname.setText(tweet.getUser().getScreenName());
//        tvTweet.setText(tweet.getBody());
//        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivImage);
    }

    private void setupChildren() {
//        ivImage = (ImageView) findViewById(R.id.ivImage);
//        tvUsername = (TextView) findViewById(R.id.tvUsername);
//        tvScreenname = (TextView) findViewById(R.id.tvScreenname);
//        tvTweet = (TextView) findViewById(R.id.tvTweet);
    }
}
