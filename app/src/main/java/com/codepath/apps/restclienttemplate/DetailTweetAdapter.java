package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

/**
 * Created by teddywyly on 5/21/15.
 */
public class DetailTweetAdapter extends BaseAdapter {

    public static final int BASE_ROWS = 3;

    public static final int DETAIL_ROW = 0;
    public static final int STAT_ROW = 1;
    public static final int ACTION_ROW = 2;
    public static final int TWEET_ROW = 3;



    private Tweet tweet;
    private Context context;


    public DetailTweetAdapter(Context context, Tweet tweet) {
        this.context = context;
        this.tweet = tweet;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case DETAIL_ROW:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_detail, parent, false);
                }
                ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
                TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
                TextView tvScreename = (TextView) convertView.findViewById(R.id.tvScreenname);
                TextView tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);

                ivImage.setImageResource(android.R.color.transparent);
                tvUsername.setText(tweet.getUser().getName());
                tvScreename.setText(tweet.getUser().getScreenName());
                tvTweet.setText(tweet.getBody());

                Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivImage);                return convertView;
            case STAT_ROW:

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_stats, parent, false);
                }

                Button btnRetweet = (Button) convertView.findViewById(R.id.btnRetweet);
                Button btnFavorite = (Button) convertView.findViewById(R.id.btnFavorite);
                btnRetweet.setText(tweet.getRetweetCount() + " RETWEETS");
                btnFavorite.setText(tweet.getFavoriteCount() + " FAVORITES");

                return convertView;

            case ACTION_ROW:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_action, parent, false);
                }
                return convertView;
            default:
                break;
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return DETAIL_ROW;
            case 1:
                return STAT_ROW;
            case 2:
                return ACTION_ROW;
            default:
                return TWEET_ROW;
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
        //return position >= BASE_ROWS ? tweets.get(position-BASE_ROWS) : null;
    }

    @Override
    public int getCount() {
        return BASE_ROWS;
        //return BASE_ROWS;
//        if (tweets != null) {
//            return BASE_ROWS + tweets.size();
//        } else {
//            return BASE_ROWS;
//        }
    }

    @Override
    public int getViewTypeCount() {
        return BASE_ROWS+1;
    }


}
