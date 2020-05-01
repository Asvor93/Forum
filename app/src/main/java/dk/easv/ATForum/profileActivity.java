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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fbStorage = FirebaseStorage.getInstance();

        username = findViewById(R.id.txtProfileUsername);
        username.setFocusable(false);
        email = findViewById(R.id.txtProfileEmail);
        email.setFocusable(false);
        name = findViewById(R.id.txtName);
        name.setFocusable(false);

        imgProfile = findViewById(R.id.profileImage);
        imgProfile.setImageResource(R.drawable.qmark);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraUsingBitmap();
            }
        });
        setGUI();
    }

    private void setGUI() {
        user = (User) getIntent().getSerializableExtra("user");
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        name.setText(user.getName());
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
        imgProfile.setDrawingCacheEnabled(true);
        imgProfile.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
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
