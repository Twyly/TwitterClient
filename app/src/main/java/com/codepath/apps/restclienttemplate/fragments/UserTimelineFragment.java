package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.EndlessTweetScrollListener;
import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.activities.FollowersActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teddywyly on 5/28/15.
 */
public class UserTimelineFragment extends TweetsListFragment {


    private User user;

    private ProfileHeaderFragment fragmentProfile;
    private Drawable actionBarBackgroundDrawable;

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        final View header = inflater.inflate(R.layout.header_profile, container, false);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        fragmentProfile = new ProfileHeaderFragment();

        fragmentProfile.setListener(new ProfileHeaderFragment.ProfileHeaderFragmentListener() {
            @Override
            public void FollowingClicked(View v, User user) {
                showFollowersActivity(user, false);
            }

            @Override
            public void FollowersClicked(View v, User user) {
                showFollowersActivity(user, true);
            }
        });

        ft.replace(R.id.flContainer, fragmentProfile);
        ft.commit();
        getLvTweets().addHeaderView(header);

        fetchTweets(0);


//        actionBarBackgroundDrawable = getResources().getDrawable(R.drawable.abc_btn_radio_material);
//        actionBarBackgroundDrawable.setAlpha(0);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(actionBarBackgroundDrawable);

        getLvTweets().setOnScrollListener(new EndlessTweetScrollListener(10) {
            @Override
            public void onLoadMore(long maxID, int totalItemsCount) {
                fetchTweets(maxID);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final float headerHeight = header.getHeight() - ((AppCompatActivity) getActivity()).getSupportActionBar().getHeight();
                Log.d("HEIGHT", headerHeight + "");
                Log.d("HEIGHT", header.getTop() + "");

                float head = header.getTop() * -1;
                float base = (float)(headerHeight / 2.5);
                float scalingFactor = 1 - (head/base);

                if (scalingFactor > 0.5f) {
                    fragmentProfile.scaleProfileImage(scalingFactor);
                }

//                final float ratio = (float) Math.min(Math.max(header.getTop(), 0), headerHeight) / headerHeight;
//                final int newAlpha = (int) (ratio * 255);
//                actionBarBackgroundDrawable.setAlpha(255);

                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });

        return v;


//        Log.d("SCREENNAME", user.getScreenName());
//
//        if (!loadCachedTweets(user.getScreenName())) {
//            setupTeardownForInitialLoad(true);
//        }
//
//        //fetchTweets(0);
//
//        return v;
    }

    private void updateActionBarFromImageView() {

    }

    @Override
    protected void onSuccessfulFetch() {
        super.onSuccessfulFetch();
        fragmentProfile.setUser(user);
    }

    private void showFollowersActivity(User user, boolean forFollowers) {
        Intent i = new Intent(getActivity(), FollowersActivity.class);
        i.putExtra("ParentClass", getActivity().getClass());
        i.putExtra("user", user);
        i.putExtra("forFollowers", forFollowers);
        startActivity(i);
    }

//    public static Bitmap loadBitmapFromView(View v) {
//        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(b);
//        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
//        v.draw(c);
//        return b;
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    // Networking
//    private void fetchTweets(final long maxID) {
//
//        if (!getClient().isNetworkAvailable()) {
//            ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.NETWORK);
////            swipeContainer.setRefreshing(false);
//            setRefreshing(false);
//
//            return;
//        }
//
//        if (currentUser == null) {
//            getClient().getCurrentUser(new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    currentUser = User.fromJSON(response);
//                    fetchTweets(maxID);
//                }
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
//                    //swipeContainer.setRefreshing(false);
//                    setRefreshing(false);
//                }
//            });
//        } else {
//            getClient().getUserTimeline(user.getScreenName(), maxID, new JsonHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                    Log.d("DEBUG", json.toString());
//                    Log.d("MAX_ID", maxID + "");
//                    if (maxID <= 0) {
//                        cacheTweets(json, user.getScreenName());
//                        setupTeardownForInitialLoad(false);
//                        clearAll();
//                    }
//                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);
//
//                    if (!newTweets.isEmpty()) {
//                        Log.d("NEW TWEETS", newTweets.toString());
//                        addAll(newTweets);
//                        fragmentProfile.setUser(user);
//                    }
//                    setRefreshing(false);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
////                    swipeContainer.setRefreshing(false);
//                    setRefreshing(false);
//                }
//
//            });
//        }
//    }

    @Override
    protected String cacheName() {
        return null;
    }

    @Override
    protected TwitterClient.TweetSearchType fetchType() {
        return TwitterClient.TweetSearchType.USER_TIMELINE;
    }

    @Override
    protected String username() {
        return user.getScreenName();
    }
}
