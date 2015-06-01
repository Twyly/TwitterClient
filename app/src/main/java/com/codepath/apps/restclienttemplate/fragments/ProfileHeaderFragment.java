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
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.codepath.apps.restclienttemplate.views.HTMLTextDisplay;
import com.codepath.apps.restclienttemplate.views.ProfileImageHelper;
import com.squareup.picasso.Picasso;

/**
 * Created by teddywyly on 5/29/15.
 */
public class ProfileHeaderFragment extends Fragment {

    public interface ProfileHeaderFragmentListener {
        public void FollowingClicked(View v, User user);
        public void FollowersClicked(View v, User user);
    }

    private ImageView ivProfile;
    private ImageView ivProfileBackground;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvTagline;
    private Button btnFollowing;
    private Button btnFollowers;
    private User user;

    private HTMLTextDisplay formatter;
    private ProfileHeaderFragmentListener listener;


    public void setUser(User user) {
        this.user = user;
        updateViewsFromUser(user);
    }

    public void setListener(ProfileHeaderFragmentListener listener) {
        this.listener = listener;
    }

    public void scaleProfileImage(float scalingFactor) {
        if (ivProfile != null) {
            ivProfile.setScaleX(scalingFactor);
            ivProfile.setScaleY(scalingFactor);
        }
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
        Picasso.with(getActivity()).load(user.getBackgroundImageUrl()).placeholder(R.color.theme_text_detail).fit().transform(ProfileImageHelper.roundTransformation()).into(ivProfileBackground);
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

        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.FollowingClicked(v, user);
                }
            }
        });

        btnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.FollowersClicked(v, user);
                }
            }
        });

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
