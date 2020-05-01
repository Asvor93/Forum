package dk.easv.ATForum;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "XYZ";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 100;
    EditText nameSignUp, emailSignUp, usernameSignUp, passwordSignUp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    FirebaseStorage fbStorage;
    ImageView image;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fbStorage = FirebaseStorage.getInstance();

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
        final String userNameString = usernameSignUp.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final User newUser = new User(userNameString, nameString, emailString, url);
                    db.collection("users").add(newUser).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getId();
                                newUser.setUid(uid);
                                Intent intent = new Intent();
                                Log.d(TAG, "onComplete: User " + newUser.toString());
                                intent.putExtra("currentUser", newUser);
                                Role role = new Role("user");
                                db.collection("roles").document(uid).set(role).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Log.d(TAG, "SignUp: " + "Role successfully added");
                                        } else {
                                            Log.d(TAG, "SignUp: " + " Failed to add Role with error: " + task.getException());
                                        }
                                    }
                                });
                                finish();
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
            uploadPicture();
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadPicture() {
        StorageReference storageRef = fbStorage.getReference();
        final StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID());
        // Get the data from an ImageView as bytes
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "failed to upload image to storage, got message: " + exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            if (downloadUri != null) {
                                url = downloadUri.toString();
                            }
                        } else {
                            Log.d(TAG, "failed to get the download url with error: " + task.getException());
                        }

                    }
                });
            }
        });
    }
}
