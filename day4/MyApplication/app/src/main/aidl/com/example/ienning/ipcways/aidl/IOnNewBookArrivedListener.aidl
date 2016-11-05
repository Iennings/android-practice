// IOnNewBookArrivedListener.aidl
package com.example.ienning.ipcways.aidl;

// Declare any non-default types here with import statements
import com.example.ienning.ipcways.aidl.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book newBook);
}
