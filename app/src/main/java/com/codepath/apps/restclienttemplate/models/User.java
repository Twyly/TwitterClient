package com.codepath.apps.restclienttemplate.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by teddywyly on 5/18/15.
 */

@Table(name = "users")
public class User extends Model implements Parcelable {

    @Column(name = "name")
    private String name;
    @Column(name = "uid", unique = true)
    private long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;
    @Column(name = "tagline")
    private String tagline;
    @Column(name = "followers_count")
    private int followerCount;
    @Column(name = "following_count")
    private int followingCount;
    @Column(name = "background_image_url")
    private String backgroundImageUrl;


    public String getName() {
        return name;
    }
    public long getUid() {
        return uid;
    }
    public String getScreenName() {
        return screenName;
    }
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    public String getTagline() {
        return tagline;
    }
    public int getFollowerCount() {
        return followerCount;
    }
    public int getFollowingCount() {
        return followingCount;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public static User findOrCreateFromJSON(JSONObject json) {
        User user = null;
        try {
            long unique = json.getLong("id");
            user = new Select().from(User.class).where("uid = ?" , unique).executeSingle();
            if (user == null) {
                user = User.fromJSON(json);
                user.save();
            }
        } catch (JSONException e) {
            // Hanlde JSONException
        }
        return user;
    }

    public static User fromJSON(JSONObject json) {
        User user = new User();
        Log.d("USER", json.toString());
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.tagline = json.getString("description");
            user.followerCount = json.getInt("followers_count");
            user.followingCount = json.getInt("friends_count");
            user.backgroundImageUrl = json.getString("profile_background_image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<Tweet> items() {
        return getMany(Tweet.class, "user");
    }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(uid);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
        dest.writeString(tagline);
        dest.writeInt(followerCount);
        dest.writeInt(followingCount);
        dest.writeString(backgroundImageUrl);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        name = in.readString();
        uid = in.readLong();
        screenName = in.readString();
        profileImageUrl = in.readString();
        tagline = in.readString();
        followerCount = in.readInt();
        followingCount = in.readInt();
        backgroundImageUrl = in.readString();
    }

    public User() {
        //normal actions performed by class, it's still a normal object!
    }
}
