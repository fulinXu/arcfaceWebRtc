package com.landsky.camera.entity;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.GenderInfo;

public class Person {
    FaceInfo faceInfo;
    int genderInfo;
    int ageInfo;

    public int getGenderInfo() {
        return genderInfo;
    }

    public int getAgeInfo() {
        return ageInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public void setGenderInfo(int genderInfo) {
        this.genderInfo = genderInfo;
    }

    public void setAgeInfo(int ageInfo) {
        this.ageInfo = ageInfo;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    @Override
    public String toString() {
        return "{" +
                "faceInfo=" + faceInfo +
                ", genderInfo=" + genderInfo +
                ", ageInfo=" + ageInfo +
                '}';
    }
}
