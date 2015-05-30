package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.EndlessTweetScrollListener;
import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.activities.DetailTweetActivity;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
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
public abstract class TweetsListFragment extends Fragment implements ComposeTweetDialog.ComposteTweetDialogListener {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;

    private TwitterClient client;
    public User currentUser;

    public ListView getLvTweets() {
        return lvTweets;
    }

    public TwitterClient getClient() {
        return client;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void addAll(List<Tweet> tweets) {
        aTweets.addAll(tweets);
    }
    public void clearAll() {
        aTweets.clear();
    }
    public void setRefreshing(boolean on) {
        swipeContainer.setRefreshing(on);
    }

//    public static TweetsListFragment newInstance(User user) {
//        TweetsListFragment fragment = new TweetsListFragment();
//        Bundle args = new Bundle();
//        args.putParcelable("user", user);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        setupViewsAndAdapter(v);
        return v;

    }

    private void setupViewsAndAdapter(View v) {

        // Add twitter bird

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);

        lvTweets.setAdapter(aTweets);

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet tweet = tweets.get(position);
                showDetaillActivity(tweet);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //fetchTweets(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    public void showComposeDialog(final Tweet tweet) {

        if (getCurrentUser() == null) {

            getClient().getCurrentUser(new JsonHttpResponseHandler() {
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
            FragmentManager fm = getChildFragmentManager(); // getActivity().getSupportFragmentManager();
            ComposeTweetDialog composeDialog;
            if (tweet != null) {
                composeDialog = ComposeTweetDialog.newInstance(getCurrentUser(), tweet);
            } else {
                composeDialog = ComposeTweetDialog.newInstance(getCurrentUser());
            }
            composeDialog.show(fm, "fragment_compose_tweet");
        }
    }

    private void showDetaillActivity(Tweet tweet) {
        Intent intent = new Intent(getActivity(), DetailTweetActivity.class);
        intent.putExtra("tweet", tweet);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    public abstract String tweetsCacheName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets, new TweetsArrayAdapter.TweetsArrayAdapterListener() {
            @Override
            public void replyToTweetClicked(Tweet tweet) {
                showComposeDialog(tweet);
            }
        });
    }


    // ComposeTweetDialogListener
    @Override
    public void onFinishComposeTweet(Tweet tweet) {
        tweet.save();
        aTweets.insert(tweet, 0);
    }

}
