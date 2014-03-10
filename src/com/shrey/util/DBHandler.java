package com.shrey.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shrey.pojos.User;

public class DBHandler extends SQLiteOpenHelper {
private static final int DATABASE_NUMBER = 1;
	
	private static final String DATABASE_NAME = "Snipeshot";
	
	private static final String TABLE_USERS = "users";
	
	private static final String KEY_USERNAME ="username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_USERID = "uid";
	private static final String KEY_SCORE = "score";
	
	public DBHandler(Context ctx){
		super(ctx, DATABASE_NAME, null, DATABASE_NUMBER);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + KEY_USERID + " INTEGER PRIMARY KEY,"   
		+ KEY_USERNAME + " TEXT," + KEY_PASSWORD
				+ " TEXT" + ")";
		Log.d("debug",CREATE_USER_TABLE);
		db.execSQL(CREATE_USER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USERS);
		onCreate(db);
		
	}
	
	public void addUser(User user) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_USERNAME, user.getuserName()); // 
	    values.put(KEY_PASSWORD, user.getPassword()); // 
	 
	    // Inserting Row
	    db.insert(TABLE_USERS, null, values);
	    
	    db.close(); // Closing database connection
	}
	
	User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_USERS, new String[] {KEY_USERID,
                KEY_USERNAME, KEY_PASSWORD }, KEY_USERID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(1));
        // return contact
        return user;
    }
	
	public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                
               // user.setId(Integer.parseInt(cursor.getString(0)));
                
                user.setuserName(cursor.getString(1));
                
                user.setpassWord(cursor.getString(2));
                
                
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return userList;
    }
	
	 public int updateUser(User u) {
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        ContentValues values = new ContentValues();
	        values.put(KEY_USERNAME, u.getuserName());
	        values.put(KEY_PASSWORD, u.getPassword());
	        
	 
	        // updating row
	        return db.update(TABLE_USERS, values, KEY_USERID + " = ?",
	                new String[] { String.valueOf(u.getId()) });
	    }
	 
	    // Deleting single contact
	    public void deleteUser(User u) {
	        SQLiteDatabase db = this.getWritableDatabase();
	        db.delete(TABLE_USERS, KEY_USERNAME + " = ?",
	                new String[] {String.valueOf(u.getId()) });
	        db.close();
	    }
	    
	    public int getUsersCount() {
	        String countQuery = "SELECT  * FROM " + TABLE_USERS;
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(countQuery, null);
	        cursor.close();
	 
	        // return count
	        return cursor.getCount();
	    }
	 
}
