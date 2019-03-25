package game.raminduweeraman.colour.memory.com.colourgame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */
public class ColourGameDatabaseHelper extends SQLiteOpenHelper {
    private String TAG = ColourGameDatabaseHelper.class.getSimpleName();

    public ColourGameDatabaseHelper(Context context) {
        super(context, IConstants.DATABASE_NAME, null, IConstants.DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableChatUser(db);
    }


    private void createTableChatUser(SQLiteDatabase db) {
        Log.d(TAG, "createTableChatUser");
        String CREATE_TABLE_CHAT_USER = "CREATE TABLE " +
                IConstants.TABLE_NAME + "("
                + IConstants.DB_TABLE_COLUMN_SCORE + " INTEGER  , "
                + IConstants.DB_TABLE_COLUMN_NAME + " INTEGER  , "
                + IConstants.DB_TABLE_COLUMN_LAST_UPDATED + " VARCHAR(200) )";
        db.execSQL(CREATE_TABLE_CHAT_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");


    }

}

