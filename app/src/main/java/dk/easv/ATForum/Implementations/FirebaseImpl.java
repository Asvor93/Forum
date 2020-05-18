package dk.easv.ATForum.Implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.Topic;
import dk.easv.ATForum.Models.User;

public class FirebaseImpl implements IDataAccess {
    private static final String TAG = "XYZ";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private static String userUid;

    public FirebaseImpl() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void createUser(final Map<String, Object> user, String password, final IOnResult<User> callback) {
        final String emailString = user.get("email").toString();
        final String nameString = user.get("name").toString();
        final String usernameString = user.get("username").toString();

        firebaseAuth.createUserWithEmailAndPassword(emailString, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String uid = task.getResult().getUser().getUid();
                            db.collection("users").document(uid).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                String photoURL = "";
                                                if (user.get("photoURL") != null) {
                                                    photoURL = user.get("photoURL").toString();
                                                }
                                                User newUser = new User(usernameString, nameString, emailString, photoURL);
                                                newUser.setUid(uid);
                                                callback.onResult(newUser);
                                            } else {
                                                Log.d(TAG, "failed adding user to database with error: " + task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "failed to create auth user with error: " + task.getException());
                        }
                    }
                });
    }

    @Override
    public void getAllUsers(final String uid, final IOnResult<List<User>> callback) {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        user.setUid(document.getId());
                        if (!user.getUid().equals(uid)) {
                            users.add(user);
                        }
                    }
                    callback.onResult(users);
                } else {
                    Log.d(TAG, "get users failed " + task.getException());
                }
            }
        });
    }

    @Override
    public void updateUser(final Map<String, Object> user, final String uid,
                           final IOnResult<User> callback) {
        db.collection("users").document(uid)
                .set(user, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Something went wrong, got error: " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String newUsername = user.get("username").toString();
                String newName = user.get("name").toString();
                String newPhotoURL = user.get("photoURL").toString();
                String email = user.get("email").toString();
                User changedUser = new User(newUsername, newName, email, newPhotoURL);
                changedUser.setUid(uid);
                callback.onResult(changedUser);
                Log.d(TAG, "Successfully updated the user");
            }
        });
    }

    @Override
    public void deleteUser(String id) {
        db.collection("users").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "user deleted ");
                } else {
                    Log.d(TAG, "delete user failed" + task.getException());
                }
            }
        });
    }

    @Override
    public void createRole(Map<String, Object> role, final String uid, final IOnResult<Role> callback) {
        db.collection("roles").document(uid).set(role).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final Role newRole = new Role("user", uid);
                    callback.onResult(newRole);
                    Log.d(TAG, "SignUp: " + "Role successfully added");
                } else {
                    Log.d(TAG, "SignUp: " + " Failed to add Role with error: " + task.getException());
                }
            }
        });
    }

    @Override
    public void getAllRoles(final IOnResult<List<Role>> callback) {

        db.collection("roles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Role> roles = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Role role = document.toObject(Role.class);
                        role.setUid(document.getId());
                        if (!role.getUid().equals(userUid)) {
                            roles.add(role);
                        }
                    }
                    callback.onResult(roles);
                }
            }
        });
    }

    @Override
    public void login(String email, String password, final IOnResult<User> callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Login successful " + task.getResult().getUser().getEmail());
                            db.collection("users").document(task.getResult()
                                    .getUser().getUid()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                User user = null;
                                                if (document != null) {
                                                    user = document.toObject(User.class);
                                                    if (user != null) {
                                                        userUid = task.getResult().getId();
                                                        user.setUid(userUid);
                                                        callback.onResult(user);
                                                    }
                                                } else {
                                                    Log.d(TAG, "No such document", task.getException());
                                                }
                                            } else {
                                                Log.d(TAG, "Failed with message: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.w(TAG, "createUserWithEmail failure: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void getRole(final String uid, final IOnResult<Role> callback) {
        db.collection("roles")
                .document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Role role;
                            DocumentSnapshot doc = task.getResult();
                            if (doc != null) {
                                role = doc.toObject(Role.class);
                                role.setUid(uid);
                                callback.onResult(role);
                                Log.d(TAG, "login role: " + role.getRoleName());
                            }
                        } else {
                            Log.d(TAG, "Failed with message: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public void getAllCategories(final IOnResult<List<Category>> callback) {
        db.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Category> categories = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Category category = document.toObject(Category.class);
                        category.setUid(document.getId());
                        categories.add(category);

                    }
                    if (!categories.isEmpty()) {
                        callback.onResult(categories);
                    }
                } else {
                    Log.d(TAG, "get categories failed " + task.getException());
                }
            }
        });
    }

    @Override
    public void deleteCategory(String id) {
        db.collection("categories").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "category deleted ");
                } else {
                    Log.d(TAG, "delete category failed" + task.getException());
                }
            }
        });
    }

    @Override
    public void createCategory(final Map<String, Object> category, final IOnResult<Category> callback) {
        db.collection("categories").add(category).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String catNameString = category.get("categoryName").toString();
                    String catDescription = category.get("description").toString();
                    String catId = task.getResult().getId();

                    Category newCat = new Category(catNameString, catDescription);
                    newCat.setUid(catId);

                    callback.onResult(newCat);
                } else {
                    Log.d(TAG, "Failed to create a new category with message: \n" + task.getException());
                }
            }
        });
    }

    @Override
    public void editCategory(final Map<String, Object> category, final String id, final IOnResult<Category> callback) {
        db.collection("categories").document(id)
                .set(category, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String newCategoryName = category.get("categoryName").toString();
                    String newDescription = category.get("description").toString();

                    Category updatedCategory = new Category(newCategoryName, newDescription);

                    updatedCategory.setUid(id);
                    callback.onResult(updatedCategory);
                    Log.d(TAG, "Successfully updated the category");
                } else {
                    Log.d(TAG, "Failed to update category with message: " + task.getException());
                }
            }
        });
    }

    @Override
    public void getCategory(String id) {

    }

    @Override
    public void getTopics(String id, final IOnResult<List<Topic>> callback) {
        db.collection("topics").whereEqualTo("categoryId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    List<Topic> catTopics = new ArrayList<>();

                    if (!docs.isEmpty()) {
                        for (int i = 0; i < docs.size(); i++) {
                            Topic topic = docs.get(i).toObject(Topic.class);
                            topic.setId(docs.get(i).getId());
                            catTopics.add(topic);
                        }
                    }
                    callback.onResult(catTopics);
                } else {
                    Log.d(TAG, "Unable to get topics from the specified category, got error: \n"
                            + task.getException());
                }
            }
        });
    }

    @Override
    public void getTopic(String id, IOnResult<Topic> callback) {

    }

    @Override
    public void createTopic(final Map<String, Object> topic, final IOnResult<Topic> callback) {
        db.collection("topics").add(topic).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String topicNameString = topic.get("topicName").toString();
                    String topicDescription = topic.get("description").toString();
                    User topicAuthor = (User) topic.get("author");
                    String categoryId = topic.get("categoryId").toString();

                    String topicId = task.getResult().getId();

                    Topic newTopic = new Topic(topicNameString, topicDescription, topicAuthor, categoryId);
                    newTopic.setId(topicId);

                    callback.onResult(newTopic);
                } else {
                    Log.d(TAG, "Failed to create a new topic with message: \n" + task.getException());
                }
            }
        });
    }

    @Override
    public void deleteTopic(String id) {
        db.collection("topics").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "topic deleted ");
                } else {
                    Log.d(TAG, "delete topic failed" + task.getException());
                }
            }
        });
    }

    @Override
    public void updateTopic(final Map<String, Object> topic, final String id, final IOnResult<Topic> callback) {
        db.collection("topics").document(id)
                .set(topic, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String updateTopicName = topic.get("topicName").toString();
                    String updateDescription = topic.get("description").toString();

                    Topic updatedTopic = new Topic(updateTopicName, updateDescription);

                    updatedTopic.setId(id);
                    callback.onResult(updatedTopic);
                    Log.d(TAG, "Successfully updated the topic");
                } else {
                    Log.d(TAG, "Failed to update topic with message: " + task.getException());
                }
            }
        });
    }

    @Override
    public void getComments(String id, final IOnResult<List<Comment>> callback) {
        db.collection("comments").whereEqualTo("topicId", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    List<Comment> topicComments = new ArrayList<>();

                    if (!docs.isEmpty()) {
                        for (int i = 0; i < docs.size(); i++) {
                            Comment comment = docs.get(i).toObject(Comment.class);
                            comment.setId(docs.get(i).getId());
                            topicComments.add(comment);
                        }
                    }
                    callback.onResult(topicComments);
                } else {
                    Log.d(TAG, "Unable to get the comments from the specified topic, got error: \n"
                            + task.getException());
                }
            }
        });
    }

    @Override
    public void createComment(final Map<String, Object> comment, final IOnResult<Comment> callback) {
        db.collection("comments").add(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    String commentMessageString = comment.get("message").toString();
                    User commentAuthor = (User) comment.get("author");
                    String topicId = comment.get("topicId").toString();
                    String commentId = task.getResult().getId();

                    Comment newComment = new Comment(commentMessageString, commentAuthor, topicId);
                    newComment.setId(commentId);

                    callback.onResult(newComment);
                } else {
                    Log.d(TAG, "Failed to create a new comment with message: \n" + task.getException());
                }
            }
        });
    }

    @Override
    public void editComment(final Map<String, Object> comment, final String id, final IOnResult<Comment> callback) {
        db.collection("topics").document(id)
                .set(comment, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String editMessage = comment.get("message").toString();

                    Comment updatedComment = new Comment(editMessage);

                    updatedComment.setId(id);
                    callback.onResult(updatedComment);
                    Log.d(TAG, "Successfully updated the comment");
                } else {
                    Log.d(TAG, "Failed to update message with message: " + task.getException());
                }
            }
        });
    }

}
