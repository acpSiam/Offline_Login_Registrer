package bmarpc.acpsiam.offlineloginregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;

    public static final String DB_NAME = "USERS.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "USERS_INFO";
    public static final String _id = "_id";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String USER_NAME = "USER_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "psw";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = " CREATE TABLE " + TABLE_NAME + " ("
                + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FIRST_NAME + " TEXT, "
                + LAST_NAME + " TEXT, "
                + USER_NAME + " TEXT, "
                + EMAIL + " TEXT, "
                + PASSWORD + " PASSWORD)";


        sqLiteDatabase.execSQL(query);


        //This is to create a demo user
        //TODO: REMOVE THESE LINES (46 - 50) TO PREVENT APP FROM CREATING A DEMO USER
        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME + " (" + FIRST_NAME + " ," + LAST_NAME + " ," + USER_NAME + " ," + EMAIL + " ," + PASSWORD + ") " +
                "VALUES ('Admin','Admin','admin','contact@admin.com','123')");
        Toast.makeText(context, "Demo user created", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }



    public void addUser(String fName, String lName, String userName, String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FIRST_NAME, fName);
        contentValues.put(LAST_NAME, lName);
        contentValues.put(USER_NAME, userName);
        contentValues.put(EMAIL, email);
        contentValues.put(PASSWORD, password);

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }


    public boolean loginAuth(String UserID, String password){
        Boolean succeed = false;

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.getCount() != 0){
            while (cursor.moveToNext()){
                String userName = cursor.getString(3).trim();
                String PASSWORD = cursor.getString(5).trim();

                if (userName.equals(UserID) && password.equals(PASSWORD) ){
                    succeed = true;
                    cursor.close();
                    break;
                }

            }
        }

        return succeed;
    }
}
