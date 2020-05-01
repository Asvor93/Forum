package dk.easv.ATForum.Interfaces;

import android.widget.ImageView;

public interface IUploadManager {

    interface IONResult {
            void onResult(String res);
    }


    void uploadPicture(ImageView img, IONResult callback);
}
