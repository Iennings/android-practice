package com.example.ienning.ipcways.manualbinder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;


import java.util.List;

/**
 * Created by ienning on 16-11-5.
 */

public class BookManagerlmpl extends Binder implements IBookManager {
    public BookManagerlmpl() {
        this.attachInterface(this, DESCRIPTOR);
    }
    public static IBookManager asInterface(IBinder obj) {
        if ((obj == null))
        {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if ((iin != null) && (iin instanceof IBookManager))
        {
            return ((IBookManager) iin);
        }
        return new BookManagerlmpl.Proxy(obj);
    }
    @Override
    public IBinder asBinder() {
        return this;
    }
    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (0 != data.readInt()) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }
    @Override
    public List<Book> getBookList() throws RemoteException{
        return null;
    }
    @Override
    public void addBook(com.example.ienning.ipcways.manualbinder.Book book) throws RemoteException{

    }

    private static class Proxy implements IBookManager {
        private IBinder remote;

        Proxy(IBinder remote) {
            this.remote = remote;
        }
        @Override
        public IBinder asBinder()
        {
            return remote;
        }
        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }
        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                remote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }
        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null))
                {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                remote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }
    }
}
