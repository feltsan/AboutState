package john.aboutstate.support;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by john on 02.12.14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_STATE = "stata_table";
    public static final String UID = "_id";
    public static final String NAME_STATE = "nameState";
    public static final String LAT_STATE = "lat_state";
    public static final String LNG_STATE = "lng_state";
    public static final String CAPITAL_STATE = "capital_state";
    public static final String REGION_STATE = "region_state";
    public static final String AREA_STATE = "area_state";
    public static final String CALL_CODE_STATE = "call_code_state";

    private static final String CREATE_TABLE_STATE = "CREATE TABLE IF NOT EXISTS " + TABLE_STATE
            + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME_STATE + " VARCHAR(255),"
            + LAT_STATE + " VARCHAR(255)," + LNG_STATE + " VARCHAR(255),"
            + CAPITAL_STATE + " VARCHAR(255)," + REGION_STATE + " VARCHAR(255),"
            + AREA_STATE + " VARCHAR(255)," + CALL_CODE_STATE + " VARCHAR(255));";


    private static final String DELETE_STATE = "DROP TABLE IF EXISTS " + TABLE_STATE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STATE);
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_STATE);
        onCreate(db);
    }


}
