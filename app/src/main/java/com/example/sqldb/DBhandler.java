package com.example.sqldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class DBhandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "Information";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Personal";
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String ROLL_COL = "rollno";
    private static final String DEPARTMENT_COL = "department";
    private static final String  DOB= "dob";
    private static final String  EMAIL= "email";
    private static final String  PASS= "password";
    private static final String  GENDER= "gender";
    private static final String  IMAGE= "image";
    public DBhandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + ROLL_COL + " INTEGER,"
                + DEPARTMENT_COL + " TEXT,"
                + EMAIL + " TEXT,"
                + PASS + " TEXT,"
                + GENDER + " TEXT,"
                + IMAGE+ " BLOB,"
                + DOB + " TEXT)";
        db.execSQL(query);
    }
    public void addInformation(String stud_Name, int stud_rollno, String stud_Depart, String stud_email, String stud_pass, String stud_gender, String stud_dob, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, stud_Name);
        values.put(ROLL_COL,stud_rollno);
        values.put(DEPARTMENT_COL, stud_Depart);
        values.put(EMAIL, stud_email);
        values.put(PASS, stud_pass);
        values.put(GENDER, stud_gender);
        values.put(DOB, stud_dob);
        values.put(IMAGE,image);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean check(String username,String password)
    {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            byte[] hashPassword = messageDigest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashPassword) {
                stringBuilder.append(String.format("%02x", b));
            }
            password = stringBuilder.toString();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursorCourses = db.rawQuery("SELECT "+PASS+" FROM " + TABLE_NAME, null);
            if(cursorCourses.moveToFirst())
            {
                if (password.equals(cursorCourses.getString(0)));
                return true;
            }

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public Cursor readCourses(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME+" WHERE "+EMAIL+" LIKE '%"+id+"%'", null);
        return cursorCourses;
    }
}







