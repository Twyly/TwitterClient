package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.codepath.apps.restclienttemplate.views.HTMLTextDisplay;
import com.codepath.apps.restclienttemplate.views.ProfileImageHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by teddywyly on 5/29/15.
 */
public class ProfileHeaderFragment extends Fragment {

    private ImageView ivProfile;
    private ImageView ivProfileBackground;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvTagline;
    private Button btnFollowing;
    private Button btnFollowers;

    private HTMLTextDisplay formatter;


    public void setUser(User user) {
        updateViewsFromUser(user);
    }

    private void updateViewsFromUser(User user) {
        tvUsername.setText(formatter.usernameSpanned(user.getName()));
        tvScreenname.setText(formatter.screenameSpanned(user.getScreenName()));
        tvTagline.setText(user.getTagline());
        btnFollowing.setText(formatter.statSpanned(getString(R.string.following_label), user.getFollowingCount(), false));
        btnFollowers.setText(formatter.statSpanned(getString(R.string.followers_label), user.getFollowerCount(), true));
        ivProfile.setImageResource(0);
        ivProfileBackground.setImageResource(0);
        Picasso.with(getActivity()).load(user.getProfileImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(ivProfile);
        Picasso.with(getActivity()).load(user.getBackgroundImageUrl()).fit().transform(ProfileImageHelper.roundTransformation()).into(ivProfileBackground);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);
        ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        ivProfileBackground = (ImageView) view.findViewById(R.id.ivProfileBackground);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvScreenname = (TextView) view.findViewById(R.id.tvScreenname);
        tvTagline = (TextView) view.findViewById(R.id.tvTagline);
        btnFollowing = (Button) view.findViewById(R.id.btnFollowing);
        btnFollowers = (Button) view.findViewById(R.id.btnFollowers);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formatter = new HTMLTextDisplay(getActivity().getResources());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
