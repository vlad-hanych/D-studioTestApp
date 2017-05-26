package com.foxy_corporation.d_studiotestapp.model.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vlad on 26.05.2017.
 */

public class UserData implements Parcelable {
    private String username;

    private String last_login;

    public UserData (String username, String lastLogin) {
        this.username = username;
        this.last_login = lastLogin;
    }

    protected UserData(Parcel in) {
        username = in.readString();
        last_login = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getLast_login() {
        return last_login;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.last_login);
    }
}
