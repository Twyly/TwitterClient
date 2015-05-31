package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.ErrorHelper;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.adapters.UsersArrayAdapter;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.networking.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by teddywyly on 5/31/15.
 */
public class FollowersFragment extends Fragment {

    private UsersArrayAdapter aUsers;
    private ArrayList<User> users;
    private ListView lvUsers;
    private SwipeRefreshLayout swipeContainer;
    private ProgressBar pbProgress;

    private TwitterClient client;

    public User user;
    public boolean forFollowers;


    public static FollowersFragment newInstance(User user, boolean forFollowers) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        args.putBoolean("forFollowers", forFollowers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        users = new ArrayList<User>();
        aUsers = new UsersArrayAdapter(getActivity(), users);

        user = getArguments().getParcelable("user");
        forFollowers = getArguments().getBoolean("forFollowers");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_followers_list, container, false);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvUsers = (ListView) v.findViewById(R.id.lvUsers);
        lvUsers.setAdapter(aUsers);
        pbProgress = (ProgressBar) v.findViewById(R.id.pbProgress);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //fetchTweets(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        fetchUsers(0);
        setupTeardownForInitialLoad(true);

        return v;
    }


    private void fetchUsers(long cursor) {
        if (forFollowers) {
            client.getUserFollowers(user.getScreenName(), cursor, userResponseHandler(cursor));
        } else {
            client.getUserFollowing(user.getScreenName(), cursor, userResponseHandler(cursor));
        }
    }

    private JsonHttpResponseHandler userResponseHandler(final long cursor) {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (cursor == 0) {
                    aUsers.clear();
                    setupTeardownForInitialLoad(false);
                }

                Log.d("JSON RESPONSE", response.toString());
                try {
                    JSONArray userJSON = response.getJSONArray("users");
                    long nextCursor = response.getLong("next_cursor");
                    ArrayList<User> newUsers = User.fromJSONArray(userJSON);
                    if (!newUsers.isEmpty()) {
                        aUsers.addAll(newUsers);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
                }

                swipeContainer.setRefreshing(false);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                ErrorHelper.showErrorAlert(getActivity(), ErrorHelper.ErrorType.GENERIC);
                swipeContainer.setRefreshing(false);
            }
        };
    }

    public void setupTeardownForInitialLoad(boolean isStartup) {
        if (isStartup) {
            lvUsers.setVisibility(View.INVISIBLE);
            pbProgress.setVisibility(View.VISIBLE);
        } else {
            lvUsers.setVisibility(View.VISIBLE);
            pbProgress.setVisibility(View.INVISIBLE);
        }
    }
}
