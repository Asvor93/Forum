package dk.easv.ATForum.Implementations;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.LoginActivity;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class FirebaseImpl implements IDataAccess {
    private static final String TAG = "XYZ";
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    public FirebaseImpl() {
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void createUser(final Map<String, Object> user, String password, final IONUserResult callback) {
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
    public void getAllUsers(final String uid, final IONUsersResult callback) {
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
                           final IONUserResult callback) {
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
    public void createRole(Map<String, Object> role, final String uid, final IONRoleResult callback) {
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
    public void getAllRoles(final String uid, final IONRolesResult callback) {

        db.collection("roles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Role> roles = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Role role = document.toObject(Role.class);
                        role.setUid(document.getId());
                        if (!role.getUid().equals(uid)) {
                            roles.add(role);
                        }
                    }
                    callback.onResult(roles);
                }
            }
        });
    }

    @Override
    public void login(String email, String password, final IONUserResult callback) {
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
                                                        user.setUid(task.getResult().getId());
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
    public void getRole(final String uid, final IONRoleResult callback) {
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
    public void getAllCategories(final IONCategoriesResult callback) {
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
                    callback.onResult(categories);
                } else {
                    Log.d(TAG, "get categories failed " + task.getException());
                }
            }
        });
    }
}
