package com.example.ienning.ipcways.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by ienning on 16-11-5.
 */

public class BookProvider extends ContentProvider {
    private static final String TAG = "Ienning";
    public static final String AUTHORITY = "com.example.ienning.book.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }
    private Context context;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate: current thread:" + Thread.currentThread().getName());
        context = getContext();
        initProviderData();
        return true;
    }

    private void initProviderData() {
        db = new MyDatabaseHelper(context).getWritableDatabase();
        db.execSQL("delete from " + MyDatabaseHelper.BOOK_TABLE_NAME);
        db.execSQL("delete from " + MyDatabaseHelper.USER_TABLE_NAME);
        db.execSQL("insert into book values(3,'Android');");
        db.execSQL("insert into book values(4, 'iOS');");
        db.execSQL("insert into book values(5, 'Html');");
        db.execSQL("insert into user values(1, 'jake', 1);");
        db.execSQL("insert into user values(2, 'Ienning', 0);");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.i(TAG, "query: ,current thread " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("UnSupported URI: " + uri);
        }
        return db.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public String getType(Uri uri) {
        Log.i(TAG, "getType: ");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(TAG, "insert: ");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("UnSuported URIL" + uri);
        }
        db.insert(table, null, values);
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i(TAG, "delete: ");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsuported URI: " + uri);
        }
        int count = db.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i(TAG, "update: ");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("UnSport Uri: " + uri);
        }
        int row = db.update(table, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }
    private String getTableName(Uri uri) {
        String tableName = null;
        //进行Uri匹配,匹配到后返回之前添加到uriMatcer中的CODE值
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = MyDatabaseHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = MyDatabaseHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
