package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.views.HTMLTextDisplay;
import com.codepath.apps.restclienttemplate.views.ProfileImageHelper;
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


import java.util.Date;
import java.util.List;

/**
 * Created by teddywyly on 5/18/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public interface TweetsArrayAdapterListener {
        public void replyClicked(Tweet tweet);
        public void profileClicked(User user);
    }

    private class ViewHolder {
        ImageButton profile;
        ImageView preview;
        TextView username;
        TextView body;
        TextView timestamp;
        Button retweet;
        Button favorite;
        Button reply;

        ImageView retweetIcon;
        TextView retweetText;
    }

    private HTMLTextDisplay formatter;
    private PrettyTime prettyTime;
    private TweetsArrayAdapterListener listener;

    public TweetsArrayAdapter(Context context, List<Tweet> objects, TweetsArrayAdapterListener listener) {
        super(context, 0, objects);
        this.formatter = new HTMLTextDisplay(context.getResources());
        this.listener = listener;
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
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
            viewHolder.profile = (ImageButton) convertView.findViewById(R.id.ibProfileImage);
            viewHolder.preview = (ImageView) convertView.findViewById(R.id.ivPreview);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.body = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.retweet = (Button) convertView.findViewById(R.id.btnRetweet);
            viewHolder.favorite = (Button) convertView.findViewById(R.id.btnFavorite);
            viewHolder.reply = (Button) convertView.findViewById(R.id.btnReply);

            viewHolder.retweetIcon = (ImageView) convertView.findViewById(R.id.ivRetweet);
            viewHolder.retweetText = (TextView) convertView.findViewById(R.id.tvRetweet);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.reply.setTag(tweet);

        viewHolder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Reply Screen
                listener.replyClicked(tweet);
            }
        });

        viewHolder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Reply Screen
                listener.profileClicked(tweet.getUser());
            }
        });

        viewHolder.username.setText(TextUtils.concat(formatter.usernameSpanned(tweet.getUser().getName()), " ", formatter.screenameSpanned(tweet.getUser().getScreenName())));
        viewHolder.body.setText(tweet.getBody());
        viewHolder.timestamp.setText(getRelativeTimeAgo(tweet.getTimestamp()));

        viewHolder.profile.setImageResource(android.R.color.transparent);
        viewHolder.preview.setImageResource(0);

        viewHolder.retweet.setText(Integer.toString(tweet.getRetweetCount()));
        viewHolder.favorite.setText(Integer.toString(tweet.getFavoriteCount()));
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(viewHolder.profile);

        if (tweet.getMediaURL() != null) {
            resizeImageView(viewHolder.preview, true);
            Picasso.with(getContext()).load(tweet.getMediaURL()).placeholder(R.color.theme_text_detail).transform(ProfileImageHelper.roundTransformation()).into(viewHolder.preview);
        } else {
            resizeImageView(viewHolder.preview, false);
        }

        if (tweet.getRetweetedFrom() != null) {
            viewHolder.retweetText.setText(tweet.getRetweetedFrom().getName() + " retweeted");
            viewHolder.retweetText.setVisibility(View.VISIBLE);
            viewHolder.retweetIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.retweetText.setVisibility(View.GONE);
            viewHolder.retweetIcon.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void resizeImageView(ImageView imageView, boolean forImage) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = forImage ? (int) getContext().getResources().getDimension(R.dimen.preview_image_height) : 0;
        imageView.setLayoutParams(params);
    }



    private String getRelativeTimeAgo(long millis) {
        return prettyTime.format(new Date(millis));
    }


}
