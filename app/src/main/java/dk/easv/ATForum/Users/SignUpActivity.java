package dk.easv.ATForum.Users;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.r0adkll.slidr.Slidr;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Implementations.UploadManagerImpl;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Interfaces.IUploadManager;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class SignUpActivity extends AppCompatActivity {
    // Tag used for logging
    private static final String TAG = "XYZ";

    // Request code used when starting the camera intent
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 100;

    // Edits text views used when creating a new user
    EditText nameSignUp, emailSignUp, usernameSignUp, passwordSignUp;

    // Holds the current image for the new user. Default image if none has been taken.
    // also has an onClickListener attached.
    ImageView image;

    // Url string for the image. Contains a reference to the default picture in the firestore storage if no new picture is taken
    String url;

    // Interface for handling of uploading images
    IUploadManager uploadImg;

    // DataAccess interface for handling calls to the database
    private IDataAccess dataAccess;

    // Checks if the app is currently uploading an image. Prevents the signUp method from proceeding if false
    private boolean uploadingImg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Slidr.attach(this);

        dataAccess = DataAccessFactory.getInstance();
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
        url = getString(R.string.defaultImageLink);

        Button btnConfirm = findViewById(R.id.btnConfirmSignUp);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    // Opens the camera intent expecting a result
    private void openCameraUsingBitmap() {
        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP);
        } else
            Log.d(TAG, "camera app could NOT be started");
    }

    // Sign up formula for the user. Method first calls the createUser method on the dataAccess interface
    // and then calls the createRole method on the interface. The results are returned through a callback for each method
    private void signUp() {
        if (!uploadingImg) {
            final String emailString = emailSignUp.getText().toString();
            final String passwordString = passwordSignUp.getText().toString();
            final String nameString = nameSignUp.getText().toString();
            final String usernameString = usernameSignUp.getText().toString();
            Map<String, Object> user = new HashMap<>();
            user.put("email", emailString);
            user.put("name", nameString);
            user.put("username", usernameString);
            user.put("photoURL", url);
            dataAccess.createUser(user, passwordString, new IDataAccess.IOnResult<User>() {
                @Override
                public void onResult(User user) {
                    final Intent result = new Intent();
                    result.putExtra("currentUser", user);

                    Map<String, Object> role = new HashMap<>();
                    role.put("roleName", "user");

                    dataAccess.createRole(role, user.getUid(), new IDataAccess.IOnResult<Role>() {
                        @Override
                        public void onResult(Role role) {
                            result.putExtra("role", role);
                            setResult(RESULT_OK, result);
                            Toast.makeText(SignUpActivity.this, "User successfully created",Toast.LENGTH_LONG ).show();
                            finish();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, "Currently uploading image", Toast.LENGTH_LONG).show();
        }
    }

    // Handles getting results from activities started for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uploadingImg = true;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
            uploadImg.uploadPicture(image, new IUploadManager.IONResult() {
                @Override
                public void onResult(String res) {
                    url = res;
                    Log.d(TAG, "PhotoURL " + url);
                    uploadingImg = false;
                }
            });
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
