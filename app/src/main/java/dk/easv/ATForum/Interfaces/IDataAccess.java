package dk.easv.ATForum.Interfaces;

import java.util.List;
import java.util.Map;

import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public interface IDataAccess {
    void createUser(final Map<String, Object> user, String password, final IONUserResult callback);

    void getAllUsers(String uid, IONUsersResult callback);

    void updateUser(Map<String, Object> user, String uid, IONUserResult callback);

    void deleteUser(String id);

    void createRole(Map<String, Object> role, String uid, IONRoleResult callback);

    void getAllRoles(String uid, IONRolesResult callback);

    void logout();

    interface IONUsersResult {
        void onResult(List<User> users);
    }

    interface IONUserResult {
        void onResult(User user);
    }

    interface IONRoleResult {
        void onResult(Role role);
    }

    interface IONRolesResult {
        void onResult(List<Role> roles);
    }

}
