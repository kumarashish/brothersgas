package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;



/**
 * Created by ashish.kumar on 12-07-2018.
 */

public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

//    public void insert(ArtistDetailsModel model) {
//        ContentValues contentValue = new ContentValues();
//        contentValue.put(DatabaseHelper.name, model.getName());
//        contentValue.put(DatabaseHelper.image,model.getImage());
//        contentValue.put(DatabaseHelper.url, model.getUrl());
//        contentValue.put(DatabaseHelper.link, model.getLink());
//        contentValue.put(DatabaseHelper.published, model.getPublished());
//        contentValue.put(DatabaseHelper.summary, model.getSummary());
//        contentValue.put(DatabaseHelper.content, model.getContent());
//        contentValue.put(DatabaseHelper.isbookmarked, model.isbookmarked());
//       long val= database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
//        Log.d("Status", "insert: "+val);
//    }
//
//    public void register(RegisterModel model) {
//        ContentValues contentValue = new ContentValues();
//        contentValue.put(DatabaseHelper.name, model.getName());
//        contentValue.put(DatabaseHelper.email,model.getEmailId());
//        contentValue.put(DatabaseHelper.password, model.getPassword());
//        contentValue.put(DatabaseHelper.phone, model.getPhoneNumber());
//        long val= database.insert(DatabaseHelper.REGISTRATION_TABLE, null, contentValue);
//        Log.d("Status", "insert: "+val);
//    }
//
//    public int updateRecord(ArtistDetailsModel model, Boolean value) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.isbookmarked, value);
//        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.name + " Like '%" + model.getName() + "%'", null);
//        return i;
//    }
//
//    public int updateDetails(ArtistDetailsModel model) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.summary, model.getSummary());
//        contentValues.put(DatabaseHelper.content, model.getContent());
//        contentValues.put(DatabaseHelper.published, model.getPublished());
//        contentValues.put(DatabaseHelper.link, model.getLink());
//        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.name + " Like '%" + model.getName() + "%'", null);
//        if (i == -1) {
//            insert(model);
//        }
//        return i;
//    }
//
//    public boolean isRecordExist(ArtistDetailsModel model) {
//        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.name + " Like '%" + model.getName() + "%'", null);
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            return true;
//        } else {
//            return false;
//        }
//
//    }

    public boolean checkloginDetails(String emailId, String password)
    {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.REGISTRATION_TABLE + " where " + DatabaseHelper.email + " Like '%" + emailId + "%' and "+ DatabaseHelper.password+ " Like '%" + password+"%'" , null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean IsEmailExixts(String emailId)
    {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.REGISTRATION_TABLE + " where " + DatabaseHelper.email + " Like '%" + emailId+ "%'" , null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getOfflineDetails(String name) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.name + " Like '%" + name + "%'", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor;
        } else {
            return null;
        }


    }
    public boolean isBookmarked(String name, String image) {
        if (name.contains("~")) {
            name = name.replaceAll("~", "");
        }
        if (name.contains("'")) {
            name = name.replaceAll("'", "");
        }
        Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.isbookmarked + " FROM " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.name + " Like '%" + name + "%'" , null);
           cursor.moveToFirst();
        try {
            if (cursor.getCount() > 0) {
                String val = cursor.getString(0);
                if (val.equalsIgnoreCase("1")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }catch (Exception ex)
        {
            return false;
        }
    }

//    public ArrayList<ArtistDetailsModel> getList() {
//        ArrayList<ArtistDetailsModel> list = new ArrayList<>();
//        String[] columns = new String[]{DatabaseHelper._ID, DatabaseHelper.name, DatabaseHelper.image, DatabaseHelper.url, DatabaseHelper.link, DatabaseHelper.published, DatabaseHelper.summary, DatabaseHelper.content, DatabaseHelper.isbookmarked};
//        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
//        if (!cursor.moveToFirst()) {
//            return list;
//        }
//        do {
//            ArtistDetailsModel model = new ArtistDetailsModel(cursor);
//            list.add(model);
//        } while (cursor.moveToNext());
//        cursor.close();
//        return list;
//    }
//
//    public ArrayList<ArtistDetailsModel> getBookMarkedList() {
//        ArrayList<ArtistDetailsModel> list = new ArrayList<>();
//        Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " where " + DatabaseHelper.isbookmarked + " Like '%" + 1 + "%'", null);
//        if (!cursor.moveToFirst()) {
//            return list;
//        }
//        do {
//            ArtistDetailsModel model = new ArtistDetailsModel(cursor);
//            list.add(model);
//        } while (cursor.moveToNext());
//        cursor.close();
//        return list;
//    }
    }


