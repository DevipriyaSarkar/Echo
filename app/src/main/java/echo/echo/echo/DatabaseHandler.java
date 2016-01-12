package echo.echo.echo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AlarmManager";
    private static final String TABLE_ALARMS = "AlarmTable";
    private static final String KEY_ID = "id";
    private static final String KEY_EVENT = "event1";
    private static final String KEY_REMIND = "remind1";
    private static final String KEY_KEY="key1";
    private static final String KEY_CHECK="check1";
    private static final String KEY_DETAIL="detail1";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_EVENT + " TEXT NOT NULL, "
                + KEY_REMIND + " TEXT NOT NULL, " + KEY_KEY+" TEXT NOT NULL, " + KEY_CHECK +" INTEGER NOT NULL, "+KEY_DETAIL
                +" TEXT NOT NULL);";
        Log.e("Inside",CREATE_ALARMS_TABLE);
        db.execSQL(CREATE_ALARMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new alarmItems
    void addAlarmItems(AlarmItems alarmItems) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT, alarmItems.getEvent()); // AlarmItems Name
        values.put(KEY_REMIND, alarmItems.getRemind());// AlarmItems Phone
        values.put(KEY_KEY, alarmItems.getKey());
        values.put(KEY_CHECK, alarmItems.isCheck());
        values.put(KEY_DETAIL, alarmItems.getDetail());
        // Inserting Row
        Log.e("Inside" ,""+db.insert(TABLE_ALARMS, null, values));
        Log.d("Works", alarmItems.getDetail());
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single alarmItems
    AlarmItems getAlarmItems(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS, new String[] { KEY_ID,
                        KEY_EVENT, KEY_REMIND,KEY_KEY,KEY_CHECK,KEY_DETAIL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AlarmItems alarmItems = new AlarmItems(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),Integer.parseInt(cursor.getString(4)));
        // return alarmItems
        return alarmItems;
    }

    // code to get all Alarms in a list view
    public ArrayList<AlarmItems> getAllAlarmItems() {
        ArrayList<AlarmItems> alarmItemsList = new ArrayList<AlarmItems>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ALARMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Inside","Get");
        Log.e("Inside"," "+cursor.getCount());
        if(cursor==null)
            Log.d("cursor","null");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AlarmItems alarmItems = new AlarmItems();
                alarmItems.setId(Integer.parseInt(cursor.getString(0)));
                alarmItems.setEvent(cursor.getString(1));
                alarmItems.setRemind(cursor.getString(2));
                alarmItems.setKey(cursor.getString(3));
                alarmItems.setCheck(Integer.parseInt(cursor.getString(4)));
                alarmItems.setDetail(cursor.getString(5));
                Log.d("Inside",alarmItems.getDetail());
                // Adding alarmItems to list
                alarmItemsList.add(alarmItems);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return alarmItems list
        return alarmItemsList;
    }

    // code to update the single alarmItems
    public int updateAlarmItems(AlarmItems alarmItems) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT, alarmItems.getEvent());
        values.put(KEY_REMIND, alarmItems.getRemind());
        values.put(KEY_KEY,alarmItems.getKey());
        values.put(KEY_CHECK,alarmItems.isCheck());
        values.put(KEY_DETAIL,alarmItems.getDetail());
        // updating row
        int r= db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(alarmItems.getId()) });
        db.close();
        return r;
    }

    // Deleting single alarmItems
    public void deleteAlarmItems(AlarmItems alarmItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?",
                new String[] { String.valueOf(alarmItems.getId()) });
        db.close();
    }

    // Getting Alarms Count
    public int getAlarmItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

}