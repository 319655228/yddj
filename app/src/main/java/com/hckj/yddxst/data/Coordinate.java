package com.hckj.yddxst.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author luteng on 2020/1/8 16:31
 */
public class Coordinate implements Parcelable {
    public static final Creator<Coordinate> CREATOR = new Creator<Coordinate>() {
        @Override
        public Coordinate createFromParcel(Parcel source) {
            return new Coordinate(source);
        }

        @Override
        public Coordinate[] newArray(int size) {
            return new Coordinate[size];
        }
    };

    public Coordinate(Parcel source) {
        this.fixX = source.readFloat();
        this.fixY = source.readFloat();
        this.fixZ = source.readFloat();
    }

    public Coordinate(float fixX, float fixY, float fixZ) {
        this.fixX = fixX;
        this.fixY = fixY;
        this.fixZ = fixZ;
    }

    private float fixX;
    private float fixY;
    private float fixZ;

    public float getFixX() {
        return fixX;
    }

    public void setFixX(float fixX) {
        this.fixX = fixX;
    }

    public float getFixY() {
        return fixY;
    }

    public void setFixY(float fixY) {
        this.fixY = fixY;
    }

    public float getFixZ() {
        return fixZ;
    }

    public void setFixZ(float fixZ) {
        this.fixZ = fixZ;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "fixX=" + fixX +
                ", fixY=" + fixY +
                ", fixZ=" + fixZ +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.fixX);
        dest.writeFloat(this.fixY);
        dest.writeFloat(this.fixZ);
    }
}
