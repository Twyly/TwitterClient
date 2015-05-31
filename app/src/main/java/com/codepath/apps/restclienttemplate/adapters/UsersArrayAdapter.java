package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.views.HTMLTextDisplay;
import com.codepath.apps.restclienttemplate.views.ProfileImageHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by teddywyly on 5/31/15.
 */
public class UsersArrayAdapter extends ArrayAdapter<User> {

    private class ViewHolder {
        ImageButton profile;
        TextView username;
        TextView screenname;
        TextView tagline;
    }

    private HTMLTextDisplay formatter;

    public UsersArrayAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
        this.formatter = new HTMLTextDisplay(context.getResources());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User user = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
            viewHolder.profile = (ImageButton) convertView.findViewById(R.id.ibProfileImage);
            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.screenname = (TextView) convertView.findViewById(R.id.tvScreenname);
            viewHolder.tagline = (TextView) convertView.findViewById(R.id.tvTagline);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(formatter.usernameSpanned(user.getName()));
        viewHolder.screenname.setText(formatter.screenameSpanned(user.getScreenName()));
        viewHolder.tagline.setText(formatter.taglineSpanned(user.getTagline()));

        viewHolder.profile.setImageResource(0);
        Picasso.with(getContext()).load(user.getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(viewHolder.profile);

        return convertView;
    }
}
