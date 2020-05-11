package dk.easv.ATForum.Interfaces;

import java.util.List;
import java.util.Map;

import dk.easv.ATForum.Models.User;

public interface IDataAccess {
    User createUser(User user);

    void getAllUsers(IONUsersResult callback);

    void updateUser(Map<String, Object> user, String uid, IONUserResult callback);

    void deleteUser(String id);

    interface IONUsersResult {
        void onResult(List<User> users);
    }

    interface IONUserResult {
        void onResult(User user);
    }
}
