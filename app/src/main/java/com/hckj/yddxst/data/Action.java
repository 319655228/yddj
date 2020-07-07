package com.hckj.yddxst.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Richie on 2019.03.27
 */
public class Action extends EntityParcelable {
    public static final Parcelable.Creator<Action> CREATOR = new Parcelable.Creator<Action>() {
        @Override
        public Action createFromParcel(Parcel source) {
            return new Action(source);
        }

        @Override
        public Action[] newArray(int size) {
            return new Action[size];
        }
    };

    public Action(Parcel source) {
        this.name = source.readString();
        this.icon = source.readString();
        this.path = source.readString();
    }

    private String icon;
    private String path;

    public Action(String name, String icon, String path) {
        this.name = name;
        this.icon = icon;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.path);
    }
}
