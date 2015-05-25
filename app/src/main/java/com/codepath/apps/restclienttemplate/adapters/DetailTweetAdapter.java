package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.HTMLTextDisplay;
import com.codepath.apps.restclienttemplate.ProfileImageHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.DetailTweetActivity;
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
    private HTMLTextDisplay formatter;
    private ListView listView;

    public DetailTweetAdapter(Context context, Tweet tweet) {
        this.context = context;
        this.tweet = tweet;
        this.formatter = new HTMLTextDisplay(context.getResources());
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
                configureDetailRow(convertView);
               return convertView;
            case STAT_ROW:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_stats, parent, false);
                }
                configureStatsRow(convertView);
                return convertView;
            case ACTION_ROW:
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet_action, parent, false);
                    configureNewActionRow(convertView);

                }
                return convertView;
            default:
                break;
        }
        return convertView;
    }

    private void configureDetailRow(View convertView) {
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        ImageView ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvScreename = (TextView) convertView.findViewById(R.id.tvScreenname);
        TextView tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
        TextView tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);


        ivImage.setImageResource(android.R.color.transparent);
        ivMedia.setImageResource(0);
        tvUsername.setText(formatter.usernameSpanned(tweet.getUser().getName()));
        tvScreename.setText(formatter.screenameSpanned(tweet.getUser().getScreenName()));
        tvTweet.setText(tweet.getBody());
        tvTimestamp.setText(tweet.getCreatedAt());
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(ivImage);

        if (tweet.getMediaURL() != null) {
            float ratio = (float)tweet.getMediaWidth()/tweet.getMediaHeight();
            float width = ivMedia.getMeasuredWidth();
            int height = (int)(width/ratio);
            Log.d("RATIO", ratio + "");
            Log.d("Width", width + "");
            Log.d("HEIGHT", width/ratio + "");
            Picasso.with(getContext()).load(tweet.getMediaURL()).transform(ProfileImageHelper.roundTransformation()).placeholder(R.color.theme_text_detail).into(ivMedia);
        }

    }

    private void configureStatsRow(View convertView) {
        Button btnRetweet = (Button) convertView.findViewById(R.id.btnRetweet);
        Button btnFavorite = (Button) convertView.findViewById(R.id.btnFavorite);
        btnRetweet.setText(formatter.statSpanned("RETWEET", tweet.getRetweetCount()));
        btnFavorite.setText(formatter.statSpanned("FAVORITE", tweet.getFavoriteCount()));
    }

    private void configureNewActionRow(View convertView) {
        ImageButton ibReply = (ImageButton) convertView.findViewById(R.id.ibReply);
        ibReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailTweetActivity detailActivity = (DetailTweetActivity) getContext();
                detailActivity.showReplyComposeDialog(tweet);
            }
        });

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
