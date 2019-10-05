package com.lannasoftware.somehelp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_USER = "table_user";

    public static final String COL_USER_ID        = "col_user_id";
    public static final String COL_USER_FIRSTNAME    = "col_user_firstname";
    public static final String COL_USER_LASTNAME    = "col_user_lastname";
    public static final String COL_USER_STAY_CONNECTED    = "col_user_stay_connected";
    public static final String COL_USER_MEMBER_SINCE    = "col_user_member_since";
    public static final String COL_USER_ID_FIRESTORE   = "col_user_id_firestore";
    public static final String COL_USER_ABOUT = "col_user_about";
    public static final String COL_USER_SEXE = "col_user_sexe";
    public static final String COL_USER_VILLE = "col_user_ville";
    public static final String COL_USER_EMAIL = "col_user_email";
    public static final String COL_USER_TELEPHONE = "col_user_telephone";
    public static final String COL_USER_PIECE_IDENTITE = "col_user_piece_identite";
    public static final String COL_USER_MODE_HOST = "col_user_mode_host";
    public static final String COL_USER_LANGUE = "col_user_langue";

    // Nom donn�e � ma base de donn�es et version de celle-ci
    private static final String DATABASE_NAME = "somehelplocaldb.db";
    private static final int DATABASE_VERSION = 11;

    // Requ�te SQL cr�ant ma base de donn�es et les champs la constituant
    private static final String DATABASE_CREATE_USER_DB
            = "create table " + TABLE_USER + "(" + COL_USER_ID + " integer primary key, "
            + COL_USER_FIRSTNAME + " text not null, "
            + COL_USER_LASTNAME + " text not null, "
            + COL_USER_STAY_CONNECTED + " text, "
            + COL_USER_MEMBER_SINCE + " text not null, "
            + COL_USER_ID_FIRESTORE + " text, "
            + COL_USER_ABOUT + " text, "
            + COL_USER_SEXE + " text, "
            + COL_USER_VILLE + " text, "
            + COL_USER_EMAIL + " text, "
            + COL_USER_TELEPHONE + " text, "
            + COL_USER_PIECE_IDENTITE + " text, "
            + COL_USER_MODE_HOST + " text not null, "
            + COL_USER_LANGUE + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Cr�e la base de donn�es
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USER_DB);
    }

    // M�thode mettant � jour la base de donn�es lorsque la version de celle-ci � vari�
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}
