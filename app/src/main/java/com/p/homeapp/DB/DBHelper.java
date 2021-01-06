package com.p.homeapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import com.p.homeapp.entities.User;

import java.time.LocalDateTime;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "HomeApp.db";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_LOGIN = "LOGIN";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_CREATE_DATE = "CREATE_DATE";
    public static final String COLUMN_ROLE = "ROLE";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {
        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOGIN + " TEXT, "+
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_CREATE_DATE + " DATE, " +
                COLUMN_ROLE + " TEXT)";

        myDB.execSQL(createUserTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
        String onUpgradeUserTableStatement = "DROP TABLE IF EXISTS " + USER_TABLE;
        myDB.execSQL(onUpgradeUserTableStatement);

        onCreate(myDB);
    }

    public boolean addOne (User user){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LOGIN, user.getLogin());
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_PASSWORD, user.getPassword());
        contentValues.put(COLUMN_CREATE_DATE, user.getCreateDate().toString());
        contentValues.put(COLUMN_ROLE, user.getRole());

        long insert = myDB.insert(USER_TABLE, null, contentValues);
        myDB.close();
        if(insert == -1){
            return false;
        } else {
            return true;
        }
    }


    public User getUser(String username){
        User user = new User();
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_LOGIN + " = ?" , new String[] {username});
        if(cursor.moveToFirst()){
            do{
                user.setId(cursor.getInt(0));
                user.setLogin(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    user.setCreateDate(LocalDateTime.parse(cursor.getString(4)));
                }
                user.setRole(cursor.getString(5));
            } while (cursor.moveToNext());
        }
        cursor.close();
        myDB.close();
        return user;
    }
}
