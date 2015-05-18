package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by teddywyly on 5/18/15.
 */


public class Tweet {

    private String body;
    private long uid;
    private User user;
    private String createdAt;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                JSONObject tweetJSON = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJSON);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return tweet;
    }

    public void Tweet(JSONObject json) {

    }

}
