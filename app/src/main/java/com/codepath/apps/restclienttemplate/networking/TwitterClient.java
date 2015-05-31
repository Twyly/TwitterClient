package com.codepath.apps.restclienttemplate.networking;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "lvze5ROKO8CkhwuCXzFu4VNXM";       // Change this
	public static final String REST_CONSUMER_SECRET = "bsBRj3elfK0XflREA97SBifLK5XwxgBImPS4mEcJ7SwCwnpYAj"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptwitterclient"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}

	// Method == Endpoint

    // Timeline
    public void getHomeTimeline(long maxID, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        if (maxID > 0) {
            params.put("max_id", maxID);
        }
        getClient().get(apiURL, params, handler);
    }

   public void getMentionsTimeline(long maxID, AsyncHttpResponseHandler handler) {
       String apiURL = getApiUrl("statuses/mentions_timeline.json");
       RequestParams params = new RequestParams();
       params.put("count", 25);
       params.put("since_id", 1);
       if (maxID > 0) {
           params.put("max_id", maxID);
       }
       getClient().get(apiURL, params, handler);
   }

    public void getUserTimeline(String screenname, long maxID, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screenname", screenname);
        params.put("since_id", 1);
        if (maxID > 0) {
            params.put("max_id", maxID);
        }
        getClient().get(apiURL, params, handler);
    }

    public void getUserFollowers(String screenname, long cursor, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screenname", screenname);
        if (cursor > 0) {
            params.put("max_id", cursor);
        }
        getClient().get(apiURL, params, handler);
    }

    public void getUserFollowing(String screenname, long cursor, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screenname", screenname);
        if (cursor > 0) {
            params.put("cursor", cursor);
        }
        getClient().get(apiURL, params, handler);
    }

    public void tweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        getClient().post(apiURL, params, handler);
    }

    public void reply(String tweet, long replyToID, AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        params.put("in_reply_to_status_id", replyToID);
        getClient().post(apiURL, params, handler);
    }

	// Verify Credentials
	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		String apiURL = getApiUrl("account/verify_credentials.json");
		getClient().get(apiURL, null, handler);
	}

    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public Boolean isOnline() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}