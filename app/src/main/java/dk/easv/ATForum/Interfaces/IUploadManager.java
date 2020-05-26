package dk.easv.ATForum.Interfaces;

import android.widget.ImageView;

public interface IUploadManager {

    // Callback for the url result
    interface IONResult {
            void onResult(String res);
    }

    // Uploads a picture to the database and sets the image to the img view
    void uploadPicture(ImageView img, IONResult callback);
}
