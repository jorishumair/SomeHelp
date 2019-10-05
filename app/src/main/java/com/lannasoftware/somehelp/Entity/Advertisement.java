package com.lannasoftware.somehelp.Entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Advertisement{

    private String sAdvertisementIdFirestore;
    private String sAdvertisementCategorie;
    private String sAdvertisementDate;
    private String sAdvertisementDescription;
    private String sAdvertisementLocation;
    private String sAdvertisementPrice;
    private String sAdvertisementTitle;
    private String sAdvertisementType;
    private String sAdvertisementUserIdFirestore;
    private String sAdvertisementUserLocation;
    private String sAdvertisementUserName;

    public Advertisement(String sAdvertisementIdFirestore, String sAdvertisementCategorie, String sAdvertisementDate, String sAdvertisementDescription, String sAdvertisementLocation, String sAdvertisementPrice, String sAdvertisementTitle, String sAdvertisementType, String sAdvertisementUserIdFirestore, String sAdvertisementUserLocation, String sAdvertisementUserName) {
        this.sAdvertisementIdFirestore = sAdvertisementIdFirestore;
        this.sAdvertisementCategorie = sAdvertisementCategorie;
        this.sAdvertisementDate = sAdvertisementDate;
        this.sAdvertisementDescription = sAdvertisementDescription;
        this.sAdvertisementLocation = sAdvertisementLocation;
        this.sAdvertisementPrice = sAdvertisementPrice;
        this.sAdvertisementTitle = sAdvertisementTitle;
        this.sAdvertisementType = sAdvertisementType;
        this.sAdvertisementUserIdFirestore = sAdvertisementUserIdFirestore;
        this.sAdvertisementUserLocation = sAdvertisementUserLocation;
        this.sAdvertisementUserName = sAdvertisementUserName;
    }

    public Advertisement(){
    }

    public String getsAdvertisementIdFirestore() {
        return sAdvertisementIdFirestore;
    }

    public void setsAdvertisementIdFirestore(String sAdvertisementIdFirestore) {
        this.sAdvertisementIdFirestore = sAdvertisementIdFirestore;
    }

    public String getsAdvertisementCategorie() {
        return sAdvertisementCategorie;
    }

    public void setsAdvertisementCategorie(String sAdvertisementCategorie) {
        this.sAdvertisementCategorie = sAdvertisementCategorie;
    }

    public String getsAdvertisementDate() {
        return sAdvertisementDate;
    }

    public void setsAdvertisementDate(String sAdvertisementDate) {
        this.sAdvertisementDate = sAdvertisementDate;
    }

    public String getsAdvertisementDescription() {
        return sAdvertisementDescription;
    }

    public void setsAdvertisementDescription(String sAdvertisementDescription) {
        this.sAdvertisementDescription = sAdvertisementDescription;
    }

    public String getsAdvertisementLocation() {
        return sAdvertisementLocation;
    }

    public void setsAdvertisementLocation(String sAdvertisementLocation) {
        this.sAdvertisementLocation = sAdvertisementLocation;
    }

    public String getsAdvertisementPrice() {
        return sAdvertisementPrice;
    }

    public void setsAdvertisementPrice(String sAdvertisementPrice) {
        this.sAdvertisementPrice = sAdvertisementPrice;
    }

    public String getsAdvertisementTitle() {
        return sAdvertisementTitle;
    }

    public void setsAdvertisementTitle(String sAdvertisementTitle) {
        this.sAdvertisementTitle = sAdvertisementTitle;
    }

    public String getsAdvertisementType() {
        return sAdvertisementType;
    }

    public void setsAdvertisementType(String sAdvertisementType) {
        this.sAdvertisementType = sAdvertisementType;
    }

    public String getsAdvertisementUserIdFirestore() {
        return sAdvertisementUserIdFirestore;
    }

    public void setsAdvertisementUserIdFirestore(String sAdvertisementUserIdFirestore) {
        this.sAdvertisementUserIdFirestore = sAdvertisementUserIdFirestore;
    }

    public String getsAdvertisementUserLocation() {
        return sAdvertisementUserLocation;
    }

    public void setsAdvertisementUserLocation(String sAdvertisementUserLocation) {
        this.sAdvertisementUserLocation = sAdvertisementUserLocation;
    }

    public String getsAdvertisementUserName() {
        return sAdvertisementUserName;
    }

    public void setsAdvertisementUserName(String sAdvertisementUserName) {
        this.sAdvertisementUserName = sAdvertisementUserName;
    }
}
