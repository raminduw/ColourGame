package game.raminduweeraman.colour.memory.com.colourgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */
public class ScoreDataSource {

    private String TAG = ScoreDataSource.class.getSimpleName();

    // Database fields
    private String[] allColumns = {IConstants.DB_TABLE_COLUMN_SCORE,
            IConstants.DB_TABLE_COLUMN_NAME,
            IConstants.DB_TABLE_COLUMN_LAST_UPDATED};

    private SQLiteDatabase db;
    private ColourGameDatabaseHelper dbHelper;

    public ScoreDataSource(Context context) {
        dbHelper = new ColourGameDatabaseHelper(context);
    }

    private void open() {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close() {
        dbHelper.close();
    }


    public void addScore(String userName, int score) {

        open();
        ContentValues values = new ContentValues();
        values.put(IConstants.DB_TABLE_COLUMN_NAME, userName);
        values.put(IConstants.DB_TABLE_COLUMN_SCORE, score);

        SimpleDateFormat sdf = new SimpleDateFormat(IConstants.DB_DATE_TIME_FORMAT);
        String currentDateTime = sdf.format(new Date());
        values.put(IConstants.DB_TABLE_COLUMN_LAST_UPDATED, currentDateTime);

        db.insert(IConstants.TABLE_NAME, null, values);

        close();
    }


    public ArrayList<ScoreObject> getAllScores() {
        open();
        Cursor result = db.query(IConstants.TABLE_NAME,
                allColumns, null, null, null, null, IConstants.DB_TABLE_COLUMN_SCORE + " DESC");
        ArrayList<ScoreObject> scores = new ArrayList<ScoreObject>();
        ScoreObject scoreObject;
        try {
            if (result.moveToFirst()) {
                do {
                    scoreObject = new ScoreObject();
                    scoreObject.setUserName(result.getString(result.getColumnIndex(IConstants.DB_TABLE_COLUMN_NAME)));
                    scoreObject.setHighestScore(result.getInt(result.getColumnIndex(IConstants.DB_TABLE_COLUMN_SCORE)));
                    scoreObject.setLastUpdated(result.getString(result.getColumnIndex(IConstants.DB_TABLE_COLUMN_LAST_UPDATED)));
                    scores.add(scoreObject);
                } while (result.moveToNext());

            }
        } catch (Exception ex) {
            Log.e(TAG, "getAllScores Exception " + ex.getLocalizedMessage());
        } finally {
            close();
        }
        return scores;
    }


    public ScoreObject getCurrentScore(String userName) {
        ScoreObject scoreObject = null;
        open();
        Cursor result = db.query(IConstants.TABLE_NAME,
                allColumns, IConstants.DB_TABLE_COLUMN_NAME + "=?",
                new String[]{String.valueOf(userName)}, null, null, null, null);
        try {
            if (result.moveToFirst()) {
                do {
                    scoreObject = new ScoreObject();
                    scoreObject.setUserName(result.getString(result.getColumnIndex(IConstants.DB_TABLE_COLUMN_NAME)));
                    scoreObject.setHighestScore(result.getInt(result.getColumnIndex(IConstants.DB_TABLE_COLUMN_SCORE)));
                    scoreObject.setLastUpdated(result.getString(result.getColumnIndex(IConstants.DB_TABLE_COLUMN_LAST_UPDATED)));
                } while (result.moveToNext());
            }
        } catch (Exception ex) {
            Log.e(TAG, "getCurrentScore Exception " + ex.getLocalizedMessage());
        } finally {
            close();
        }
        return scoreObject;

    }
}
