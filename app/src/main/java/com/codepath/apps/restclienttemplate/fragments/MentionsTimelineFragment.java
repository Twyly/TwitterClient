package com.codepath.apps.restclienttemplate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class MentionsTimelineFragment extends TweetsListFragment {

    public static final String CACHE_NAME = "Mentions";

    @Override
    protected String cacheName() {
        return CACHE_NAME;
    }

    @Override
    protected TwitterClient.TweetSearchType fetchType() {
        return TwitterClient.TweetSearchType.MENTIONS_TIMELINE;
    }
}
