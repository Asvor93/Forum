package dk.easv.ATForum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.Implementations.UploadManagerImpl;
import dk.easv.ATForum.Interfaces.IUploadManager;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "XYZ";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 100;
    EditText nameSignUp, emailSignUp, usernameSignUp, passwordSignUp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    ImageView image;
    String url;
    IUploadManager uploadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uploadImg = new UploadManagerImpl();

        image = findViewById(R.id.ivProfilePicture);
        image.setImageResource(R.drawable.qmark);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraUsingBitmap();
            }
        });

        nameSignUp = findViewById(R.id.txtNameSignUp);
        nameSignUp.setHint("Name");
        emailSignUp = findViewById(R.id.txtEmailSignUp);
        emailSignUp.setHint("Email");
        usernameSignUp = findViewById(R.id.txtUserNameSignUp);
        usernameSignUp.setHint("Username");
        passwordSignUp = findViewById(R.id.txtPasswordSignUp);
        passwordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordSignUp.setHint("Password");

        Button btnConfirm = findViewById(R.id.btnConfirmSignUp);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void openCameraUsingBitmap() {
        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP);
        } else
            Log.d(TAG, "camera app could NOT be started");
    }

    private void signUp() {
        final String emailString = emailSignUp.getText().toString();
        final String passwordString = passwordSignUp.getText().toString();
        final String nameString = nameSignUp.getText().toString();
        final String usernameString = usernameSignUp.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", emailString);
                    user.put("name", nameString);
                    user.put("username", usernameString);
                    user.put("photoURL", url);

                    Log.d(TAG,"New user photoURL " + url);
                    db.collection("users").add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                final User newUser = new User(usernameString, nameString, emailString, url);
                                String uid = task.getResult().getId();
                                newUser.setUid(uid);
                                final Intent intent = new Intent();
                                Log.d(TAG, "onComplete: User " + newUser.toString());
                                intent.putExtra("currentUser", newUser);
                                final Role role = new Role("user");
                                db.collection("roles").document(uid).set(role).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            intent.putExtra("role", role);
                                            finish();
                                            Log.d(TAG, "SignUp: " + "Role successfully added");
                                        } else {
                                            Log.d(TAG, "SignUp: " + " Failed to add Role with error: " + task.getException());
                                        }
                                    }
                                });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            uploadImg.uploadPicture(image, new IUploadManager.IONResult() {
                @Override
                public void onResult(String res) {
                    url = res;
                    Log.d(TAG,"PhotoURL " + url);
                }
            });
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
