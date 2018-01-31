package com.yiftah.jobtimer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by משפחת שלזינגר on 02/08/2017.
 */

public class MyDBHandler extends SQLiteOpenHelper {
    static String dbName = "JobTimer";
    static int version = 1;
    SQLiteDatabase db = null;
    public MyDBHandler (Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("CREATE TABLE " + dbName + " (" +
                "startWork datetime," +
                "money int," +
                "moneyPerHour int" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
        db.execSQL("CREATE TABLE " + dbName + " (" +
                "startWork datetime," +
                "money int," +
                "moneyPerHour int" +
                ")");
    }
    public String getStartTime () {
        db = getReadableDatabase();
        if (db == null)
            return "";
        Cursor c = null;
        c = db.rawQuery("SELECT startWork FROM " + dbName, null);
        c.moveToFirst();
        if (c.isAfterLast())
            return "";
        String result = "";
        try {
            result = c.getString(c.getColumnIndex("startWork"));
        }
        catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }
    public int getMoney () {
        db = getReadableDatabase();
        if (db == null)
            return 0;
        Cursor c = null;
        c = db.rawQuery("SELECT money FROM " + dbName, null);
        c.moveToFirst();
        if (c.isAfterLast())
            return 0;
        int result = 0;
        try {
            result = c.getInt(c.getColumnIndex("money"));
        }
        catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }
    public int getMoneyPerHour () {
        db = getReadableDatabase();
        if (db == null)
            return 0;
        Cursor c = null;
        c = db.rawQuery("SELECT moneyPerHour FROM " + dbName, null);
        c.moveToFirst();
        if (c.isAfterLast())
            return 0;
        int result = 0;
        try {
            result = c.getInt(c.getColumnIndex("moneyPerHour"));
        }
        catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }
    private void set (String startTime, int money, int moneyPerHour) {
        db = getWritableDatabase();
        if (db == null)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + dbName);
        db.execSQL("CREATE TABLE " + dbName + " (" +
                "startWork datetime," +
                "money int," +
                "moneyPerHour int" +
                ")");
        db.execSQL("INSERT INTO " + dbName + "(startWork, money, moneyPerHour) " +
                "VALUES ('" + startTime + "', " + money + ", " + moneyPerHour + ")");
    }
    public void setStartTime (String startTime) {
        set (startTime, getMoney(), getMoneyPerHour());
    }
    public void setMoney (int money) {
        set (getStartTime(), money, getMoneyPerHour());
    }
    public void setMoneyPerHour (int moneyPerHour) {
        set (getStartTime(), getMoney(), moneyPerHour);
    }
    public void reset () {
        set ("", 0, 0);
    }
}
