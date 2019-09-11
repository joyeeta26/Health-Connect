package com.example.joyeeta.easyHealth;

/**
 * Created by Joyeeta on 9/9/2019.
 */

public class Organ {
    private static final int NO_IMAGE_PROVIDED = -1;
    private String mOrgan_name;
    private String mDescription;
    private String mOrganDonateCenter;
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    public Organ(String organ_name, int imageResourceId, String description) {
        mOrgan_name = organ_name;
        mImageResourceId = imageResourceId;
        mDescription = description;
    }

    public String getOrgan_name() {
        return mOrgan_name;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    public String getmDescription() {
        return mDescription;
    }

}
