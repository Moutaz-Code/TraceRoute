package motaz.pharaon.adu.ac.ae.traceroute.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDatabase extends SQLiteOpenHelper {
    /*
     * Purpose:
     * - SQLite save-state helper, if SQLite is required for the project.
     *
     * Responsibilities:
     * - Create tables for discovered evidence, thread state, current room, and final timeline completion.
     * - Keep CRUD details behind GameRepository.
     *
     * Depends on:
     * - GameContract.
     * - Android SQLite APIs when implemented.
     *
     * Used by:
     * - GameRepository.
     *
     * Note:
     * - This MVP uses a compact key/value table.
     * - Thread, statement, and timeline reveal state is currently derived from collected evidence.
     */

    private static final String DATABASE_NAME = "traceroute_save.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_SAVE_STATE_TABLE =
            "CREATE TABLE " + GameContract.SaveState.TABLE_NAME + " ("
                    + GameContract.SaveState.COLUMN_KEY + " TEXT PRIMARY KEY, "
                    + GameContract.SaveState.COLUMN_VALUE + " TEXT NOT NULL"
                    + ")";

    private static final String DROP_SAVE_STATE_TABLE =
            "DROP TABLE IF EXISTS " + GameContract.SaveState.TABLE_NAME;

    public GameDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_SAVE_STATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DROP_SAVE_STATE_TABLE);
        onCreate(database);
    }

    public void putValue(String key, String value) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameContract.SaveState.COLUMN_KEY, key);
        contentValues.put(GameContract.SaveState.COLUMN_VALUE, value);

        database.insertWithOnConflict(
                GameContract.SaveState.TABLE_NAME,
                null,
                contentValues,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    public String getValue(String key, String defaultValue) {
        SQLiteDatabase database = getReadableDatabase();

        try (Cursor cursor = database.query(
                GameContract.SaveState.TABLE_NAME,
                new String[] {GameContract.SaveState.COLUMN_VALUE},
                GameContract.SaveState.COLUMN_KEY + " = ?",
                new String[] {key},
                null,
                null,
                null
        )) {
            if (cursor.moveToFirst()) {
                return cursor.getString(0);
            }
        }

        return defaultValue;
    }

    public void clearSaveState() {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(GameContract.SaveState.TABLE_NAME, null, null);
    }
}
