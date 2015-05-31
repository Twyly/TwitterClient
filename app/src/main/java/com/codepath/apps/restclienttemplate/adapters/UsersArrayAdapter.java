package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.views.HTMLTextDisplay;

import java.util.List;

/**
 * Created by teddywyly on 5/31/15.
 */
public class UsersArrayAdapter extends ArrayAdapter<User> {

    private class ViewHolder {

    }

    private HTMLTextDisplay formatter;

    public UsersArrayAdapter(Context context, List<User> objects) {
        super(context, 0, objects);
        this.formatter = new HTMLTextDisplay(context.getResources());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        return convertView;
    }
}
