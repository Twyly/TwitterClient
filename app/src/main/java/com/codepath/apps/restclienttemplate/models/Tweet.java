package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by teddywyly on 5/18/15.
 */

@Table(name = "tweets")
public class Tweet extends Model implements Parcelable {

    @Column(name = "body")
    private String body;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "created_at", index = true)
    private String createdAt;
    @Column(name = "retweet_count")
    private int retweetCount;
    @Column(name = "favorite_count")
    private int favoriteCount;

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
    public int getFavoriteCount() {
        return favoriteCount;
    }
    public int getRetweetCount() {
        return retweetCount;
    }

    public Tweet() {
        super();
    }

    public static ArrayList<Tweet> saveFromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for (int i=0; i<jsonArray.length(); i++) {
            try {
                JSONObject tweetJSON = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.createAndSaveFromJSON(tweetJSON);
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

    public static Tweet createAndSaveFromJSON(JSONObject json) {
        Tweet tweet = Tweet.fromJSON(json);
        if (tweet.uid != 0) {
            tweet.save();
        }
        return tweet;
    }

    public static Tweet fromJSON(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.findOrCreateFromJSON(json.getJSONObject("user"));
            tweet.retweetCount = json.getInt("retweet_count");
            tweet.favoriteCount = json.getInt("favorite_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
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
        dest.writeInt(retweetCount);
        dest.writeInt(favoriteCount);
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
        retweetCount = in.readInt();
        favoriteCount = in.readInt();
    }

}
