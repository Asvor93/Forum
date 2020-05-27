package dk.easv.ATForum.Users;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Implementations.UploadManagerImpl;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Interfaces.IUploadManager;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.User;

public class ProfileActivity extends MenuActivity {
    // Tag for logging
    private static final String TAG = "XYZ";

    // Request code for camera intent
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 100;

    // Request code for the permissions
    private static final int PERMISSION_REQUEST_CODE = 101;

    // Views for showing user information
    EditText username, email, name;

    // View for showing user image
    ImageView imgProfile;

    // The url of the picture that is taken when taking a new picture
    String url;

    // The interface that manages uploading images to the database
    IUploadManager uploadImg;

    // A boolean to switch between editing and viewing
    boolean editing;

    // The interface that controls data access
    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        checkPermissions();
        Slidr.attach(this);

        uploadImg = new UploadManagerImpl();
        dataAccess = DataAccessFactory.getInstance();
        final Button okButton = findViewById(R.id.btnEditOk);
        okButton.setVisibility(View.INVISIBLE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEdit();
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
        username.setFocusable(false);
        username.setFocusableInTouchMode(false);
        email = findViewById(R.id.txtProfileEmail);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        profileMenuItem.setVisible(false);
        profileMenuItem.setEnabled(false);
        return true;
    }

    /**
     * Creates a map for the updated user and uses it as a parameter for the updateUser method of the dataAccess interface
     */
    private void submitEdit() {
        final String newUsername = username.getText().toString();
        final String newName = name.getText().toString();
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("email", currentUser.getEmail());
        updatedUser.put("name", newName);
        updatedUser.put("username", newUsername);
        updatedUser.put("photoURL", url);
        final String uid = currentUser.getUid();

        // Method call and callback for updating the current user
        dataAccess.updateUser(updatedUser, uid, new IDataAccess.IOnResult<User>() {
            @Override
            public void onResult(User user) {
                Intent editResult = new Intent();
                editResult.putExtra("currentUser", user);
                setResult(RESULT_OK, editResult);
                toggleEditable((Button) findViewById(R.id.btnEditOk), (Button) findViewById(R.id.btnEditProfile));
                Toast.makeText(ProfileActivity.this, "User successfully updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Handles switching between edit and view by switching visibility on the ok button
     * and changing the text on the toggle button
     *
     * @param okButton The submit button for edit
     * @param v        The toggle button
     */
    private void toggleEditable(Button okButton, Button v) {
        if (!editing) {
            editing = true;
            okButton.setVisibility(View.VISIBLE);
            v.setText("Cancel");
            username.setFocusable(true);
            username.setFocusableInTouchMode(true);
            name.setFocusable(true);
            name.setFocusableInTouchMode(true);
        } else {
            editing = false;
            okButton.setVisibility(View.INVISIBLE);
            v.setText("Edit");
            username.setFocusable(false);
            username.setFocusableInTouchMode(false);
            name.setFocusable(false);
            name.setFocusableInTouchMode(false);
        }
    }

    /**
     * Gets extras and sets the gui accordingly
     */
    private void setGUI() {
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        email.setText(currentUser.getEmail());
        username.setText(currentUser.getUsername());
        name.setText(currentUser.getName());
        url = currentUser.getPhotoURL();
        Picasso.get().load(currentUser.getPhotoURL()).into(imgProfile);
    }

    /**
     * Opens up the camera intent
     */
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
                    Log.d(TAG, "Profile PhotoURL " + url);
                }
            });
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Canceled...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles permissions
     */
    private void checkPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA);

        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        }
    }

}
