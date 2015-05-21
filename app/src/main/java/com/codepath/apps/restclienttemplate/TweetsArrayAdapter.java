package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by teddywyly on 5/18/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private class ViewHolder {
        ImageView profile;
        TextView username;
        TextView body;
        TextView timestamp;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.profile = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(tweet.getUser().getName());
        viewHolder.body.setText(tweet.getBody());
        viewHolder.profile.setImageResource(android.R.color.transparent);
        viewHolder.timestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.profile);
//        Picasso.with(getContext()).load(tweet.getProfileImage()).into(viewHolder.profile);


        return convertView;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
