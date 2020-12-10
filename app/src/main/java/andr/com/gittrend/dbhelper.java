package andr.com.gittrend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import andr.com.gittrend.user;
import java.util.ArrayList;
import java.util.List;

public class dbhelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "repoManager.db";
    // User table name
    private static final String TABLE_USER = "user";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_AVATAR = "user_avatar";
    private static final String COLUMN_USER_LANGUAGE = "user_language";
    private static final String COLUMN_USER_DESCRIPTION = "user_decp";
    private static final String COLUMN_USER_STARS = "user_stars";
    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_AVATAR + " TEXT," + COLUMN_USER_LANGUAGE+ " TEXT," + COLUMN_USER_DESCRIPTION +" TEXT," + COLUMN_USER_STARS +" TEXT)";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public dbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        // Create tables again
        onCreate(db);
    }
    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(user user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_AVATAR, user.getAvatar());
        values.put(COLUMN_USER_LANGUAGE, user.getLanguage());
        values.put(COLUMN_USER_DESCRIPTION, user.getDescp());
        values.put(COLUMN_USER_STARS,user.getStars());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }
    public List<user> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_AVATAR,
                COLUMN_USER_NAME,
                COLUMN_USER_LANGUAGE,
                COLUMN_USER_DESCRIPTION,
                COLUMN_USER_STARS

        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<user> userList = new ArrayList<user>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                user user = new user();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setAvatar(cursor.getString(cursor.getColumnIndex(COLUMN_USER_AVATAR)));
                user.setLanguage(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LANGUAGE)));
                user.setDescp(cursor.getString(cursor.getColumnIndex(COLUMN_USER_DESCRIPTION)));
                user.setStars(cursor.getString(cursor.getColumnIndex(COLUMN_USER_STARS)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(user user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_AVATAR, user.getAvatar());
        values.put(COLUMN_USER_LANGUAGE, user.getLanguage());
        values.put(COLUMN_USER_DESCRIPTION, user.getDescp());
        values.put(COLUMN_USER_STARS,user.getStars());
        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }


    /**
     * This method to check user exist or not
     *
     * @param name
     * @return true/false
     */
    public boolean checkUser(String name) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";
        // selection argument
        String[] selectionArgs = {name};
        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

}
