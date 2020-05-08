package dk.easv.ATForum.Interfaces;

import java.util.List;

import dk.easv.ATForum.Models.User;

public interface IDataAccess {
    User createUser(User user);

    void getAllUsers(IONUsersResult callback);

    User updateUser(User user);

    void deleteUser(User user);

    interface IONUsersResult {
        void onResult(List<User> users);
    }
}
