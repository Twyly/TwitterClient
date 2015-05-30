package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.activeandroid.query.Delete;
import com.codepath.apps.restclienttemplate.EndlessTweetScrollListener;
import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.activities.DetailTweetActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by teddywyly on 5/28/15.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;
    private User currentUser;
    private ProfileHeaderFragment fragmentProfile;

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
        fetchTweets(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        View header = inflater.inflate(R.layout.header_profile, container, false);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        fragmentProfile = new ProfileHeaderFragment();
        ft.replace(R.id.flContainer, fragmentProfile);
        ft.commit();
        Log.d("DEBUG", fragmentProfile.toString());
        getLvTweets().addHeaderView(header);

        getLvTweets().setOnScrollListener(new EndlessTweetScrollListener(10) {
            @Override
            public void onLoadMore(long maxID, int totalItemsCount) {
                fetchTweets(maxID);
            }

//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                super.onScrollStateChanged(view, scrollState);
//                Log.d("OFFSET", view.getScrollY() + "");
//
//            }
//
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                // Resize ImageView
                //Log.d("OFFSET", getTotalScrollDistance() + "");

            }
        });

        return v;
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
                        deleteCachedTweetsAndUsers();
                        clearAll();
                    }
                    ArrayList<Tweet> newTweets = Tweet.saveFromJSONArray(json, tweetsCacheName());

                    if (!newTweets.isEmpty()) {
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

    private void deleteCachedTweetsAndUsers() {
        new Delete().from(Tweet.class).execute();
        new Delete().from(User.class).execute();
    }

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
}
