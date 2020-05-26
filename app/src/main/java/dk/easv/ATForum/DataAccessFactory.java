package dk.easv.ATForum;

import dk.easv.ATForum.Interfaces.IDataAccess;

import dk.easv.ATForum.Implementations.FirebaseImpl;

public class DataAccessFactory {


    // Returns an instance of the implementation class
    public static IDataAccess getInstance() {
        return new FirebaseImpl();
    }
}
