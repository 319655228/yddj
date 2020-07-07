package com.hckj.yddxst.data;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Richie on 2019.03.26
 */
@SuppressLint("ParcelCreator")
public class EntityWithNameId implements Parcelable {
    protected int nameId;

    public EntityWithNameId() {
    }

    protected EntityWithNameId(Parcel in) {
        this.nameId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nameId);
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

}
