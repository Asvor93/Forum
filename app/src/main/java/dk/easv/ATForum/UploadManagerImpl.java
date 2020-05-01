package dk.easv.ATForum;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import dk.easv.ATForum.Interfaces.IUploadManager;

public class UploadManagerImpl implements IUploadManager {
    private static final String TAG = "XYZ";
    private FirebaseStorage fbStorage;
    private String url;

    public UploadManagerImpl() {
        this.fbStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void uploadPicture(ImageView img, final IONResult callback) {
        StorageReference storageRef = fbStorage.getReference();
        final StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID());
        // Get the data from an ImageView as bytes
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
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
                                Log.d(TAG, "URL IMPL " + url);
                                callback.onResult(url);
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
