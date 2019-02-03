package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ashish.kumar on 12-07-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "SearchList";
    public static final String REGISTRATION_TABLE = "LoginTable";
    // Table columns
    public static final String _ID = "_id";
    public static final String name = "name";
    public static final String image = "image";
    public static final String url = "url";
    public static final String link = "link";
    public static final String published = "published";
    public static final String summary = "summary";
    public static final String content = "content";
    public static final String isbookmarked= "bookmarked";

    // Table columns


    public static final String email = "email";
    public static final String phone = "phone";
    public static final String password = "password";

    // Database Information
    static final String DB_NAME = "lastfm.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " TEXT NOT NULL, " + image + " TEXT NOT NULL, " + url + " TEXT, "+ link + " TEXT, "+ published + " TEXT, " + summary + " TEXT, " + content + " TEXT, " + isbookmarked+ " BOOLEAN);";
    private static final String CREATE_TABLE_REGISTER= "create table " + REGISTRATION_TABLE + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + email + " TEXT NOT NULL, " + name + " TEXT NOT NULL, " + phone + " TEXT, "+ password + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL( CREATE_TABLE_REGISTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REGISTRATION_TABLE);
        onCreate(db);
    }


}