package com.lannasoftware.somehelp.Entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.lannasoftware.somehelp.R;

public class User implements Parcelable {

    private long lId;
    private String sFirstname;
    private String sLastname;
    private String sStayConnected;
    private String sMemberSince;
    private String sIdFirestore;
    private String sAbout;
    private String sSexe;
    private String sVille;
    private String sEmail;
    private String sTelephone;
    private String sPieceIdentite;
    private String sLangue;

    public User(){
    }

    public User(String sNom, String sPrenom, String sStayConnected, String sMemberSince, String sIdFirestore)
    {
        this.sLastname = sNom;
        this.sFirstname = sPrenom;
        this.sStayConnected = sStayConnected;
        this.sMemberSince = sMemberSince;
        this.sIdFirestore = sIdFirestore;
    }

    public User(String sNom, String sPrenom, String sStayConnected, String sMemberSince, String sIdFirestore, String sAbout,
                String sSexe, String sVille, String sEmail, String sTelephone, String sPieceIdentite, String sLangue)
    {
        this.sLastname = sNom;
        this.sFirstname = sPrenom;
        this.sStayConnected = sStayConnected;
        this.sMemberSince = sMemberSince;
        this.sIdFirestore = sIdFirestore;
        this.sAbout = sAbout;
        this.sSexe = sSexe;
        this.sVille = sVille;
        this.sEmail = sEmail;
        this.sTelephone = sTelephone;
        this.sPieceIdentite = sPieceIdentite;
        this.sLangue = sLangue;
    }

    public User(Parcel in) {
        sFirstname = in.readString();
        sLastname = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(sFirstname);
        dest.writeString(sLastname);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public long getlId() {
        return lId;
    }

    public void setlId(long lId) {
        this.lId = lId;
    }

    public String getsFirstname() {
        return sFirstname;
    }

    public void setsFirstname(String sFirstname) {
        this.sFirstname = sFirstname;
    }

    public String getsLastname() {
        return sLastname;
    }

    public void setsLastname(String sLastname) {
        this.sLastname = sLastname;
    }

    public String getsStayConnected() {
        return sStayConnected;
    }

    public void setsStayConnected(String sStayConnected) {
        this.sStayConnected = sStayConnected;
    }

    public String getsMemberSince() {
        return sMemberSince;
    }

    public void setsMemberSince(String sMemberSince) {
        this.sMemberSince = sMemberSince;
    }

    public String getsIdFirestore() {
        return sIdFirestore;
    }

    public void setsIdFirestore(String sIdFirestore) {
        this.sIdFirestore = sIdFirestore;
    }

    public String getsAbout() {
        return sAbout;
    }

    public void setsAbout(String sAbout) {
        this.sAbout = sAbout;
    }

    public String getsSexe() {
        return sSexe;
    }

    public void setsSexe(String sSexe) {
        this.sSexe = sSexe;
    }

    public String getsVille() {
        return sVille;
    }

    public void setsVille(String sVille) {
        this.sVille = sVille;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsTelephone() {
        return sTelephone;
    }

    public void setsTelephone(String sTelephone) {
        this.sTelephone = sTelephone;
    }

    public String getsPieceIdentite() {
        return sPieceIdentite;
    }

    public void setsPieceIdentite(String sPieceIdentite) {
        this.sPieceIdentite = sPieceIdentite;
    }

    public String getsLangue() {
        return sLangue;
    }

    public void setsLangue(String sLangue) {
        this.sLangue = sLangue;
    }
}
