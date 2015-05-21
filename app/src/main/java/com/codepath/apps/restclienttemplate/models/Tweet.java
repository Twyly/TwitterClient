package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by teddywyly on 5/18/15.
 */


public class Tweet implements Parcelable {

    private String body;
    private long uid;
    private User user;
    private String createdAt;
    private String profileImage;

    public String getProfileImage() {
        return profileImage;
    }
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
            tweet.profileImage = json.getString("profile_image_url_https");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return tweet;
    }

    public void Tweet(JSONObject json) {

    }

    // Parcelable Implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(body);
        dest.writeLong(uid);
        dest.writeParcelable(user, flags);
        dest.writeString(createdAt);
        dest.writeString(profileImage);
    }

    public static final Parcelable.Creator<Tweet> CREATOR
            = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel in) {
            return new Tweet(in);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    private Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();
        profileImage = in.readString();
    }

    public Tweet() {
        //normal actions performed by class, it's still a normal object!
    }
}
