package com.example.ienning.ipcways.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.ienning.ipcways.R;
import com.example.ienning.ipcways.aidl.Book;
import com.example.ienning.ipcways.model.User;

/**
 * Created by ienning on 16-11-5.
 */

public class ProviderActivity extends Activity {
    private static final String TAG = "Ienning";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Uri bookUri = Uri.parse("content://com.example.ienning.book.provider/book");
        ContentValues values = new ContentValues();

        values.put("_id", 6);
        values.put("name", "Android艺术开发");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[] {"_id", "name"}, null , null, null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Log.i(TAG, "onCreate: query book " + book.toString());
        }
        bookCursor.close();
        Uri userUri = Uri.parse("content://com.example.ienning.book.provider/user");
        Cursor cursor = getContentResolver().query(userUri, new String[]{"_id","name", "sex"}, null, null, null);
        while(cursor.moveToNext()) {
            User user = new User();
            user.userId = cursor.getInt(0);
            user.name = cursor.getString(1);
            user.isMale = cursor.getInt(2) == 1;
            Log.i(TAG, "onCreate: query user " + user.toString());
        }
        cursor.close();
    }
}
