package com.hckj.yddxst.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Richie on 2019.03.26
 */
public abstract class EntityParcelable implements Parcelable {
    protected String name;
    public EntityParcelable() {
    }

    protected EntityParcelable(Parcel in) {
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
