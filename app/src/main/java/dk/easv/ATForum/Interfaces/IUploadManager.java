package dk.easv.ATForum.Interfaces;

import android.widget.ImageView;

public interface IUploadManager {

    /**
     * Callback for the url result
     */
    interface IONResult {
            void onResult(String res);
    }

    /**
     * Uploads a picture to the firebase firestore storage and returns the URL in a callback
     * @param img The image to upload
     * @param callback A Callback that returns the URL of the photo uploaded
     */
    void uploadPicture(ImageView img, IONResult callback);
}
