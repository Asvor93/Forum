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
    /**
     * Creates an authentication user and a user in the database with extra information
     *
     * @param user     The user that has been created in the createUser activity
     * @param password The chosen password of the new user
     * @param callback A callback that returns the newly created user
     */
    void createUser(final Map<String, Object> user, String password, final IOnResult<User> callback);

    /**
     * Gets all users except the user that calls the method
     *
     * @param uid      The id of the user currently logged in
     * @param callback A callback that returns the list of all users minus the caller
     */
    void getAllUsers(String uid, IOnResult<List<User>> callback);

    /**
     * Updates the user with the matching uid
     *
     * @param user     A map containing the changes made to the user
     * @param uid      The id of the user to be changed
     * @param callback A callback that returns the updated user
     */
    void updateUser(Map<String, Object> user, String uid, IOnResult<User> callback);

    /**
     * Deletes the user with the matching id
     *
     * @param id The id of the user to be deleted
     */
    void deleteUser(String id);

    /**
     * Used to create an initial role when an user is created
     *
     * @param role     The initial role assigned to the newly created user
     * @param uid      The id of the user for whom a role is created
     * @param callback A callback that returns the created role
     */
    void createRole(Map<String, Object> role, String uid, IOnResult<Role> callback);

    /**
     * Gets all roles except the role of the one that calls the method
     *
     * @param callback A callback that returns all the roles
     */
    void getAllRoles(IOnResult<List<Role>> callback);

    /**
     * Logs the user in and get's the corresponding user document in the database based on uid
     *
     * @param email    The email of the user trying to login
     * @param password The password of the user trying to login
     * @param callback A callback that returns the logged in user
     */
    void login(String email, String password, IOnResult<User> callback);

    /**
     * Gets the role of the user with the corresponding id
     *
     * @param uid      The id of the user
     * @param callback A callback that returns the user's role
     */
    void getRole(String uid, IOnResult<Role> callback);

    /**
     * Logs the user out
     */
    void logout();

    /**
     * Gets a list of all categories
     *
     * @param callback A callback that returns all the roles
     */
    void getAllCategories(IOnResult<List<Category>> callback);

    /**
     * Deletes a category based on id
     *
     * @param id The id of the category delete
     */
    void deleteCategory(String id);

    /**
     * Creates a category based on the values from the category map
     *
     * @param category A map of the category created in the CreateCategory activity
     * @param callback A callback that returns the created category
     */
    void createCategory(final Map<String, Object> category, final IOnResult<Category> callback);

    /**
     * Edits the category with the matching id based on the category map
     *
     * @param category A map of the changed category
     * @param id       The id of the category to edit
     * @param callback A callback that returns the edited category
     */
    void editCategory(final Map<String, Object> category, String id, final IOnResult<Category> callback);

    /**
     * Gets all topics with a categoryId that matches the id parameter
     *
     * @param id       The id of the category from which the method gets all the topics
     * @param callback A callback that returns all the topics
     */
    void getTopics(String id, IOnResult<List<Topic>> callback);

    /**
     * Gets all favorite topics from the user with an uid that matches the id parameter
     *
     * @param id       The id of the user
     * @param callback A callback that returns all the user's favorite topics
     */
    void getFavoriteTopics(String id, IOnResult<List<Topic>> callback);

    /**
     * Deletes a topic from favorite topics of the user with a matching uid to the id parameter
     *
     * @param id    The id of the user
     * @param topic The topic to remove from favorites
     */
    void deleteFavoriteTopic(String id, Topic topic);

    /**
     * Adds a topic to favorite topics for the user with the corresponding uid
     *
     * @param userId The id of the user
     * @param topic  The topic to add to favorites
     */
    void addFavoriteTopic(String userId, Topic topic);

    /**
     * Creates a new topic for the active category
     *
     * @param topic    A map of the topic created in the CreateTopic intent.
     * @param callback A callback that returns the newly created topic
     */
    void createTopic(final Map<String, Object> topic, final IOnResult<Topic> callback);

    /**
     * Deletes the topic with the matching id
     *
     * @param id The id of the topic to delete
     */
    void deleteTopic(String id);

    /**
     * Updates the topic with an id matching the id parameter based on the topic map
     *
     * @param topic    A map of the changed topic
     * @param id       The id of the topic to change
     * @param callback A callback returning the changed topic
     */
    void updateTopic(final Map<String, Object> topic, String id, final IOnResult<Topic> callback);

    /**
     * Gets all comments with a topicId that matches the id parameter
     *
     * @param id       The id of the topic for which to show all comments
     * @param callback A callback returning the comments
     */
    void getComments(String id, IOnResult<List<Comment>> callback);

    /**
     * Gets all comments with a topicId that matches the id parameter
     *
     * @param comment  A map of the newly created comment
     * @param callback A callback that returns the new comment
     */
    void createComment(final Map<String, Object> comment, final IOnResult<Comment> callback);

    /**
     * Edits the comment with an id that matches the id parameter based on the comment map
     *
     * @param comment  A map of the changed comment
     * @param id       The id of the comment to change
     * @param callback A callback returning the changed comment
     */
    void editComment(final Map<String, Object> comment, String id, final IOnResult<Comment> callback);

    /**
     * Deletes the comment with the corresponding id
     *
     * @param id The id of the comment to delete
     */
    void deleteComment(String id);

    /**
     * An interface used for callbacks
     *
     * @param <T>
     */
    interface IOnResult<T> {
        /**
         * Callback used to return the result asynchronously
         *
         * @param t A template used to allow various classes to be used instead
         */
        void onResult(T t);
    }
}
