package com.kjsce.camplus.Search;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Mayur on 12-02-2018.
 */

//This class is used to store PostList items
public class User implements Parcelable{

    private static final String TAG = "User";

    //declaring variables
    private int userId;
    private String userName;

    //constructor
    public User (int userId, String userName) {
        Log.d(TAG, "User: constructor");

        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {

        return userId;
    }

    public String getUserName() {
        return userName;
    }

    protected User(Parcel in) {

        userId = in.readInt();
        userName = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(userId);
        dest.writeString(userName);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
