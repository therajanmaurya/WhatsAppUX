package therajanmaurya.profile.views.whatsprofile;

import java.io.File;

/**
 * @author Rajan Maurya
 *         On 06/08/17.
 */
public interface EditCustomerProfileContract {

    interface View {

        void showUserInterface();

        void setCustomerIdentifier(String customerIdentifier);

        void checkWriteExternalStoragePermission();

        void checkReadExternalStoragePermission();

        void openCamera();

        void viewGallery();

        void showImageSizeExceededOrNot();

        void requestWriteExternalStorageAndCameraPermission();

        void requestReadExternalStoragePermission();

        void showPortraitUploadedSuccessfully();

        void showPortraitDeletedSuccessfully();

        void showProgressDialog(String message);

        void hideProgressDialog();

        void showMessage(String message);
    }

    interface Presenter {

        void uploadCustomerPortrait(String customerIdentifier, File file);

        void deleteCustomerPortrait(String customerIdentifier);
    }
}
