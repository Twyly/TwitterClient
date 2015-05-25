package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.HTMLTextDisplay;
import com.codepath.apps.restclienttemplate.ProfileImageHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.timeunits.Day;
import com.codepath.apps.restclienttemplate.timeunits.Hour;
import com.codepath.apps.restclienttemplate.timeunits.Minute;
import com.codepath.apps.restclienttemplate.timeunits.Second;
import com.codepath.apps.restclienttemplate.timeunits.Week;
import com.codepath.apps.restclienttemplate.timeunits.Year;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        Button retweet;
        Button favorite;
        Button reply;
    }

    private HTMLTextDisplay formatter;
    private ListView listView;
    private PrettyTime prettyTime;

    public TweetsArrayAdapter(Context context, List<Tweet> objects, ListView listView) {
        super(context, 0, objects);
        this.listView = listView;
        this.formatter = new HTMLTextDisplay(context.getResources());
        prettyTime = twitterTime();
    }

    private PrettyTime twitterTime() {
        PrettyTime p = new PrettyTime();
        p.clearUnits();
        Second second = new Second();
        Minute minute = new Minute();
        Hour hour = new Hour();
        Day day = new Day();
        Week week = new Week();
        Year year = new Year();
        p.registerUnit(second, second);
        p.registerUnit(minute, minute);
        p.registerUnit(hour, hour);
        p.registerUnit(day, day);
        p.registerUnit(week, week);
        p.registerUnit(year, year);
        return p;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.profile = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.retweet = (Button) convertView.findViewById(R.id.btnRetweet);
            viewHolder.favorite = (Button) convertView.findViewById(R.id.btnFavorite);
            viewHolder.reply = (Button) convertView.findViewById(R.id.btnReply);

//            Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gotham-Rounded-Light.ttf");
//            viewHolder.body.setTypeface(font);

            viewHolder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int positon = listView.getPositionForView(v);
                    if (position != ListView.INVALID_POSITION) {
                        Tweet tweet = getItem(position);
                        // Launch Reply Screen
                        TimelineActivity activity = (TimelineActivity) getContext();
                        activity.showComposeDialog(tweet);
                    }
                }
            });


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(TextUtils.concat(formatter.usernameSpanned(tweet.getUser().getName()), " ", formatter.screenameSpanned(tweet.getUser().getScreenName())));
        viewHolder.body.setText(tweet.getBody());
        viewHolder.profile.setImageResource(android.R.color.transparent);
        viewHolder.timestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        viewHolder.retweet.setText(Integer.toString(tweet.getRetweetCount()));
        viewHolder.favorite.setText(Integer.toString(tweet.getFavoriteCount()));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(viewHolder.profile);
        return convertView;
    }


    private String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            return prettyTime.format(new Date(dateMillis));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return concatinateDateString(relativeDate);
    }

    private String concatinateDateString(String string) {
        return string;
//        string = string.replaceAll("ago", "");
//        string = string.replaceAll("minute.*", "m");
//        string = string.replaceAll("hour.*", "h");
//        string = string.replaceAll("day.*", "d");
//        return string;
    }

}
