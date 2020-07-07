package com.hckj.yddxst.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author luteng on 2020/1/8 15:40
 */
public class Effect extends EntityParcelable {
    public static final Parcelable.Creator<Effect> CREATOR = new Parcelable.Creator<Effect>() {
        @Override
        public Effect createFromParcel(Parcel source) {
            return new Effect(source);
        }

        @Override
        public Effect[] newArray(int size) {
            return new Effect[size];
        }
    };

    public Effect(Parcel source) {
        super(source);
        this.effectID = source.readString();
        this.icon = source.readString();
        this.gender = source.readInt();
        this.bundlePath = source.createStringArrayList();
        this.defaultAnimPath = source.createTypedArrayList(Action.CREATOR);
        this.animPath = source.createTypedArrayList(Action.CREATOR);
        this.bsConfigPath = source.readString();
        this.lightPath = source.readString();
        this.backgroundPath = source.readString();
        this.coordinate = source.readParcelable(Coordinate.class.getClassLoader());
        this.coordinateHalf = source.readParcelable(Coordinate.class.getClassLoader());
        this.speakerCode = source.readString();
        this.emotionCode = source.readInt();
    }

    public Effect(String name, String effectID, String icon, int gender,
                  String avatarPath, List<String> bundlePath, List<Action> defaultAnimPath, List<Action> animPath, String bsConfigPath, String lightPath, String backgroundPath,
                  Coordinate coordinate, Coordinate coordinateHalf, String speakerCode, int emotionCode) {
        this.name = name;
        this.effectID = effectID;
        this.icon = icon;
        this.gender = gender;
        this.avatarPath = avatarPath;
        this.bundlePath = bundlePath;
        this.defaultAnimPath = defaultAnimPath;
        this.animPath = animPath;
        this.bsConfigPath = bsConfigPath;
        this.lightPath = lightPath;
        this.backgroundPath = backgroundPath;
        this.coordinate = coordinate;
        this.coordinateHalf = coordinateHalf;
        this.speakerCode = speakerCode;
        this.emotionCode = emotionCode;
    }

    private String effectID;
    private String icon;
    private int gender;
    private String avatarPath;
    private List<String> bundlePath;
    private List<Action> defaultAnimPath;
    private List<Action> animPath;
    private String bsConfigPath;
    private String lightPath;
    private String backgroundPath;
    private Coordinate coordinate;
    private Coordinate coordinateHalf;
    private String speakerCode;
    private int emotionCode;

    public String getEffectID() {
        return effectID;
    }

    public void setEffectID(String effectID) {
        this.effectID = effectID;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getBsConfigPath() {
        return bsConfigPath;
    }

    public void setBsConfigPath(String bsConfigPath) {
        this.bsConfigPath = bsConfigPath;
    }

    public String getLightPath() {
        return lightPath;
    }

    public void setLightPath(String lightPath) {
        this.lightPath = lightPath;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinateHalf() {
        return coordinateHalf;
    }

    public void setCoordinateHalf(Coordinate coordinateHalf) {
        this.coordinateHalf = coordinateHalf;
    }

    public String getSpeakerCode() {
        return speakerCode;
    }

    public void setSpeakerCode(String speakerCode) {
        this.speakerCode = speakerCode;
    }

    public int getEmotionCode() {
        return emotionCode;
    }

    public void setEmotionCode(int emotionCode) {
        this.emotionCode = emotionCode;
    }

    public List<String> getBundlePath() {
        return bundlePath;
    }

    public void setBundlePath(List<String> bundlePath) {
        this.bundlePath = bundlePath;
    }

    public List<Action> getDefaultAnimPath() {
        return defaultAnimPath;
    }

    public void setDefaultAnimPath(List<Action> defaultAnimPath) {
        this.defaultAnimPath = defaultAnimPath;
    }

    public List<Action> getAnimPath() {
        return animPath;
    }

    public void setAnimPath(List<Action> animPath) {
        this.animPath = animPath;
    }

    @Override
    public String toString() {
        return "EffectNew{" +
                "name='" + name + '\'' +
                ", effectID='" + effectID + '\'' +
                ", icon='" + icon + '\'' +
                ", gender=" + gender +
                ", avatarPath='" + avatarPath + '\'' +
                ", bundlePath=" + bundlePath +
                ", defaultAnimPath=" + defaultAnimPath +
                ", animPath=" + animPath +
                ", bsConfigPath='" + bsConfigPath + '\'' +
                ", lightPath='" + lightPath + '\'' +
                ", backgroundPath='" + backgroundPath + '\'' +
                ", coordinate=" + coordinate +
                ", coordinateHalf=" + coordinateHalf +
                ", speakerCode='" + speakerCode + '\'' +
                ", emotionCode=" + emotionCode +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.effectID);
        dest.writeString(this.icon);
        dest.writeInt(this.gender);
        dest.writeString(this.avatarPath);
        dest.writeStringList(this.bundlePath);
        dest.writeTypedList(this.defaultAnimPath);
        dest.writeTypedList(this.animPath);
        dest.writeString(this.bsConfigPath);
        dest.writeString(this.lightPath);
        dest.writeString(this.backgroundPath);
        dest.writeParcelable(this.coordinate,0);
        dest.writeParcelable(this.coordinateHalf,0);
        dest.writeString(this.speakerCode);
        dest.writeInt(this.emotionCode);
    }
}
