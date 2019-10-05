package com.lannasoftware.somehelp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.lannasoftware.somehelp.Entity.User;

public class DAOUser {

    private SQLiteDatabase cDatabase;
    private MySQLiteHelper cDbHelper;

    private String[] allColumns = {
            MySQLiteHelper.COL_USER_ID,
            MySQLiteHelper.COL_USER_LASTNAME,
            MySQLiteHelper.COL_USER_FIRSTNAME,
            MySQLiteHelper.COL_USER_STAY_CONNECTED,
            MySQLiteHelper.COL_USER_MEMBER_SINCE,
            MySQLiteHelper.COL_USER_ID_FIRESTORE,
            MySQLiteHelper.COL_USER_ABOUT,
            MySQLiteHelper.COL_USER_SEXE,
            MySQLiteHelper.COL_USER_VILLE,
            MySQLiteHelper.COL_USER_EMAIL,
            MySQLiteHelper.COL_USER_TELEPHONE,
            MySQLiteHelper.COL_USER_PIECE_IDENTITE,
            MySQLiteHelper.COL_USER_MODE_HOST,
            MySQLiteHelper.COL_USER_LANGUE
    };

    public DAOUser(Context context)
    {
        cDbHelper = new MySQLiteHelper(context);
    }

    public void Open() throws SQLException
    {
        cDatabase = cDbHelper.getWritableDatabase();
    }

    public void Close()
    {
        cDbHelper.close();
    }

    public User CreateSimpleUser(User cEntity)
    {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_LASTNAME,    cEntity.getsLastname());
        values.put(MySQLiteHelper.COL_USER_FIRSTNAME,    cEntity.getsFirstname());
        values.put(MySQLiteHelper.COL_USER_STAY_CONNECTED,    cEntity.getsStayConnected());
        values.put(MySQLiteHelper.COL_USER_MEMBER_SINCE,    cEntity.getsMemberSince());
        values.put(MySQLiteHelper.COL_USER_ID_FIRESTORE,    cEntity.getsIdFirestore());
        values.put(MySQLiteHelper.COL_USER_ABOUT,    cEntity.getsAbout());
        values.put(MySQLiteHelper.COL_USER_SEXE,    cEntity.getsSexe());
        values.put(MySQLiteHelper.COL_USER_VILLE,    cEntity.getsVille());
        values.put(MySQLiteHelper.COL_USER_EMAIL,    cEntity.getsEmail());
        values.put(MySQLiteHelper.COL_USER_TELEPHONE,    cEntity.getsTelephone());
        values.put(MySQLiteHelper.COL_USER_PIECE_IDENTITE,    cEntity.getsPieceIdentite());
        values.put(MySQLiteHelper.COL_USER_MODE_HOST,    cEntity.getsModeHost());
        values.put(MySQLiteHelper.COL_USER_LANGUE,    cEntity.getsLangue());

        long insertId = cDatabase.insert(MySQLiteHelper.TABLE_USER, null, values);

        Cursor cursor = cDatabase.query(MySQLiteHelper.TABLE_USER, allColumns, MySQLiteHelper.COL_USER_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        User newUser = new User (cEntity.getsLastname(), cEntity.getsFirstname(), cEntity.getsStayConnected(),
                                cEntity.getsMemberSince(), cEntity.getsIdFirestore(), cEntity.getsAbout(),
                                cEntity.getsSexe(), cEntity.getsVille(), cEntity.getsEmail(), cEntity.getsTelephone(),
                                cEntity.getsPieceIdentite(), cEntity.getsModeHost(), cEntity.getsLangue());

        newUser.setlId(insertId);

        cursor.close();
        return newUser;
    }

    public User GetUserById(long id) {

        //La Clause Where est directement ajout� dans la requ�te. 4�me avant derni� param�tre de celle-ci
        Cursor cursor = cDatabase.query(MySQLiteHelper.TABLE_USER, allColumns, MySQLiteHelper.COL_USER_ID + "=?", new String[] { String.valueOf(id) }, null, null, null);
        cursor.moveToFirst();

        // Cr�e un objet Article avec le r�sultat de la requ�te
        User user = new User (cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
                cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11),
                cursor.getString(12), cursor.getString(13));

        user.setlId(cursor.getLong(0));

        cursor.close();
        return user;
    }

    public void Delete()
    {
        cDatabase.execSQL("delete from "+MySQLiteHelper.TABLE_USER);
    }

    public long CountProfiles() {
        long cnt  = DatabaseUtils.queryNumEntries(cDatabase, MySQLiteHelper.TABLE_USER);
        return cnt;
    }

    //---deletes a particular title---
    public boolean DeleteUserByIdSQLite(User user)
    {
        return cDatabase.delete(MySQLiteHelper.TABLE_USER, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) }) > 0;
    }

    public void UpdateSexe(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_SEXE, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateVille(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_VILLE, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateEmail(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_EMAIL, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateTelephone(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_TELEPHONE, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdatePieceIdentite(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_PIECE_IDENTITE, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateLangue(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_LANGUE, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateStayConnected(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_STAY_CONNECTED, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateFirstName(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_FIRSTNAME, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateLastName(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_LASTNAME, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateAbout(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_ABOUT, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }

    public void UpdateModeHost(User user, String s) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COL_USER_MODE_HOST, s);
        cDatabase.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COL_USER_ID + " =? ", new String[] { String.valueOf(user.getlId()) });
    }
}