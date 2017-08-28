package therajanmaurya.profile.views.whatsprofile;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public interface EditProfileContract {

    interface View {

        void showUserInterface();

        void checkWriteExternalStorageAndCameraPermission();

        void checkReadExternalStoragePermission();

        void openCamera();

        void viewGallery();

        void showImageSizeExceededOrNot();

        void requestWriteExternalStorageAndCameraPermission();

        void requestReadExternalStoragePermission();
    }
}
