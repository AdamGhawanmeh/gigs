package com.schoolproject.gigs;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class flashcard implements Parcelable {
    private String frontText,backText;
    private boolean showingBack;

    private Long study;

    public Long getStudy() {
        return study;
    }

    public void setStudy(Long study) {
        this.study = study;
    }

    public boolean isShowingBack() {
        return showingBack;
    }

    public void setShowingBack(boolean showingBack) {
        this.showingBack = showingBack;
    }

    public flashcard(String frontText, String backText) {
        this.frontText = frontText;
        this.backText = backText;
        study = Long.valueOf(0);
    }

    public String getFrontText() {
        return frontText;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public flashcard() {
    }


    public static final Creator<flashcard> CREATOR = new Creator<flashcard>() {
        @Override
        public flashcard createFromParcel(Parcel in) {
            return new flashcard(in);
        }
        @Override
        public flashcard[] newArray(int size) {
            return new flashcard[size];
        }
    };
    protected flashcard(Parcel in) {
        frontText = in.readString();
        backText = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(frontText);
        dest.writeString(backText);
    }
}
