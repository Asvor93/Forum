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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dk.easv.ATForum.Interfaces.IUploadManager;
import dk.easv.ATForum.Models.User;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "XYZ";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 100;
    EditText username, email, name;
    ImageView imgProfile;
    FirebaseFirestore db;
    FirebaseStorage fbStorage;
    String url;
    User user;
    IUploadManager uploadImg;
    boolean editing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        uploadImg = new UploadManagerImpl();
        fbStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        final Button okButton = findViewById(R.id.btnEditOk);
        okButton.setVisibility(View.INVISIBLE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitEdit();
            }
        });
        Button editButton = findViewById(R.id.btnEditProfile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditable(okButton, (Button) v);
            }
        });
        editing = false;
        username = findViewById(R.id.txtProfileUsername);
     //   username.setEnabled(false);
        username.setFocusable(false);
        username.setFocusableInTouchMode(false);
        email = findViewById(R.id.txtProfileEmail);
      //  email.setEnabled(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);
        name = findViewById(R.id.txtName);
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);

        imgProfile = findViewById(R.id.profileImage);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraUsingBitmap();
            }
        });
        setGUI();
    }

    private void SubmitEdit() {
        String newUsername = username.getText().toString();
        String newName = name.getText().toString();
        String newPhotoURL = url;
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("username", newUsername);
        updatedUser.put("name", newName);
        updatedUser.put("photoURL", newPhotoURL);
        String uid = user.getUid();
        db.collection("users").document(uid)
                .set(updatedUser, SetOptions.merge()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Something went wrong, got error: " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Successfully updated the user");
            }
        });

    }

    private void toggleEditable(Button okButton, Button v) {
       if (!editing) {
           editing = true;
           okButton.setVisibility(View.VISIBLE);
           v.setText("Cancel");
           username.setFocusable(true);
           //  username.setEnabled(true);
           username.setFocusableInTouchMode(true);
           name.setFocusable(true);
           name.setFocusableInTouchMode(true);
       } else {
           editing = false;
           okButton.setVisibility(View.INVISIBLE);
           v.setText("Edit");
           username.setFocusable(false);
           //  username.setEnabled(true);
           username.setFocusableInTouchMode(false);
           name.setFocusable(false);
           name.setFocusableInTouchMode(false);
       }
    }


    private void setGUI() {
        user = (User) getIntent().getSerializableExtra("user");
        Log.d(TAG, "setGUI: UserID: " + user.getUid());
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        name.setText(user.getName());
        url = user.getPhotoURL();
        Picasso.get().load(user.getPhotoURL()).into(imgProfile);
    }

    private void openCameraUsingBitmap() {
        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP);
        } else
            Log.d(TAG, "camera app could NOT be started");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgProfile.setImageBitmap(imageBitmap);
            uploadImg.uploadPicture(imgProfile, new IUploadManager.IONResult() {
                @Override
                public void onResult(String res) {
                    url = res;
                    Log.d(TAG,"Profile PhotoURL " + url);
                }
            });
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
