package dk.easv.ATForum.Interfaces;

import java.util.List;

import dk.easv.ATForum.Models.User;

public interface IDataAccess {
    User createUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(User user);
}
