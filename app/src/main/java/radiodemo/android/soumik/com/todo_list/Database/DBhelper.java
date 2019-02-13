package radiodemo.android.soumik.com.todo_list.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME="data";
    private static final int DATABASE_VERSION = 1011;
    private static final String TABLE_NAME ="note";
    private static final String DB_COLUMN ="TaskName";

    public DBhelper(Context context)
    {
        super(context,DB_NAME,null,DATABASE_VERSION);
        Log.i("msg","database created :"+ DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String SQL = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL)",TABLE_NAME,DB_COLUMN);
       // String SQL = "create table " + TABLE_NAME +DB_COLUMN + "(id INTEGER primary key AUTOINCREMENT)";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    String SQL=String.format("delete table if exists %s",TABLE_NAME);
    db.execSQL(SQL);
    }

    public void insertNewTask(String task)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(DB_COLUMN,task);
        db.insertWithOnConflict(TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String task)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,DB_COLUMN + "=?",new String[]{task});
        db.close();
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> tasklist =
                new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor myCursor = db.query(TABLE_NAME,new String[]{DB_COLUMN},null,null,null,null,null);
        while (myCursor.moveToNext()) {
            int index = myCursor.getColumnIndex(DB_COLUMN);
            tasklist.add(myCursor.getString(index));
        }
        myCursor.close();
        db.close();
        return tasklist;
    }

}
