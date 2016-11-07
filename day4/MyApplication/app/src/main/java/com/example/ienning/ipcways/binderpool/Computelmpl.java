package com.example.ienning.ipcways.binderpool;

import android.os.RemoteException;

/**
 * Created by ienning on 16-11-7.
 */

public class Computelmpl extends ICompute.Stub{
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

}
