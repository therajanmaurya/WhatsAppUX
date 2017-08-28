package therajanmaurya.profile.views.whatsprofile;

import android.graphics.Bitmap;

import java.io.File;

public interface RefreshProfilePhoto {

    void showCameraPhoto(File file);

    void showGalleryPhoto(Bitmap bitmap);

    void deletePhoto();
}
