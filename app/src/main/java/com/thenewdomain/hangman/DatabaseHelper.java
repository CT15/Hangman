package com.thenewdomain.hangman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CalvinTantio on 2/6/2017.
 */

//public class DatabaseHelper extends SQLiteOpenHelper{
//
//    private static final int DATABASE_VERSION = 1;
//
//    private static final String DATABASE_NAME = "UserManager.db";
//    private static final String TABLE_USER = "user";
//    private static final String COLUMN_USER_ID = "id";
//    private static final String COLUMN_USER_NAME = "user_name";
//    private static final String COLUMN_USER_PASSWORD = "user_password";
//
//    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
//            COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//            COLUMN_USER_NAME + " TEXT, " +
//            COLUMN_USER_PASSWORD + " TEXT" + ")";
//
//    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
//
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_USER_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(DROP_USER_TABLE);
//        this.onCreate(db);
//    }
//
//    public void addUser(UserInformation user){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_USER_NAME, user.getUserName());
//        values.put(COLUMN_USER_PASSWORD, user.getPassword());
//        db.insert(TABLE_USER, null, values);
//        db.close();
//    }
//
//    public boolean checkUser (String userName, String password, boolean checkPassword){
//        String[] columns = {COLUMN_USER_ID, COLUMN_USER_PASSWORD};
//        SQLiteDatabase db = this.getWritableDatabase();
//        String selection = COLUMN_USER_NAME + " = ?";
//        String[] selectionArgs = {userName};
//
//        Cursor cursor = db.query(TABLE_USER,
//                columns,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null);
//
//        int cursorCount = cursor.getCount();
//        db.close();
//        cursor.close();
//
//        if(cursorCount > 0){
//            if(checkPassword) {
//                if (passwordIsCorrect(userName, password)) {
//                    return true;
//                }
//            } else { //no need to check password
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean passwordIsCorrect(String userName, String password) {
//        String[] columns = {COLUMN_USER_NAME, COLUMN_USER_PASSWORD};
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Cursor cursor = db.query(TABLE_USER,
//                columns, null, null, null, null, null);
//
//        if(cursor.moveToFirst()){
//            while(cursor.moveToNext()){
//                if(cursor.getString(0).equals(userName)) break;
//            }
//            if(cursor.getString(1).equals(password)) return true;
//        }
//
//        cursor.close();
//        return false;
//    }
//
//}
