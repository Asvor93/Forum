package dk.easv.ATForum.Interfaces;

import java.util.List;
import java.util.Map;

import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Models.FavoriteTopic;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.Topic;
import dk.easv.ATForum.Models.User;

public interface IDataAccess {
    // Creates an authentication user and a user in the database with extra information
    void createUser(final Map<String, Object> user, String password, final IOnResult<User> callback);

    // Gets all users except the user that calls the method
    void getAllUsers(String uid, IOnResult<List<User>> callback);

    // Updates the user with the matching uid
    void updateUser(Map<String, Object> user, String uid, IOnResult<User> callback);

    // Deletes the user with the matching id
    void deleteUser(String id);

    // Used to create an initial role when an user is created
    void createRole(Map<String, Object> role, String uid, IOnResult<Role> callback);

    // Gets all roles except the role of the one that calls the method
    void getAllRoles(IOnResult<List<Role>> callback);

    // Logs the user in and get's the corresponding user document in the database based on uid
    void login(String email, String password, IOnResult<User> callback);

    // Gets the role of the user with the corresponding id
    void getRole(String uid, IOnResult<Role> callback);

    // Logs the user out
    void logout();

    // Gets a list of all categories
    void getAllCategories(IOnResult<List<Category>> callback);

    // Deletes a category based on id
    void deleteCategory(String id);

    // Creates a category based on the values from the category map
    void createCategory(final Map<String, Object> category, final IOnResult<Category> callback);

    // Edits the category with the matching id based on the category map
    void editCategory(final Map<String, Object> category, String id, final IOnResult<Category> callback);

    // Gets all topics with a categoryId that matches the id parameter
    void getTopics(String id, IOnResult<List<Topic>> callback);

    // Gets all favorite topics from the user with an uid that matches the id parameter
    void getFavoriteTopics(String id, IOnResult<List<Topic>> callback);

    // Deletes a topic from favorite topics of the user with a matching uid to the id parameter
    void deleteFavoriteTopic(String id, Topic topic);

    // Adds a topic to favorite topics for the user with the corresponding uid
    void addFavoriteTopic(String userId, Topic topic);

    // Creates a topic based on the topic map
    void createTopic(final Map<String, Object> topic, final IOnResult<Topic> callback);

    // Deletes the topic with the matching id
    void deleteTopic(String id);

    // Updates the topic with an id matching the id parameter based on the topic map
    void updateTopic(final Map<String, Object> topic, String id, final IOnResult<Topic> callback);

    // Gets all comments with a topicId that matches the id parameter
    void getComments(String id, IOnResult<List<Comment>> callback);

    // Creates a comment based on the comment map
    void createComment(final Map<String, Object> comment, final IOnResult<Comment> callback);

    // Edits the comment with an id that matches the id parameter based on the comment map
    void editComment(final Map<String, Object> comment, String id, final IOnResult<Comment> callback);

    // Deletes the comment with the corresponding id
    void deleteComment(String id);

    // Callback used to return the result asynchronously
    interface IOnResult<T> {
        void onResult(T comments);
    }
}
