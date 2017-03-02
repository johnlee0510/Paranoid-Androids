package com.example.olivia.myapplication.model;

/**
 * Created by Shuopeng Zhou on 3/1/2017.
 * Enums for Waterquality
 */

public enum waterQuality {
    SAFE("SAFE"),
    TREATABLE("TREATABLE"),
    UNTREATABLE("UNTREATABLE");

    private String waterQuality;

    /**
     *  returns the waterQuality of A REPORT
     * @param waterQuality an enum waterQuality that
     */
     waterQuality(String waterQuality) {
        this.waterQuality = waterQuality;
    }
}
