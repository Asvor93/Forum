package dk.easv.ATForum;

import android.content.Context;

import dk.easv.ATForum.Interfaces.IDataAccess;

import dk.easv.ATForum.Implementations.FirebaseImpl;

public class DataAccessFactory {


    // Returns an instance of the SQLite implementation class
    public static IDataAccess getInstance(Context context ) {
        return new FirebaseImpl(context);
    }
}
