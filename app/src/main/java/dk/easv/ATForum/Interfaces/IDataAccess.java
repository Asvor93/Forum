package dk.easv.ATForum.Interfaces;

import java.util.List;
import java.util.Map;

import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.Topic;
import dk.easv.ATForum.Models.User;

public interface IDataAccess {
    void createUser(final Map<String, Object> user, String password, final IOnResult<User> callback);

    void getAllUsers(String uid, IOnResult<List<User>> callback);

    void updateUser(Map<String, Object> user, String uid, IOnResult<User> callback);

    void deleteUser(String id);

    void createRole(Map<String, Object> role, String uid, IOnResult<Role> callback);

    void getAllRoles(IOnResult<List<Role>> callback);

    void login(String email, String password, IOnResult<User> callback);

    void getRole(String uid, IOnResult<Role> callback);

    void logout();

    void getAllCategories(IOnResult<List<Category>> callback);

    void deleteCategory(String id);

    void createCategory(final Map<String, Object> category, final IOnResult<Category> callback);

    void editCategory(final Map<String, Object> category, String id, final IOnResult<Category> callback);

    void getCategory(String id);

    void getTopics(String id, IOnResult<List<Topic>> callback);

    void getTopic(String id, IOnResult<Topic> callback);

    void createTopic(final Map<String, Object> topic, final IOnResult<Topic> callback);

    void deleteTopic(String id);

    void updateTopic(final Map<String, Object> topic, String id, final IOnResult<Topic> callback);

    void getComments(String id, IOnResult<List<Comment>> callback);

    void createComment(final Map<String, Object> comment, final IOnResult<Comment> callback);

    interface IOnResult<T> {
        void onResult(T comments);
    }
}
