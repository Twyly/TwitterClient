package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.EndlessTweetScrollListener;
import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.NetworkListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.activities.DetailTweetActivity;
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

    private TwitterClient client;
    private User currentUser;
    private ProfileHeaderFragment fragmentProfile;
    private Drawable actionBarBackgroundDrawable;

    private NetworkListener networkListener;

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    public static UserTimelineFragment newInstance(String screenname) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenname", screenname);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        loadCachedTweets();
        fetchTweets(0);
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
                final int headerHeight = header.getHeight() - ((AppCompatActivity) getActivity()).getSupportActionBar().getHeight();
                Log.d("HEIGHT", headerHeight + "");
                Log.d("HEIGHT", header.getTop() + "");
//                final float ratio = (float) Math.min(Math.max(header.getTop(), 0), headerHeight) / headerHeight;
//                final int newAlpha = (int) (ratio * 255);
//                actionBarBackgroundDrawable.setAlpha(255);
//                float scalingFactor = 0.51f; // scale down to half the size
//                if (scalingFactor > 0.5f) {
//                    fragmentProfile.scaleProfileImage(scalingFactor);
//                }
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });

        return v;
    }

    private void updateActionBarFromImageView() {

    }

    private void showFollowersActivity(User user, boolean forFollowers) {
        Intent i = new Intent(getActivity(), FollowersActivity.class);
        i.putExtra("user", user);
        i.putExtra("forFollowers", forFollowers);
        startActivity(i);
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Networking
    private void fetchTweets(final long maxID) {

        if (!client.isNetworkAvailable()) {
            ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.NETWORK);
//            swipeContainer.setRefreshing(false);
            setRefreshing(false);

            return;
        }

        if (currentUser == null) {
            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    currentUser = User.fromJSON(response);
                    fragmentProfile.setUser(currentUser);
                    Log.d("DEBUG", "SETTING USER");
                    fetchTweets(maxID);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
                    //swipeContainer.setRefreshing(false);
                    setRefreshing(false);
                }
            });
        } else {
            client.getUserTimeline(currentUser.getScreenName(), maxID, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());
                    if (maxID <= 0) {
                        if (networkListener != null) {
                            networkListener.finishedInitialLoad(true);
                        }
                        //deleteCachedTweetsAndUsers();
                        clearAll();
                    }
                    ArrayList<Tweet> newTweets = Tweet.saveFromJSONArray(json, tweetsCacheName());

                    if (!newTweets.isEmpty()) {
                        Log.d("NEW TWEETS", newTweets.toString());
                        addAll(newTweets);
                    }
//                    swipeContainer.setRefreshing(false);
                    setRefreshing(false);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
//                    swipeContainer.setRefreshing(false);
                    setRefreshing(false);
                }

            });
        }
    }

//    private void deleteCachedTweetsAndUsers() {
//        new Delete().from(Tweet.class).execute();
//        new Delete().from(User.class).execute();
//    }

    public void showComposeDialog(final Tweet tweet) {

        if (currentUser == null) {

            client.getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    currentUser = User.fromJSON(response);
                    showComposeDialog(tweet);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
                }
            });
        } else {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            ComposeTweetDialog composeDialog;
            if (tweet != null) {
                composeDialog = ComposeTweetDialog.newInstance(currentUser, tweet);
            } else {
                composeDialog = ComposeTweetDialog.newInstance(currentUser);
            }
            composeDialog.show(fm, "fragment_compose_tweet");
        }
    }

    @Override
    public String tweetsCacheName() {
        return null;
    }

    private void loadCachedTweets() {
//        List<Tweet> cachedTweets = new Select().from(Tweet.class)
//                .where("cache_name = ?", tweetsCacheName())
//                .orderBy("timestamp DESC").limit(100).execute();
//        addAll(cachedTweets);
        List<Tweet> cachedTweets = new Select().from(Tweet.class)
                .orderBy("timestamp DESC").limit(100).execute();
        addAll(cachedTweets);
    }
}
