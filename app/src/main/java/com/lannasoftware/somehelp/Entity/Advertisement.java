package com.lannasoftware.somehelp.Entity;

import java.util.ArrayList;
import java.util.List;

public class Advertisement{

    private String advertisement_id_firestore;
    private String advertisement_categorie;
    private String advertisement_date;
    private String advertisement_description;
    private List<String> advertisement_hashtag;
    private String advertisement_location;
    private String advertisement_price;
    private String advertisement_title;
    private String advertisement_type;
    private String user_id_firestore;
    private String user_location;
    private String user_name;
    private ArrayList<String> advertisement_images;

    public Advertisement(String sAdvertisementIdFirestore, String sAdvertisementCategorie,
                         String sAdvertisementDate, String sAdvertisementDescription,
                         List<String> advertisement_hashtag, String sAdvertisementLocation,
                         String sAdvertisementPrice, String sAdvertisementTitle,
                         String sAdvertisementType, String sAdvertisementUserIdFirestore,
                         String sAdvertisementUserLocation, String sAdvertisementUserName,
                         ArrayList<String> sAdvertisementImages) {

        this.advertisement_id_firestore = sAdvertisementIdFirestore;
        this.advertisement_categorie = sAdvertisementCategorie;
        this.advertisement_date = sAdvertisementDate;
        this.advertisement_description = sAdvertisementDescription;
        this.advertisement_location = sAdvertisementLocation;
        this.advertisement_price = sAdvertisementPrice;
        this.advertisement_title = sAdvertisementTitle;
        this.advertisement_type = sAdvertisementType;
        this.user_id_firestore = sAdvertisementUserIdFirestore;
        this.user_location = sAdvertisementUserLocation;
        this.user_name = sAdvertisementUserName;
        this.advertisement_images = sAdvertisementImages;
    }

    public Advertisement(){
    }

    public String getAdvertisement_id_firestore() {
        return advertisement_id_firestore;
    }

    public void setAdvertisement_id_firestore(String advertisement_id_firestore) {
        this.advertisement_id_firestore = advertisement_id_firestore;
    }

    public String getAdvertisement_categorie() {
        return advertisement_categorie;
    }

    public void setAdvertisement_categorie(String advertisement_categorie) {
        this.advertisement_categorie = advertisement_categorie;
    }

    public String getAdvertisement_date() {
        return advertisement_date;
    }

    public void setAdvertisement_date(String advertisement_date) {
        this.advertisement_date = advertisement_date;
    }

    public String getAdvertisement_description() {
        return advertisement_description;
    }

    public void setAdvertisement_description(String advertisement_description) {
        this.advertisement_description = advertisement_description;
    }

    public List<String> getAdvertisement_hashtag() {
        return advertisement_hashtag;
    }

    public void setAdvertisement_hashtag(List<String> advertisement_hashtag) {
        this.advertisement_hashtag = advertisement_hashtag;
    }

    public String getAdvertisement_location() {
        return advertisement_location;
    }

    public void setAdvertisement_location(String advertisement_location) {
        this.advertisement_location = advertisement_location;
    }

    public String getAdvertisement_price() {
        return advertisement_price;
    }

    public void setAdvertisement_price(String advertisement_price) {
        this.advertisement_price = advertisement_price;
    }

    public String getAdvertisement_title() {
        return advertisement_title;
    }

    public void setAdvertisement_title(String advertisement_title) {
        this.advertisement_title = advertisement_title;
    }

    public String getAdvertisement_type() {
        return advertisement_type;
    }

    public void setAdvertisement_type(String advertisement_type) {
        this.advertisement_type = advertisement_type;
    }

    public String getUser_id_firestore() {
        return user_id_firestore;
    }

    public void setUser_id_firestore(String user_id_firestore) {
        this.user_id_firestore = user_id_firestore;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<String> getAdvertisement_images() {
        return advertisement_images;
    }

    public void setAdvertisement_images(ArrayList<String> advertisement_images) {
        this.advertisement_images = advertisement_images;
    }
}
