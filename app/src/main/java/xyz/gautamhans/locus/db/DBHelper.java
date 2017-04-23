package xyz.gautamhans.locus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gautam on 21-Apr-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reminders";
    private static final int DATABASE_VERSION = 1;
    private static final String REMINDERS_TABLE = "reminders_table";
    private static final String REM_TABLE =
            "CREATE TABLE " + REMINDERS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, longitude REAL, latitude REAL, placeId TEXT, address TEXT, radius INT)";

    private static DBHelper sInstance;
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + REMINDERS_TABLE);
        onCreate(db);
    }

    // Insert into Database
    public void insertIntoDB(
            String title, String description, Double longitude, Double latitude,
            String placeID, String address, int radius) {
        Log.d("insert", "before insert");

        // 1.get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("description", description);
        values.put("longitude", longitude);
        values.put("latitude", latitude);
        values.put("placeId", placeID);
        values.put("address", address);
        values.put("radius", radius);

        // 3. insert values into table
        db.insert(REMINDERS_TABLE, null, values);

        // 4. Close db
        db.close();
        Log.i("insert into DB", "After insert");
    }

    //Retrieve data from Database
    public List<DatabaseModel> getDataFromDB() {
        List<DatabaseModel> modelList = new ArrayList<DatabaseModel>();
        String query = "SELECT * FROM " + REMINDERS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                DatabaseModel model = new DatabaseModel();
                model.setId(cursor.getInt(0));
                model.setTitle(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                model.setLongitude(cursor.getDouble(3));
                model.setLatitude(cursor.getDouble(4));
                model.setPlaceID(cursor.getString(5));
                model.setAddress(cursor.getString(6));
                model.setRadius(cursor.getInt(7));
                modelList.add(model);
            } while (cursor.moveToNext());
        }
        Log.d("reminders table data", modelList.toString());
        return modelList;
    }

    //Delete a row from the db
    public void deleteARow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(REMINDERS_TABLE, RemindersContract.ID + " = " + id, null);
        Log.d(String.valueOf(this), id + " deleted");
        db.close();
    }
}

