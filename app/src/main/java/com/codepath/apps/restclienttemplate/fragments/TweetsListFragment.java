package com.codepath.apps.restclienttemplate.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.activeandroid.query.Select;
import com.codepath.apps.restclienttemplate.EndlessTweetScrollListener;
import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.activities.DetailTweetActivity;
import com.codepath.apps.restclienttemplate.activities.ProfileActivity;
import com.codepath.apps.restclienttemplate.activities.TimelineActivity;
import com.codepath.apps.restclienttemplate.adapters.TweetsArrayAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by teddywyly on 5/28/15.
 */
public abstract class TweetsListFragment extends Fragment implements ComposeTweetDialog.ComposeTweetDialogListener {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar pbProgress;
    private ProgressBar pbFooter;

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

    public ProgressBar getPbProgress() {
        return pbProgress;
    }

    protected String cacheName() {
        return null;
    };

    protected String username() {
        return null;
    }
    protected void onSuccessfulFetch() {

    }
    protected void onFailedFetch() {

    }
    protected abstract TwitterClient.TweetSearchType fetchType();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        setupViewsAndAdapter(v, savedInstanceState);

        if (!loadCachedTweets(cacheName())) {
            setupTeardownForInitialLoad(true);
        }
        User cachedUser = getCachedCurrentUser();
        if (cachedUser != null) {
            currentUser = cachedUser;
        }

        //fetchTweets(0);
        return v;

    }

    private void setupViewsAndAdapter(View v, Bundle savedInstanceState) {

        // Add twitter bird
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        pbProgress = (ProgressBar) v.findViewById(R.id.pbProgress);
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
                fetchTweets(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);
        getLvTweets().setOnScrollListener(new EndlessTweetScrollListener(10) {
            @Override
            public void onLoadMore(long maxID, int totalItemsCount) {
                Log.d("LOAD MORE", maxID + "");
                fetchTweets(maxID);
            }
        });
        getPbProgress().setVisibility(View.INVISIBLE);

        View footer = getLayoutInflater(savedInstanceState).inflate(R.layout.footer_progress, null);
        pbFooter = (ProgressBar)footer.findViewById(R.id.pbFooter);
        lvTweets.addFooterView(footer);
        lvTweets.setAdapter(aTweets);
    }

    private void setFooterProgressVisable(boolean visable) {
        if (visable) {
            pbFooter.setVisibility(View.VISIBLE);
        } else {
            pbFooter.setVisibility(View.GONE);
        }
    }

    public void showComposeDialog(final Tweet tweet) {

        if (getCurrentUser() == null) {
            getClient().getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    cacheUserJSON(response);
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

    private void showProfileActivity(User user) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("ParentClass", getActivity().getClass());
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void setupTeardownForInitialLoad(boolean isStartup) {
        if (isStartup) {
            getLvTweets().setVisibility(View.INVISIBLE);
            getPbProgress().setVisibility(View.VISIBLE);
        } else {
            getLvTweets().setVisibility(View.VISIBLE);
            getPbProgress().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets, new TweetsArrayAdapter.TweetsArrayAdapterListener() {
            @Override
            public void replyClicked(Tweet tweet) {
                showComposeDialog(tweet);
            }
            public void profileClicked(User user) {
                showProfileActivity(user);
            }
            public void retweetClicked(Tweet tweet) { retweet(tweet); }
            public void favoriteClicked(Tweet tweet) { favorite(tweet); }
        });
    }

    private void retweet(Tweet tweet) {
        if (tweet.isRetweeted()) {
            final CharSequence[] items = {"Undo retweet"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        } else {
            final CharSequence[] items = {"Retweet"};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        }
    }

    private void favorite(final Tweet tweet) {
        if (tweet.isFavorited()) {
            tweet.setFavorited(false);
            aTweets.notifyDataSetChanged();
            getClient().unfavorite(tweet.getUid(), new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    tweet.setFavorited(true);
                    aTweets.notifyDataSetChanged();
                }
            });
        } else {
            tweet.setFavorited(true);
            aTweets.notifyDataSetChanged();
            getClient().favorite(tweet.getUid(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    tweet.setFavorited(false);
                    aTweets.notifyDataSetChanged();
                }
            });
        }
    }

    // Networking
    public void fetchTweets(final long maxID) {

        if (!getClient().isNetworkAvailable()) {
            ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.NETWORK);
            setRefreshing(false);
            return;
        }

        if (getCurrentUser() == null) {
            getClient().getCurrentUser(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    cacheUserJSON(response);
                    currentUser = User.fromJSON(response);
                    fetchTweets(maxID);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("ERROR", errorResponse.toString());
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
                    setRefreshing(false);
                }
            });
        } else {

            if (maxID > 0) {
                setFooterProgressVisable(true);
            }

            getClient().getTweets(fetchType(), maxID, username(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    Log.d("DEBUG", json.toString());
                    if (maxID <= 0) {
                        cacheTweets(json, cacheName());
                        setupTeardownForInitialLoad(false);
                        clearAll();
                    }
                    ArrayList<Tweet> newTweets = Tweet.fromJSONArray(json);

                    if (!newTweets.isEmpty()) {
                        addAll(newTweets);
                    }
                    setRefreshing(false);
                    setFooterProgressVisable(false);
                    onSuccessfulFetch();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("ERROR", errorResponse.toString());
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
                    setRefreshing(false);
                    setFooterProgressVisable(true);
                    onFailedFetch();
                }
            });
        }
    }


    public void onFinishComposeTweet(Tweet tweet, Tweet replytTo) {
        if (replytTo == null) {
            aTweets.insert(tweet, 0);
        }
    }

    //================================================================================
    // Caching
    //================================================================================

    private User getCachedCurrentUser() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String jsonString = pref.getString("current_user", "");
        if (jsonString.equals("")) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(jsonString);
            return User.fromJSON(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void cacheUserJSON(JSONObject json) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("current_user", json.toString());
        edit.commit();
    }

    protected boolean loadCachedTweets(String cacheName) {
        if (cacheName == null) {
            return false;
        }

        JSONArray cachedArray = getCachedJSONResponse(cacheName);
        if (cachedArray == null) {
            return false;
        } else {
            List<Tweet> cachedTweets = Tweet.fromJSONArray(cachedArray);
            addAll(cachedTweets);
            return true;
        }
    }

    protected void cacheTweets(JSONArray jsonArray, String cacheName) {
        if (cacheName == null) {
            return;
        }
        cacheJSONResponse(jsonArray, cacheName);
    }

    private void cacheJSONResponse(JSONArray json, String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, json.toString());
        edit.commit();
    }

    private JSONArray getCachedJSONResponse(String key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String jsonString = pref.getString(key, "");
        if (jsonString.equals("")) {
            return null;
        }
        try {
            return new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
